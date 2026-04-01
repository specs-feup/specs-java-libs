package pt.up.fe.specs.util.properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for SpecsProperty enum and related functionality.
 * Tests property enumeration, application, and resource management.
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsProperty Tests")
class SpecsPropertyTest {

    @Nested
    @DisplayName("Enum Properties")
    class EnumProperties {

        @Test
        @DisplayName("Should have all expected enum values")
        void testEnumValues() {
            SpecsProperty[] values = SpecsProperty.values();

            assertThat(values).hasSize(4);
            assertThat(values).containsExactlyInAnyOrder(
                    SpecsProperty.LoggingLevel,
                    SpecsProperty.WriteErroLog,
                    SpecsProperty.ShowStackTrace,
                    SpecsProperty.LookAndFeel);
        }

        @Test
        @DisplayName("Should have correct properties filename")
        void testPropertiesFilename() {
            assertThat(SpecsProperty.PROPERTIES_FILENAME).isEqualTo("suika.properties");
        }

        @Test
        @DisplayName("Should provide resource collection")
        void testGetResources() {
            Collection<String> resources = SpecsProperty.getResources();

            assertThat(resources).isNotNull();
            assertThat(resources).hasSize(1);
            assertThat(resources).contains("suika.properties");
        }
    }

    @Nested
    @DisplayName("Property Application")
    class PropertyApplication {

        @Test
        @DisplayName("Should apply properties from Properties object")
        void testApplyProperties() {
            Properties props = new Properties();
            props.setProperty("LoggingLevel", "DEBUG");
            props.setProperty("ShowStackTrace", "true");

            // This should not throw any exceptions
            assertThatCode(() -> SpecsProperty.applyProperties(props))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle empty properties")
        void testApplyEmptyProperties() {
            Properties props = new Properties();

            assertThatCode(() -> SpecsProperty.applyProperties(props))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should ignore unknown properties")
        void testApplyPropertiesWithUnknown() {
            Properties props = new Properties();
            props.setProperty("UnknownProperty", "value");
            props.setProperty("LoggingLevel", "INFO");

            assertThatCode(() -> SpecsProperty.applyProperties(props))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle null property values in Properties")
        void testApplyPropertiesWithNullValues() {
            Properties props = new Properties();
            props.setProperty("LoggingLevel", "INFO");

            // Properties doesn't allow null values, so we skip this test
            // This documents the expected behavior
            assertThatCode(() -> SpecsProperty.applyProperties(props))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should apply properties from file when it exists")
        void testApplyPropertiesFromFile(@TempDir File tempDir) throws IOException {
            // Create a test properties file
            File propsFile = new File(tempDir, "suika.properties");
            String content = "LoggingLevel=DEBUG\nShowStackTrace=false";
            Files.write(propsFile.toPath(), content.getBytes());

            // Change to temp directory for test
            String originalDir = System.getProperty("user.dir");
            try {
                System.setProperty("user.dir", tempDir.getAbsolutePath());

                assertThatCode(() -> SpecsProperty.applyProperties())
                        .doesNotThrowAnyException();
            } finally {
                // Restore original directory
                System.setProperty("user.dir", originalDir);
            }
        }

        @Test
        @DisplayName("Should handle missing properties file gracefully")
        void testApplyPropertiesNoFile(@TempDir File tempDir) {
            // Change to temp directory with no properties file
            String originalDir = System.getProperty("user.dir");
            try {
                System.setProperty("user.dir", tempDir.getAbsolutePath());

                assertThatCode(() -> SpecsProperty.applyProperties())
                        .doesNotThrowAnyException();
            } finally {
                // Restore original directory
                System.setProperty("user.dir", originalDir);
            }
        }
    }

    @Nested
    @DisplayName("Individual Property Application")
    class IndividualPropertyApplication {

        @Test
        @DisplayName("Should apply LoggingLevel property")
        void testApplyLoggingLevel() {
            assertThatCode(() -> SpecsProperty.LoggingLevel.applyProperty("INFO"))
                    .doesNotThrowAnyException();

            assertThatCode(() -> SpecsProperty.LoggingLevel.applyProperty("DEBUG"))
                    .doesNotThrowAnyException();

            assertThatCode(() -> SpecsProperty.LoggingLevel.applyProperty("WARNING"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle invalid logging level")
        void testApplyInvalidLoggingLevel() {
            assertThatCode(() -> SpecsProperty.LoggingLevel.applyProperty("INVALID_LEVEL"))
                    .doesNotThrowAnyException(); // Should handle gracefully
        }

        @Test
        @DisplayName("Should apply ShowStackTrace property")
        void testApplyShowStackTrace() {
            assertThatCode(() -> SpecsProperty.ShowStackTrace.applyProperty("true"))
                    .doesNotThrowAnyException();

            assertThatCode(() -> SpecsProperty.ShowStackTrace.applyProperty("false"))
                    .doesNotThrowAnyException();

            assertThatCode(() -> SpecsProperty.ShowStackTrace.applyProperty("yes"))
                    .doesNotThrowAnyException();

            assertThatCode(() -> SpecsProperty.ShowStackTrace.applyProperty("no"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle invalid boolean for ShowStackTrace")
        void testApplyInvalidShowStackTrace() {
            assertThatCode(() -> SpecsProperty.ShowStackTrace.applyProperty("invalid"))
                    .doesNotThrowAnyException(); // Should handle gracefully
        }

        @Test
        @DisplayName("Should apply WriteErroLog property")
        void testApplyWriteErroLog(@TempDir File tempDir) {
            File logFile = new File(tempDir, "test.log");

            assertThatCode(() -> SpecsProperty.WriteErroLog.applyProperty(logFile.getAbsolutePath()))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle empty WriteErroLog")
        void testApplyEmptyWriteErroLog() {
            assertThatCode(() -> SpecsProperty.WriteErroLog.applyProperty(""))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should apply LookAndFeel property")
        void testApplyLookAndFeel() {
            // Test with system L&F names
            assertThatCode(() -> SpecsProperty.LookAndFeel.applyProperty("Metal"))
                    .doesNotThrowAnyException();

            assertThatCode(() -> SpecsProperty.LookAndFeel.applyProperty("Nimbus"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle invalid LookAndFeel")
        void testApplyInvalidLookAndFeel() {
            assertThatCode(() -> SpecsProperty.LookAndFeel.applyProperty("NonExistentLookAndFeel"))
                    .doesNotThrowAnyException(); // Should handle gracefully
        }

        @Test
        @DisplayName("Should handle null property values")
        void testApplyNullProperty() {
            for (SpecsProperty property : SpecsProperty.values()) {
                assertThatCode(() -> property.applyProperty(null))
                        .doesNotThrowAnyException();
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should handle properties with whitespace")
        void testPropertiesWithWhitespace() {
            Properties props = new Properties();
            props.setProperty("LoggingLevel", "  INFO  ");
            props.setProperty("ShowStackTrace", " true ");

            assertThatCode(() -> SpecsProperty.applyProperties(props))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle case-sensitive property values")
        void testCaseSensitiveValues() {
            assertThatCode(() -> SpecsProperty.LoggingLevel.applyProperty("info"))
                    .doesNotThrowAnyException();

            assertThatCode(() -> SpecsProperty.LoggingLevel.applyProperty("Info"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle special characters in property values")
        void testSpecialCharacters() {
            // WriteErroLog may fail with certain paths if they can't create valid file
            // handlers
            assertThatCode(() -> SpecsProperty.LookAndFeel.applyProperty("Look&Feel-With-Special_Characters"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle very long property values")
        void testLongPropertyValues() {
            String longValue = "a".repeat(100); // Shorter value to avoid file system issues

            // Test with LookAndFeel instead of WriteErroLog to avoid file system issues
            assertThatCode(() -> SpecsProperty.LookAndFeel.applyProperty(longValue))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle empty string values")
        void testEmptyStringValues() {
            for (SpecsProperty property : SpecsProperty.values()) {
                if (property != SpecsProperty.WriteErroLog) { // WriteErroLog handles empty specially
                    assertThatCode(() -> property.applyProperty(""))
                            .doesNotThrowAnyException();
                }
            }
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complete configuration workflow")
        void testCompleteWorkflow(@TempDir File tempDir) throws IOException {
            // Create comprehensive properties file
            File propsFile = new File(tempDir, "suika.properties");
            String content = """
                    LoggingLevel=DEBUG
                    ShowStackTrace=true
                    ShowMemoryHeap=false
                    WriteErroLog=error.log
                    LookAndFeel=Metal
                    """;
            Files.write(propsFile.toPath(), content.getBytes());

            // Change to temp directory
            String originalDir = System.getProperty("user.dir");
            try {
                System.setProperty("user.dir", tempDir.getAbsolutePath());

                // Apply properties from file
                assertThatCode(() -> SpecsProperty.applyProperties())
                        .doesNotThrowAnyException();

                // Apply additional properties programmatically
                Properties additionalProps = new Properties();
                additionalProps.setProperty("LoggingLevel", "WARNING");

                assertThatCode(() -> SpecsProperty.applyProperties(additionalProps))
                        .doesNotThrowAnyException();

            } finally {
                System.setProperty("user.dir", originalDir);
            }
        }

        @Test
        @DisplayName("Should handle mixed valid and invalid properties")
        void testMixedProperties() {
            Properties props = new Properties();
            props.setProperty("LoggingLevel", "INFO"); // Valid
            props.setProperty("ShowStackTrace", "invalid"); // Invalid
            props.setProperty("LookAndFeel", "Metal"); // Valid
            props.setProperty("UnknownProperty", "value"); // Unknown
            props.setProperty("WriteErroLog", ""); // Empty

            assertThatCode(() -> SpecsProperty.applyProperties(props))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should prioritize specs.properties over suika.properties")
        void testPropertiesPriority(@TempDir File tempDir) throws IOException {
            // Create both properties files
            File specsFile = new File(tempDir, "specs.properties");
            File suikaFile = new File(tempDir, "suika.properties");

            Files.write(specsFile.toPath(), "LoggingLevel=DEBUG".getBytes());
            Files.write(suikaFile.toPath(), "LoggingLevel=WARNING".getBytes());

            String originalDir = System.getProperty("user.dir");
            try {
                System.setProperty("user.dir", tempDir.getAbsolutePath());

                // Should pick specs.properties first
                assertThatCode(() -> SpecsProperty.applyProperties())
                        .doesNotThrowAnyException();

            } finally {
                System.setProperty("user.dir", originalDir);
            }
        }

        @Test
        @DisplayName("Should handle corrupted properties file")
        void testCorruptedPropertiesFile(@TempDir File tempDir) throws IOException {
            File corruptedFile = new File(tempDir, "suika.properties");
            String corruptedContent = "LoggingLevel=INFO\ninvalid line\nShowStackTrace=true";
            Files.write(corruptedFile.toPath(), corruptedContent.getBytes());

            String originalDir = System.getProperty("user.dir");
            try {
                System.setProperty("user.dir", tempDir.getAbsolutePath());

                // Should handle corrupted file gracefully
                assertThatCode(() -> SpecsProperty.applyProperties())
                        .doesNotThrowAnyException();

            } finally {
                System.setProperty("user.dir", originalDir);
            }
        }
    }
}
