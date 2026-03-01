package br.com.relojoaria.adapter;

import br.com.relojoaria.dto.request.MaterialUsageRequest;
import br.com.relojoaria.dto.response.MaterialUsageResponse;
import br.com.relojoaria.entity.MaterialUsage;
import br.com.relojoaria.entity.Product;
import br.com.relojoaria.entity.ServiceOrder;
import br.com.relojoaria.error.exception.NotFoundException;
import br.com.relojoaria.repository.ProductRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MaterialUsageAdapter {

    @Autowired
    protected ProductRepository productRepository;

    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productPrice", source = "product.price")
    @Mapping(target = "unit", source = "product.unit")
    public abstract MaterialUsageResponse toResponse(MaterialUsage entity);

    @Mapping(target = "productName", source = "product.name")
    public abstract MaterialUsageRequest toRequest(MaterialUsage entity);

    public MaterialUsage toEntity(MaterialUsageRequest dto) {

        Product product = productRepository.findByName(dto.getProductName())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        MaterialUsage entity = new MaterialUsage();
        entity.setProduct(product);
        entity.setQuantityUsed(dto.getQuantityUsed());

        // cálculo do subtotal
        entity.setSubTotal(
                product.getPrice().multiply(dto.getQuantityUsed())
        );

        return entity;
    }

    public List<MaterialUsage> toEntityList(List<MaterialUsageRequest> list) {
        return list.stream()
                .map(this::toEntity)
                .toList();
    }
}
