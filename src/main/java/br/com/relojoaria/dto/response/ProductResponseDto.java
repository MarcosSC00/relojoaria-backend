package br.com.relojoaria.dto.response;

import br.com.relojoaria.enums.UnitType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private UnitType unit;
    private BigDecimal price;
}
