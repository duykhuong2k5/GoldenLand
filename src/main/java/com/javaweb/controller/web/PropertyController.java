package com.javaweb.controller.web;

import com.javaweb.entity.TransactionEntity;
import com.javaweb.model.dto.TransactionDTO;
import com.javaweb.model.request.TransactionCreateRequest;
import com.javaweb.service.impl.TransactionServiceImpl;
import com.javaweb.utils.UploadFileUtils;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/property")
public class PropertyController {

    private final TransactionServiceImpl transactionService;
    private final UploadFileUtils uploadFileUtils;

    public PropertyController(TransactionServiceImpl transactionService, UploadFileUtils uploadFileUtils) {
        this.transactionService = transactionService;
        this.uploadFileUtils = uploadFileUtils;
    }

    @GetMapping("/{id}")
    public String propertyDetail(@PathVariable Long id, Model model) {
        model.addAttribute("propertyId", id);
        return "web/property/detail";
    }

    @PostMapping("/{id}/rent")
    public ResponseEntity<TransactionDTO> rentProperty(@PathVariable Long id,
                                                        @Validated @ModelAttribute TransactionCreateRequest request) {
        request.setPropertyId(id);
        TransactionDTO transaction = transactionService.createTransaction(request);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<String> submitReview(@PathVariable Long id,
                                               @RequestParam("userId") Long userId,
                                               @RequestParam("rating") Integer rating,
                                               @RequestParam("content") String content,
                                               @RequestParam(value = "media", required = false) List<MultipartFile> mediaFiles) {
        if (content == null || content.trim().length() < 50) {
            return ResponseEntity.badRequest().body("Review content must be at least 50 characters");
        }
        if (rating == null || rating < 1 || rating > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 1 and 5");
        }
        if (mediaFiles != null) {
            mediaFiles.forEach(uploadFileUtils::uploadReviewMedia);
        }
        return ResponseEntity.ok("Review submitted successfully");
    }

    @GetMapping("/user/{userId}/history")
    public String rentalHistory(@PathVariable Long userId, Model model) {
        List<TransactionDTO> history = transactionService.getRentalHistory(userId);
        model.addAttribute("transactions", history);
        return "web/profile/rental-history";
    }

    @PostMapping("/{transactionId}/status")
    public ResponseEntity<TransactionDTO> updateStatus(@PathVariable Long transactionId,
                                                       @RequestParam("status") TransactionEntity.Status status) {
        return transactionService.updateStatus(transactionId, status)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
