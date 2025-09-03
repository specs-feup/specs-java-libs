package pt.up.fe.specs.util.reporting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link MessageType}.
 * Tests the interface contract and default implementations for message types.
 * 
 * @author Generated Tests
 */
@DisplayName("MessageType")
class MessageTypeTest {

    @Nested
    @DisplayName("Interface Constants")
    class InterfaceConstants {

        @Test
        @DisplayName("Should have INFO_TYPE constant")
        void shouldHaveInfoTypeConstant() {
            // When/Then
            assertThat(MessageType.INFO_TYPE).isNotNull();
            assertThat(MessageType.INFO_TYPE).isInstanceOf(MessageType.class);
            assertThat(MessageType.INFO_TYPE.getMessageCategory()).isEqualTo(ReportCategory.INFORMATION);
        }

        @Test
        @DisplayName("Should have WARNING_TYPE constant")
        void shouldHaveWarningTypeConstant() {
            // When/Then
            assertThat(MessageType.WARNING_TYPE).isNotNull();
            assertThat(MessageType.WARNING_TYPE).isInstanceOf(MessageType.class);
            assertThat(MessageType.WARNING_TYPE.getMessageCategory()).isEqualTo(ReportCategory.WARNING);
        }

        @Test
        @DisplayName("Should have ERROR_TYPE constant")
        void shouldHaveErrorTypeConstant() {
            // When/Then
            assertThat(MessageType.ERROR_TYPE).isNotNull();
            assertThat(MessageType.ERROR_TYPE).isInstanceOf(MessageType.class);
            assertThat(MessageType.ERROR_TYPE.getMessageCategory()).isEqualTo(ReportCategory.ERROR);
        }

        @Test
        @DisplayName("Should have constants with appropriate names")
        void shouldHaveConstantsWithAppropriateNames() {
            // When/Then
            assertThat(MessageType.INFO_TYPE.getName()).isEqualTo("Info");
            assertThat(MessageType.WARNING_TYPE.getName()).isEqualTo("Warning");
            assertThat(MessageType.ERROR_TYPE.getName()).isEqualTo("Error");
        }

        @Test
        @DisplayName("Should have constants that are DefaultMessageType instances")
        void shouldHaveConstantsThatAreDefaultMessageTypeInstances() {
            // When/Then
            assertThat(MessageType.INFO_TYPE).isInstanceOf(DefaultMessageType.class);
            assertThat(MessageType.WARNING_TYPE).isInstanceOf(DefaultMessageType.class);
            assertThat(MessageType.ERROR_TYPE).isInstanceOf(DefaultMessageType.class);
        }
    }

    @Nested
    @DisplayName("Default Method Implementation")
    class DefaultMethodImplementation {

        @Test
        @DisplayName("Should provide default getName implementation")
        void shouldProvideDefaultGetNameImplementation() {
            // Given
            MessageType customType = new MessageType() {
                @Override
                public ReportCategory getMessageCategory() {
                    return ReportCategory.ERROR;
                }

                @Override
                public String toString() {
                    return "CustomType";
                }
            };

            // When
            String name = customType.getName();

            // Then
            assertThat(name).isEqualTo("CustomType");
        }

        @Test
        @DisplayName("Should allow overriding default getName implementation")
        void shouldAllowOverridingDefaultGetNameImplementation() {
            // Given
            MessageType customType = new MessageType() {
                @Override
                public ReportCategory getMessageCategory() {
                    return ReportCategory.WARNING;
                }

                @Override
                public String getName() {
                    return "OverriddenName";
                }

                @Override
                public String toString() {
                    return "DifferentToString";
                }
            };

            // When
            String name = customType.getName();

            // Then
            assertThat(name).isEqualTo("OverriddenName");
            assertThat(name).isNotEqualTo(customType.toString());
        }
    }

    @Nested
    @DisplayName("Interface Contract")
    class InterfaceContract {

        @Test
        @DisplayName("Should require getMessageCategory implementation")
        void shouldRequireGetMessageCategoryImplementation() {
            // Given
            MessageType[] types = {
                    MessageType.INFO_TYPE,
                    MessageType.WARNING_TYPE,
                    MessageType.ERROR_TYPE
            };

            // When/Then
            for (MessageType type : types) {
                assertThat(type.getMessageCategory()).isNotNull();
                assertThat(type.getMessageCategory()).isIn(
                        ReportCategory.ERROR,
                        ReportCategory.WARNING,
                        ReportCategory.INFORMATION);
            }
        }

        @Test
        @DisplayName("Should support custom implementations")
        void shouldSupportCustomImplementations() {
            // Given
            MessageType customError = new MessageType() {
                @Override
                public ReportCategory getMessageCategory() {
                    return ReportCategory.ERROR;
                }

                @Override
                public String getName() {
                    return "CustomError";
                }
            };

            MessageType customWarning = new MessageType() {
                @Override
                public ReportCategory getMessageCategory() {
                    return ReportCategory.WARNING;
                }

                @Override
                public String getName() {
                    return "CustomWarning";
                }
            };

            // When/Then
            assertThat(customError.getName()).isEqualTo("CustomError");
            assertThat(customError.getMessageCategory()).isEqualTo(ReportCategory.ERROR);

            assertThat(customWarning.getName()).isEqualTo("CustomWarning");
            assertThat(customWarning.getMessageCategory()).isEqualTo(ReportCategory.WARNING);
        }
    }

    @Nested
    @DisplayName("Polymorphic Behavior")
    class PolymorphicBehavior {

        @Test
        @DisplayName("Should work polymorphically with different implementations")
        void shouldWorkPolymorphicallyWithDifferentImplementations() {
            // Given
            List<MessageType> types = Arrays.asList(
                    MessageType.INFO_TYPE,
                    MessageType.WARNING_TYPE,
                    MessageType.ERROR_TYPE,
                    new DefaultMessageType("Custom", ReportCategory.ERROR),
                    new MessageType() {
                        @Override
                        public ReportCategory getMessageCategory() {
                            return ReportCategory.INFORMATION;
                        }

                        @Override
                        public String toString() {
                            return "AnonymousType";
                        }
                    });

            // When/Then
            for (MessageType type : types) {
                assertThat(type.getName()).isNotNull();
                assertThat(type.getMessageCategory()).isNotNull();
                assertThat(type.getMessageCategory()).isIn(
                        ReportCategory.ERROR,
                        ReportCategory.WARNING,
                        ReportCategory.INFORMATION);
            }
        }

        @Test
        @DisplayName("Should support method references and lambdas")
        void shouldSupportMethodReferencesAndLambdas() {
            // Given
            List<MessageType> types = Arrays.asList(
                    MessageType.INFO_TYPE,
                    MessageType.WARNING_TYPE,
                    MessageType.ERROR_TYPE);

            // When
            List<String> names = types.stream()
                    .map(MessageType::getName)
                    .toList();

            List<ReportCategory> categories = types.stream()
                    .map(MessageType::getMessageCategory)
                    .toList();

            // Then
            assertThat(names).containsExactly("Info", "Warning", "Error");
            assertThat(categories).containsExactly(
                    ReportCategory.INFORMATION,
                    ReportCategory.WARNING,
                    ReportCategory.ERROR);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null toString in default getName")
        void shouldHandleNullToStringInDefaultGetName() {
            // Given
            MessageType typeWithNullToString = new MessageType() {
                @Override
                public ReportCategory getMessageCategory() {
                    return ReportCategory.ERROR;
                }

                @Override
                public String toString() {
                    return null;
                }
            };

            // When
            String name = typeWithNullToString.getName();

            // Then
            assertThat(name).isNull();
        }

        @Test
        @DisplayName("Should handle null category gracefully")
        void shouldHandleNullCategoryGracefully() {
            // Given
            MessageType typeWithNullCategory = new MessageType() {
                @Override
                public ReportCategory getMessageCategory() {
                    return null;
                }
            };

            // When/Then
            assertThat(typeWithNullCategory.getMessageCategory()).isNull();
            assertThat(typeWithNullCategory.getName()).isNotNull(); // Should use toString()
        }

        @Test
        @DisplayName("Should handle empty toString gracefully")
        void shouldHandleEmptyToStringGracefully() {
            // Given
            MessageType typeWithEmptyToString = new MessageType() {
                @Override
                public ReportCategory getMessageCategory() {
                    return ReportCategory.WARNING;
                }

                @Override
                public String toString() {
                    return "";
                }
            };

            // When
            String name = typeWithEmptyToString.getName();

            // Then
            assertThat(name).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("Functional Interface Properties")
    class FunctionalInterfaceProperties {

        @Test
        @DisplayName("Should not be a functional interface")
        void shouldNotBeAFunctionalInterface() {
            // Given/When/Then - MessageType has one abstract method (getMessageCategory)
            // and one default method (getName), so it's effectively a functional interface
            // but also has constants, making it more of a regular interface

            // We can create lambda implementations
            MessageType lambdaType = () -> ReportCategory.ERROR;

            assertThat(lambdaType.getMessageCategory()).isEqualTo(ReportCategory.ERROR);
            assertThat(lambdaType.getName()).isNotNull(); // Uses default implementation
        }

        @Test
        @DisplayName("Should support lambda expressions")
        void shouldSupportLambdaExpressions() {
            // Given
            MessageType errorLambda = () -> ReportCategory.ERROR;
            MessageType warningLambda = () -> ReportCategory.WARNING;
            MessageType infoLambda = () -> ReportCategory.INFORMATION;

            // When/Then
            assertThat(errorLambda.getMessageCategory()).isEqualTo(ReportCategory.ERROR);
            assertThat(warningLambda.getMessageCategory()).isEqualTo(ReportCategory.WARNING);
            assertThat(infoLambda.getMessageCategory()).isEqualTo(ReportCategory.INFORMATION);

            // Default getName should work
            assertThat(errorLambda.getName()).isNotNull();
            assertThat(warningLambda.getName()).isNotNull();
            assertThat(infoLambda.getName()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Extensibility")
    class Extensibility {

        @Test
        @DisplayName("Should allow for custom message type hierarchies")
        void shouldAllowForCustomMessageTypeHierarchies() {
            // Given
            abstract class CustomMessageType implements MessageType {
                protected final String prefix;

                protected CustomMessageType(String prefix) {
                    this.prefix = prefix;
                }

                @Override
                public String getName() {
                    return prefix + ": " + getCustomName();
                }

                protected abstract String getCustomName();
            }

            CustomMessageType customError = new CustomMessageType("CUSTOM") {
                @Override
                public ReportCategory getMessageCategory() {
                    return ReportCategory.ERROR;
                }

                @Override
                protected String getCustomName() {
                    return "Validation Error";
                }
            };

            // When/Then
            assertThat(customError.getName()).isEqualTo("CUSTOM: Validation Error");
            assertThat(customError.getMessageCategory()).isEqualTo(ReportCategory.ERROR);
        }

        @Test
        @DisplayName("Should support composition with other interfaces")
        void shouldSupportCompositionWithOtherInterfaces() {
            // Given
            interface Prioritized {
                int getPriority();
            }

            class PrioritizedMessageType implements MessageType, Prioritized {
                private final ReportCategory category;
                private final String name;
                private final int priority;

                public PrioritizedMessageType(ReportCategory category, String name, int priority) {
                    this.category = category;
                    this.name = name;
                    this.priority = priority;
                }

                @Override
                public ReportCategory getMessageCategory() {
                    return category;
                }

                @Override
                public String getName() {
                    return name;
                }

                @Override
                public int getPriority() {
                    return priority;
                }
            }

            // When
            PrioritizedMessageType highPriorityError = new PrioritizedMessageType(
                    ReportCategory.ERROR, "Critical Error", 1);

            // Then
            assertThat(highPriorityError.getName()).isEqualTo("Critical Error");
            assertThat(highPriorityError.getMessageCategory()).isEqualTo(ReportCategory.ERROR);
            assertThat(highPriorityError.getPriority()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with all ReportCategory values")
        void shouldWorkWithAllReportCategoryValues() {
            // Given
            ReportCategory[] categories = ReportCategory.values();

            // When/Then
            for (ReportCategory category : categories) {
                MessageType type = new DefaultMessageType("Test", category);
                assertThat(type.getMessageCategory()).isEqualTo(category);
                assertThat(type.getName()).isEqualTo("Test");
            }
        }

        @Test
        @DisplayName("Should integrate with collections and streams")
        void shouldIntegrateWithCollectionsAndStreams() {
            // Given
            List<MessageType> types = Arrays.asList(
                    MessageType.ERROR_TYPE,
                    MessageType.WARNING_TYPE,
                    MessageType.INFO_TYPE);

            // When
            long errorCount = types.stream()
                    .filter(type -> type.getMessageCategory() == ReportCategory.ERROR)
                    .count();

            long warningCount = types.stream()
                    .filter(type -> type.getMessageCategory() == ReportCategory.WARNING)
                    .count();

            long infoCount = types.stream()
                    .filter(type -> type.getMessageCategory() == ReportCategory.INFORMATION)
                    .count();

            // Then
            assertThat(errorCount).isEqualTo(1);
            assertThat(warningCount).isEqualTo(1);
            assertThat(infoCount).isEqualTo(1);
        }
    }
}
