package org.suikasoft.jOptions.storedefinition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link StoreDefinitionProvider}.
 * 
 * Tests the functional interface for providing store definitions.
 * 
 * @author Generated Tests
 */
@DisplayName("StoreDefinitionProvider Tests")
class StoreDefinitionProviderTest {

    @Mock
    private StoreDefinition mockStoreDefinition;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Functional Interface Tests")
    class FunctionalInterfaceTests {

        @Test
        @DisplayName("Should work as lambda expression")
        void testProvider_AsLambda_ReturnsStoreDefinition() {
            // Given
            StoreDefinitionProvider provider = () -> mockStoreDefinition;

            // When
            StoreDefinition result = provider.getStoreDefinition();

            // Then
            assertThat(result).isSameAs(mockStoreDefinition);
        }

        @Test
        @DisplayName("Should work as method reference")
        void testProvider_AsMethodReference_ReturnsStoreDefinition() {
            // Given
            TestDefinitionSource source = new TestDefinitionSource(mockStoreDefinition);
            StoreDefinitionProvider provider = source::getDefinition;

            // When
            StoreDefinition result = provider.getStoreDefinition();

            // Then
            assertThat(result).isSameAs(mockStoreDefinition);
        }

        @Test
        @DisplayName("Should work as anonymous class")
        void testProvider_AsAnonymousClass_ReturnsStoreDefinition() {
            // Given
            StoreDefinitionProvider provider = new StoreDefinitionProvider() {
                @Override
                public StoreDefinition getStoreDefinition() {
                    return mockStoreDefinition;
                }
            };

            // When
            StoreDefinition result = provider.getStoreDefinition();

            // Then
            assertThat(result).isSameAs(mockStoreDefinition);
        }

        @Test
        @DisplayName("Should handle null return")
        void testProvider_ReturnsNull_HandlesGracefully() {
            // Given
            StoreDefinitionProvider provider = () -> null;

            // When
            StoreDefinition result = provider.getStoreDefinition();

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should propagate exceptions")
        void testProvider_ThrowsException_PropagatesException() {
            // Given
            RuntimeException expectedException = new RuntimeException("Test exception");
            StoreDefinitionProvider provider = () -> {
                throw expectedException;
            };

            // When/Then
            assertThatThrownBy(() -> provider.getStoreDefinition())
                    .isSameAs(expectedException);
        }
    }

    @Nested
    @DisplayName("Usage Pattern Tests")
    class UsagePatternTests {

        @Test
        @DisplayName("Should work with higher-order functions")
        void testProvider_WithHigherOrderFunction_ProcessesCorrectly() {
            // Given
            when(mockStoreDefinition.getName()).thenReturn("TestStore");
            StoreDefinitionProvider provider = () -> mockStoreDefinition;

            // When
            String result = extractName(provider);

            // Then
            assertThat(result).isEqualTo("TestStore");
        }

        @Test
        @DisplayName("Should support composition")
        void testProvider_WithComposition_ChainsCorrectly() {
            // Given
            when(mockStoreDefinition.getName()).thenReturn("OriginalStore");
            StoreDefinitionProvider baseProvider = () -> mockStoreDefinition;

            StoreDefinition wrappedDefinition = mock(StoreDefinition.class);
            when(wrappedDefinition.getName()).thenReturn("WrappedStore");

            StoreDefinitionProvider wrappingProvider = () -> {
                baseProvider.getStoreDefinition(); // Call base provider to test composition
                return wrappedDefinition; // Simulate some transformation
            };

            // When
            StoreDefinition result = wrappingProvider.getStoreDefinition();

            // Then
            assertThat(result).isSameAs(wrappedDefinition);
            assertThat(result.getName()).isEqualTo("WrappedStore");

            // Verify base provider was called
            verify(mockStoreDefinition, never()).getName(); // Base mock wasn't used in final result
        }

        @Test
        @DisplayName("Should work with conditional logic")
        void testProvider_WithConditionalLogic_ChoosesCorrectly() {
            // Given
            StoreDefinition alternativeDefinition = mock(StoreDefinition.class);
            when(mockStoreDefinition.getName()).thenReturn("Default");
            when(alternativeDefinition.getName()).thenReturn("Alternative");

            boolean useAlternative = true;
            StoreDefinitionProvider conditionalProvider = () -> useAlternative ? alternativeDefinition
                    : mockStoreDefinition;

            // When
            StoreDefinition result = conditionalProvider.getStoreDefinition();

            // Then
            assertThat(result).isSameAs(alternativeDefinition);
            assertThat(result.getName()).isEqualTo("Alternative");
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle multiple calls")
        void testProvider_MultipleCalls_ReturnsConsistently() {
            // Given
            StoreDefinitionProvider provider = () -> mockStoreDefinition;

            // When
            StoreDefinition result1 = provider.getStoreDefinition();
            StoreDefinition result2 = provider.getStoreDefinition();

            // Then
            assertThat(result1).isSameAs(mockStoreDefinition);
            assertThat(result2).isSameAs(mockStoreDefinition);
            assertThat(result1).isSameAs(result2);
        }

        @Test
        @DisplayName("Should work with complex expressions")
        void testProvider_ComplexExpression_ExecutesCorrectly() {
            // Given
            when(mockStoreDefinition.getName()).thenReturn("TestStore");

            StoreDefinitionProvider complexProvider = () -> {
                // Simulate complex logic
                String prefix = "Test";
                if (mockStoreDefinition.getName().startsWith(prefix)) {
                    return mockStoreDefinition;
                }
                return null;
            };

            // When
            StoreDefinition result = complexProvider.getStoreDefinition();

            // Then
            assertThat(result).isSameAs(mockStoreDefinition);
        }
    }

    // Helper methods for testing
    private String extractName(StoreDefinitionProvider provider) {
        return provider.getStoreDefinition().getName();
    }

    // Helper class for method reference testing
    private static class TestDefinitionSource {
        private final StoreDefinition definition;

        TestDefinitionSource(StoreDefinition definition) {
            this.definition = definition;
        }

        StoreDefinition getDefinition() {
            return definition;
        }
    }
}
