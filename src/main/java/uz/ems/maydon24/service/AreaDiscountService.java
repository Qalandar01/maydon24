package uz.ems.maydon24.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.ems.maydon24.exeption.NotFoundException;
import uz.ems.maydon24.models.dto.request.AreaDiscountUpsertDto;
import uz.ems.maydon24.models.dto.response.AreaDiscountResponseDto;
import uz.ems.maydon24.models.entity.Area;
import uz.ems.maydon24.models.entity.AreaDiscount;
import uz.ems.maydon24.models.entity.User;
import uz.ems.maydon24.repository.AreaDiscountRepository;
import uz.ems.maydon24.repository.AreaRepository;
import uz.ems.maydon24.repository.UserRepository;
import uz.ems.maydon24.repository.projection.AreaDiscountProjection;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaDiscountService {

    private final AreaRepository areaRepository;
    private final AreaDiscountRepository areaDiscountRepository;
    private final UserRepository userRepository;

    @Transactional
    public AreaDiscountResponseDto create(Long areaId, AreaDiscountUpsertDto dto) {
        Area area = getActiveArea(areaId);
        User user = resolveUser(dto.getUserId());
        Long userId = user == null ? null : user.getId();

        AreaDiscount entity = areaDiscountRepository
                .findByAreaIdAndUserIdAndFromDateAndToDateAndFromTimeAndToTimeAndIsDeletedFalse(
                        areaId,
                        userId,
                        dto.getFromDate(),
                        dto.getToDate(),
                        dto.getFromTime(),
                        dto.getToTime())
                .orElse(null);

        if (entity == null) {
            entity = AreaDiscount.builder()
                    .area(area)
                    .user(user)
                    .fromDate(dto.getFromDate())
                    .toDate(dto.getToDate())
                    .fromTime(dto.getFromTime())
                    .toTime(dto.getToTime())
                    .build();
            entity.setDeleted(false);
        }

        if (dto.getDiscountPercent() != null) {
            entity.setDiscountPercent(dto.getDiscountPercent());
        }
        if (dto.getActive() != null) {
            entity.setActive(dto.getActive());
        } else if (entity.getActive() == null) {
            entity.setActive(Boolean.TRUE);
        }

        AreaDiscount saved = areaDiscountRepository.save(entity);
        return getById(areaId, saved.getId());
    }

    public List<AreaDiscountResponseDto> getAll(Long areaId) {
        getActiveArea(areaId);
        return areaDiscountRepository.findAllProjectedByAreaId(areaId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public AreaDiscountResponseDto getById(Long areaId, Long id) {
        getActiveArea(areaId);
        AreaDiscountProjection projection = areaDiscountRepository.findProjectedByIdAndAreaId(id, areaId)
                .orElseThrow(() -> new NotFoundException("Area discount not found: " + id));
        return toDto(projection);
    }

    @Transactional
    public AreaDiscountResponseDto update(Long areaId, Long id, AreaDiscountUpsertDto dto) {
        AreaDiscount entity = getActiveEntity(areaId, id);
        if (dto.getDiscountPercent() != null) {
            entity.setDiscountPercent(dto.getDiscountPercent());
        }
        if (dto.getFromDate() != null) {
            entity.setFromDate(dto.getFromDate());
        }
        if (dto.getToDate() != null) {
            entity.setToDate(dto.getToDate());
        }
        if (dto.getFromTime() != null) {
            entity.setFromTime(dto.getFromTime());
        }
        if (dto.getToTime() != null) {
            entity.setToTime(dto.getToTime());
        }
        if (dto.getUserId() != null) {
            entity.setUser(resolveUser(dto.getUserId()));
        }
        if (dto.getActive() != null) {
            entity.setActive(dto.getActive());
        }
        areaDiscountRepository.save(entity);
        return getById(areaId, id);
    }

    @Transactional
    public void delete(Long areaId, Long id) {
        AreaDiscount entity = getActiveEntity(areaId, id);
        entity.setDeleted(true);
        areaDiscountRepository.save(entity);
    }

    private Area getActiveArea(Long areaId) {
        return areaRepository.findByIdAndIsDeletedFalse(areaId)
                .orElseThrow(() -> new NotFoundException("Area not found: " + areaId));
    }

    private AreaDiscount getActiveEntity(Long areaId, Long id) {
        getActiveArea(areaId);
        return areaDiscountRepository.findByIdAndAreaIdAndIsDeletedFalse(id, areaId)
                .orElseThrow(() -> new NotFoundException("Area discount not found: " + id));
    }

    private User resolveUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
    }

    private AreaDiscountResponseDto toDto(AreaDiscountProjection projection) {
        return AreaDiscountResponseDto.builder()
                .id(projection.getId())
                .areaId(projection.getAreaId())
                .discountPercent(projection.getDiscountPercent())
                .fromDate(projection.getFromDate())
                .toDate(projection.getToDate())
                .fromTime(projection.getFromTime())
                .toTime(projection.getToTime())
                .userId(projection.getUserId())
                .active(projection.getActive())
                .build();
    }
}
