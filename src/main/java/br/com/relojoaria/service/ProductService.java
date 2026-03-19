package br.com.relojoaria.service;

import br.com.relojoaria.dto.request.ProductRequestDto;
import br.com.relojoaria.dto.response.ProductAnalysis;
import br.com.relojoaria.dto.response.ProductData;
import br.com.relojoaria.dto.response.ProductResponseDto;

import java.util.List;

public interface ProductService {
    ProductResponseDto create(ProductRequestDto productRequestDto);
    ProductResponseDto update(String productName, ProductRequestDto productRequestDto);
    ProductResponseDto getByName(String productName);
    ProductResponseDto getById(Long productId);
    List<ProductResponseDto> getAll();
    void delete(String productName);
    ProductAnalysis  getProductAnalysis(String productName);
    List<String> getJustNameProducts();
    List<ProductData> getProductDataChart();
}
