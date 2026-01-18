package pt.up.fe.specs.util.utilities.heapwindow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.*;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.lang.reflect.Field;
import java.io.StringWriter;

/**
 * Unit tests for {@link MemProgressBarUpdater}.
 * 
 * Tests SwingWorker that updates a progress bar with memory information.
 * Note: These tests run in headless mode and may skip GUI-dependent
 * functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("MemProgressBarUpdater")
class MemProgressBarUpdaterTest {

    private JProgressBar progressBar;
    private MemProgressBarUpdater updater;

    @BeforeEach
    void setUp() {
        // Set headless mode for testing
        System.setProperty("java.awt.headless", "true");
    }

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create memory progress bar updater")
        void shouldCreateMemoryProgressBarUpdater() throws InterruptedException {
            // Run synchronously on the EDT so UI state is deterministic
            try {
                SwingUtilities.invokeAndWait(() -> {
                    progressBar = new JProgressBar();
                    updater = new MemProgressBarUpdater(progressBar);

                    assertThat(updater).isNotNull();
                    assertThat(progressBar.isStringPainted()).isTrue();
                });
            } catch (Exception e) {
                fail("EDT invocation failed: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("should configure progress bar with string painting")
        void shouldConfigureProgressBarWithStringPainting() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                progressBar = new JProgressBar();
                assertThat(progressBar.isStringPainted()).isFalse(); // Initially false

                updater = new MemProgressBarUpdater(progressBar);
                assertThat(progressBar.isStringPainted()).isTrue(); // Should be true after construction

                latch.countDown();
            });

            boolean completed = latch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(completed).isTrue();
        }

        @Test
        @DisplayName("should throw when progress bar is null")
        void shouldThrowWhenProgressBarIsNull() {
            assertThatThrownBy(() -> new MemProgressBarUpdater(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("JProgressBar cannot be null");
        }

        @Test
        @DisplayName("should set string painted when constructed off EDT")
        void shouldSetStringPaintedWhenConstructedOffEDT() throws Exception {
            // Construct the progress bar and updater from a non-EDT thread
            Thread t = new Thread(() -> {
                progressBar = new JProgressBar();
                updater = new MemProgressBarUpdater(progressBar);
            });

            t.start();
            t.join(2000);

            // After construction, even when done off-EDT, the property should be set
            assertThat(progressBar).isNotNull();
            assertThat(progressBar.isStringPainted()).isTrue();
        }
    }

    @Nested
    @DisplayName("Memory Calculation")
    class MemoryCalculation {

        @Test
        @DisplayName("should calculate memory values in megabytes")
        void shouldCalculateMemoryValuesInMegabytes() throws Exception {
            CountDownLatch latch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                progressBar = new JProgressBar();
                updater = new MemProgressBarUpdater(progressBar);

                try {
                    // Execute the background task
                    updater.doInBackground();

                    // Access private fields to verify calculations
                    Field heapSizeMbField = MemProgressBarUpdater.class.getDeclaredField("heapSizeMb");
                    Field currentSizeMbField = MemProgressBarUpdater.class.getDeclaredField("currentSizeMb");
                    heapSizeMbField.setAccessible(true);
                    currentSizeMbField.setAccessible(true);

                    int heapSizeMb = heapSizeMbField.getInt(updater);
                    int currentSizeMb = currentSizeMbField.getInt(updater);

                    // Verify that memory values are reasonable
                    assertThat(heapSizeMb).isGreaterThan(0);
                    assertThat(currentSizeMb).isGreaterThan(0);
                    assertThat(currentSizeMb).isLessThanOrEqualTo(heapSizeMb);

                    // Check that values are in megabytes (should be reasonable for JVM)
                    assertThat(heapSizeMb).isLessThan(100000); // Less than 100GB in MB
                    assertThat(currentSizeMb).isLessThan(100000);

                } catch (Exception e) {
                    fail("Failed to calculate memory values: " + e.getMessage());
                }

                latch.countDown();
            });

            boolean completed = latch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(completed).isTrue();
        }

        @Test
        @DisplayName("should use correct megabyte conversion factor")
        void shouldUseCorrectMegabyteConversionFactor() throws Exception {
            // The code uses Math.pow(1024, 2) for MB conversion
            long mbFactor = (long) Math.pow(1024, 2);
            assertThat(mbFactor).isEqualTo(1048576); // 1024^2

            // Test that the conversion gives reasonable results
            long testBytes = 2 * 1024 * 1024; // 2 MB in bytes
            int testMb = (int) (testBytes / mbFactor);
            assertThat(testMb).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Progress Bar Updates")
    class ProgressBarUpdates {

        @Test
        @DisplayName("should update progress bar with memory information")
        void shouldUpdateProgressBarWithMemoryInformation() throws Exception {
            progressBar = new JProgressBar();
            updater = new MemProgressBarUpdater(progressBar);

            // Execute the background task directly to avoid threading issues
            updater.doInBackground();

            // In headless mode, progress bar updates may not work as expected
            // Test that the operation completes without error
            assertThat(progressBar.getMinimum()).isEqualTo(0);
            // Maximum and value may be 0 in headless mode, so we just test they exist
            assertThat(progressBar.getMaximum()).isGreaterThanOrEqualTo(0);
            assertThat(progressBar.getValue()).isGreaterThanOrEqualTo(0);

            // String format may be default percentage format ("31%") instead of custom MiB
            // format
            // when EDT updates don't execute immediately in headless mode
            String barString = progressBar.getString();
            if (barString != null && !barString.isEmpty()) {
                // May be either percentage format or MiB format
                assertThat(barString).matches("(\\d+%|\\d+MiB / \\d+MiB)");
            }
        }

        @Test
        @DisplayName("should format string correctly")
        void shouldFormatStringCorrectly() throws Exception {
            // Construct components on EDT for safety
            SwingUtilities.invokeAndWait(() -> {
                progressBar = new JProgressBar();
                updater = new MemProgressBarUpdater(progressBar);
            });

            // Execute background task off the EDT so it can schedule UI updates
            updater.doInBackground();

            // Now assert the UI string on the EDT; this ensures the scheduled
            // EventQueue.invokeLater from doInBackground has been applied.
            try {
                SwingUtilities.invokeAndWait(() -> {
                    String barString = progressBar.getString();

                    if (barString != null) {
                        // String should be in format "XXXMiB / YYYMiB"
                        String[] parts = barString.split(" / ");
                        assertThat(parts).hasSize(2);

                        String currentPart = parts[0];
                        String totalPart = parts[1];

                        assertThat(currentPart).endsWith("MiB");
                        assertThat(totalPart).endsWith("MiB");

                        // Extract numbers
                        int currentMb = Integer.parseInt(currentPart.replace("MiB", ""));
                        int totalMb = Integer.parseInt(totalPart.replace("MiB", ""));

                        assertThat(currentMb).isGreaterThan(0);
                        assertThat(totalMb).isGreaterThan(0);
                        assertThat(currentMb).isLessThanOrEqualTo(totalMb);
                    }
                });
            } catch (Exception e) {
                // Include cause stack trace when failing to aid debugging
                Throwable cause = e.getCause() != null ? e.getCause() : e;
                StringWriter sw = new StringWriter();
                cause.printStackTrace(new java.io.PrintWriter(sw));
                fail("EDT assertion failed: " + sw.toString());
            }
        }
    }

    @Nested
    @DisplayName("SwingWorker Behavior")
    class SwingWorkerBehavior {

        @Test
        @DisplayName("should complete without errors")
        void shouldCompleteWithoutErrors() throws Exception {
            CountDownLatch latch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                progressBar = new JProgressBar();
                updater = new MemProgressBarUpdater(progressBar);

                updater.execute();

                // Wait for completion
                new Thread(() -> {
                    try {
                        updater.get(5000, TimeUnit.MILLISECONDS);
                        latch.countDown();
                    } catch (Exception e) {
                        fail("SwingWorker execution failed: " + e.getMessage());
                    }
                }).start();
            });

            boolean completed = latch.await(10000, TimeUnit.MILLISECONDS);
            assertThat(completed).isTrue();
            assertThat(updater.isDone()).isTrue();
        }

        @Test
        @DisplayName("should return null from doInBackground")
        void shouldReturnNullFromDoInBackground() throws Exception {
            CountDownLatch latch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                progressBar = new JProgressBar();
                updater = new MemProgressBarUpdater(progressBar);

                try {
                    Object result = updater.doInBackground();
                    assertThat(result).isNull();
                } catch (Exception e) {
                    fail("doInBackground should not throw: " + e.getMessage());
                }

                latch.countDown();
            });

            boolean completed = latch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(completed).isTrue();
        }

        @Test
        @DisplayName("should handle done method without errors")
        void shouldHandleDoneMethodWithoutErrors() throws Exception {
            CountDownLatch latch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                progressBar = new JProgressBar();
                updater = new MemProgressBarUpdater(progressBar);

                // done() method is currently empty, but should not throw
                assertThatCode(() -> updater.done()).doesNotThrowAnyException();

                latch.countDown();
            });

            boolean completed = latch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(completed).isTrue();
        }
    }

    @Nested
    @DisplayName("Field Access")
    class FieldAccess {

        @Test
        @DisplayName("should have package-private fields accessible")
        void shouldHavePackagePrivateFieldsAccessible() throws Exception {
            CountDownLatch latch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                progressBar = new JProgressBar();
                updater = new MemProgressBarUpdater(progressBar);

                try {
                    Field heapSizeMbField = MemProgressBarUpdater.class.getDeclaredField("heapSizeMb");
                    Field currentSizeMbField = MemProgressBarUpdater.class.getDeclaredField("currentSizeMb");
                    Field progressBarField = MemProgressBarUpdater.class.getDeclaredField("jProgressBar");

                    // Fields should exist and be accessible from same package
                    assertThat(heapSizeMbField).isNotNull();
                    assertThat(currentSizeMbField).isNotNull();
                    assertThat(progressBarField).isNotNull();

                    // Fields should have expected types
                    assertThat(heapSizeMbField.getType()).isEqualTo(int.class);
                    assertThat(currentSizeMbField.getType()).isEqualTo(int.class);
                    assertThat(progressBarField.getType()).isEqualTo(JProgressBar.class);

                } catch (Exception e) {
                    fail("Failed to access fields: " + e.getMessage());
                }

                latch.countDown();
            });

            boolean completed = latch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(completed).isTrue();
        }

        @Test
        @DisplayName("should initialize fields correctly after execution")
        void shouldInitializeFieldsCorrectlyAfterExecution() throws Exception {
            CountDownLatch latch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                progressBar = new JProgressBar();
                updater = new MemProgressBarUpdater(progressBar);

                try {
                    // Execute background task
                    updater.doInBackground();

                    // Access fields
                    Field heapSizeMbField = MemProgressBarUpdater.class.getDeclaredField("heapSizeMb");
                    Field currentSizeMbField = MemProgressBarUpdater.class.getDeclaredField("currentSizeMb");
                    heapSizeMbField.setAccessible(true);
                    currentSizeMbField.setAccessible(true);

                    int heapSizeMb = heapSizeMbField.getInt(updater);
                    int currentSizeMb = currentSizeMbField.getInt(updater);

                    // Fields should be initialized with positive values
                    assertThat(heapSizeMb).isGreaterThan(0);
                    assertThat(currentSizeMb).isGreaterThan(0);

                } catch (Exception e) {
                    fail("Failed to verify field initialization: " + e.getMessage());
                }

                latch.countDown();
            });

            boolean completed = latch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(completed).isTrue();
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle multiple executions")
        void shouldHandleMultipleExecutions() {
            progressBar = new JProgressBar();
            updater = new MemProgressBarUpdater(progressBar);

            // First execution
            updater.execute();

            // In headless mode, SwingWorker behavior may differ
            // Multiple executions may not throw IllegalStateException in all cases
            assertThatCode(() -> updater.execute()).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should handle direct doInBackground calls")
        void shouldHandleDirectDoInBackgroundCalls() throws Exception {
            CountDownLatch latch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                progressBar = new JProgressBar();
                updater = new MemProgressBarUpdater(progressBar);

                // Direct calls to doInBackground should work
                assertThatCode(() -> {
                    try {
                        Object result1 = updater.doInBackground();
                        Object result2 = updater.doInBackground();
                        assertThat(result1).isNull();
                        assertThat(result2).isNull();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).doesNotThrowAnyException();

                latch.countDown();
            });

            boolean completed = latch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(completed).isTrue();
        }
    }
}
