package uz.ems.maydon24.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.ems.maydon24.exeption.NotFoundException;
import uz.ems.maydon24.models.entity.AreaMedia;
import uz.ems.maydon24.repository.AreaMediaRepository;
import uz.ems.maydon24.response.ApiResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final AreaMediaRepository areaMediaRepository;

    public ApiResponse<List<Long>> uploadFiles(List<MultipartFile> files) throws IOException {
        List<Long> ids = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }

            try {
                AreaMedia media = new AreaMedia();
                media.setContentType(file.getContentType());
                media.setDataPhoto(file.getBytes());
                media.setDeleted(false);
                media.setArea(null);

                AreaMedia saved = areaMediaRepository.save(media);
                ids.add(saved.getId());
            } catch (IOException e) {
                throw new IOException("Faylni o'qishda xatolik: " + e.getMessage());
            }
        }

        return ApiResponse.success("result", ids);

    }

    public ApiResponse<Long> delete(Long id) {
        AreaMedia areaMedia = areaMediaRepository.findByIdAndDeleted(id).orElseThrow(()->new NotFoundException("Ma'lumot topilmadi"));

            areaMedia.setDeleted(true);

        areaMediaRepository.save(areaMedia);
        return ApiResponse.success("rasm muvaffaqiyatli o'chirildi", id);
    }
}
