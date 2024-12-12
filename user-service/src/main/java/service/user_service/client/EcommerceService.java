package service.user_service.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import service.user_service.dto.request.OrderDto;

@FeignClient(name = "ecommerce-service", url = "http://localhost:8080")
public interface EcommerceService {


    @PostMapping("/order/add")
    void add(@RequestBody @Valid OrderDto orderDto);
}
