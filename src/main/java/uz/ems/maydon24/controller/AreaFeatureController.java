package uz.ems.maydon24.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ems.maydon24.models.dto.request.AreaFeatureUpsertDto;
import uz.ems.maydon24.models.dto.response.AreaFeatureResponseDto;
import uz.ems.maydon24.service.AreaFeatureService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/areas/{areaId}/features")
public class AreaFeatureController {

    private final AreaFeatureService areaFeatureService;

    @PostMapping
    public ResponseEntity<AreaFeatureResponseDto> create(@PathVariable Long areaId,
                                                         @RequestBody AreaFeatureUpsertDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(areaFeatureService.create(areaId, dto));
    }

    @GetMapping
    public ResponseEntity<List<AreaFeatureResponseDto>> getAll(@PathVariable Long areaId) {
        return ResponseEntity.ok(areaFeatureService.getAll(areaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaFeatureResponseDto> getById(@PathVariable Long areaId,
                                                          @PathVariable Long id) {
        return ResponseEntity.ok(areaFeatureService.getById(areaId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaFeatureResponseDto> update(@PathVariable Long areaId,
                                                         @PathVariable Long id,
                                                         @RequestBody AreaFeatureUpsertDto dto) {
        return ResponseEntity.ok(areaFeatureService.update(areaId, id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long areaId,
                                       @PathVariable Long id) {
        areaFeatureService.delete(areaId, id);
        return ResponseEntity.noContent().build();
    }
}
