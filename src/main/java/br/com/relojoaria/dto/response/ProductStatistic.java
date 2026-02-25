package br.com.relojoaria.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductStatistic {
    private String productName;
    private BigDecimal totalUsed;
    private LocalDateTime date;
}
