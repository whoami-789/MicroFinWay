package com.MicroFinWay.controller;

import com.MicroFinWay.service.OperationalDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/operational-day")
@RequiredArgsConstructor
public class OperationalDayController {

    private final OperationalDayService operationalDayService;

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
