package com.example.stopwatch.dto; // CORRECTED

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // Lombok: Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class StopwatchStatusResponse {
    private boolean running;
    private String elapsedTime;
    private List<String> laps;
    private String message;
}
