package br.com.relojoaria.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClientWithServicesResponse {
    private Long id;
    private String name;
    private String phone;
    private LocalDateTime createdAt;
    private List<ServiceOrderCustom> services;
}
