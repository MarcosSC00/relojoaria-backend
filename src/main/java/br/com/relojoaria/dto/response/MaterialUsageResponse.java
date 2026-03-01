package br.com.relojoaria.dto.response;

import br.com.relojoaria.enums.UnitType;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class MaterialUsageResponse {
    private Long id;
    private String productName;
    private UnitType unit;
    private BigDecimal productPrice;
    private BigDecimal quantityUsed;
    private BigDecimal subTotal;
}
