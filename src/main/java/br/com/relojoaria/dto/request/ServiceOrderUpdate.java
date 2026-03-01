package br.com.relojoaria.dto.request;

import br.com.relojoaria.enums.ServiceStatus;
import br.com.relojoaria.enums.ServiceType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ServiceOrderUpdate {
    private String clientName;

    @Size(max = 200, message = "Title must be at most 200 characters")
    private String title;

    @Size(max = 200, message = "Description must be at most 200 characters")
    private String description;

    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    @Enumerated(EnumType.STRING)
    private ServiceType type;

    @Digits(integer = 8, fraction = 2, message = "The value must have a maximum of three decimal places")
    private BigDecimal addValue = BigDecimal.ZERO;

    private LocalDateTime endDate;

}
