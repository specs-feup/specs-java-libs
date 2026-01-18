package pt.up.fe.specs.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Comprehensive test suite for DotRenderFormat enum.
 * Tests all enum values, flag generation, extension mapping, and format
 * utilities.
 * 
 * @author Generated Tests
 */
@DisplayName("DotRenderFormat Tests")
class DotRenderFormatTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @Test
        @DisplayName("enum should have expected values")
        void testEnumValues() {
            // Verify all expected enum values exist
            DotRenderFormat[] values = DotRenderFormat.values();

            assertThat(values).hasSize(2);
            assertThat(values).containsExactlyInAnyOrder(
                    DotRenderFormat.PNG,
                    DotRenderFormat.SVG);
        }

        @Test
        @DisplayName("enum values should have correct names")
        void testEnumNames() {
            assertThat(DotRenderFormat.PNG.name()).isEqualTo("PNG");
            assertThat(DotRenderFormat.SVG.name()).isEqualTo("SVG");
        }

        @Test
        @DisplayName("valueOf should return correct enum values")
        void testValueOf() {
            assertThat(DotRenderFormat.valueOf("PNG")).isEqualTo(DotRenderFormat.PNG);
            assertThat(DotRenderFormat.valueOf("SVG")).isEqualTo(DotRenderFormat.SVG);
        }

        @Test
        @DisplayName("valueOf should throw exception for invalid names")
        void testValueOf_InvalidNames() {
            assertThatThrownBy(() -> DotRenderFormat.valueOf("PDF"))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> DotRenderFormat.valueOf("JPEG"))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> DotRenderFormat.valueOf("png"))
                    .isInstanceOf(IllegalArgumentException.class); // Case sensitive
        }
    }

    @Nested
    @DisplayName("Flag Generation Tests")
    class FlagGenerationTests {

        @Test
        @DisplayName("getFlag should return correct flags for PNG")
        void testGetFlag_PNG() {
            // Execute
            String flag = DotRenderFormat.PNG.getFlag();

            // Verify
            assertThat(flag).isEqualTo("-Tpng");
        }

        @Test
        @DisplayName("getFlag should return correct flags for SVG")
        void testGetFlag_SVG() {
            // Execute
            String flag = DotRenderFormat.SVG.getFlag();

            // Verify
            assertThat(flag).isEqualTo("-Tsvg");
        }

        @ParameterizedTest
        @EnumSource(DotRenderFormat.class)
        @DisplayName("getFlag should start with -T for all formats")
        void testGetFlag_AllFormats(DotRenderFormat format) {
            // Execute
            String flag = format.getFlag();

            // Verify
            assertThat(flag).startsWith("-T");
            assertThat(flag).hasSize(format.name().length() + 2); // -T + lowercase name
        }

        @ParameterizedTest
        @EnumSource(DotRenderFormat.class)
        @DisplayName("getFlag should use lowercase format name")
        void testGetFlag_LowercaseNames(DotRenderFormat format) {
            // Execute
            String flag = format.getFlag();

            // Verify
            String expectedFlag = "-T" + format.name().toLowerCase();
            assertThat(flag).isEqualTo(expectedFlag);
        }
    }

    @Nested
    @DisplayName("Extension Generation Tests")
    class ExtensionGenerationTests {

        @Test
        @DisplayName("getExtension should return correct extension for PNG")
        void testGetExtension_PNG() {
            // Execute
            String extension = DotRenderFormat.PNG.getExtension();

            // Verify
            assertThat(extension).isEqualTo("png");
        }

        @Test
        @DisplayName("getExtension should return correct extension for SVG")
        void testGetExtension_SVG() {
            // Execute
            String extension = DotRenderFormat.SVG.getExtension();

            // Verify
            assertThat(extension).isEqualTo("svg");
        }

        @ParameterizedTest
        @EnumSource(DotRenderFormat.class)
        @DisplayName("getExtension should return lowercase format name for all formats")
        void testGetExtension_AllFormats(DotRenderFormat format) {
            // Execute
            String extension = format.getExtension();

            // Verify
            assertThat(extension).isEqualTo(format.name().toLowerCase());
            assertThat(extension).isLowerCase();
            assertThat(extension).doesNotContain(".");
        }

        @ParameterizedTest
        @EnumSource(DotRenderFormat.class)
        @DisplayName("getExtension should not contain dots or special characters")
        void testGetExtension_NoSpecialCharacters(DotRenderFormat format) {
            // Execute
            String extension = format.getExtension();

            // Verify
            assertThat(extension).matches("[a-z]+"); // Only lowercase letters
            assertThat(extension).doesNotContain(".");
            assertThat(extension).doesNotContain("-");
            assertThat(extension).doesNotContain("_");
        }
    }

    @Nested
    @DisplayName("Integration and Consistency Tests")
    class IntegrationConsistencyTests {

        @Test
        @DisplayName("flag and extension should be consistent")
        void testFlagExtensionConsistency() {
            for (DotRenderFormat format : DotRenderFormat.values()) {
                String flag = format.getFlag();
                String extension = format.getExtension();

                // Flag should be -T + extension
                assertThat(flag).isEqualTo("-T" + extension);
            }
        }

        @Test
        @DisplayName("all formats should have non-empty flags and extensions")
        void testNonEmptyValues() {
            for (DotRenderFormat format : DotRenderFormat.values()) {
                assertThat(format.getFlag()).isNotEmpty();
                assertThat(format.getExtension()).isNotEmpty();
                assertThat(format.name()).isNotEmpty();
            }
        }

        @Test
        @DisplayName("formats should have unique flags")
        void testUniqueFlags() {
            DotRenderFormat[] formats = DotRenderFormat.values();

            for (int i = 0; i < formats.length; i++) {
                for (int j = i + 1; j < formats.length; j++) {
                    assertThat(formats[i].getFlag())
                            .isNotEqualTo(formats[j].getFlag());
                }
            }
        }

        @Test
        @DisplayName("formats should have unique extensions")
        void testUniqueExtensions() {
            DotRenderFormat[] formats = DotRenderFormat.values();

            for (int i = 0; i < formats.length; i++) {
                for (int j = i + 1; j < formats.length; j++) {
                    assertThat(formats[i].getExtension())
                            .isNotEqualTo(formats[j].getExtension());
                }
            }
        }
    }

    @Nested
    @DisplayName("Usage and Functionality Tests")
    class UsageFunctionalityTests {

        @Test
        @DisplayName("formats should be usable in switch statements")
        void testSwitchStatementUsage() {
            // Test that enum can be used in switch statements
            for (DotRenderFormat format : DotRenderFormat.values()) {
                String result = switch (format) {
                    case PNG -> "Portable Network Graphics";
                    case SVG -> "Scalable Vector Graphics";
                };

                assertThat(result).isNotEmpty();
            }
        }

        @Test
        @DisplayName("formats should be comparable")
        void testComparable() {
            // Enums implement Comparable by ordinal order
            DotRenderFormat[] values = DotRenderFormat.values();

            for (int i = 0; i < values.length - 1; i++) {
                assertThat(values[i].compareTo(values[i + 1])).isLessThan(0);
                assertThat(values[i + 1].compareTo(values[i])).isGreaterThan(0);
                assertThat(values[i].compareTo(values[i])).isEqualTo(0);
            }
        }

        @Test
        @DisplayName("formats should have consistent toString")
        void testToString() {
            for (DotRenderFormat format : DotRenderFormat.values()) {
                // toString should return the name by default
                assertThat(format.toString()).isEqualTo(format.name());
            }
        }

        @Test
        @DisplayName("formats should be serializable")
        void testSerializable() {
            // Enums are Serializable by default
            for (DotRenderFormat format : DotRenderFormat.values()) {
                assertThat(format).isInstanceOf(java.io.Serializable.class);
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesErrorHandlingTests {

        @Test
        @DisplayName("methods should handle all enum values without exceptions")
        void testAllMethodsAllValues() {
            for (DotRenderFormat format : DotRenderFormat.values()) {
                // These should not throw exceptions
                assertThatCode(() -> {
                    format.getFlag();
                    format.getExtension();
                    format.name();
                    format.toString();
                    format.ordinal();
                }).doesNotThrowAnyException();
            }
        }

        @Test
        @DisplayName("ordinal values should be consistent")
        void testOrdinalValues() {
            DotRenderFormat[] values = DotRenderFormat.values();

            for (int i = 0; i < values.length; i++) {
                assertThat(values[i].ordinal()).isEqualTo(i);
            }
        }

        @Test
        @DisplayName("enum should support iteration")
        void testIteration() {
            int count = 0;
            for (DotRenderFormat format : DotRenderFormat.values()) {
                assertThat(format).isNotNull();
                count++;
            }

            assertThat(count).isEqualTo(DotRenderFormat.values().length);
        }

        @Test
        @DisplayName("enum should be thread-safe")
        void testThreadSafety() {
            // Enum values are thread-safe singletons
            DotRenderFormat format1 = DotRenderFormat.PNG;
            DotRenderFormat format2 = DotRenderFormat.valueOf("PNG");

            assertThat(format1).isSameAs(format2);
        }
    }
}
