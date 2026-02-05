package uz.ems.maydon24.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ems.maydon24.models.dto.request.AreaScheduleUpsertDto;
import uz.ems.maydon24.models.dto.response.AreaScheduleResponseDto;
import uz.ems.maydon24.service.AreaScheduleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/areas/{areaId}/schedules")
public class AreaScheduleController {

    private final AreaScheduleService areaScheduleService;

    @PostMapping
    public ResponseEntity<AreaScheduleResponseDto> create(@PathVariable Long areaId,
                                                          @RequestBody AreaScheduleUpsertDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(areaScheduleService.create(areaId, dto));
    }

    @GetMapping
    public ResponseEntity<List<AreaScheduleResponseDto>> getAll(@PathVariable Long areaId) {
        return ResponseEntity.ok(areaScheduleService.getAll(areaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaScheduleResponseDto> getById(@PathVariable Long areaId,
                                                           @PathVariable Long id) {
        return ResponseEntity.ok(areaScheduleService.getById(areaId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaScheduleResponseDto> update(@PathVariable Long areaId,
                                                          @PathVariable Long id,
                                                          @RequestBody AreaScheduleUpsertDto dto) {
        return ResponseEntity.ok(areaScheduleService.update(areaId, id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long areaId,
                                       @PathVariable Long id) {
        areaScheduleService.delete(areaId, id);
        return ResponseEntity.noContent().build();
    }
}
