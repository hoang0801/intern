package com.example.payment_service.service;

import com.example.payment_service.dto.response.PaymentDto;
import com.example.payment_service.dto.response.ResponseDto;
import com.example.payment_service.dto.request.SearchDto;


import java.util.List;
import java.util.Optional;

public interface PaymentService {
    // Tạo mới Payment
    PaymentDto create(PaymentDto paymentDto);


    // Xóa Payment theo ID
    void delete(Integer id);

    // Tìm Payment theo ID
    Optional<PaymentDto> findById(Integer id);

    // Tìm kiếm Payments với phân trang và sắp xếp
    ResponseDto<List<PaymentDto>> find(SearchDto searchDTO);
}
