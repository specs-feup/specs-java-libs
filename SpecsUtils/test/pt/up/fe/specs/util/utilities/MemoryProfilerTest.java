package pt.up.fe.specs.util.utilities;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for {@link MemoryProfiler}.
 * 
 * Tests memory profiling functionality including file output and periodic
 * measurement.
 * Note: Some tests use short timeouts and may be timing-sensitive.
 * 
 * @author Generated Tests
 */
@DisplayName("MemoryProfiler")
class MemoryProfilerTest {

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create with default-like constructor (use temp file)")
        void shouldCreateWithDefaultConstructor(@TempDir Path tempDir) {
            File outputFile = tempDir.resolve("test_memory_default.csv").toFile();

            MemoryProfiler profiler = new MemoryProfiler(500, TimeUnit.MILLISECONDS, outputFile);

            assertThat(profiler).isNotNull();
        }

        @Test
        @DisplayName("should create with custom parameters")
        void shouldCreateWithCustomParameters(@TempDir Path tempDir) {
            File outputFile = tempDir.resolve("test_memory.csv").toFile();

            MemoryProfiler profiler = new MemoryProfiler(1000, TimeUnit.MILLISECONDS, outputFile);

            assertThat(profiler).isNotNull();
        }

        @Test
        @DisplayName("should handle null output file")
        void shouldHandleNullOutputFile() {
            // Should not throw during construction
            MemoryProfiler profiler = new MemoryProfiler(100, TimeUnit.MILLISECONDS, null);

            assertThat(profiler).isNotNull();
        }
    }

    @Nested
    @DisplayName("File Creation")
    class FileCreation {

        @Test
        @DisplayName("should create output file when executing")
        void shouldCreateOutputFileWhenExecuting(@TempDir Path tempDir) throws InterruptedException {
            File outputFile = tempDir.resolve("memory_test.csv").toFile();
            assertThat(outputFile).doesNotExist();

            MemoryProfiler profiler = new MemoryProfiler(50, TimeUnit.MILLISECONDS, outputFile);

            // Start profiling in a separate thread
            Thread profilingThread = new Thread(() -> profiler.execute());
            profilingThread.start();

            // Wait a short time for file creation
            Thread.sleep(200);

            // Stop the profiling thread
            profilingThread.interrupt();
            profilingThread.join(1000); // Wait up to 1 second

            // File should have been created
            assertThat(outputFile).exists();
        }

        @Test
        @DisplayName("should handle existing output file")
        void shouldHandleExistingOutputFile(@TempDir Path tempDir) throws IOException, InterruptedException {
            File outputFile = tempDir.resolve("existing_memory.csv").toFile();
            Files.write(outputFile.toPath(), "existing,content\n".getBytes());
            assertThat(outputFile).exists();

            MemoryProfiler profiler = new MemoryProfiler(50, TimeUnit.MILLISECONDS, outputFile);

            // Start profiling briefly
            Thread profilingThread = new Thread(() -> profiler.execute());
            profilingThread.start();

            Thread.sleep(200);
            profilingThread.interrupt();
            profilingThread.join(1000);

            // File should still exist and have content
            assertThat(outputFile).exists();
            String content = Files.readString(outputFile.toPath());
            assertThat(content).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Memory Measurement")
    class MemoryMeasurement {

        @Test
        @DisplayName("should write memory measurements to file")
        void shouldWriteMemoryMeasurementsToFile(@TempDir Path tempDir) throws InterruptedException, IOException {
            File outputFile = tempDir.resolve("memory_measurements.csv").toFile();

            MemoryProfiler profiler = new MemoryProfiler(100, TimeUnit.MILLISECONDS, outputFile);

            // Start profiling
            Thread profilingThread = new Thread(() -> profiler.execute());
            profilingThread.start();

            // Let it run for enough time to capture multiple measurements
            Thread.sleep(350); // Should capture at least 2-3 measurements

            profilingThread.interrupt();
            profilingThread.join(1000);

            // File should have measurement data
            assertThat(outputFile).exists();
            String content = Files.readString(outputFile.toPath());
            assertThat(content).isNotEmpty();

            // Content should have timestamp,memory format
            String[] lines = content.split("\n");
            assertThat(lines.length).isGreaterThan(0);

            // Check format of first line (should have timestamp,memory)
            if (lines[0].trim().length() > 0) {
                assertThat(lines[0]).contains(",");
                String[] parts = lines[0].split(",");
                assertThat(parts).hasSize(2);

                // First part should be timestamp
                assertThat(parts[0]).matches("\\d{4}\\.\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d+");

                // Second part should be memory value (number)
                assertThat(parts[1]).matches("\\d+(?:\\.\\d+)?");
            }
        }

        @Test
        @DisplayName("should handle different time units")
        void shouldHandleDifferentTimeUnits(@TempDir Path tempDir) throws InterruptedException {
            File outputFile = tempDir.resolve("memory_timeunits.csv").toFile();

            // Test with nanoseconds (very frequent)
            MemoryProfiler profiler = new MemoryProfiler(100_000_000, TimeUnit.NANOSECONDS, outputFile); // 100ms

            Thread profilingThread = new Thread(() -> profiler.execute());
            profilingThread.start();

            Thread.sleep(250);

            profilingThread.interrupt();
            profilingThread.join(1000);

            assertThat(outputFile).exists();
        }
    }

    @Nested
    @DisplayName("Thread Management")
    class ThreadManagement {

        @Test
        @DisplayName("should handle thread interruption gracefully")
        void shouldHandleThreadInterruptionGracefully(@TempDir Path tempDir) throws InterruptedException {
            File outputFile = tempDir.resolve("memory_interrupt.csv").toFile();

            MemoryProfiler profiler = new MemoryProfiler(1000, TimeUnit.MILLISECONDS, outputFile);

            Thread profilingThread = new Thread(() -> profiler.execute());
            profilingThread.start();

            // Interrupt immediately
            Thread.sleep(50);
            profilingThread.interrupt();

            // Should terminate gracefully
            profilingThread.join(2000);
            assertThat(profilingThread.isAlive()).isFalse();
        }

        @Test
        @DisplayName("should execute in separate thread")
        void shouldExecuteInSeparateThread(@TempDir Path tempDir) {
            File outputFile = tempDir.resolve("memory_thread.csv").toFile();

            MemoryProfiler profiler = new MemoryProfiler(100, TimeUnit.MILLISECONDS, outputFile);

            String mainThreadName = Thread.currentThread().getName();

            // Execute should return immediately, not block
            long startTime = System.currentTimeMillis();
            profiler.execute();
            long endTime = System.currentTimeMillis();

            // Should return quickly (not wait for profiling to complete)
            assertThat(endTime - startTime).isLessThan(1000);

            // Main thread should continue normally
            assertThat(Thread.currentThread().getName()).isEqualTo(mainThreadName);
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandling {

        @Test
        @DisplayName("should handle file creation errors gracefully")
        void shouldHandleFileCreationErrorsGracefully(@TempDir Path tempDir) throws InterruptedException {
            // Try to create file in non-existent directory
            File invalidFile = new File(tempDir.toFile(), "nonexistent/directory/memory.csv");

            MemoryProfiler profiler = new MemoryProfiler(100, TimeUnit.MILLISECONDS, invalidFile);

            // Should not throw exception, but handle gracefully
            Thread profilingThread = new Thread(() -> profiler.execute());
            profilingThread.start();

            Thread.sleep(200);
            profilingThread.interrupt();
            profilingThread.join(1000);

            // Should terminate without hanging
            assertThat(profilingThread.isAlive()).isFalse();
        }
    }

    @Nested
    @DisplayName("Integration")
    class Integration {

        @Test
        @DisplayName("should work with default constructor values (use temp file)")
        void shouldWorkWithDefaultConstructorValues(@TempDir Path tempDir) throws InterruptedException {
            // Use a temp file instead of the default working-directory file
            File outputFile = tempDir.resolve("memory_profile.csv").toFile();

            MemoryProfiler profiler = new MemoryProfiler(500, TimeUnit.MILLISECONDS, outputFile);

            Thread profilingThread = new Thread(() -> profiler.execute());
            profilingThread.start();

            // Let it run briefly
            Thread.sleep(100);

            profilingThread.interrupt();
            profilingThread.join(1000);

            // Ensure temp file exists
            assertThat(outputFile).exists();
        }

        @Test
        @DisplayName("should handle very short periods")
        void shouldHandleVeryShortPeriods(@TempDir Path tempDir) throws InterruptedException {
            File outputFile = tempDir.resolve("memory_short.csv").toFile();

            MemoryProfiler profiler = new MemoryProfiler(1, TimeUnit.MILLISECONDS, outputFile);

            Thread profilingThread = new Thread(() -> profiler.execute());
            profilingThread.start();

            Thread.sleep(50); // Let it run briefly

            profilingThread.interrupt();
            profilingThread.join(1000);

            assertThat(outputFile).exists();
        }

        @Test
        @DisplayName("should handle very long periods")
        void shouldHandleVeryLongPeriods(@TempDir Path tempDir) throws InterruptedException {
            File outputFile = tempDir.resolve("memory_long.csv").toFile();

            MemoryProfiler profiler = new MemoryProfiler(10, TimeUnit.SECONDS, outputFile);

            Thread profilingThread = new Thread(() -> profiler.execute());
            profilingThread.start();

            // Don't wait for the period, just verify it starts properly
            Thread.sleep(100);

            profilingThread.interrupt();
            profilingThread.join(1000);

            assertThat(outputFile).exists();
        }
    }
}
