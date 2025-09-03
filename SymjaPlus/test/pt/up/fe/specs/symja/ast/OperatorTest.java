package pt.up.fe.specs.symja.ast;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Unit tests for {@link Operator}.
 * 
 * @author Generated Tests
 */
@DisplayName("Operator")
class OperatorTest {

    @Nested
    @DisplayName("Operator Properties Tests")
    class OperatorPropertiesTests {

        @Test
        @DisplayName("Should have correct symbols for all operators")
        void testGetSymbol_AllOperators_ReturnsCorrectSymbols() {
            assertThat(Operator.Plus.getSymbol()).isEqualTo("+");
            assertThat(Operator.Minus.getSymbol()).isEqualTo("-");
            assertThat(Operator.Times.getSymbol()).isEqualTo("*");
            assertThat(Operator.Power.getSymbol()).isEqualTo("^");
            assertThat(Operator.UnaryMinus.getSymbol()).isEqualTo("-");
        }

        @Test
        @DisplayName("Should have correct priorities for all operators")
        void testGetPriority_AllOperators_ReturnsCorrectPriorities() {
            assertThat(Operator.Plus.getPriority()).isEqualTo(2);
            assertThat(Operator.Minus.getPriority()).isEqualTo(2);
            assertThat(Operator.Times.getPriority()).isEqualTo(3);
            assertThat(Operator.Power.getPriority()).isEqualTo(4);
            assertThat(Operator.UnaryMinus.getPriority()).isEqualTo(4);
        }

        @Test
        @DisplayName("Should verify operator priority hierarchy")
        void testPriorityHierarchy_OperatorPrecedence_IsCorrect() {
            // Plus and Minus have same priority
            assertThat(Operator.Plus.getPriority()).isEqualTo(Operator.Minus.getPriority());

            // Times has higher priority than Plus/Minus
            assertThat(Operator.Times.getPriority()).isGreaterThan(Operator.Plus.getPriority());
            assertThat(Operator.Times.getPriority()).isGreaterThan(Operator.Minus.getPriority());

            // Power and UnaryMinus have highest priority
            assertThat(Operator.Power.getPriority()).isGreaterThan(Operator.Times.getPriority());
            assertThat(Operator.UnaryMinus.getPriority()).isGreaterThan(Operator.Times.getPriority());

            // Power and UnaryMinus have same priority
            assertThat(Operator.Power.getPriority()).isEqualTo(Operator.UnaryMinus.getPriority());
        }

        @ParameterizedTest
        @EnumSource(Operator.class)
        @DisplayName("Should have non-null symbol for all operators")
        void testGetSymbol_AllOperators_ReturnsNonNullSymbol(Operator operator) {
            assertThat(operator.getSymbol()).isNotNull();
            assertThat(operator.getSymbol()).isNotEmpty();
        }

        @ParameterizedTest
        @EnumSource(Operator.class)
        @DisplayName("Should have positive priority for all operators")
        void testGetPriority_AllOperators_ReturnsPositivePriority(Operator operator) {
            assertThat(operator.getPriority()).isPositive();
        }
    }

    @Nested
    @DisplayName("Operator Conversion Tests")
    class OperatorConversionTests {

        @Test
        @DisplayName("Should convert from Symja symbol strings correctly")
        void testFromSymjaSymbol_ValidOperators_ReturnsCorrectOperator() {
            assertThat(Operator.fromSymjaSymbol("Plus")).isEqualTo(Operator.Plus);
            assertThat(Operator.fromSymjaSymbol("Minus")).isEqualTo(Operator.Minus);
            assertThat(Operator.fromSymjaSymbol("Times")).isEqualTo(Operator.Times);
            assertThat(Operator.fromSymjaSymbol("Power")).isEqualTo(Operator.Power);
            assertThat(Operator.fromSymjaSymbol("UnaryMinus")).isEqualTo(Operator.UnaryMinus);
        }

        @Test
        @DisplayName("Should throw exception for unknown Symja symbol")
        void testFromSymjaSymbol_UnknownSymbol_ThrowsException() {
            assertThatThrownBy(() -> Operator.fromSymjaSymbol("UnknownOperator"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("No enum constant pt.up.fe.specs.symja.ast.Operator.UnknownOperator");
        }

        @Test
        @DisplayName("Should throw exception for null Symja symbol")
        void testFromSymjaSymbol_NullSymbol_ThrowsException() {
            assertThatThrownBy(() -> Operator.fromSymjaSymbol(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should throw exception for empty Symja symbol")
        void testFromSymjaSymbol_EmptySymbol_ThrowsException() {
            assertThatThrownBy(() -> Operator.fromSymjaSymbol(""))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Operator Enum Tests")
    class OperatorEnumTests {

        @Test
        @DisplayName("Should have expected number of operators")
        void testOperatorCount_EnumValues_HasExpectedCount() {
            Operator[] operators = Operator.values();
            assertThat(operators).hasSize(5);
        }

        @Test
        @DisplayName("Should contain all expected operators")
        void testOperatorValues_EnumValues_ContainsExpectedOperators() {
            Operator[] operators = Operator.values();
            assertThat(operators).containsExactlyInAnyOrder(
                    Operator.Plus,
                    Operator.Minus,
                    Operator.Times,
                    Operator.Power,
                    Operator.UnaryMinus);
        }

        @Test
        @DisplayName("Should support valueOf operations")
        void testValueOf_AllOperators_ReturnsCorrectOperator() {
            assertThat(Operator.valueOf("Plus")).isEqualTo(Operator.Plus);
            assertThat(Operator.valueOf("Minus")).isEqualTo(Operator.Minus);
            assertThat(Operator.valueOf("Times")).isEqualTo(Operator.Times);
            assertThat(Operator.valueOf("Power")).isEqualTo(Operator.Power);
            assertThat(Operator.valueOf("UnaryMinus")).isEqualTo(Operator.UnaryMinus);
        }

        @Test
        @DisplayName("Should throw exception for invalid valueOf")
        void testValueOf_InvalidOperator_ThrowsException() {
            assertThatThrownBy(() -> Operator.valueOf("InvalidOperator"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Operator Comparison Tests")
    class OperatorComparisonTests {

        @Test
        @DisplayName("Should correctly compare operator priorities")
        void testPriorityComparison_DifferentOperators_ReturnsCorrectComparison() {
            // Lower priority operators
            assertThat(Operator.Plus.getPriority()).isLessThan(Operator.Times.getPriority());
            assertThat(Operator.Minus.getPriority()).isLessThan(Operator.Times.getPriority());

            // Medium priority operators
            assertThat(Operator.Times.getPriority()).isLessThan(Operator.Power.getPriority());
            assertThat(Operator.Times.getPriority()).isLessThan(Operator.UnaryMinus.getPriority());

            // Equal priority operators
            assertThat(Operator.Plus.getPriority()).isEqualTo(Operator.Minus.getPriority());
            assertThat(Operator.Power.getPriority()).isEqualTo(Operator.UnaryMinus.getPriority());
        }

        @Test
        @DisplayName("Should support operator sorting by priority")
        void testOperatorSorting_ByPriority_WorksCorrectly() {
            java.util.List<Operator> operators = java.util.Arrays.asList(
                    Operator.Power, Operator.Plus, Operator.Times, Operator.Minus, Operator.UnaryMinus);

            // Sort by priority (ascending)
            operators.sort(java.util.Comparator.comparingInt(Operator::getPriority));

            // Verify sorting order
            assertThat(operators.get(0)).isIn(Operator.Plus, Operator.Minus); // Priority 2
            assertThat(operators.get(1)).isIn(Operator.Plus, Operator.Minus); // Priority 2
            assertThat(operators.get(2)).isEqualTo(Operator.Times); // Priority 3
            assertThat(operators.get(3)).isIn(Operator.Power, Operator.UnaryMinus); // Priority 4
            assertThat(operators.get(4)).isIn(Operator.Power, Operator.UnaryMinus); // Priority 4
        }
    }

    @Nested
    @DisplayName("Operator String Representation Tests")
    class OperatorStringRepresentationTests {

        @Test
        @DisplayName("Should have meaningful string representation")
        void testToString_AllOperators_ReturnsOperatorName() {
            assertThat(Operator.Plus.toString()).isEqualTo("Plus");
            assertThat(Operator.Minus.toString()).isEqualTo("Minus");
            assertThat(Operator.Times.toString()).isEqualTo("Times");
            assertThat(Operator.Power.toString()).isEqualTo("Power");
            assertThat(Operator.UnaryMinus.toString()).isEqualTo("UnaryMinus");
        }

        @ParameterizedTest
        @EnumSource(Operator.class)
        @DisplayName("Should have consistent toString and name")
        void testToString_AllOperators_MatchesName(Operator operator) {
            assertThat(operator.toString()).isEqualTo(operator.name());
        }
    }

    @Nested
    @DisplayName("Mathematical Properties Tests")
    class MathematicalPropertiesTests {

        @Test
        @DisplayName("Should identify binary operators correctly")
        void testBinaryOperators_MathematicalSemantics_IdentifiesCorrectly() {
            // Binary operators require two operands
            java.util.Set<Operator> binaryOperators = java.util.Set.of(
                    Operator.Plus, Operator.Minus, Operator.Times, Operator.Power);

            for (Operator op : binaryOperators) {
                // Binary operators typically have lower priorities for associativity
                assertThat(op.getPriority()).isLessThanOrEqualTo(4);
            }
        }

        @Test
        @DisplayName("Should identify unary operators correctly")
        void testUnaryOperators_MathematicalSemantics_IdentifiesCorrectly() {
            // UnaryMinus is the only unary operator
            assertThat(Operator.UnaryMinus.getPriority()).isEqualTo(4); // High priority for unary operations
            assertThat(Operator.UnaryMinus.getSymbol()).isEqualTo("-");
        }

        @Test
        @DisplayName("Should handle operator associativity priorities")
        void testOperatorAssociativity_MathematicalRules_HandledCorrectly() {
            // Left associative operators (same priority): Plus, Minus
            assertThat(Operator.Plus.getPriority()).isEqualTo(Operator.Minus.getPriority());

            // Right associative operators typically have higher priority: Power
            assertThat(Operator.Power.getPriority()).isGreaterThan(Operator.Times.getPriority());

            // Unary operators have highest priority
            assertThat(Operator.UnaryMinus.getPriority()).isEqualTo(Operator.Power.getPriority());
        }
    }
}
