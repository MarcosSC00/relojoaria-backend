package br.com.relojoaria.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MonthlyServiceCountDto {
    private int month;
    private BigDecimal total;
}
