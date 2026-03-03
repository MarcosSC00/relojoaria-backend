package br.com.relojoaria.adapter;

import br.com.relojoaria.dto.request.ClientRequest;
import br.com.relojoaria.dto.response.ClientResponse;
import br.com.relojoaria.dto.response.ClientWithServicesResponse;
import br.com.relojoaria.dto.response.ServiceOrderCustom;
import br.com.relojoaria.entity.Client;
import br.com.relojoaria.entity.ServiceOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientAdapter {
    ClientResponse toResponseDTO(Client entity);
    Client toEntity(ClientRequest dto);

    @Mapping(target = "services", source = "serviceOrders")
    ClientWithServicesResponse toResponse(Client client);

    List<ClientWithServicesResponse> toResponseList(List<Client> clients);

    // Mapeia cada ServiceOrder → ServiceOrderCustom
    ServiceOrderCustom toCustom(ServiceOrder serviceOrder);
}
