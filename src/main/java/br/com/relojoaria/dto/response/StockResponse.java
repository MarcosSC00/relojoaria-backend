package br.com.relojoaria.dto.response;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

@Data
public class StockResponse {
    private Long id;
    private String productName;
    private BigDecimal price;
    private BigDecimal currentQuantity;
    private String unit;
    private BigDecimal qtdUsed;

    @PrePersist
    public void prePersist() {
        DecimalFormat df = new DecimalFormat("#.#00,00");
        BigDecimal formatedPrice = new BigDecimal(df.format(this.currentQuantity));
        setCurrentQuantity(formatedPrice);
    }
}
