package uz.ems.maydon24.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.ems.maydon24.exeption.NotFoundException;
import uz.ems.maydon24.models.dto.request.AreaPriceUpsertDto;
import uz.ems.maydon24.models.dto.response.AreaPriceResponseDto;
import uz.ems.maydon24.models.entity.Area;
import uz.ems.maydon24.models.entity.AreaPrice;
import uz.ems.maydon24.models.enums.AreaPriceType;
import uz.ems.maydon24.repository.AreaPriceRepository;
import uz.ems.maydon24.repository.AreaRepository;
import uz.ems.maydon24.repository.projection.AreaPriceProjection;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaPriceService {

    private final AreaRepository areaRepository;
    private final AreaPriceRepository areaPriceRepository;

    @Transactional
    public AreaPriceResponseDto create(Long areaId, AreaPriceUpsertDto dto) {
        Area area = getActiveArea(areaId);

        AreaPrice entity = null;
        if (dto.getType() != null) {
            entity = areaPriceRepository
                    .findByAreaIdAndTypeAndIsDeletedFalse(areaId, dto.getType())
                    .orElse(null);
        }

        if (entity == null) {
            entity = AreaPrice.builder()
                    .area(area)
                    .build();
            entity.setDeleted(false);
        }

        entity.setType(dto.getType());
        if (dto.getPricePerHour() != null) {
            entity.setPricePerHour(dto.getPricePerHour());
        }

        AreaPrice saved = areaPriceRepository.save(entity);
        return getById(areaId, saved.getId());
    }

    public List<AreaPriceResponseDto> getAll(Long areaId) {
        getActiveArea(areaId);
        return areaPriceRepository.findAllProjectedByAreaId(areaId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public AreaPriceResponseDto getById(Long areaId, Long id) {
        getActiveArea(areaId);
        AreaPriceProjection projection = areaPriceRepository.findProjectedByIdAndAreaId(id, areaId)
                .orElseThrow(() -> new NotFoundException("Area price not found: " + id));
        return toDto(projection);
    }

    @Transactional
    public AreaPriceResponseDto update(Long areaId, Long id, AreaPriceUpsertDto dto) {
        AreaPrice entity = getActiveEntity(areaId, id);
        if (dto.getType() != null) {
            entity.setType(dto.getType());
        }
        if (dto.getPricePerHour() != null) {
            entity.setPricePerHour(dto.getPricePerHour());
        }
        areaPriceRepository.save(entity);
        return getById(areaId, id);
    }

    @Transactional
    public void delete(Long areaId, Long id) {
        AreaPrice entity = getActiveEntity(areaId, id);
        entity.setDeleted(true);
        areaPriceRepository.save(entity);
    }

    private Area getActiveArea(Long areaId) {
        return areaRepository.findByIdAndIsDeletedFalse(areaId)
                .orElseThrow(() -> new NotFoundException("Area not found: " + areaId));
    }

    private AreaPrice getActiveEntity(Long areaId, Long id) {
        getActiveArea(areaId);
        return areaPriceRepository.findByIdAndAreaIdAndIsDeletedFalse(id, areaId)
                .orElseThrow(() -> new NotFoundException("Area price not found: " + id));
    }

    private AreaPriceResponseDto toDto(AreaPriceProjection projection) {
        return AreaPriceResponseDto.builder()
                .id(projection.getId())
                .areaId(projection.getAreaId())
                .type(parsePriceType(projection.getType()))
                .pricePerHour(projection.getPricePerHour())
                .build();
    }

    private AreaPriceType parsePriceType(String type) {
        if (type == null) {
            return null;
        }
        return AreaPriceType.valueOf(type);
    }
}
