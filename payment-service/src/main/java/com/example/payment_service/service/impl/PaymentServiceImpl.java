package com.example.payment_service.service.impl;

import com.example.payment_service.dto.response.PaymentDto;
import com.example.payment_service.dto.response.ResponseDto;
import com.example.payment_service.dto.request.SearchDto;
import com.example.payment_service.entity.Payment;
import com.example.payment_service.repository.PaymentRepository;
import com.example.payment_service.service.PaymentService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class PaymentServiceImpl implements PaymentService {
    @Autowired
    PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentDto create(PaymentDto paymentDto) {

        if (paymentDto.getPaymentMethod() != null && !paymentDto.getPaymentMethod().isEmpty()) {
            // Nếu có nhập phương thức thanh toán, giữ nguyên giá trị
            paymentDto.setPaymentMethod(paymentDto.getPaymentMethod());
        } else {
            // Nếu không có phương thức thanh toán, có thể để mặc định hoặc xử lý khác
            paymentDto.setPaymentMethod("COD");
        }

        paymentDto.setTimestamp(LocalDateTime.now());

        // Kiểm tra nếu orderCode không null hoặc không rỗng
        if (paymentDto.getOrderCode() == null || paymentDto.getOrderCode().isEmpty()) {
            throw new IllegalArgumentException("Order code is required");
        }
        ModelMapper mapper = new ModelMapper();
        Payment payment = mapper.map(paymentDto, Payment.class);
        paymentRepository.save(payment);
        // Chuyển lại Entity thành DTO để trả về
        paymentDto.setId(payment.getId());
        return paymentDto;
    }



    @Override
    @Transactional
    public void delete(Integer id) {
        paymentRepository.deleteById(id);
    }

    @Override
    public Optional<PaymentDto> findById(Integer id) {
        return paymentRepository.findById(id)
                .map(this::convertToDto); // Chuyển từ entity sang DTO
    }


    // Tìm kiếm các Payment với phân trang và sắp xếp
    @Override
    public ResponseDto<List<PaymentDto>> find(SearchDto searchDTO) {
        List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
                .map(order -> {
                    if (order.getOrder().equals(SearchDto.ASC))
                        return Sort.Order.asc(order.getProperty());

                    return Sort.Order.desc(order.getProperty());
                }).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

        // Truy vấn Payments với phân trang
        List<Payment> page = paymentRepository.findAll(pageable).getContent();

        // Chuyển từ List<Payment> sang List<PaymentDTO>
        List<PaymentDto> paymentDTOs = page.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        ResponseDto<List<PaymentDto>> responseDTO = new ResponseDto<>();
        responseDTO.setData(paymentDTOs);
        return responseDTO;
    }

    private PaymentDto convertToDto(Payment payment) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(payment, PaymentDto.class);
    }

}
