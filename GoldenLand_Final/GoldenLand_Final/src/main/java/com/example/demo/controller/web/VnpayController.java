package com.example.demo.controller.web;

import com.example.demo.entity.Transaction;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.VnpayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

@Controller
public class VnpayController {

    @Autowired
    private VnpayService vnpayService;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/vnpay_return")
    public ModelAndView vnpayReturn(@RequestParam Map<String, String> params) {
        Map<String, Object> result = vnpayService.handleReturn(params);
        ModelAndView mav = new ModelAndView();
        mav.addObject("transactionInfo", result);

        boolean success = (boolean) result.getOrDefault("success", false);
        if (!success) {
            mav.setViewName("web/payment-fail");
            return mav;
        }

        Long txnId = (Long) result.get("txnRef");
        mav.addObject("txnId", txnId);
        mav.setViewName("web/payment-success");
        return mav;
    }

    @GetMapping("/api/payment/receipt/{txnId}")
    public ResponseEntity<byte[]> downloadReceipt(@PathVariable Long txnId) {
        Transaction tx = transactionRepository.findById(txnId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giao dịch"));

        try (PDDocument doc = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PDType0Font fontRegular = PDType0Font.load(
                    doc, VnpayController.class.getResourceAsStream("/fonts/NotoSans-Regular.ttf"), true);
            PDType0Font fontItalic = PDType0Font.load(
                    doc, VnpayController.class.getResourceAsStream("/fonts/NotoSans-Italic.ttf"), true);

            DecimalFormatSymbols vns = new DecimalFormatSymbols(new Locale("vi", "VN"));
            vns.setGroupingSeparator('.');
            DecimalFormat df = new DecimalFormat("#,###", vns);
            String amountStr = df.format(tx.getAmount()) + " VND";
            String createdAt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(tx.getCreatedDate());

            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            float y = page.getMediaBox().getHeight() - 50f;

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(fontRegular, 18);
                cs.newLineAtOffset(50f, y);
                cs.showText("BIÊN LAI THANH TOÁN VNPay");
                cs.endText();

                y -= 30;
                String[] lines = new String[]{
                        "Mã giao dịch: " + tx.getId(),
                        "Loại giao dịch: " + tx.getCode(),
                        "Số tiền: " + amountStr,
                        "Trạng thái: " + tx.getStatus(),
                        "Ngày tạo: " + createdAt,
                        "Ghi chú: " + (tx.getNote() != null ? tx.getNote() : "(Không có)")
                };

                for (String line : lines) {
                    y -= 18;
                    cs.beginText();
                    cs.setFont(fontRegular, 12);
                    cs.newLineAtOffset(50f, y);
                    cs.showText(line);
                    cs.endText();
                }

                y -= 36;
                cs.beginText();
                cs.setFont(fontItalic, 12);
                cs.newLineAtOffset(50f, y);
                cs.showText("Cảm ơn quý khách đã sử dụng dịch vụ của GodendLand!");
                cs.endText();
            }

            doc.save(out);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receipt-" + txnId + ".pdf");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
