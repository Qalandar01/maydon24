package uz.ems.maydon24.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.ems.maydon24.exeption.NotFoundException;
import uz.ems.maydon24.models.dto.request.AreaScheduleUpsertDto;
import uz.ems.maydon24.models.dto.response.AreaScheduleResponseDto;
import uz.ems.maydon24.models.entity.Area;
import uz.ems.maydon24.models.entity.AreaSchedule;
import uz.ems.maydon24.models.entity.User;
import uz.ems.maydon24.repository.AreaRepository;
import uz.ems.maydon24.repository.AreaScheduleRepository;
import uz.ems.maydon24.repository.UserRepository;
import uz.ems.maydon24.repository.projection.AreaScheduleProjection;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaScheduleService {

    private final AreaRepository areaRepository;
    private final AreaScheduleRepository areaScheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public AreaScheduleResponseDto create(Long areaId, AreaScheduleUpsertDto dto) {
        Area area = getActiveArea(areaId);

        AreaSchedule entity = areaScheduleRepository
                .findByAreaIdAndDateAndStartTimeAndEndTimeAndIsDeletedFalse(
                        areaId,
                        dto.getDate(),
                        dto.getStartTime(),
                        dto.getEndTime())
                .orElse(null);

        if (entity == null) {
            entity = AreaSchedule.builder()
                    .area(area)
                    .date(dto.getDate())
                    .startTime(dto.getStartTime())
                    .endTime(dto.getEndTime())
                    .build();
            entity.setDeleted(false);
        }

        if (dto.getBooked() != null) {
            entity.setBooked(dto.getBooked());
        } else if (entity.getBooked() == null) {
            entity.setBooked(Boolean.FALSE);
        }
        entity.setBookedBy(resolveUser(dto.getBookedById()));

        AreaSchedule saved = areaScheduleRepository.save(entity);
        return getById(areaId, saved.getId());
    }

    public List<AreaScheduleResponseDto> getAll(Long areaId) {
        getActiveArea(areaId);
        return areaScheduleRepository.findAllProjectedByAreaId(areaId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public AreaScheduleResponseDto getById(Long areaId, Long id) {
        getActiveArea(areaId);
        AreaScheduleProjection projection = areaScheduleRepository.findProjectedByIdAndAreaId(id, areaId)
                .orElseThrow(() -> new NotFoundException("Area schedule not found: " + id));
        return toDto(projection);
    }

    @Transactional
    public AreaScheduleResponseDto update(Long areaId, Long id, AreaScheduleUpsertDto dto) {
        AreaSchedule entity = getActiveEntity(areaId, id);
        if (dto.getDate() != null) {
            entity.setDate(dto.getDate());
        }
        if (dto.getStartTime() != null) {
            entity.setStartTime(dto.getStartTime());
        }
        if (dto.getEndTime() != null) {
            entity.setEndTime(dto.getEndTime());
        }
        if (dto.getBooked() != null) {
            entity.setBooked(dto.getBooked());
        }
        entity.setBookedBy(resolveUser(dto.getBookedById()));
        areaScheduleRepository.save(entity);
        return getById(areaId, id);
    }

    @Transactional
    public void delete(Long areaId, Long id) {
        AreaSchedule entity = getActiveEntity(areaId, id);
        entity.setDeleted(true);
        areaScheduleRepository.save(entity);
    }

    private Area getActiveArea(Long areaId) {
        return areaRepository.findByIdAndIsDeletedFalse(areaId)
                .orElseThrow(() -> new NotFoundException("Area not found: " + areaId));
    }

    private AreaSchedule getActiveEntity(Long areaId, Long id) {
        getActiveArea(areaId);
        return areaScheduleRepository.findByIdAndAreaIdAndIsDeletedFalse(id, areaId)
                .orElseThrow(() -> new NotFoundException("Area schedule not found: " + id));
    }

    private User resolveUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
    }

    private AreaScheduleResponseDto toDto(AreaScheduleProjection projection) {
        return AreaScheduleResponseDto.builder()
                .id(projection.getId())
                .areaId(projection.getAreaId())
                .date(projection.getDate())
                .startTime(projection.getStartTime())
                .endTime(projection.getEndTime())
                .booked(projection.getBooked())
                .bookedById(projection.getBookedById())
                .build();
    }
}
