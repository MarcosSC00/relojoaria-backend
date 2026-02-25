package br.com.relojoaria.dto.response;

import br.com.relojoaria.enums.ServiceStatus;
import br.com.relojoaria.enums.ServiceType;
import lombok.Data;

import java.util.List;

@Data
public class SubServiceResponse {
    private String title;
    private String description;
}
