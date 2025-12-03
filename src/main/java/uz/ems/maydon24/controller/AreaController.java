package uz.ems.maydon24.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.ems.maydon24.models.dto.request.AreaCreateDto;
import uz.ems.maydon24.models.dto.request.AreaUpdateDto;
import uz.ems.maydon24.models.dto.response.AreaResponseDto;
import uz.ems.maydon24.service.AreaService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/areas")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService areaService;



    @PostMapping("/create")
    public ResponseEntity<AreaResponseDto> create(
            @RequestBody AreaCreateDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(areaService.create(dto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<AreaResponseDto> getById(@RequestParam Long id) {
        return ResponseEntity.ok(areaService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<AreaResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(areaService.getAll(pageable));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<AreaResponseDto> update(
            @PathVariable Long id,
            @RequestPart("dto") AreaUpdateDto dto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        return ResponseEntity.ok(areaService.update(id, dto, files));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        areaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
