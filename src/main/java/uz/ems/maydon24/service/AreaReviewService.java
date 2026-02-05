package uz.ems.maydon24.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.ems.maydon24.exeption.NotFoundException;
import uz.ems.maydon24.models.dto.request.AreaReviewUpsertDto;
import uz.ems.maydon24.models.dto.response.AreaReviewResponseDto;
import uz.ems.maydon24.models.entity.Area;
import uz.ems.maydon24.models.entity.AreaReview;
import uz.ems.maydon24.models.entity.User;
import uz.ems.maydon24.repository.AreaRepository;
import uz.ems.maydon24.repository.AreaReviewRepository;
import uz.ems.maydon24.repository.UserRepository;
import uz.ems.maydon24.repository.projection.AreaReviewProjection;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaReviewService {

    private final AreaRepository areaRepository;
    private final AreaReviewRepository areaReviewRepository;
    private final UserRepository userRepository;

    @Transactional
    public AreaReviewResponseDto create(Long areaId, AreaReviewUpsertDto dto) {
        Area area = getActiveArea(areaId);
        User user = resolveUser(dto.getUserId());
        AreaReview parent = resolveParent(areaId, dto.getParentReviewId());

        Long userId = user == null ? null : user.getId();
        Long parentId = parent == null ? null : parent.getId();

        AreaReview entity = areaReviewRepository.findByAreaIdAndUserIdAndParentReviewIdAndIsDeletedFalse(
                        areaId,
                        userId,
                        parentId)
                .orElse(null);

        if (entity == null) {
            entity = AreaReview.builder()
                    .area(area)
                    .user(user)
                    .parentReview(parent)
                    .build();
            entity.setDeleted(false);
        }

        if (dto.getRating() != null) {
            entity.setRating(dto.getRating());
        }
        if (dto.getComment() != null) {
            entity.setComment(dto.getComment());
        }
        if (dto.getVerified() != null) {
            entity.setIsVerified(dto.getVerified());
        } else if (entity.getIsVerified() == null) {
            entity.setIsVerified(Boolean.FALSE);
        }

        AreaReview saved = areaReviewRepository.save(entity);
        return getById(areaId, saved.getId());
    }

    public List<AreaReviewResponseDto> getAll(Long areaId) {
        getActiveArea(areaId);
        return areaReviewRepository.findAllProjectedByAreaId(areaId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public AreaReviewResponseDto getById(Long areaId, Long id) {
        getActiveArea(areaId);
        AreaReviewProjection projection = areaReviewRepository.findProjectedByIdAndAreaId(id, areaId)
                .orElseThrow(() -> new NotFoundException("Area review not found: " + id));
        return toDto(projection);
    }

    @Transactional
    public AreaReviewResponseDto update(Long areaId, Long id, AreaReviewUpsertDto dto) {
        AreaReview entity = getActiveEntity(areaId, id);
        if (dto.getUserId() != null) {
            entity.setUser(resolveUser(dto.getUserId()));
        }
        if (dto.getRating() != null) {
            entity.setRating(dto.getRating());
        }
        if (dto.getComment() != null) {
            entity.setComment(dto.getComment());
        }
        if (dto.getVerified() != null) {
            entity.setIsVerified(dto.getVerified());
        }
        if (dto.getParentReviewId() != null) {
            entity.setParentReview(resolveParent(areaId, dto.getParentReviewId()));
        }

        areaReviewRepository.save(entity);
        return getById(areaId, id);
    }

    @Transactional
    public void delete(Long areaId, Long id) {
        AreaReview entity = getActiveEntity(areaId, id);
        entity.setDeleted(true);
        areaReviewRepository.save(entity);
    }

    private Area getActiveArea(Long areaId) {
        return areaRepository.findByIdAndIsDeletedFalse(areaId)
                .orElseThrow(() -> new NotFoundException("Area not found: " + areaId));
    }

    private AreaReview getActiveEntity(Long areaId, Long id) {
        getActiveArea(areaId);
        return areaReviewRepository.findByIdAndAreaIdAndIsDeletedFalse(id, areaId)
                .orElseThrow(() -> new NotFoundException("Area review not found: " + id));
    }

    private User resolveUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
    }

    private AreaReview resolveParent(Long areaId, Long parentReviewId) {
        if (parentReviewId == null) {
            return null;
        }
        return areaReviewRepository.findByIdAndAreaIdAndIsDeletedFalse(parentReviewId, areaId)
                .orElseThrow(() -> new NotFoundException("Parent review not found: " + parentReviewId));
    }

    private AreaReviewResponseDto toDto(AreaReviewProjection projection) {
        return AreaReviewResponseDto.builder()
                .id(projection.getId())
                .areaId(projection.getAreaId())
                .userId(projection.getUserId())
                .rating(projection.getRating())
                .comment(projection.getComment())
                .verified(projection.getVerified())
                .parentReviewId(projection.getParentReviewId())
                .build();
    }
}
