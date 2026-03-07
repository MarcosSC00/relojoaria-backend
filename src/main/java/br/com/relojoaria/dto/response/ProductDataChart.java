package br.com.relojoaria.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductDataChart {
    private String name;
    private BigDecimal quantity;
}
