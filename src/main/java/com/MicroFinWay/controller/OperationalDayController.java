package com.MicroFinWay.controller;

import com.MicroFinWay.service.OperationalDayService;
import com.MicroFinWay.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/operational-day")
@RequiredArgsConstructor
public class OperationalDayController {

    private final OperationalDayService operationalDayService;
    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCurrentOperationalDay() {
        LocalDate currentDay = organizationService.getCurrentOperationalDay();
        boolean closed = organizationService.isOperationalDayClosed();
        return ResponseEntity.ok(Map.of(
                "operationalDay", currentDay,
                "closed", closed
        ));
    }

    @PostMapping("/open")
    public ResponseEntity<String> open(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        operationalDayService.openOperationalDay(date);
        return ResponseEntity.ok("Операционный день открыт: " + date);
    }

    @PostMapping("/close")
    public ResponseEntity<String> close(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        operationalDayService.closeOperationalDay(date);
        return ResponseEntity.ok("Операционный день закрыт: " + date);
    }
}
