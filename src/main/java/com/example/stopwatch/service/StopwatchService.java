package com.example.stopwatch.service; // CORRECTED

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class StopwatchService {

    private long startTimeNanos;
    private long stopTimeNanos;
    private boolean running;
    private final List<Long> lapTimesNanos; // Stores elapsed time at each lap

    public StopwatchService() {
        this.lapTimesNanos = new ArrayList<>();
        reset(); // Initialize in a clean state
    }

    public synchronized void start() {
        if (running) {
            throw new IllegalStateException("Stopwatch is already running.");
        }
        this.startTimeNanos = System.nanoTime();
        this.running = true;
        this.lapTimesNanos.clear(); // Clear laps on new start
        System.out.println("Stopwatch started.");
    }

    public synchronized void stop() {
        if (!running) {
            throw new IllegalStateException("Stopwatch is not running.");
        }
        this.stopTimeNanos = System.nanoTime();
        this.running = false;
        System.out.println("Stopwatch stopped.");
    }

    public synchronized void reset() {
        this.startTimeNanos = 0;
        this.stopTimeNanos = 0;
        this.running = false;
        this.lapTimesNanos.clear();
        System.out.println("Stopwatch reset.");
    }

    public synchronized void lap() {
        if (!running) {
            throw new IllegalStateException("Stopwatch is not running. Cannot record a lap.");
        }
        long currentLapTimeNanos = System.nanoTime() - this.startTimeNanos;
        this.lapTimesNanos.add(currentLapTimeNanos);
        System.out.println("Lap recorded at: " + formatDuration(currentLapTimeNanos));
    }

    public synchronized long getElapsedTimeNanos() {
        if (running) {
            return System.nanoTime() - startTimeNanos;
        } else {
            // If stopped, return the duration between start and stop
            // If reset (startTimeNanos is 0) and not running, elapsed time is 0
            return (startTimeNanos == 0) ? 0 : stopTimeNanos - startTimeNanos;
        }
    }

    public synchronized String getFormattedElapsedTime() {
        return formatDuration(getElapsedTimeNanos());
    }

    public synchronized List<String> getFormattedLapTimes() {
        if (lapTimesNanos.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> formattedLaps = new ArrayList<>();
        for (Long lapNano : lapTimesNanos) {
            formattedLaps.add(formatDuration(lapNano));
        }
        return formattedLaps;
    }
    
    public synchronized boolean isRunning() {
        return running;
    }

    private String formatDuration(long nanos) {
        if (nanos < 0) nanos = 0; // Should not happen with System.nanoTime() differences

        long hours = TimeUnit.NANOSECONDS.toHours(nanos);
        nanos -= TimeUnit.HOURS.toNanos(hours);
        long minutes = TimeUnit.NANOSECONDS.toMinutes(nanos);
        nanos -= TimeUnit.MINUTES.toNanos(minutes);
        long seconds = TimeUnit.NANOSECONDS.toSeconds(nanos);
        nanos -= TimeUnit.SECONDS.toNanos(seconds);
        long millis = TimeUnit.NANOSECONDS.toMillis(nanos);

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }
}
