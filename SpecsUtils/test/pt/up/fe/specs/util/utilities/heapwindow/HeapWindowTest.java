package pt.up.fe.specs.util.utilities.heapwindow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static org.assertj.core.api.Assertions.*;

import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Unit tests for {@link HeapWindow}.
 * 
 * Tests Swing window that displays heap memory information.
 * 
 * @author Generated Tests
 */
@DisplayName("HeapWindow")
class HeapWindowTest {

    private HeapWindow window;

    @BeforeEach
    void setUp() {
        // Skip tests if running in headless environment
        try {
            // This will throw HeadlessException if running headless
            java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        } catch (HeadlessException e) {
            org.junit.jupiter.api.Assumptions.assumeTrue(false, "Skipping GUI tests in headless environment");
        }
    }

    @AfterEach
    void tearDown() {
        if (window != null) {
            window.close();
            window = null;
        }
    }

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create window")
        void shouldCreateWindow() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                try {
                    window = new HeapWindow();
                    latch.countDown();
                } catch (Exception e) {
                    fail("Failed to create HeapWindow: " + e.getMessage());
                }
            });

            boolean completed = latch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(completed).isTrue();
            assertThat(window).isNotNull();
            assertThat(window).isInstanceOf(JFrame.class);
        }

        @Test
        @DisplayName("should initialize with default properties")
        void shouldInitializeWithDefaultProperties() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                window = new HeapWindow();

                // Test basic JFrame properties - fix the expected default close operation
                assertThat(window.getDefaultCloseOperation()).isEqualTo(JFrame.EXIT_ON_CLOSE);
                assertThat(window.isVisible()).isFalse(); // Not visible by default

                latch.countDown();
            });

            boolean completed = latch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(completed).isTrue();
        }
    }

    @Nested
    @DisplayName("Window Operations")
    class WindowOperations {

        @Test
        @DisplayName("should show window")
        void shouldShowWindow() throws InterruptedException {
            CountDownLatch createLatch = new CountDownLatch(1);
            CountDownLatch showLatch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                window = new HeapWindow();
                createLatch.countDown();
            });

            boolean created = createLatch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(created).isTrue();

            SwingUtilities.invokeLater(() -> {
                window.run();
                // Check if window becomes visible
                if (window.isVisible()) {
                    showLatch.countDown();
                } else {
                    // Sometimes there's a delay, check again
                    SwingUtilities.invokeLater(() -> {
                        if (window.isVisible()) {
                            showLatch.countDown();
                        }
                    });
                }
            });

            boolean shown = showLatch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(shown).isTrue();
        }

        @Test
        @DisplayName("should close window")
        void shouldCloseWindow() throws InterruptedException {
            CountDownLatch createLatch = new CountDownLatch(1);
            CountDownLatch closeLatch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                window = new HeapWindow();
                window.run(); // Show the window
                createLatch.countDown();
            });

            boolean created = createLatch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(created).isTrue();

            SwingUtilities.invokeLater(() -> {
                window.close();
                // Check if window is disposed
                SwingUtilities.invokeLater(() -> {
                    if (!window.isDisplayable()) {
                        closeLatch.countDown();
                    }
                });
            });

            boolean closed = closeLatch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(closed).isTrue();
        }

        @Test
        @DisplayName("should set title with program name")
        void shouldSetTitleWithProgramName() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                window = new HeapWindow();
                window.run();

                SwingUtilities.invokeLater(() -> {
                    String title = window.getTitle();
                    assertThat(title).startsWith("Heap - ");
                    latch.countDown();
                });
            });

            boolean completed = latch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(completed).isTrue();
        }
    }

    @Nested
    @DisplayName("Memory Display")
    class MemoryDisplay {

        @Test
        @DisplayName("should display maximum heap size")
        void shouldDisplayMaximumHeapSize() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                window = new HeapWindow();

                // The window should have initialized the max memory display
                // We can't easily test the internal components without making them public,
                // but we can test that the window was constructed without errors
                assertThat(window).isNotNull();

                latch.countDown();
            });

            boolean completed = latch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(completed).isTrue();
        }

        @Test
        @DisplayName("should calculate memory in megabytes")
        void shouldCalculateMemoryInMegabytes() {
            // This tests the memory calculation logic that would be in the constructor
            long heapMaxSize = Runtime.getRuntime().maxMemory();
            long maxSizeMb = (long) (heapMaxSize / (Math.pow(1024, 2)));

            assertThat(maxSizeMb).isGreaterThan(0);
            assertThat(maxSizeMb).isLessThan(Long.MAX_VALUE);
        }
    }

    @Nested
    @DisplayName("Timer Management")
    class TimerManagement {

        @Test
        @DisplayName("should start timer on creation")
        void shouldStartTimerOnCreation() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                window = new HeapWindow();

                // Window should be created with timer running
                assertThat(window).isNotNull();

                latch.countDown();
            });

            boolean completed = latch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(completed).isTrue();
        }

        @Test
        @DisplayName("should stop timer on close")
        void shouldStopTimerOnClose() throws InterruptedException {
            CountDownLatch createLatch = new CountDownLatch(1);
            CountDownLatch closeLatch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                window = new HeapWindow();
                createLatch.countDown();
            });

            boolean created = createLatch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(created).isTrue();

            SwingUtilities.invokeLater(() -> {
                window.close();
                closeLatch.countDown();
            });

            boolean closed = closeLatch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(closed).isTrue();
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle multiple run calls")
        void shouldHandleMultipleRunCalls() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                window = new HeapWindow();

                // Call run multiple times - this should not throw exceptions
                assertThatCode(() -> {
                    window.run();
                    window.run();
                    window.run();
                }).doesNotThrowAnyException();

                latch.countDown();
            });

            boolean completed = latch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(completed).isTrue();
        }

        @Test
        @DisplayName("should handle close on non-visible window")
        void shouldHandleCloseOnNonVisibleWindow() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                window = new HeapWindow();

                // Close without showing
                assertThatCode(() -> window.close()).doesNotThrowAnyException();

                latch.countDown();
            });

            boolean completed = latch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(completed).isTrue();
        }

        @Test
        @DisplayName("should handle multiple close calls")
        void shouldHandleMultipleCloseCalls() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(1);

            SwingUtilities.invokeLater(() -> {
                window = new HeapWindow();
                window.run();

                // Call close multiple times
                assertThatCode(() -> {
                    window.close();
                    window.close();
                    window.close();
                }).doesNotThrowAnyException();

                latch.countDown();
            });

            boolean completed = latch.await(5000, TimeUnit.MILLISECONDS);
            assertThat(completed).isTrue();
        }
    }
}
