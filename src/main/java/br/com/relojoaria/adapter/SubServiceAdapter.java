package br.com.relojoaria.adapter;

import br.com.relojoaria.dto.response.SubServiceResponse;
import br.com.relojoaria.entity.SubService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubServiceAdapter {

    SubServiceResponse toResponse(SubService subServiceOrder);
    SubService toEntity(SubServiceResponse subServiceOrderResponse);
}
