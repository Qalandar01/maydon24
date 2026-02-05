package uz.ems.maydon24.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.ems.maydon24.exeption.NotFoundException;
import uz.ems.maydon24.models.dto.request.AreaCreateDto;
import uz.ems.maydon24.models.dto.request.AreaUpdateDto;
import uz.ems.maydon24.models.dto.response.AreaResponseDto;
import uz.ems.maydon24.models.entity.*;
import uz.ems.maydon24.models.enums.AreaType;
import uz.ems.maydon24.models.enums.BookingStatus;
import uz.ems.maydon24.repository.*;
import uz.ems.maydon24.repository.projection.AreaMediaProjection;
import uz.ems.maydon24.repository.projection.AreaProjection;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AreaService {

    private final AreaRepository areaRepository;
    private final AreaMediaRepository areaMediaRepository;
    private final AreaFeatureRepository areaFeatureRepository;
    private final AreaPriceRepository areaPriceRepository;
    private final AreaDiscountRepository areaDiscountRepository;
    private final AreaScheduleRepository areaScheduleRepository;
    private final AreaReviewRepository areaReviewRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public AreaResponseDto create(AreaCreateDto dto) {
        Area area = resolveAreaForUpsert(dto)
                .orElseGet(() -> {
                    Area entity = new Area();
                    entity.setDeleted(false);
                    return entity;
                });

        mergeCreateData(area, dto);
        areaRepository.save(area);

        if (dto.getMediaIds() != null) {
            softDeleteMedia(area.getId());
            bindMediaIds(area, dto.getMediaIds());
        }

        return getById(area.getId());
    }

    public AreaResponseDto getById(Long id) {
        AreaProjection projection = areaRepository.findProjectedById(id)
                .orElseThrow(() -> new NotFoundException("Area not found: " + id));
        return toDto(projection);
    }

    public Page<AreaResponseDto> getAll(Pageable pageable) {
        return areaRepository.findAllActiveProjected(pageable)
                .map(this::toDto);
    }

    @Transactional
    public AreaResponseDto update(Long id, AreaUpdateDto dto, List<MultipartFile> files) {
        Area area = getActiveArea(id);

        mergeUpdateData(area, dto);
        areaRepository.save(area);

        if (files != null && !files.isEmpty()) {
            softDeleteMedia(id);
            saveFiles(area, files);
        }

        return getById(id);
    }

    @Transactional
    public void delete(Long id) {
        Area area = getActiveArea(id);
        area.setDeleted(true);
        areaRepository.save(area);

        softDeleteMedia(id);
        softDeleteFeatures(id);
        softDeletePrices(id);
        softDeleteDiscounts(id);
        softDeleteSchedules(id);
        softDeleteReviews(id);
        softDeleteBookings(id);
    }

    private Optional<Area> resolveAreaForUpsert(AreaCreateDto dto) {
        if (!StringUtils.hasText(dto.getName()) || !StringUtils.hasText(dto.getPhoneNumber())) {
            return Optional.empty();
        }
        return areaRepository.findByNameAndPhoneNumberAndIsDeletedFalse(dto.getName(), dto.getPhoneNumber());
    }

    private void mergeCreateData(Area area, AreaCreateDto dto) {
        if (dto.getName() != null) {
            area.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            area.setDescription(dto.getDescription());
        }
        if (dto.getPhoneNumber() != null) {
            area.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getHeight() != null) {
            area.setHeight(dto.getHeight());
        }
        if (dto.getWidth() != null) {
            area.setWidth(dto.getWidth());
        }
        if (dto.getAreaType() != null) {
            area.setAreaType(dto.getAreaType());
        }

        if (dto.getAddress() != null) {
            area.setAddress(dto.getAddress());
        } else if (area.getAddress() == null) {
            area.setAddress("");
        }

        if (dto.getLatitude() != null) {
            area.setLatitude(dto.getLatitude());
        } else if (area.getLatitude() == null) {
            area.setLatitude(0D);
        }

        if (dto.getLongitude() != null) {
            area.setLongitude(dto.getLongitude());
        } else if (area.getLongitude() == null) {
            area.setLongitude(0D);
        }

        if (dto.getVisibility() != null) {
            area.setVisibility(dto.getVisibility());
        }
    }

    private void mergeUpdateData(Area area, AreaUpdateDto dto) {
        if (dto.getName() != null) {
            area.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            area.setDescription(dto.getDescription());
        }
        if (dto.getPhoneNumber() != null) {
            area.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getAddress() != null) {
            area.setAddress(dto.getAddress());
        }
        if (dto.getLatitude() != null) {
            area.setLatitude(dto.getLatitude());
        }
        if (dto.getLongitude() != null) {
            area.setLongitude(dto.getLongitude());
        }
        if (dto.getVisibility() != null) {
            area.setVisibility(dto.getVisibility());
        }
        if (dto.getHeight() != null) {
            area.setHeight(dto.getHeight());
        }
        if (dto.getWidth() != null) {
            area.setWidth(dto.getWidth());
        }
        if (dto.getAreaType() != null) {
            area.setAreaType(dto.getAreaType());
        }
    }

    private Area getActiveArea(Long id) {
        return areaRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Area not found: " + id));
    }

    private void bindMediaIds(Area area, List<Long> mediaIds) {
        if (mediaIds == null || mediaIds.isEmpty()) {
            return;
        }

        List<AreaMedia> medias = areaMediaRepository.findAllById(mediaIds);
        for (AreaMedia media : medias) {
            media.setArea(area);
            media.setDeleted(false);
            areaMediaRepository.save(media);
        }
    }

    private void softDeleteMedia(Long areaId) {
        List<AreaMedia> medias = areaMediaRepository.findAllByAreaIdAndIsDeletedFalse(areaId);
        for (AreaMedia media : medias) {
            media.setDeleted(true);
            areaMediaRepository.save(media);
        }
    }

    private void softDeleteFeatures(Long areaId) {
        List<AreaFeature> features = areaFeatureRepository.findAllByAreaIdAndIsDeletedFalse(areaId);
        for (AreaFeature feature : features) {
            feature.setDeleted(true);
            areaFeatureRepository.save(feature);
        }
    }

    private void softDeletePrices(Long areaId) {
        List<AreaPrice> prices = areaPriceRepository.findAllByAreaIdAndIsDeletedFalse(areaId);
        for (AreaPrice price : prices) {
            price.setDeleted(true);
            areaPriceRepository.save(price);
        }
    }

    private void softDeleteDiscounts(Long areaId) {
        List<AreaDiscount> discounts = areaDiscountRepository.findAllByAreaIdAndIsDeletedFalse(areaId);
        for (AreaDiscount discount : discounts) {
            discount.setDeleted(true);
            areaDiscountRepository.save(discount);
        }
    }

    private void softDeleteSchedules(Long areaId) {
        List<AreaSchedule> schedules = areaScheduleRepository.findAllByAreaIdAndIsDeletedFalse(areaId);
        for (AreaSchedule schedule : schedules) {
            schedule.setDeleted(true);
            areaScheduleRepository.save(schedule);
        }
    }

    private void softDeleteReviews(Long areaId) {
        List<AreaReview> reviews = areaReviewRepository.findAllByAreaIdAndIsDeletedFalse(areaId);
        for (AreaReview review : reviews) {
            review.setDeleted(true);
            areaReviewRepository.save(review);
        }
    }

    private void softDeleteBookings(Long areaId) {
        List<Booking> bookings = bookingRepository.findAllByAreaIdAndIsDeletedFalse(areaId);
        for (Booking booking : bookings) {
            booking.setDeleted(true);
            booking.setStatus(BookingStatus.CANCELED);
            bookingRepository.save(booking);
        }
    }

    private void saveFiles(Area area, List<MultipartFile> files) {
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            try {
                AreaMedia media = new AreaMedia();
                media.setArea(area);
                media.setDataPhoto(file.getBytes());
                media.setContentType(file.getContentType());
                media.setDeleted(false);
                areaMediaRepository.save(media);
            } catch (Exception e) {
                throw new RuntimeException("File read error: " + e.getMessage());
            }
        }
    }

    private AreaResponseDto toDto(AreaProjection projection) {
        List<String> media = areaMediaRepository.findAllProjectedByAreaId(projection.getId())
                .stream()
                .map(this::toBase64DataUrl)
                .toList();

        return AreaResponseDto.builder()
                .id(projection.getId())
                .name(projection.getName())
                .description(projection.getDescription())
                .phoneNumber(projection.getPhoneNumber())
                .address(projection.getAddress())
                .latitude(projection.getLatitude())
                .longitude(projection.getLongitude())
                .visibility(projection.getVisibility())
                .height(projection.getHeight())
                .width(projection.getWidth())
                .areaType(parseAreaType(projection.getAreaType()))
                .media(media)
                .build();
    }

    private String toBase64DataUrl(AreaMediaProjection projection) {
        return "data:" + projection.getContentType() + ";base64,"
                + Base64.getEncoder().encodeToString(projection.getDataPhoto());
    }

    private AreaType parseAreaType(String areaType) {
        if (areaType == null) {
            return null;
        }
        return AreaType.valueOf(areaType);
    }
}
