package com.example.payment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {

    private int status; // HTTP status code (200, 400, 500, etc.)
    private String msg; // Message to describe the response

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data; // Generic data payload

    // Constructor bổ sung để chỉ thêm `status` và `msg`
    public ResponseDto(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    // Tiện ích để tạo ResponseDto cho các trường hợp thành công
    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(200, "Success", data);
    }

    // Tiện ích để tạo ResponseDto cho các trường hợp lỗi
    public static <T> ResponseDto<T> error(String msg) {
        return new ResponseDto<>(500, msg, null);
    }

    // Tiện ích để tạo ResponseDto khi không có dữ liệu trả về
    public static <T> ResponseDto<T> noData() {
        return new ResponseDto<>(204, "No Content", null);
    }

    // Tiện ích để tạo ResponseDto khi có yêu cầu không hợp lệ
    public static <T> ResponseDto<T> badRequest(String msg) {
        return new ResponseDto<>(400, msg, null);
    }
}
