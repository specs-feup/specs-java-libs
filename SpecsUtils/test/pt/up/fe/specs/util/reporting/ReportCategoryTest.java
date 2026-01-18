package pt.up.fe.specs.util.reporting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ReportCategory}.
 * Tests the implementation of report message categorization.
 * 
 * @author Generated Tests
 */
@DisplayName("ReportCategory")
class ReportCategoryTest {

    @Nested
    @DisplayName("Enum Values")
    class EnumValues {

        @Test
        @DisplayName("Should have ERROR category")
        void shouldHaveErrorCategory() {
            // When/Then
            assertThat(ReportCategory.ERROR).isNotNull();
            assertThat(ReportCategory.ERROR.name()).isEqualTo("ERROR");
        }

        @Test
        @DisplayName("Should have WARNING category")
        void shouldHaveWarningCategory() {
            // When/Then
            assertThat(ReportCategory.WARNING).isNotNull();
            assertThat(ReportCategory.WARNING.name()).isEqualTo("WARNING");
        }

        @Test
        @DisplayName("Should have INFORMATION category")
        void shouldHaveInformationCategory() {
            // When/Then
            assertThat(ReportCategory.INFORMATION).isNotNull();
            assertThat(ReportCategory.INFORMATION.name()).isEqualTo("INFORMATION");
        }

        @Test
        @DisplayName("Should have exactly three categories")
        void shouldHaveExactlyThreeCategories() {
            // When
            ReportCategory[] values = ReportCategory.values();

            // Then
            assertThat(values).hasSize(3);
            assertThat(values).containsExactlyInAnyOrder(
                    ReportCategory.ERROR,
                    ReportCategory.WARNING,
                    ReportCategory.INFORMATION);
        }

        @Test
        @DisplayName("Should have unique values")
        void shouldHaveUniqueValues() {
            // When
            Set<ReportCategory> uniqueValues = Arrays.stream(ReportCategory.values())
                    .collect(Collectors.toSet());

            // Then
            assertThat(uniqueValues).hasSize(ReportCategory.values().length);
        }
    }

    @Nested
    @DisplayName("Enum Behavior")
    class EnumBehavior {

        @Test
        @DisplayName("Should support valueOf operations")
        void shouldSupportValueOfOperations() {
            // When/Then
            assertThat(ReportCategory.valueOf("ERROR")).isEqualTo(ReportCategory.ERROR);
            assertThat(ReportCategory.valueOf("WARNING")).isEqualTo(ReportCategory.WARNING);
            assertThat(ReportCategory.valueOf("INFORMATION")).isEqualTo(ReportCategory.INFORMATION);
        }

        @Test
        @DisplayName("Should maintain ordinal ordering")
        void shouldMaintainOrdinalOrdering() {
            // When/Then
            assertThat(ReportCategory.ERROR.ordinal()).isEqualTo(0);
            assertThat(ReportCategory.WARNING.ordinal()).isEqualTo(1);
            assertThat(ReportCategory.INFORMATION.ordinal()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should support comparison operations")
        void shouldSupportComparisonOperations() {
            // When/Then
            assertThat(ReportCategory.ERROR.compareTo(ReportCategory.WARNING)).isLessThan(0);
            assertThat(ReportCategory.WARNING.compareTo(ReportCategory.INFORMATION)).isLessThan(0);
            assertThat(ReportCategory.INFORMATION.compareTo(ReportCategory.ERROR)).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should support equality operations")
        void shouldSupportEqualityOperations() {
            // When/Then
            assertThat(ReportCategory.ERROR).isEqualTo(ReportCategory.ERROR);
            assertThat(ReportCategory.WARNING).isEqualTo(ReportCategory.WARNING);
            assertThat(ReportCategory.INFORMATION).isEqualTo(ReportCategory.INFORMATION);

            assertThat(ReportCategory.ERROR).isNotEqualTo(ReportCategory.WARNING);
            assertThat(ReportCategory.WARNING).isNotEqualTo(ReportCategory.INFORMATION);
            assertThat(ReportCategory.INFORMATION).isNotEqualTo(ReportCategory.ERROR);
        }

        @Test
        @DisplayName("Should have consistent hashCode")
        void shouldHaveConsistentHashCode() {
            // When/Then
            assertThat(ReportCategory.ERROR.hashCode()).isEqualTo(ReportCategory.ERROR.hashCode());
            assertThat(ReportCategory.WARNING.hashCode()).isEqualTo(ReportCategory.WARNING.hashCode());
            assertThat(ReportCategory.INFORMATION.hashCode()).isEqualTo(ReportCategory.INFORMATION.hashCode());
        }

        @Test
        @DisplayName("Should have meaningful toString")
        void shouldHaveMeaningfulToString() {
            // When/Then
            assertThat(ReportCategory.ERROR.toString()).isEqualTo("ERROR");
            assertThat(ReportCategory.WARNING.toString()).isEqualTo("WARNING");
            assertThat(ReportCategory.INFORMATION.toString()).isEqualTo("INFORMATION");
        }
    }

    @Nested
    @DisplayName("Usage in Switch Statements")
    class UsageInSwitchStatements {

        @Test
        @DisplayName("Should work in switch statements")
        void shouldWorkInSwitchStatements() {
            // Given
            ReportCategory[] categories = {
                    ReportCategory.ERROR,
                    ReportCategory.WARNING,
                    ReportCategory.INFORMATION
            };

            // When/Then
            for (ReportCategory category : categories) {
                String result = switch (category) {
                    case ERROR -> "error";
                    case WARNING -> "warning";
                    case INFORMATION -> "info";
                };

                assertThat(result).isNotNull();
                assertThat(result).isIn("error", "warning", "info");
            }
        }

        @Test
        @DisplayName("Should support exhaustive switch coverage")
        void shouldSupportExhaustiveSwitchCoverage() {
            // When/Then - This test verifies that all enum values are covered
            for (ReportCategory category : ReportCategory.values()) {
                boolean handled = switch (category) {
                    case ERROR, WARNING, INFORMATION -> true;
                };
                assertThat(handled).isTrue();
            }
        }
    }

    @Nested
    @DisplayName("Serialization")
    class Serialization {

        @Test
        @DisplayName("Should be serializable as enum")
        void shouldBeSerializableAsEnum() {
            // When/Then - Enums are inherently serializable
            assertThat(ReportCategory.ERROR).isInstanceOf(Enum.class);
            assertThat(ReportCategory.WARNING).isInstanceOf(Enum.class);
            assertThat(ReportCategory.INFORMATION).isInstanceOf(Enum.class);
        }
    }

    @Nested
    @DisplayName("Thread Safety")
    class ThreadSafety {

        @Test
        @DisplayName("Should be thread-safe")
        void shouldBeThreadSafe() throws InterruptedException {
            // Given
            final int numThreads = 10;
            Thread[] threads = new Thread[numThreads];
            boolean[] results = new boolean[numThreads];

            // When
            for (int i = 0; i < numThreads; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    // Access enum values concurrently
                    ReportCategory error = ReportCategory.ERROR;
                    ReportCategory warning = ReportCategory.WARNING;
                    ReportCategory info = ReportCategory.INFORMATION;

                    results[index] = (error == ReportCategory.ERROR) &&
                            (warning == ReportCategory.WARNING) &&
                            (info == ReportCategory.INFORMATION);
                });
                threads[i].start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // Then
            for (boolean result : results) {
                assertThat(result).isTrue();
            }
        }
    }

    @Nested
    @DisplayName("Functional Programming Support")
    class FunctionalProgrammingSupport {

        @Test
        @DisplayName("Should work with streams and filters")
        void shouldWorkWithStreamsAndFilters() {
            // When
            long errorCount = Arrays.stream(ReportCategory.values())
                    .filter(category -> category == ReportCategory.ERROR)
                    .count();

            long warningCount = Arrays.stream(ReportCategory.values())
                    .filter(category -> category == ReportCategory.WARNING)
                    .count();

            long infoCount = Arrays.stream(ReportCategory.values())
                    .filter(category -> category == ReportCategory.INFORMATION)
                    .count();

            // Then
            assertThat(errorCount).isEqualTo(1);
            assertThat(warningCount).isEqualTo(1);
            assertThat(infoCount).isEqualTo(1);
        }

        @Test
        @DisplayName("Should work with mapping operations")
        void shouldWorkWithMappingOperations() {
            // When
            Set<String> names = Arrays.stream(ReportCategory.values())
                    .map(ReportCategory::name)
                    .collect(Collectors.toSet());

            // Then
            assertThat(names).containsExactlyInAnyOrder("ERROR", "WARNING", "INFORMATION");
        }
    }
}
