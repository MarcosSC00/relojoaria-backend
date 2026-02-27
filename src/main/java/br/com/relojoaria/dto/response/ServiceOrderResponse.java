package br.com.relojoaria.dto.response;

import br.com.relojoaria.enums.ServiceStatus;
import br.com.relojoaria.enums.ServiceType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ServiceOrderResponse {
    private Long id;
    private Long clientId;
    private String clientName;
    private String title;
    private String description;
    private ServiceStatus status;
    private BigDecimal addValue;
    private BigDecimal subServicesPrice;
    private ServiceType type;
    private List<MaterialUsageResponse> items;
    private List<SubServiceResponse> subServices;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime endDate;
}
