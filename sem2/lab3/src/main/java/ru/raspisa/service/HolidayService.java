package ru.raspisa.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.raspisa.dto.HolidayDto;
import ru.raspisa.dto.HolidayRequest;
import ru.raspisa.entity.Holiday;
import ru.raspisa.repository.HolidayRepository;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    public List<HolidayDto> findAll() {
        return holidayRepository.findAll().stream()
                .map(this::toDto)
                .sorted(Comparator.comparing(HolidayDto::startDate))
                .toList();
    }

    @Transactional
    public HolidayDto create(HolidayRequest req) {
        Holiday h = new Holiday();
        applyRequest(h, req);
        return toDto(holidayRepository.save(h));
    }

    @Transactional
    public HolidayDto update(Long id, HolidayRequest req) {
        Holiday h = holidayRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Запись не найдена: id=" + id));
        applyRequest(h, req);
        return toDto(holidayRepository.save(h));
    }

    @Transactional
    public void delete(Long id) {
        if (!holidayRepository.existsById(id)) {
            throw new NoSuchElementException("Запись не найдена: id=" + id);
        }
        holidayRepository.deleteById(id);
    }

    private void applyRequest(Holiday h, HolidayRequest req) {
        h.setType(req.type());
        h.setName(req.name());
        h.setStartDate(req.startDate());

        h.setEndDate(req.endDate() != null ? req.endDate() : req.startDate());
        h.setYearly(req.yearly() != null && req.yearly());
    }

    private HolidayDto toDto(Holiday h) {
        return new HolidayDto(h.getId(), h.getType(), h.getName(), h.getStartDate(), h.getEndDate(), h.isYearly());
    }
}
