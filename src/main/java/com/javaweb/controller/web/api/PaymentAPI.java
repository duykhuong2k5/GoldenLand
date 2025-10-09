package com.javaweb.controller.web.api;

import com.javaweb.entity.PaymentEntity;
import com.javaweb.model.dto.PaymentDTO;
import com.javaweb.model.request.PaymentRequest;
import com.javaweb.model.response.PaymentResponse;
import com.javaweb.service.impl.PaymentServiceImpl;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentAPI {

    private final PaymentServiceImpl paymentService;

    public PaymentAPI(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.initiatePayment(request));
    }

    @PostMapping("/{paymentId}/status")
    public ResponseEntity<PaymentDTO> updateStatus(@PathVariable Long paymentId,
                                                   @RequestParam("status") PaymentEntity.Status status,
                                                   @RequestParam(value = "gatewayTransactionId", required = false) String gatewayTransactionId) {
        Optional<PaymentDTO> payment = paymentService.updateStatus(paymentId, status, gatewayTransactionId);
        return payment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/gateway")
    public ResponseEntity<PaymentDTO> findByGatewayTransaction(@RequestParam("id") String gatewayTransactionId) {
        return paymentService.findByGatewayTransactionId(gatewayTransactionId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
