package com.example.payment_service.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SearchDto {
    public static final int MAX_20 = 20;
    public static final int MAX_200 = 200;
    public static final String ASC = "asc";
    public static final String DESC = "desc";

    @Min(value = 0)
    private int page;

    @Min(value = 1)
    @Max(value = MAX_200)
    @Builder.Default
    private int size = MAX_20;

    @Builder.Default
    private String value = "";

    private Integer id;
    
    private List<OrderBy> orders;

    @Builder.Default
    private Map<String, String> filterBys = new HashMap<>();

    @Data
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderBy {
        @Builder.Default
        private String order = ASC;// asc, desc
        private String property;
    }
}
