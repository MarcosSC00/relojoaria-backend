package br.com.relojoaria.service.impl;

import br.com.relojoaria.adapter.MaterialUsageAdapter;
import br.com.relojoaria.dto.response.MaterialUsageResponse;
import br.com.relojoaria.entity.MaterialUsage;
import br.com.relojoaria.error.exception.NotFoundException;
import br.com.relojoaria.repository.MaterialUsageRepository;
import br.com.relojoaria.service.MaterialUsageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class MaterialUsageServiceImpl implements MaterialUsageService {

    private final MaterialUsageRepository repository;
    private final MaterialUsageAdapter materialUsageAdapter;

    @Override
    public List<MaterialUsageResponse> getAll() {
        List<MaterialUsage> materialUsages = repository.findAll();
        if (materialUsages.isEmpty()) {
            throw (new NotFoundException("Nenhum uso de material encontrado"));
        }
        return materialUsages.stream().map(materialUsageAdapter::toResponse).toList();
    }

    @Override
    public MaterialUsageResponse getById(Long id) {
        MaterialUsage materialUsage = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nenhum uso de material encontrado"));
        return materialUsageAdapter.toResponse(materialUsage);
    }
}
