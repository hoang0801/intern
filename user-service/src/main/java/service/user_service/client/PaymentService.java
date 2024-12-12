package service.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import service.user_service.dto.MessageDTO;
import service.user_service.dto.request.PaymentRequest;
import service.user_service.dto.response.PaymentResponse;

@FeignClient(name = "payment-service", url = "http://localhost:8081")
public interface PaymentService {

    @PostMapping("/payments/create")
     void createPayment(@RequestBody PaymentRequest paymentRequest) ;
}