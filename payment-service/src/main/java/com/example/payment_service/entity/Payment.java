package com.example.payment_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;             // ID thanh toán

    private Double amount;       // Số tiền thanh toán

    private String paymentMethod; // Phương thức thanh toán (e.g., Credit Card, PayPal)

    private Integer orderId;        // ID đơn hàng liên kết với thanh toán

    private String orderCode;     // mã vận đơn order

    private LocalDateTime timestamp;

}
