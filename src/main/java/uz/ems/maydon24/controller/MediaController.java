package uz.ems.maydon24.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.ems.maydon24.response.ApiResponse;
import uz.ems.maydon24.service.MediaService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<Long>>> uploadFiles(
            @RequestPart("files") List<MultipartFile> files
    ) throws IOException {
        ApiResponse<List<Long>> mediaIds = mediaService.uploadFiles(files);
        return ResponseEntity.ok(mediaIds);
    }

    @DeleteMapping("/delete")
    public ApiResponse<Long> delete(@RequestParam Long id) {
        return mediaService.delete(id);
    }
}
