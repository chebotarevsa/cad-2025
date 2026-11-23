package ru.raspisa.web;

import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.raspisa.dto.HolidayDto;
import ru.raspisa.dto.HolidayRequest;
import ru.raspisa.service.HolidayService;

import java.util.List;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping
    public List<HolidayDto> list() {
        return holidayService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<HolidayDto> create(@Valid @RequestBody HolidayRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(holidayService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public HolidayDto update(@PathVariable Long id, @Valid @RequestBody HolidayRequest request) {
        return holidayService.update(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        holidayService.delete(id);
    }
}
