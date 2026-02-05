package uz.ems.maydon24.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ems.maydon24.models.dto.request.AreaPriceUpsertDto;
import uz.ems.maydon24.models.dto.response.AreaPriceResponseDto;
import uz.ems.maydon24.service.AreaPriceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/areas/{areaId}/prices")
public class AreaPriceController {

    private final AreaPriceService areaPriceService;

    @PostMapping
    public ResponseEntity<AreaPriceResponseDto> create(@PathVariable Long areaId,
                                                       @RequestBody AreaPriceUpsertDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(areaPriceService.create(areaId, dto));
    }

    @GetMapping
    public ResponseEntity<List<AreaPriceResponseDto>> getAll(@PathVariable Long areaId) {
        return ResponseEntity.ok(areaPriceService.getAll(areaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaPriceResponseDto> getById(@PathVariable Long areaId,
                                                        @PathVariable Long id) {
        return ResponseEntity.ok(areaPriceService.getById(areaId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaPriceResponseDto> update(@PathVariable Long areaId,
                                                       @PathVariable Long id,
                                                       @RequestBody AreaPriceUpsertDto dto) {
        return ResponseEntity.ok(areaPriceService.update(areaId, id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long areaId,
                                       @PathVariable Long id) {
        areaPriceService.delete(areaId, id);
        return ResponseEntity.noContent().build();
    }
}
