package br.com.relojoaria.dto;

import br.com.relojoaria.enums.UnitType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    @NotBlank(message = "Product name is required")
    @Size(max = 100, min = 3, message = "invalid product name")
    private String name;

    @NotNull(groups = UnitType.class)
    private UnitType unit;

    @NotNull
    @Digits(integer = 7, fraction = 3, message = "The price must have a maximum of three decimal places")
    private BigDecimal price;
}
