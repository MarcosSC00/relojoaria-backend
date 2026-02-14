package br.com.relojoaria.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServiceOrderCustom {
    private Long id;
    private String title;
    private BigDecimal qtdProductUsed;
    private BigDecimal totalProductPrice;
    private Long amountService;

    public ServiceOrderCustom(
            Long id,
            String title,
            BigDecimal qtdProductUsed,
            BigDecimal totalProductPrice,
            Long amountService
    ) {
        this.id = id;
        this.title = title;
        this.qtdProductUsed = qtdProductUsed;
        this.totalProductPrice = totalProductPrice;
        this.amountService = amountService;
    }
}
