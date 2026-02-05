package uz.ems.maydon24.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ems.maydon24.models.dto.request.AreaReviewUpsertDto;
import uz.ems.maydon24.models.dto.response.AreaReviewResponseDto;
import uz.ems.maydon24.service.AreaReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/areas/{areaId}/reviews")
public class AreaReviewController {

    private final AreaReviewService areaReviewService;

    @PostMapping
    public ResponseEntity<AreaReviewResponseDto> create(@PathVariable Long areaId,
                                                        @RequestBody AreaReviewUpsertDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(areaReviewService.create(areaId, dto));
    }

    @GetMapping
    public ResponseEntity<List<AreaReviewResponseDto>> getAll(@PathVariable Long areaId) {
        return ResponseEntity.ok(areaReviewService.getAll(areaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaReviewResponseDto> getById(@PathVariable Long areaId,
                                                         @PathVariable Long id) {
        return ResponseEntity.ok(areaReviewService.getById(areaId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaReviewResponseDto> update(@PathVariable Long areaId,
                                                        @PathVariable Long id,
                                                        @RequestBody AreaReviewUpsertDto dto) {
        return ResponseEntity.ok(areaReviewService.update(areaId, id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long areaId,
                                       @PathVariable Long id) {
        areaReviewService.delete(areaId, id);
        return ResponseEntity.noContent().build();
    }
}
