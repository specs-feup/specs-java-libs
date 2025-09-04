package pt.up.fe.specs.util.parsing.comments;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for TextElementType enum.
 * Tests enum values, constants, and behavior patterns.
 * 
 * @author Generated Tests
 */
@DisplayName("TextElementType Tests")
public class TextElementTypeTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @Test
        @DisplayName("Should have all expected enum values")
        void testEnumValues_AllExpectedValues_Present() {
            // Act
            TextElementType[] values = TextElementType.values();

            // Assert
            assertThat(values).hasSize(4);
            assertThat(values).containsExactlyInAnyOrder(
                    TextElementType.INLINE_COMMENT,
                    TextElementType.MULTILINE_COMMENT,
                    TextElementType.PRAGMA,
                    TextElementType.PRAGMA_MACRO);
        }

        @Test
        @DisplayName("Should support valueOf for all enum constants")
        void testValueOf_AllConstants_ReturnsCorrectEnums() {
            // Act & Assert
            assertThat(TextElementType.valueOf("INLINE_COMMENT")).isEqualTo(TextElementType.INLINE_COMMENT);
            assertThat(TextElementType.valueOf("MULTILINE_COMMENT")).isEqualTo(TextElementType.MULTILINE_COMMENT);
            assertThat(TextElementType.valueOf("PRAGMA")).isEqualTo(TextElementType.PRAGMA);
            assertThat(TextElementType.valueOf("PRAGMA_MACRO")).isEqualTo(TextElementType.PRAGMA_MACRO);
        }

        @Test
        @DisplayName("Should throw exception for invalid valueOf")
        void testValueOf_InvalidName_ThrowsException() {
            // Act & Assert
            assertThatThrownBy(() -> TextElementType.valueOf("INVALID_TYPE"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Enum Constants Tests")
    class EnumConstantsTests {

        @Test
        @DisplayName("Should have consistent ordinal values")
        void testEnumOrdinals_ConsistentValues_MaintainOrder() {
            // Act & Assert - Test ordinal values for consistency
            assertThat(TextElementType.INLINE_COMMENT.ordinal()).isEqualTo(0);
            assertThat(TextElementType.MULTILINE_COMMENT.ordinal()).isEqualTo(1);
            assertThat(TextElementType.PRAGMA.ordinal()).isEqualTo(2);
            assertThat(TextElementType.PRAGMA_MACRO.ordinal()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should have consistent name() values")
        void testEnumNames_ConsistentValues_ReturnCorrectNames() {
            // Act & Assert
            assertThat(TextElementType.INLINE_COMMENT.name()).isEqualTo("INLINE_COMMENT");
            assertThat(TextElementType.MULTILINE_COMMENT.name()).isEqualTo("MULTILINE_COMMENT");
            assertThat(TextElementType.PRAGMA.name()).isEqualTo("PRAGMA");
            assertThat(TextElementType.PRAGMA_MACRO.name()).isEqualTo("PRAGMA_MACRO");
        }

        @Test
        @DisplayName("Should have consistent toString() values")
        void testEnumToString_ConsistentValues_ReturnCorrectStrings() {
            // Act & Assert
            assertThat(TextElementType.INLINE_COMMENT.toString()).isEqualTo("INLINE_COMMENT");
            assertThat(TextElementType.MULTILINE_COMMENT.toString()).isEqualTo("MULTILINE_COMMENT");
            assertThat(TextElementType.PRAGMA.toString()).isEqualTo("PRAGMA");
            assertThat(TextElementType.PRAGMA_MACRO.toString()).isEqualTo("PRAGMA_MACRO");
        }
    }

    @Nested
    @DisplayName("Enum Equality and Comparison Tests")
    class EnumEqualityTests {

        @Test
        @DisplayName("Should maintain reference equality")
        void testEnumEquality_ReferenceEquality_Maintained() {
            // Act & Assert - Enum constants should be singletons
            assertThat(TextElementType.INLINE_COMMENT == TextElementType.valueOf("INLINE_COMMENT")).isTrue();
            assertThat(TextElementType.MULTILINE_COMMENT == TextElementType.valueOf("MULTILINE_COMMENT")).isTrue();
            assertThat(TextElementType.PRAGMA == TextElementType.valueOf("PRAGMA")).isTrue();
            assertThat(TextElementType.PRAGMA_MACRO == TextElementType.valueOf("PRAGMA_MACRO")).isTrue();
        }

        @Test
        @DisplayName("Should support equals() method correctly")
        void testEnumEquals_CorrectBehavior_WorksAsExpected() {
            // Act & Assert
            assertThat(TextElementType.INLINE_COMMENT.equals(TextElementType.INLINE_COMMENT)).isTrue();
            assertThat(TextElementType.INLINE_COMMENT.equals(TextElementType.MULTILINE_COMMENT)).isFalse();
            assertThat(TextElementType.INLINE_COMMENT.equals(null)).isFalse();
            assertThat(TextElementType.INLINE_COMMENT.equals("INLINE_COMMENT")).isFalse();
        }

        @Test
        @DisplayName("Should support hashCode() consistency")
        void testEnumHashCode_Consistency_Maintained() {
            // Act & Assert - Same enum should have same hashCode
            assertThat(TextElementType.INLINE_COMMENT.hashCode())
                    .isEqualTo(TextElementType.INLINE_COMMENT.hashCode());

            // Different enums may have different hashCodes
            assertThat(TextElementType.INLINE_COMMENT.hashCode())
                    .isNotEqualTo(TextElementType.MULTILINE_COMMENT.hashCode());
        }
    }

    @Nested
    @DisplayName("Usage Pattern Tests")
    class UsagePatternTests {

        @Test
        @DisplayName("Should work in switch statements")
        void testEnumSwitchStatements_AllValues_WorkCorrectly() {
            // Act & Assert - Test all enum values in switch
            for (TextElementType type : TextElementType.values()) {
                String result = switch (type) {
                    case INLINE_COMMENT -> "inline";
                    case MULTILINE_COMMENT -> "multiline";
                    case PRAGMA -> "pragma";
                    case PRAGMA_MACRO -> "pragma_macro";
                };

                assertThat(result).isNotNull().isNotEmpty();
            }
        }

        @Test
        @DisplayName("Should work with collections")
        void testEnumCollections_SetOperations_WorkCorrectly() {
            // Arrange
            var commentTypes = java.util.Set.of(
                    TextElementType.INLINE_COMMENT,
                    TextElementType.MULTILINE_COMMENT);
            var pragmaTypes = java.util.Set.of(
                    TextElementType.PRAGMA,
                    TextElementType.PRAGMA_MACRO);

            // Act & Assert
            assertThat(commentTypes).hasSize(2);
            assertThat(pragmaTypes).hasSize(2);
            assertThat(commentTypes).doesNotContainAnyElementsOf(pragmaTypes);
        }

        @Test
        @DisplayName("Should be categorizable by functionality")
        void testEnumCategorization_FunctionalGrouping_WorksCorrectly() {
            // Act - Categorize by comment vs pragma types
            boolean isCommentType = isCommentType(TextElementType.INLINE_COMMENT);
            boolean isPragmaType = isPragmaType(TextElementType.PRAGMA);

            // Assert
            assertThat(isCommentType).isTrue();
            assertThat(isPragmaType).isTrue();
            assertThat(isCommentType(TextElementType.PRAGMA)).isFalse();
            assertThat(isPragmaType(TextElementType.INLINE_COMMENT)).isFalse();
        }

        private boolean isCommentType(TextElementType type) {
            return type == TextElementType.INLINE_COMMENT || type == TextElementType.MULTILINE_COMMENT;
        }

        private boolean isPragmaType(TextElementType type) {
            return type == TextElementType.PRAGMA || type == TextElementType.PRAGMA_MACRO;
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with TextElement interface")
        void testTextElementIntegration_AllTypes_SupportedCorrectly() {
            // Act & Assert - All enum values should work with TextElement
            for (TextElementType type : TextElementType.values()) {
                assertThatCode(() -> {
                    TextElement element = TextElement.newInstance(type, "test text");
                    assertThat(element.type()).isEqualTo(type);
                }).doesNotThrowAnyException();
            }
        }

        @Test
        @DisplayName("Should maintain consistency across enum operations")
        void testEnumConsistency_MultipleOperations_MaintainState() {
            // Arrange
            TextElementType original = TextElementType.INLINE_COMMENT;

            // Act - Multiple operations
            String name = original.name();
            int ordinal = original.ordinal();
            TextElementType fromName = TextElementType.valueOf(name);
            TextElementType fromArray = TextElementType.values()[ordinal];

            // Assert - All should reference same enum constant
            assertThat(fromName).isSameAs(original);
            assertThat(fromArray).isSameAs(original);
            assertThat(fromName == fromArray).isTrue();
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle enum serialization concepts")
        void testEnumSerialization_Consistency_Maintained() {
            // Act & Assert - Enum should have consistent string representation
            for (TextElementType type : TextElementType.values()) {
                String stringForm = type.toString();
                TextElementType reconstructed = TextElementType.valueOf(stringForm);
                assertThat(reconstructed).isSameAs(type);
            }
        }

        @Test
        @DisplayName("Should handle comparison operations")
        void testEnumComparison_NaturalOrdering_WorksCorrectly() {
            // Act & Assert - Enum comparison should work based on ordinal
            assertThat(TextElementType.INLINE_COMMENT.compareTo(TextElementType.MULTILINE_COMMENT)).isNegative();
            assertThat(TextElementType.PRAGMA.compareTo(TextElementType.INLINE_COMMENT)).isPositive();
            assertThat(TextElementType.PRAGMA_MACRO.compareTo(TextElementType.PRAGMA_MACRO)).isZero();
        }

        @Test
        @DisplayName("Should work in complex conditional logic")
        void testEnumConditionalLogic_ComplexScenarios_WorkCorrectly() {
            // Act & Assert - Complex logical operations
            for (TextElementType type : TextElementType.values()) {
                boolean isComment = type == TextElementType.INLINE_COMMENT ||
                        type == TextElementType.MULTILINE_COMMENT;
                boolean isPragma = type == TextElementType.PRAGMA ||
                        type == TextElementType.PRAGMA_MACRO;

                // Should be either comment or pragma, but not both
                assertThat(isComment || isPragma).isTrue();
                assertThat(isComment && isPragma).isFalse();
            }
        }
    }
}
