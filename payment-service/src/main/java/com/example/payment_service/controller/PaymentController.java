package com.example.payment_service.controller;

import com.example.payment_service.dto.request.PaymentRequest;
import com.example.payment_service.dto.response.PaymentDto;
import com.example.payment_service.dto.response.PaymentResponse;
import com.example.payment_service.dto.response.ResponseDto;
import com.example.payment_service.dto.request.SearchDto;
import com.example.payment_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setAmount(paymentRequest.getAmount());
            paymentDto.setPaymentMethod(paymentRequest.getPaymentMethod());
            paymentDto.setOrderId(paymentRequest.getOrderId());
            paymentDto.setOrderCode(paymentRequest.getOrderCode()); // Sửa lại dòng này để lấy từ paymentRequest
            // Tạo mới thanh toán
            PaymentDto createdPayment = paymentService.create(paymentDto);

            // Tạo response
            PaymentResponse response = new PaymentResponse();
            response.setPaymentId(createdPayment.getId());
            response.setStatus("SUCCESS");
            response.setTimestamp(java.time.LocalDateTime.now());


            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaymentResponse());
        }
    }

    // Xóa thanh toán
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deletePayment(@PathVariable Integer id) {
        try {
            paymentService.delete(id);
            return ResponseEntity.ok(ResponseDto.success(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.error("Error deleting payment: " + e.getMessage()));
        }
    }

    // Lấy thông tin thanh toán theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<PaymentDto>> getPaymentById(@PathVariable Integer id) {
        Optional<PaymentDto> paymentDTO = paymentService.findById(id);
        return paymentDTO
                .map(payment -> ResponseEntity.ok(ResponseDto.success(payment)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.error("Payment not found")));
    }

    // Tìm kiếm và phân trang
    @PostMapping("/search")
    public ResponseEntity<ResponseDto<?>> searchPayments(@RequestBody SearchDto searchDto) {
        try {
            ResponseDto<?> response = paymentService.find(searchDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.error("Error searching payments: " + e.getMessage()));
        }
    }
}
