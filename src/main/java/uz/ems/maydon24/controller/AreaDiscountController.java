package uz.ems.maydon24.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ems.maydon24.models.dto.request.AreaDiscountUpsertDto;
import uz.ems.maydon24.models.dto.response.AreaDiscountResponseDto;
import uz.ems.maydon24.service.AreaDiscountService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/areas/{areaId}/discounts")
public class AreaDiscountController {

    private final AreaDiscountService areaDiscountService;

    @PostMapping
    public ResponseEntity<AreaDiscountResponseDto> create(@PathVariable Long areaId,
                                                          @RequestBody AreaDiscountUpsertDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(areaDiscountService.create(areaId, dto));
    }

    @GetMapping
    public ResponseEntity<List<AreaDiscountResponseDto>> getAll(@PathVariable Long areaId) {
        return ResponseEntity.ok(areaDiscountService.getAll(areaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaDiscountResponseDto> getById(@PathVariable Long areaId,
                                                           @PathVariable Long id) {
        return ResponseEntity.ok(areaDiscountService.getById(areaId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaDiscountResponseDto> update(@PathVariable Long areaId,
                                                          @PathVariable Long id,
                                                          @RequestBody AreaDiscountUpsertDto dto) {
        return ResponseEntity.ok(areaDiscountService.update(areaId, id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long areaId,
                                       @PathVariable Long id) {
        areaDiscountService.delete(areaId, id);
        return ResponseEntity.noContent().build();
    }
}
