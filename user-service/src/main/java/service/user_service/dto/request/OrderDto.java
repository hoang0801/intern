package service.user_service.dto.request;

import lombok.Data;
import service.user_service.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Integer id;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress; // Địa chỉ người nhận

    private Double weight; // Trọng lượng hàng hóa

    private String paymentMethod;

    private UserDto user; // Tên khách hàng (user)

    private String orderCode;

    private Double TotalAmount;

    private Double codAmount;

    private LocalDateTime updatedAt; // Thời gian cập nhật trạng thái đơn hàng









}
