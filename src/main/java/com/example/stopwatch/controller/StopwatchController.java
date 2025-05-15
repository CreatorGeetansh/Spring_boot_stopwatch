package com.example.stopwatch.controller;

import com.example.stopwatch.dto.StopwatchStatusResponse; // CORRECTED
import com.example.stopwatch.service.StopwatchService;  // CORRECTED
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stopwatch")
@CrossOrigin(origins = "*")
public class StopwatchController {

    private final StopwatchService stopwatchService;

    public StopwatchController(StopwatchService stopwatchService) {
        this.stopwatchService = stopwatchService;
    }

    @PostMapping("/start")
    public ResponseEntity<StopwatchStatusResponse> start() {
        try {
            stopwatchService.start();
            return ResponseEntity.ok(getStatusResponse("Stopwatch started."));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(getStatusResponse(e.getMessage()));
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<StopwatchStatusResponse> stop() {
        try {
            stopwatchService.stop();
            return ResponseEntity.ok(getStatusResponse("Stopwatch stopped. Final time: " + stopwatchService.getFormattedElapsedTime()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(getStatusResponse(e.getMessage()));
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<StopwatchStatusResponse> reset() {
        stopwatchService.reset();
        return ResponseEntity.ok(getStatusResponse("Stopwatch reset."));
    }

    @PostMapping("/lap")
    public ResponseEntity<StopwatchStatusResponse> lap() {
        try {
            stopwatchService.lap();
            return ResponseEntity.ok(getStatusResponse("Lap recorded."));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(getStatusResponse(e.getMessage()));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<StopwatchStatusResponse> status() {
        return ResponseEntity.ok(getStatusResponse("Current stopwatch status."));
    }

    private StopwatchStatusResponse getStatusResponse(String message) {
        return new StopwatchStatusResponse(
                stopwatchService.isRunning(),
                stopwatchService.getFormattedElapsedTime(),
                stopwatchService.getFormattedLapTimes(),
                message
        );
    }
}
