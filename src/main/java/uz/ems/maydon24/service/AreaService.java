package uz.ems.maydon24.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.ems.maydon24.models.dto.request.AreaCreateDto;
import uz.ems.maydon24.models.dto.request.AreaUpdateDto;
import uz.ems.maydon24.models.dto.response.AreaResponseDto;
import uz.ems.maydon24.models.entity.Area;
import uz.ems.maydon24.models.entity.AreaMedia;
import uz.ems.maydon24.repository.AreaMediaRepository;
import uz.ems.maydon24.repository.AreaRepository;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaService  {

    private final AreaRepository areaRepository;
    private final AreaMediaRepository areaMediaRepository;

    public AreaResponseDto create(AreaCreateDto dto) {

        Area area = new Area();
        area.setName(dto.getName());
        area.setDescription(dto.getDescription());
        area.setPhoneNumber(dto.getPhoneNumber());
        area.setHeight(dto.getHeight());
        area.setWidth(dto.getWidth());
        area.setAreaType(dto.getAreaType());
        area.setDeleted(false);

        areaRepository.save(area);

        if (dto.getMediaIds() != null && !dto.getMediaIds().isEmpty()) {
            List<AreaMedia> medias = areaMediaRepository.findAllById(dto.getMediaIds());
            for (AreaMedia media : medias) {
                media.setArea(area);
                media.setDeleted(false);
                areaMediaRepository.save(media);
            }
        }

        return toDto(area);
    }


    public AreaResponseDto getById(Long id) {
        Area area = areaRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Area topilmadi"));

        return toDto(area);
    }

    public Page<AreaResponseDto> getAll(Pageable pageable) {
        return areaRepository.findAllActive(pageable).map(this::toDto);
    }

    public AreaResponseDto update(Long id, AreaUpdateDto dto, List<MultipartFile> files) {

        Area area = areaRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Area topilmadi"));

        area.setName(dto.getName());
        area.setDescription(dto.getDescription());
        area.setPhoneNumber(dto.getPhoneNumber());
        area.setHeight(dto.getHeight());
        area.setWidth(dto.getWidth());
        area.setAreaType(dto.getAreaType());

        areaRepository.save(area);

        List<AreaMedia> medias = areaMediaRepository.findAllByAreaIdAndIsDeletedFalse(id);
        for (AreaMedia m : medias) {
            m.setDeleted(true);
            areaMediaRepository.save(m);
        }

        saveFiles(area, files);

        return toDto(area);
    }

    public void delete(Long id) {
        Area area = areaRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Area topilmadi"));

        area.setDeleted(true);
        areaRepository.save(area);

        List<AreaMedia> medias = areaMediaRepository.findAllByAreaIdAndIsDeletedFalse(id);
        for (AreaMedia m : medias) {
            m.setDeleted(true);
            areaMediaRepository.save(m);
        }
    }




    private void saveFiles(Area area, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return;

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;

            try {
                AreaMedia media = new AreaMedia();
                media.setArea(area);
                media.setDataPhoto(file.getBytes());
                media.setContentType(file.getContentType());
                media.setDeleted(false);
                areaMediaRepository.save(media);
            } catch (Exception e) {
                throw new RuntimeException("File o'qishda xatolik: " + e.getMessage());
            }
        }
    }


    private AreaResponseDto toDto(Area area) {

        List<String> base64List = areaMediaRepository
                .findAllByAreaIdAndIsDeletedFalse(area.getId())
                .stream()
                .map(m -> "data:" + m.getContentType() + ";base64," +
                        Base64.getEncoder().encodeToString(m.getDataPhoto()))
                .toList();

        return AreaResponseDto.builder()
                .id(area.getId())
                .name(area.getName())
                .description(area.getDescription())
                .phoneNumber(area.getPhoneNumber())
                .height(area.getHeight())
                .width(area.getWidth())
                .areaType(area.getAreaType())
                .media(base64List)
                .build();
    }
}
