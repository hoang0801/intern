package com.example.payment_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    private Double amount;       // Số tiền thanh toán

    private String paymentMethod; // Phương thức thanh toán

    private Integer orderId;        // ID đơn hàng

    private String orderCode;  //mvđ

}
