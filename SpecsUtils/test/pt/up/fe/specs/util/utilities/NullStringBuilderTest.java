package pt.up.fe.specs.util.utilities;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for {@link NullStringBuilder}.
 * 
 * Tests null-object pattern implementation for string building that performs no
 * operations.
 * 
 * @author Generated Tests
 */
@DisplayName("NullStringBuilder")
class NullStringBuilderTest {

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create successfully")
        void shouldCreateSuccessfully() {
            try (NullStringBuilder builder = new NullStringBuilder()) {
                assertThat(builder).isNotNull();
                assertThat(builder).isInstanceOf(BufferedStringBuilder.class);
            }
        }
    }

    @Nested
    @DisplayName("Append Operations")
    class AppendOperations {

        @Test
        @DisplayName("should return self when appending string")
        void shouldReturnSelfWhenAppendingString() {
            try (NullStringBuilder builder = new NullStringBuilder()) {
                BufferedStringBuilder result = builder.append("test");

                assertThat(result).isSameAs(builder);
            }
        }

        @Test
        @DisplayName("should ignore all append operations")
        void shouldIgnoreAllAppendOperations() {
            try (NullStringBuilder builder = new NullStringBuilder()) {
                builder.append("first");
                builder.append("second");
                builder.append("third");

                // Should not store anything - verify by checking toString behavior
                // Note: Since NullStringBuilder inherits from BufferedStringBuilder,
                // we need to verify it doesn't build any content
                assertThat(builder.toString()).isEmpty();
            }
        }

        @Test
        @DisplayName("should handle null append gracefully")
        void shouldHandleNullAppendGracefully() {
            try (NullStringBuilder builder = new NullStringBuilder()) {
                BufferedStringBuilder result = builder.append(null);

                assertThat(result).isSameAs(builder);
                assertThat(builder.toString()).isEmpty();
            }
        }

        @Test
        @DisplayName("should handle empty string append")
        void shouldHandleEmptyStringAppend() {
            try (NullStringBuilder builder = new NullStringBuilder()) {
                BufferedStringBuilder result = builder.append("");

                assertThat(result).isSameAs(builder);
                assertThat(builder.toString()).isEmpty();
            }
        }

        @Test
        @DisplayName("should handle large string append")
        void shouldHandleLargeStringAppend() {
            try (NullStringBuilder builder = new NullStringBuilder()) {
                String largeString = "a".repeat(10000);

                BufferedStringBuilder result = builder.append(largeString);

                assertThat(result).isSameAs(builder);
                assertThat(builder.toString()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("Save Operations")
    class SaveOperations {

        @Test
        @DisplayName("should handle save operation without error")
        void shouldHandleSaveOperationWithoutError() {
            try (NullStringBuilder builder = new NullStringBuilder()) {
                builder.append("content");

                // Should not throw any exception
                builder.save();

                // Content should still be empty
                assertThat(builder.toString()).isEmpty();
            }
        }

        @Test
        @DisplayName("should handle multiple save operations")
        void shouldHandleMultipleSaveOperations() {
            try (NullStringBuilder builder = new NullStringBuilder()) {
                builder.save();
                builder.append("test");
                builder.save();
                builder.append("more");
                builder.save();

                assertThat(builder.toString()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("Null Object Pattern")
    class NullObjectPattern {

        @Test
        @DisplayName("should implement null object pattern correctly")
        void shouldImplementNullObjectPatternCorrectly(@TempDir Path tempDir) throws IOException {
            Path tempFile = tempDir.resolve("test.txt");

            try (BufferedStringBuilder normalBuilder = new BufferedStringBuilder(tempFile.toFile());
                    BufferedStringBuilder nullBuilder = new NullStringBuilder()) {

                // Perform same operations on both
                normalBuilder.append("test");
                nullBuilder.append("test");

                normalBuilder.append(" content");
                nullBuilder.append(" content");

                // Normal builder should have content, null builder should not
                assertThat(normalBuilder.toString()).isEqualTo("test content");
                assertThat(nullBuilder.toString()).isEmpty();
            }
        }

        @Test
        @DisplayName("should be usable as BufferedStringBuilder replacement")
        void shouldBeUsableAsBufferedStringBuilderReplacement() {
            // Test polymorphic usage
            try (BufferedStringBuilder builder = new NullStringBuilder()) {
                // Should be able to use all BufferedStringBuilder methods without error
                builder.append("test");
                builder.save();

                assertThat(builder.toString()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("Method Chaining")
    class MethodChaining {

        @Test
        @DisplayName("should support method chaining")
        void shouldSupportMethodChaining() {
            try (NullStringBuilder builder = new NullStringBuilder()) {
                BufferedStringBuilder result = builder
                        .append("first")
                        .append("second")
                        .append("third");

                assertThat(result).isSameAs(builder);
                assertThat(result.toString()).isEmpty();
            }
        }

        @Test
        @DisplayName("should maintain fluent interface")
        void shouldMaintainFluentInterface() {
            try (NullStringBuilder builder = new NullStringBuilder()) {
                // Should be able to chain operations indefinitely
                builder.append("a").append("b").append("c");
                builder.save();
                builder.append("d").append("e");

                assertThat(builder.toString()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("Performance")
    class Performance {

        @Test
        @DisplayName("should handle many operations efficiently")
        void shouldHandleManyOperationsEfficiently() {
            try (NullStringBuilder builder = new NullStringBuilder()) {
                // Perform many operations - should be very fast since nothing is stored
                for (int i = 0; i < 10000; i++) {
                    builder.append("content" + i);
                    if (i % 100 == 0) {
                        builder.save();
                    }
                }

                assertThat(builder.toString()).isEmpty();
            }
        }
    }
}
