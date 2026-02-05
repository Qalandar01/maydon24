package uz.ems.maydon24.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.ems.maydon24.exeption.NotFoundException;
import uz.ems.maydon24.models.dto.request.AreaFeatureUpsertDto;
import uz.ems.maydon24.models.dto.response.AreaFeatureResponseDto;
import uz.ems.maydon24.models.entity.Area;
import uz.ems.maydon24.models.entity.AreaFeature;
import uz.ems.maydon24.models.enums.AreaFeatureType;
import uz.ems.maydon24.repository.AreaFeatureRepository;
import uz.ems.maydon24.repository.AreaRepository;
import uz.ems.maydon24.repository.projection.AreaFeatureProjection;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaFeatureService {

    private final AreaRepository areaRepository;
    private final AreaFeatureRepository areaFeatureRepository;

    @Transactional
    public AreaFeatureResponseDto create(Long areaId, AreaFeatureUpsertDto dto) {
        Area area = getActiveArea(areaId);

        AreaFeature entity = null;
        if (dto.getFeatureName() != null) {
            entity = areaFeatureRepository
                    .findByAreaIdAndFeatureNameAndIsDeletedFalse(areaId, dto.getFeatureName())
                    .orElse(null);
        }

        if (entity == null) {
            entity = AreaFeature.builder()
                    .area(area)
                    .build();
            entity.setDeleted(false);
        }

        entity.setFeatureName(dto.getFeatureName());
        if (dto.getAvailable() != null) {
            entity.setAvailable(dto.getAvailable());
        }

        AreaFeature saved = areaFeatureRepository.save(entity);
        return getById(areaId, saved.getId());
    }

    public List<AreaFeatureResponseDto> getAll(Long areaId) {
        getActiveArea(areaId);
        return areaFeatureRepository.findAllProjectedByAreaId(areaId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public AreaFeatureResponseDto getById(Long areaId, Long id) {
        getActiveArea(areaId);
        AreaFeatureProjection projection = areaFeatureRepository.findProjectedByIdAndAreaId(id, areaId)
                .orElseThrow(() -> new NotFoundException("Area feature not found: " + id));
        return toDto(projection);
    }

    @Transactional
    public AreaFeatureResponseDto update(Long areaId, Long id, AreaFeatureUpsertDto dto) {
        AreaFeature entity = getActiveEntity(areaId, id);
        if (dto.getFeatureName() != null) {
            entity.setFeatureName(dto.getFeatureName());
        }
        if (dto.getAvailable() != null) {
            entity.setAvailable(dto.getAvailable());
        }
        areaFeatureRepository.save(entity);
        return getById(areaId, id);
    }

    @Transactional
    public void delete(Long areaId, Long id) {
        AreaFeature entity = getActiveEntity(areaId, id);
        entity.setDeleted(true);
        areaFeatureRepository.save(entity);
    }

    private Area getActiveArea(Long areaId) {
        return areaRepository.findByIdAndIsDeletedFalse(areaId)
                .orElseThrow(() -> new NotFoundException("Area not found: " + areaId));
    }

    private AreaFeature getActiveEntity(Long areaId, Long id) {
        getActiveArea(areaId);
        return areaFeatureRepository.findByIdAndAreaIdAndIsDeletedFalse(id, areaId)
                .orElseThrow(() -> new NotFoundException("Area feature not found: " + id));
    }

    private AreaFeatureResponseDto toDto(AreaFeatureProjection projection) {
        return AreaFeatureResponseDto.builder()
                .id(projection.getId())
                .areaId(projection.getAreaId())
                .featureName(parseFeatureName(projection.getFeatureName()))
                .available(projection.getAvailable())
                .build();
    }

    private AreaFeatureType parseFeatureName(String featureName) {
        if (featureName == null) {
            return null;
        }
        return AreaFeatureType.valueOf(featureName);
    }
}
