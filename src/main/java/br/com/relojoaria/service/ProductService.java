package br.com.relojoaria.service;

import br.com.relojoaria.dto.ProductDto;
import java.util.List;

public interface ProductService {
    ProductDto create(ProductDto productRequestDto);
    ProductDto update(String productName, ProductDto productRequestDto);
    ProductDto getByName(String productName);
    ProductDto getById(Long productId);
    List<ProductDto> getAll();
    void delete(String productName);

}
