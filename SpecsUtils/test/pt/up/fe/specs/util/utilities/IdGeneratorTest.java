package pt.up.fe.specs.util.utilities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link IdGenerator}.
 * 
 * Tests ID generation functionality with prefixes and auto-incrementing
 * suffixes.
 * 
 * @author Generated Tests
 */
@DisplayName("IdGenerator")
class IdGeneratorTest {

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create with default constructor")
        void shouldCreateWithDefaultConstructor() {
            IdGenerator generator = new IdGenerator();

            assertThat(generator).isNotNull();
        }

        @Test
        @DisplayName("should create with null ID")
        void shouldCreateWithNullId() {
            IdGenerator generator = new IdGenerator((String) null);

            assertThat(generator).isNotNull();
        }

        @Test
        @DisplayName("should create with string ID")
        void shouldCreateWithStringId() {
            IdGenerator generator = new IdGenerator("test");

            assertThat(generator).isNotNull();
        }

        @Test
        @DisplayName("should create copy from another generator")
        void shouldCreateCopyFromAnotherGenerator() {
            IdGenerator original = new IdGenerator("original");
            original.next("prefix");

            IdGenerator copy = new IdGenerator(original);

            assertThat(copy).isNotNull();
            // Copy should continue the sequence
            assertThat(copy.next("prefix")).isEqualTo("originalprefix2");
        }
    }

    @Nested
    @DisplayName("ID Generation")
    class IdGeneration {

        @Test
        @DisplayName("should generate sequential IDs with prefix")
        void shouldGenerateSequentialIdsWithPrefix() {
            IdGenerator generator = new IdGenerator();

            assertThat(generator.next("var")).isEqualTo("var1");
            assertThat(generator.next("var")).isEqualTo("var2");
            assertThat(generator.next("var")).isEqualTo("var3");
        }

        @Test
        @DisplayName("should maintain separate counters for different prefixes")
        void shouldMaintainSeparateCountersForDifferentPrefixes() {
            IdGenerator generator = new IdGenerator();

            assertThat(generator.next("var")).isEqualTo("var1");
            assertThat(generator.next("func")).isEqualTo("func1");
            assertThat(generator.next("var")).isEqualTo("var2");
            assertThat(generator.next("func")).isEqualTo("func2");
        }

        @Test
        @DisplayName("should prepend instance ID to prefix when configured")
        void shouldPrependInstanceIdToPrefixWhenConfigured() {
            IdGenerator generator = new IdGenerator("instance_");

            assertThat(generator.next("var")).isEqualTo("instance_var1");
            assertThat(generator.next("var")).isEqualTo("instance_var2");
            assertThat(generator.next("func")).isEqualTo("instance_func1");
        }

        @Test
        @DisplayName("should handle empty prefix")
        void shouldHandleEmptyPrefix() {
            IdGenerator generator = new IdGenerator();

            assertThat(generator.next("")).isEqualTo("1");
            assertThat(generator.next("")).isEqualTo("2");
        }

        @Test
        @DisplayName("should handle empty prefix with instance ID")
        void shouldHandleEmptyPrefixWithInstanceId() {
            IdGenerator generator = new IdGenerator("test_");

            assertThat(generator.next("")).isEqualTo("test_1");
            assertThat(generator.next("")).isEqualTo("test_2");
        }
    }

    @Nested
    @DisplayName("State Management")
    class StateManagement {

        @Test
        @DisplayName("should maintain state across multiple prefixes")
        void shouldMaintainStateAcrossMultiplePrefixes() {
            IdGenerator generator = new IdGenerator();

            // Use different prefixes in mixed order
            assertThat(generator.next("a")).isEqualTo("a1");
            assertThat(generator.next("b")).isEqualTo("b1");
            assertThat(generator.next("c")).isEqualTo("c1");
            assertThat(generator.next("a")).isEqualTo("a2");
            assertThat(generator.next("b")).isEqualTo("b2");
            assertThat(generator.next("a")).isEqualTo("a3");
        }

        @Test
        @DisplayName("should copy state correctly")
        void shouldCopyStateCorrectly() {
            IdGenerator original = new IdGenerator("prefix_");
            original.next("var");
            original.next("func");
            original.next("var");

            IdGenerator copy = new IdGenerator(original);

            // Copy should continue from where original left off
            assertThat(copy.next("var")).isEqualTo("prefix_var3");
            assertThat(copy.next("func")).isEqualTo("prefix_func2");

            // Original should be unaffected by copy operations
            assertThat(original.next("var")).isEqualTo("prefix_var3");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle very long prefixes")
        void shouldHandleVeryLongPrefixes() {
            IdGenerator generator = new IdGenerator();
            String longPrefix = "a".repeat(1000);

            String result = generator.next(longPrefix);

            assertThat(result).startsWith(longPrefix);
            assertThat(result).endsWith("1");
        }

        @Test
        @DisplayName("should handle special characters in prefix")
        void shouldHandleSpecialCharactersInPrefix() {
            IdGenerator generator = new IdGenerator();

            assertThat(generator.next("var$")).isEqualTo("var$1");
            assertThat(generator.next("func_")).isEqualTo("func_1");
            assertThat(generator.next("test-")).isEqualTo("test-1");
            assertThat(generator.next("123")).isEqualTo("1231");
        }

        @Test
        @DisplayName("should handle null instance ID consistently")
        void shouldHandleNullInstanceIdConsistently() {
            IdGenerator generator1 = new IdGenerator();
            IdGenerator generator2 = new IdGenerator((String) null);

            assertThat(generator1.next("test")).isEqualTo(generator2.next("test"));
        }
    }
}
