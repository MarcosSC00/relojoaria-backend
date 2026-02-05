package br.com.relojoaria.adapter;

import br.com.relojoaria.dto.ProductDto;
import br.com.relojoaria.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductAdapter {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "unit", source = "unit")
    @Mapping(target = "price", source = "price")
    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);
}
