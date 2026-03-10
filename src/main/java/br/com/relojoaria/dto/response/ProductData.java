package br.com.relojoaria.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductData {
    private String name;
    private BigDecimal quantity;
    private BigDecimal current_qtd;
}
