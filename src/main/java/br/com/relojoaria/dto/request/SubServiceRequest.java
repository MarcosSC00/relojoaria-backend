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

    @NotBlank(message = "title is required")
    private String price;
}
