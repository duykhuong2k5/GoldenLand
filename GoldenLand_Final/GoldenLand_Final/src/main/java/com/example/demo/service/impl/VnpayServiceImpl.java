package com.example.demo.service.impl;

import com.example.demo.entity.Building;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Transaction;
import com.example.demo.model.request.TransactionCreateRequest;
import com.example.demo.model.response.ResponseDTO;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.VnpayService;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VnpayServiceImpl implements VnpayService {

    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.returnUrl}")
    private String vnp_ReturnUrl;

    @Value("${vnpay.payUrl}")
    private String vnp_PayUrl;

    private final TransactionRepository transactionRepository;

    public VnpayServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @PostConstruct
    private void normalizeConfig() {
        vnp_TmnCode    = stripBomAndTrim(vnp_TmnCode);
        vnp_HashSecret = stripBomAndTrim(vnp_HashSecret);
        vnp_PayUrl     = stripBomAndTrim(vnp_PayUrl);
        vnp_ReturnUrl  = stripBomAndTrim(vnp_ReturnUrl);
    }

    private String stripBomAndTrim(String s) {
        if (s == null) return null;
        return s.replace("\uFEFF", "").trim();
    }

    private static String nowVnpDate() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    private static String buildEncodedQuery(Map<String, String> params) throws Exception {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder query = new StringBuilder();
        boolean first = true;
        for (String k : keys) {
            String v = params.get(k);
            if (v == null || v.isEmpty()) continue;
            if (!first) query.append('&');
            query.append(URLEncoder.encode(k, StandardCharsets.UTF_8.name()))
                 .append('=')
                 .append(URLEncoder.encode(v, StandardCharsets.UTF_8.name()));
            first = false;
        }
        return query.toString();
    }

    private static String hmacSHA512Hex(String key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA512");
        SecretKeySpec ks = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        mac.init(ks);
        byte[] out = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder(out.length * 2);
        for (byte b : out) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    @Override
    public ResponseDTO createPayment(TransactionCreateRequest req) {
        ResponseDTO response = new ResponseDTO();

        try {
            // 1️⃣ Lưu transaction PENDING trước khi redirect
            Transaction tx = new Transaction();
            tx.setAmount(req.getAmount());
            tx.setStatus("PENDING");
            tx.setCode((req.getCode() == null || req.getCode().isBlank()) ? "RENT_PAYMENT" : req.getCode());
            tx.setType(req.getType());
            tx.setNote(req.getNote());
            tx.setCreatedDate(new Date());
            tx.setCreatedBy("PAYMENT_GATEWAY");

            if (req.getCustomerId() != null) {
                Customer c = new Customer();
                c.setId(req.getCustomerId());
                tx.setCustomer(c);
            }

            if (req.getBuildingId() != null) {
                Building b = new Building();
                b.setId(req.getBuildingId());
                tx.setBuilding(b);
            }

            tx = transactionRepository.save(tx);

            // 2️⃣ Gửi dữ liệu sang VNPAY
            long amountVnd = tx.getAmount() != null ? tx.getAmount() : 0L;
            Map<String, String> vnp = new HashMap<>();
            vnp.put("vnp_Version", "2.1.0");
            vnp.put("vnp_Command", "pay");
            vnp.put("vnp_TmnCode", vnp_TmnCode);
            vnp.put("vnp_Amount", String.valueOf(amountVnd * 100));
            vnp.put("vnp_CurrCode", "VND");
            vnp.put("vnp_TxnRef", String.valueOf(tx.getId()));
            vnp.put("vnp_OrderInfo", "Thanh toan giao dich #" + tx.getId());
            vnp.put("vnp_OrderType", "other");
            vnp.put("vnp_Locale", "vn");
            vnp.put("vnp_ReturnUrl", vnp_ReturnUrl);
            vnp.put("vnp_IpAddr", "127.0.0.1");
            vnp.put("vnp_CreateDate", nowVnpDate());

            String encodedQuery = buildEncodedQuery(vnp);
            String secureHash = hmacSHA512Hex(vnp_HashSecret, encodedQuery);
            String finalUrl = vnp_PayUrl + "?" + encodedQuery +
                    "&vnp_SecureHashType=HMACSHA512" +
                    "&vnp_SecureHash=" + secureHash;

            response.setMessage("success");
            response.setData(finalUrl);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("error");
            response.setData(e.getMessage());
        }
        return response;
    }

    @Override
    public Map<String, Object> handleReturn(Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            String secureHashFromVnp = params.get("vnp_SecureHash");
            Map<String, String> verifyParams = new HashMap<>(params);
            verifyParams.remove("vnp_SecureHash");
            verifyParams.remove("vnp_SecureHashType");

            String encodedQuery = buildEncodedQuery(verifyParams);
            String myHash = hmacSHA512Hex(vnp_HashSecret, encodedQuery);
            if (!myHash.equalsIgnoreCase(secureHashFromVnp)) {
                result.put("success", false);
                result.put("message", "Chữ ký không hợp lệ");
                return result;
            }

            Long txnId = Long.parseLong(params.get("vnp_TxnRef"));
            String responseCode = params.get("vnp_ResponseCode");
            long amount = Long.parseLong(params.getOrDefault("vnp_Amount", "0")) / 100;

            Transaction tx = transactionRepository.findById(txnId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy giao dịch id=" + txnId));

            boolean success = "00".equals(responseCode);
            tx.setStatus(success ? "SUCCESS" : "FAILED");
            if (amount > 0) tx.setAmount(amount);
            tx.setModifiedBy("VNPAY");
            tx.setModifiedDate(new Date());
            transactionRepository.save(tx);

            result.put("success", success);
            result.put("message", success ? "Thanh toán thành công!" : "Thanh toán thất bại!");
            result.put("txnRef", txnId);
            result.put("amount", tx.getAmount());
            result.put("customerId", tx.getCustomer() != null ? tx.getCustomer().getId() : null);
            result.put("buildingId", tx.getBuilding() != null ? tx.getBuilding().getId() : null);
            result.put("code", tx.getCode());
            result.put("note", tx.getNote());
            result.put("vnp_TxnRef", String.valueOf(txnId));

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Lỗi xử lý callback: " + e.getMessage());
        }
        return result;
    }
}
