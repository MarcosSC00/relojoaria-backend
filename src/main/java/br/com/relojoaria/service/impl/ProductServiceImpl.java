package br.com.relojoaria.service.impl;

import br.com.relojoaria.adapter.ProductAdapter;
import br.com.relojoaria.dto.request.ProductRequestDto;
import br.com.relojoaria.dto.response.ProductAnalysis;
import br.com.relojoaria.dto.response.ProductData;
import br.com.relojoaria.dto.response.ProductResponseDto;
import br.com.relojoaria.entity.Product;
import br.com.relojoaria.error.exception.NotFoundException;
import br.com.relojoaria.repository.ProductRepository;
import br.com.relojoaria.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductAdapter productAdapter;

    @Override
    public ProductResponseDto create(ProductRequestDto productRequestDto) {
        Product entity = productAdapter.toEntity(productRequestDto);
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        if (productRepository.existsByName(entity.getName())) {
            throw new IllegalArgumentException("Produto já existente");
        }
        Product saved = productRepository.save(entity);
        return productAdapter.toDto(saved);
    }

    @Override
    public ProductResponseDto update(String productName, ProductRequestDto productRequestDto) {
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
        product.setName(productRequestDto.getName());
        product.setUnit(productRequestDto.getUnit());
        product.setPrice(parsePrice(productRequestDto.getPrice()));

        productRepository.save(product);
        return productAdapter.toDto(product);
    }

    @Override
    public ProductResponseDto getByName(String productName) {
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
        return productAdapter.toDto(product);
    }

    @Override
    public ProductResponseDto getById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
        return productAdapter.toDto(product);
    }

    @Override
    public List<ProductResponseDto> getAll() {
        List<Product> products = productRepository.findAll();
        if(products.isEmpty()) {
            return new ArrayList<>();
        }
        return products.stream()
                .map(productAdapter::toDto).toList();
    }

    @Override
    public void delete(String productName) {
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
        productRepository.delete(product);
    }

    @Override
    public ProductAnalysis getProductAnalysis(String productName) {
        ProductAnalysis productAnalysis =  productRepository.getProductAnalysisByName(productName);
        if(productAnalysis == null) {
            throw new NotFoundException("Dados não encontrados");
        }
        return productAnalysis;
    }

    @Override
    public List<String> getJustNameProducts() {
        List<String> result = productRepository.getJustNameProducts();
        if(result.isEmpty()) {
            return new ArrayList<>();
        }
        return result;
    }

    @Override
    public List<ProductData> getProductDataChart() {
        List<ProductData> result = productRepository.getProductDataChart();
        if(result.isEmpty()) {
            return new ArrayList<>();
        }
        return result;
    }

    public BigDecimal parsePrice(String value) {
        if (value == null || value.isBlank()) return null;

        // remove separador de milhar
        value = value.replace(".", "");

        // troca vírgula por ponto
        value = value.replace(",", ".");

        return new BigDecimal(value);
    }

}
