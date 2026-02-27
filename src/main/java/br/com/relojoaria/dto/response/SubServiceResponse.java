package br.com.relojoaria.dto.response;

import br.com.relojoaria.enums.ServiceStatus;
import br.com.relojoaria.enums.ServiceType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SubServiceResponse {
    private String title;
    private String description;
    private BigDecimal price;
}
