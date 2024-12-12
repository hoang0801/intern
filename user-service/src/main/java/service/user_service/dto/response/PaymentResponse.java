package service.user_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Integer paymentId;     // ID thanh toán

    private String status;      // Trạng thái thanh toán (e.g., SUCCESS, FAILED)

    private LocalDateTime timestamp; // Thời gian thanh toán
}