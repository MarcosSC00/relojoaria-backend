package br.com.relojoaria.service;

import br.com.relojoaria.dto.ProductDto;
import br.com.relojoaria.dto.response.ProductAnalysis;
import br.com.relojoaria.dto.response.ProductDataChart;

import java.util.List;

public interface ProductService {
    ProductDto create(ProductDto productRequestDto);
    ProductDto update(String productName, ProductDto productRequestDto);
    ProductDto getByName(String productName);
    ProductDto getById(Long productId);
    List<ProductDto> getAll();
    void delete(String productName);
    ProductAnalysis  getProductAnalysis(String productName);
    List<String> getJustNameProducts();
    List<ProductDataChart> getProductDataChart();
}
