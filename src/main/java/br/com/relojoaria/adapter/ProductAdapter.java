package br.com.relojoaria.adapter;

import br.com.relojoaria.dto.request.ProductRequestDto;
import br.com.relojoaria.dto.response.ProductResponseDto;
import br.com.relojoaria.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ProductAdapter {

    ProductResponseDto toDto(Product product);

    @Mapping(target = "price", source = "price")
    Product toEntity(ProductRequestDto productRequestDto);

    default BigDecimal map(String value) {
        if (value == null || value.isBlank()) return null;

        try {
            value = value.replace(".", "").replace(",", ".");
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Preço inválido: " + value);
        }
    }
}
