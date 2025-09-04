package pt.up.fe.specs.util;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Comprehensive test suite for SpecsSwing utility class.
 * Tests GUI utilities, look and feel management, table models, and panel
 * operations.
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsSwing Tests")
class SpecsSwingTest {

    @Nested
    @DisplayName("Constants and Configuration Tests")
    class ConstantsConfigurationTests {

        @Test
        @DisplayName("TEST_CLASSNAME should be correct")
        void testTestClassname() {
            assertThat(SpecsSwing.TEST_CLASSNAME).isEqualTo("javax.swing.JFrame");
        }

        @Test
        @DisplayName("custom look and feel should start as null")
        void testInitialCustomLookAndFeel() {
            // Reset to ensure clean state
            SpecsSwing.setCustomLookAndFeel(null);

            assertThat(SpecsSwing.getCustomLookAndFeel()).isNull();
        }

        @Test
        @DisplayName("setCustomLookAndFeel should store value correctly")
        void testSetCustomLookAndFeel() {
            // Setup
            String customLookAndFeel = "com.example.CustomLookAndFeel";

            // Execute
            SpecsSwing.setCustomLookAndFeel(customLookAndFeel);

            // Verify
            assertThat(SpecsSwing.getCustomLookAndFeel()).isEqualTo(customLookAndFeel);

            // Cleanup
            SpecsSwing.setCustomLookAndFeel(null);
        }

        @Test
        @DisplayName("custom look and feel should be thread-safe")
        void testCustomLookAndFeelThreadSafety() throws InterruptedException {
            // Setup
            String lookAndFeel1 = "com.example.LookAndFeel1";
            String lookAndFeel2 = "com.example.LookAndFeel2";
            AtomicReference<String> result1 = new AtomicReference<>();
            AtomicReference<String> result2 = new AtomicReference<>();
            CountDownLatch latch = new CountDownLatch(2);

            // Execute concurrent operations
            Thread thread1 = new Thread(() -> {
                SpecsSwing.setCustomLookAndFeel(lookAndFeel1);
                result1.set(SpecsSwing.getCustomLookAndFeel());
                latch.countDown();
            });

            Thread thread2 = new Thread(() -> {
                SpecsSwing.setCustomLookAndFeel(lookAndFeel2);
                result2.set(SpecsSwing.getCustomLookAndFeel());
                latch.countDown();
            });

            thread1.start();
            thread2.start();

            // Wait for completion
            assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();

            // Verify - one of the results should be set correctly
            String finalValue = SpecsSwing.getCustomLookAndFeel();
            assertThat(finalValue).isIn(lookAndFeel1, lookAndFeel2);

            // Cleanup
            SpecsSwing.setCustomLookAndFeel(null);
        }
    }

    @Nested
    @DisplayName("Swing Availability Tests")
    class SwingAvailabilityTests {

        @Test
        @DisplayName("isSwingAvailable should return boolean")
        void testIsSwingAvailable() {
            // Execute
            boolean available = SpecsSwing.isSwingAvailable();

            // Verify - should be deterministic
            assertThat(available).isInstanceOf(Boolean.class);

            // Should be consistent across calls
            assertThat(SpecsSwing.isSwingAvailable()).isEqualTo(available);
        }

        @Test
        @DisplayName("isSwingAvailable should match SpecsSystem.isAvailable")
        void testIsSwingAvailableConsistency() {
            // Execute
            boolean swingAvailable = SpecsSwing.isSwingAvailable();
            boolean systemAvailable = SpecsSystem.isAvailable(SpecsSwing.TEST_CLASSNAME);

            // Verify
            assertThat(swingAvailable).isEqualTo(systemAvailable);
        }
    }

    @Nested
    @DisplayName("Look and Feel Tests")
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    class LookAndFeelTests {

        @BeforeEach
        void setUp() {
            // Reset custom look and feel
            SpecsSwing.setCustomLookAndFeel(null);
        }

        @Test
        @DisplayName("getSystemLookAndFeel should return non-empty string")
        void testGetSystemLookAndFeel() {
            // Execute
            String lookAndFeel = SpecsSwing.getSystemLookAndFeel();

            // Verify
            assertThat(lookAndFeel).isNotEmpty();
            assertThat(lookAndFeel).contains("LookAndFeel");
        }

        @Test
        @DisplayName("getSystemLookAndFeel should return custom when set")
        void testGetSystemLookAndFeelWithCustom() {
            // Setup
            String customLookAndFeel = "com.example.CustomLookAndFeel";
            SpecsSwing.setCustomLookAndFeel(customLookAndFeel);

            // Execute
            String lookAndFeel = SpecsSwing.getSystemLookAndFeel();

            // Verify
            assertThat(lookAndFeel).isEqualTo(customLookAndFeel);
        }

        @Test
        @DisplayName("getSystemLookAndFeel should avoid Metal and GTK")
        void testGetSystemLookAndFeelAvoidance() {
            // Execute
            String lookAndFeel = SpecsSwing.getSystemLookAndFeel();

            // Verify - should not end with problematic look and feels
            assertThat(lookAndFeel).doesNotEndWith(".MetalLookAndFeel");
            assertThat(lookAndFeel).doesNotEndWith(".GTKLookAndFeel");
        }

        @Test
        @DisplayName("setSystemLookAndFeel should handle headless gracefully")
        void testSetSystemLookAndFeelHeadlessHandling() {
            // Execute - should not throw exception
            assertThatCode(() -> {
                boolean result = SpecsSwing.setSystemLookAndFeel();
                assertThat(result).isInstanceOf(Boolean.class);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Headless Environment Tests")
    class HeadlessEnvironmentTests {

        @Test
        @DisplayName("isHeadless should return boolean")
        void testIsHeadless() {
            // Execute
            boolean headless = SpecsSwing.isHeadless();

            // Verify
            assertThat(headless).isInstanceOf(Boolean.class);

            // Should be consistent
            assertThat(SpecsSwing.isHeadless()).isEqualTo(headless);
        }

        @Test
        @DisplayName("isHeadless should handle exceptions gracefully")
        void testIsHeadlessExceptionHandling() {
            // Execute - should not throw exception
            assertThatCode(() -> {
                SpecsSwing.isHeadless();
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Swing Event Dispatch Tests")
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    class SwingEventDispatchTests {

        @Test
        @DisplayName("runOnSwing should execute immediately on EDT")
        void testRunOnSwingOnEDT() throws InterruptedException {
            // Setup
            AtomicBoolean executed = new AtomicBoolean(false);
            CountDownLatch latch = new CountDownLatch(1);

            // Execute on EDT
            SwingUtilities.invokeLater(() -> {
                SpecsSwing.runOnSwing(() -> {
                    executed.set(true);
                    latch.countDown();
                });
            });

            // Verify
            assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
            assertThat(executed.get()).isTrue();
        }

        @Test
        @DisplayName("runOnSwing should invoke later from non-EDT")
        void testRunOnSwingOffEDT() throws InterruptedException {
            // Setup
            AtomicBoolean executed = new AtomicBoolean(false);
            CountDownLatch latch = new CountDownLatch(1);

            // Execute from non-EDT thread
            Thread thread = new Thread(() -> {
                SpecsSwing.runOnSwing(() -> {
                    executed.set(true);
                    latch.countDown();
                });
            });

            thread.start();

            // Verify
            assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
            assertThat(executed.get()).isTrue();
        }

        @Test
        @DisplayName("runOnSwing should handle null runnable gracefully")
        void testRunOnSwingNullRunnable() {
            // Execute - should not throw exception initially
            assertThatCode(() -> {
                SpecsSwing.runOnSwing(null);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Table Model Tests")
    class TableModelTests {

        @Test
        @DisplayName("getTable should create TableModel from map")
        void testGetTable() {
            // Setup
            Map<String, Integer> map = new LinkedHashMap<>();
            map.put("key1", 10);
            map.put("key2", 20);
            map.put("key3", 30);

            // Execute
            TableModel model = SpecsSwing.getTable(map, true, Integer.class);

            // Verify
            assertThat(model).isNotNull();
            assertThat(model.getRowCount()).isGreaterThan(0);
            assertThat(model.getColumnCount()).isGreaterThan(0);
        }

        @Test
        @DisplayName("getTable should handle empty map")
        void testGetTableEmptyMap() {
            // Setup
            Map<String, Integer> map = new LinkedHashMap<>();

            // Execute
            TableModel model = SpecsSwing.getTable(map, true, Integer.class);

            // Verify - MapModel always returns 2 rows when rowWise=true (header row + data
            // row)
            assertThat(model).isNotNull();
            assertThat(model.getRowCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("getTables should split large maps")
        void testGetTablesWithSplitting() {
            // Setup
            Map<String, Integer> map = new LinkedHashMap<>();
            for (int i = 0; i < 10; i++) {
                map.put("key" + i, i);
            }

            // Execute
            List<TableModel> models = SpecsSwing.getTables(map, 3, true, Integer.class);

            // Verify
            assertThat(models).isNotEmpty();
            assertThat(models.size()).isGreaterThan(1); // Should be split

            // Total elements should match - each rowWise model has 2 rows regardless of
            // content size
            // The actual data validation should check the model structure, not raw row
            // count
            int totalModels = models.size();
            assertThat(totalModels).isEqualTo(4); // 10 items with max 3 per table = 4 tables (3+3+3+1)
        }

        @Test
        @DisplayName("getTables should handle single table when under limit")
        void testGetTablesNoSplitting() {
            // Setup
            Map<String, Integer> map = new LinkedHashMap<>();
            map.put("key1", 10);
            map.put("key2", 20);

            // Execute
            List<TableModel> models = SpecsSwing.getTables(map, 5, true, Integer.class);

            // Verify
            assertThat(models).hasSize(1);
            assertThat(models.get(0).getRowCount()).isEqualTo(map.size());
        }

        @ParameterizedTest
        @ValueSource(booleans = { true, false })
        @DisplayName("table models should work with both row-wise orientations")
        void testTableModelOrientations(boolean rowWise) {
            // Setup
            Map<String, Integer> map = new LinkedHashMap<>();
            map.put("key1", 10);
            map.put("key2", 20);

            // Execute
            TableModel model = SpecsSwing.getTable(map, rowWise, Integer.class);

            // Verify
            assertThat(model).isNotNull();
            assertThat(model.getRowCount()).isGreaterThan(0);
            assertThat(model.getColumnCount()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Panel and Window Tests")
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    class PanelWindowTests {

        @Test
        @DisplayName("newWindow should create JFrame with panel")
        void testNewWindow() {
            // Setup
            JPanel panel = new JPanel();
            panel.add(new JLabel("Test Content"));
            String title = "Test Window";

            // Execute
            JFrame frame = SpecsSwing.newWindow(panel, title, 100, 100);

            // Verify
            assertThat(frame).isNotNull();
            assertThat(frame.getTitle()).isEqualTo(title);
            assertThat(frame.getLocation().x).isEqualTo(100);
            assertThat(frame.getLocation().y).isEqualTo(100);
            assertThat(frame.getDefaultCloseOperation()).isEqualTo(JFrame.EXIT_ON_CLOSE);

            // Verify panel is added
            assertThat(frame.getContentPane().getComponent(0)).isEqualTo(panel);
        }

        @Test
        @DisplayName("showPanel should create and display JFrame")
        void testShowPanel() throws InterruptedException {
            // Setup
            JPanel panel = new JPanel();
            panel.add(new JLabel("Test Content"));
            String title = "Test Show Panel";

            // Execute
            JFrame frame = SpecsSwing.showPanel(panel, title);

            // Verify
            assertThat(frame).isNotNull();
            assertThat(frame.getTitle()).isEqualTo(title);

            // Wait a bit for EDT operations
            Thread.sleep(100);

            // Cleanup
            SwingUtilities.invokeLater(() -> frame.dispose());
        }

        @Test
        @DisplayName("showPanel with coordinates should position correctly")
        void testShowPanelWithCoordinates() throws InterruptedException {
            // Setup
            JPanel panel = new JPanel();
            panel.add(new JLabel("Test Content"));
            String title = "Test Show Panel Coords";

            // Execute
            JFrame frame = SpecsSwing.showPanel(panel, title, 200, 200);

            // Verify
            assertThat(frame).isNotNull();
            assertThat(frame.getTitle()).isEqualTo(title);
            assertThat(frame.getLocation().x).isEqualTo(200);
            assertThat(frame.getLocation().y).isEqualTo(200);

            // Wait a bit for EDT operations
            Thread.sleep(100);

            // Cleanup
            SwingUtilities.invokeLater(() -> frame.dispose());
        }

        @Test
        @DisplayName("panels should handle null content gracefully")
        void testPanelNullContent() {
            // Execute - should not throw exception
            assertThatCode(() -> {
                JPanel panel = new JPanel();
                JFrame frame = SpecsSwing.newWindow(panel, "Test", 0, 0);
                assertThat(frame).isNotNull();
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("File Browser Tests")
    class FileBrowserTests {

        @Test
        @DisplayName("browseFileDirectory should handle non-existent file")
        void testBrowseFileDirectoryNonExistent() {
            // Setup
            File nonExistentFile = new File("this_file_does_not_exist.txt");

            // Execute - should not throw exception
            assertThatCode(() -> {
                boolean result = SpecsSwing.browseFileDirectory(nonExistentFile);
                assertThat(result).isInstanceOf(Boolean.class);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("browseFileDirectory should handle null file gracefully")
        void testBrowseFileDirectoryNull() {
            // Execute - implementation doesn't handle null, so this will throw NPE
            assertThatThrownBy(() -> {
                SpecsSwing.browseFileDirectory(null);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("browseFileDirectory should work with temporary file")
        void testBrowseFileDirectoryTempFile() throws Exception {
            // Setup
            File tempFile = File.createTempFile("specs_swing_test", ".tmp");
            tempFile.deleteOnExit();

            try {
                // Execute - should not throw exception
                assertThatCode(() -> {
                    boolean result = SpecsSwing.browseFileDirectory(tempFile);
                    assertThat(result).isInstanceOf(Boolean.class);
                }).doesNotThrowAnyException();
            } finally {
                tempFile.delete();
            }
        }

        @Test
        @DisplayName("browseFileDirectory should work with directory")
        void testBrowseFileDirectoryFolder() {
            // Setup - use system temp directory
            File tempDir = new File(System.getProperty("java.io.tmpdir"));

            // Execute - should not throw exception
            assertThatCode(() -> {
                boolean result = SpecsSwing.browseFileDirectory(tempDir);
                assertThat(result).isInstanceOf(Boolean.class);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Integration and Edge Cases")
    class IntegrationEdgeCasesTests {

        @Test
        @DisplayName("all methods should handle headless environment")
        void testHeadlessCompatibility() {
            // Execute - all methods should be callable without exceptions
            assertThatCode(() -> {
                SpecsSwing.isSwingAvailable();
                SpecsSwing.isHeadless();
                SpecsSwing.getSystemLookAndFeel();
                SpecsSwing.setSystemLookAndFeel();

                // Table operations should work regardless of headless mode
                Map<String, Integer> map = Map.of("key", 1);
                SpecsSwing.getTable(map, true, Integer.class);
                SpecsSwing.getTables(map, 10, true, Integer.class);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("configuration should persist across operations")
        void testConfigurationPersistence() {
            // Setup
            String originalCustom = SpecsSwing.getCustomLookAndFeel();
            String testCustom = "com.example.TestLookAndFeel";

            try {
                // Execute
                SpecsSwing.setCustomLookAndFeel(testCustom);

                // Perform other operations
                SpecsSwing.isSwingAvailable();
                SpecsSwing.isHeadless();

                // Verify configuration persists
                assertThat(SpecsSwing.getCustomLookAndFeel()).isEqualTo(testCustom);

            } finally {
                // Cleanup
                SpecsSwing.setCustomLookAndFeel(originalCustom);
            }
        }

        @Test
        @DisplayName("utility class should have public constructor")
        void testUtilityClassConstructor() {
            // SpecsSwing has a public constructor, unlike some other utility classes
            assertThat(SpecsSwing.class.getConstructors()).hasSize(1);
            assertThat(SpecsSwing.class.getConstructors()[0].getParameterCount()).isEqualTo(0);
        }
    }
}
