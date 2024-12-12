package com.example.payment_service.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class PaymentDto {
    private Integer id;             // ID thanh toán

    private Double amount;       // Số tiền thanh toán

    private String paymentMethod; // Phương thức thanh toán

    private String orderCode;

    private LocalDateTime timestamp;

    private Integer orderId;        // ID đơn hàng
}
