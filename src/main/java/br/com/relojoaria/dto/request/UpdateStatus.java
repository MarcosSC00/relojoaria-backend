package br.com.relojoaria.dto.request;

import br.com.relojoaria.enums.ServiceStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UpdateStatus {
    @Enumerated(EnumType.STRING)
    private ServiceStatus status;
}
