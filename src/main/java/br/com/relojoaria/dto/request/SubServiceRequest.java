package br.com.relojoaria.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubServiceRequest {
    @NotBlank(message = "title is required")
    @Size(min = 3, max = 100, message = "invalid title")
    private String title;

    @Size(min = 3, max = 200, message = "invalid description")
    private String description;

    @NotNull(message = "price is required")
    @Digits(integer = 8, fraction = 2, message = "The value must have a maximum of three decimal places")
    @DecimalMin(value = "1.00", message = "the value must be greater than 1.00")
    private BigDecimal price;
}
