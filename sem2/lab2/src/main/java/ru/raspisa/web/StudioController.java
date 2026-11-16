package ru.raspisa.web;

import org.springframework.web.bind.annotation.*;
import ru.raspisa.dto.StudioDto;
import ru.raspisa.dto.StudioUpdateRequest;
import ru.raspisa.service.StudioService;

import java.util.List;

@RestController
@RequestMapping("/api/studios")
public class StudioController {

    private final StudioService studioService;

    public StudioController(StudioService studioService) {
        this.studioService = studioService;
    }

    @GetMapping
    public List<StudioDto> all() {
        return studioService.findAll();
    }

    @PutMapping("/{id}")
    public StudioDto update(@PathVariable Long id, @RequestBody StudioUpdateRequest request) {
        return studioService.update(id, request);
    }
}
