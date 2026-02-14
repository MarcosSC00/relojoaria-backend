package br.com.relojoaria.dto.response;

import br.com.relojoaria.enums.UnitType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductAnalysis {
    private Long productId;
    private String productName;
    private String productUnity;
    private BigDecimal currentProductQtd;
    private BigDecimal productPrice;
    private BigDecimal totalProductUsed;
    private BigDecimal totalPrice;

    public ProductAnalysis(Long productId,
                           String productName,
                           String productUnity,
                           BigDecimal currentProductQtd,
                           BigDecimal productPrice,
                           BigDecimal totalProductUsed,
                           BigDecimal totalPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productUnity = productUnity;
        this.currentProductQtd = currentProductQtd;
        this.productPrice = productPrice;
        this.totalProductUsed = totalProductUsed;
        this.totalPrice = totalPrice;
    }
}
