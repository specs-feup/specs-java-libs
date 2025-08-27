package pt.up.fe.specs.util.providers.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pt.up.fe.specs.util.providers.StringProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CachedStringProvider}.
 * Tests the caching behavior and delegation to underlying StringProvider.
 * 
 * @author Generated Tests
 */
@DisplayName("CachedStringProvider")
class CachedStringProviderTest {

    @Mock
    private StringProvider mockProvider;

    private static final String TEST_STRING = "test string content";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Constructor")
    class Constructor {

        @Test
        @DisplayName("Should create provider with underlying provider")
        void shouldCreateProviderWithUnderlyingProvider() {
            // When
            CachedStringProvider cachedProvider = new CachedStringProvider(mockProvider);

            // Then
            assertThat(cachedProvider).isNotNull();
        }

        @Test
        @DisplayName("Should accept null provider")
        void shouldAcceptNullProvider() {
            // When/Then - Constructor should not throw exception with null provider
            CachedStringProvider cachedProvider = new CachedStringProvider(null);
            assertThat(cachedProvider).isNotNull();
        }
    }

    @Nested
    @DisplayName("Interface Implementation")
    class InterfaceImplementation {

        @Test
        @DisplayName("Should implement StringProvider interface")
        void shouldImplementStringProviderInterface() {
            // Given
            CachedStringProvider cachedProvider = new CachedStringProvider(mockProvider);

            // Then
            assertThat(cachedProvider).isInstanceOf(StringProvider.class);
        }
    }

    @Nested
    @DisplayName("Caching Behavior")
    class CachingBehavior {

        @Test
        @DisplayName("Should call underlying provider only once")
        void shouldCallUnderlyingProviderOnlyOnce() {
            // Given
            when(mockProvider.getString()).thenReturn(TEST_STRING);
            CachedStringProvider cachedProvider = new CachedStringProvider(mockProvider);

            // When
            String result1 = cachedProvider.getString();
            String result2 = cachedProvider.getString();
            String result3 = cachedProvider.getString();

            // Then
            verify(mockProvider, times(1)).getString();
            assertThat(result1).isEqualTo(TEST_STRING);
            assertThat(result2).isEqualTo(TEST_STRING);
            assertThat(result3).isEqualTo(TEST_STRING);
        }

        @Test
        @DisplayName("Should return same instance on multiple calls")
        void shouldReturnSameInstanceOnMultipleCalls() {
            // Given
            when(mockProvider.getString()).thenReturn(TEST_STRING);
            CachedStringProvider cachedProvider = new CachedStringProvider(mockProvider);

            // When
            String result1 = cachedProvider.getString();
            String result2 = cachedProvider.getString();

            // Then
            assertThat(result1).isSameAs(result2);
        }

        @Test
        @DisplayName("Should cache different string values correctly")
        void shouldCacheDifferentStringValuesCorrectly() {
            // Given
            String differentString = "different content";
            when(mockProvider.getString()).thenReturn(differentString);
            CachedStringProvider cachedProvider = new CachedStringProvider(mockProvider);

            // When
            String result1 = cachedProvider.getString();
            String result2 = cachedProvider.getString();

            // Then
            verify(mockProvider, times(1)).getString();
            assertThat(result1).isEqualTo(differentString);
            assertThat(result2).isEqualTo(differentString);
            assertThat(result1).isSameAs(result2);
        }
    }

    @Nested
    @DisplayName("Null Handling")
    class NullHandling {

        @Test
        @DisplayName("Should throw NPE when underlying provider returns null")
        void shouldThrowNPEWhenUnderlyingProviderReturnsNull() {
            // Given
            when(mockProvider.getString()).thenReturn(null);
            CachedStringProvider cachedProvider = new CachedStringProvider(mockProvider);

            // When/Then - Implementation uses Optional.of() which throws NPE for null
            // values
            assertThatThrownBy(() -> cachedProvider.getString())
                    .isInstanceOf(NullPointerException.class);

            // Should still only call provider once
            verify(mockProvider, times(1)).getString();
        }

        @Test
        @DisplayName("Should handle null underlying provider")
        void shouldHandleNullUnderlyingProvider() {
            // Given
            CachedStringProvider cachedProvider = new CachedStringProvider(null);

            // When/Then - Should throw NPE when trying to call null provider
            assertThatThrownBy(() -> cachedProvider.getString())
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should cache empty string correctly")
        void shouldCacheEmptyStringCorrectly() {
            // Given
            when(mockProvider.getString()).thenReturn("");
            CachedStringProvider cachedProvider = new CachedStringProvider(mockProvider);

            // When
            String result1 = cachedProvider.getString();
            String result2 = cachedProvider.getString();

            // Then
            verify(mockProvider, times(1)).getString();
            assertThat(result1).isEmpty();
            assertThat(result2).isEmpty();
            assertThat(result1).isSameAs(result2);
        }

        @Test
        @DisplayName("Should cache very large strings correctly")
        void shouldCacheVeryLargeStringsCorrectly() {
            // Given
            String largeString = "x".repeat(10000);
            when(mockProvider.getString()).thenReturn(largeString);
            CachedStringProvider cachedProvider = new CachedStringProvider(mockProvider);

            // When
            String result1 = cachedProvider.getString();
            String result2 = cachedProvider.getString();

            // Then
            verify(mockProvider, times(1)).getString();
            assertThat(result1).isEqualTo(largeString);
            assertThat(result2).isEqualTo(largeString);
            assertThat(result1).isSameAs(result2);
        }

        @Test
        @DisplayName("Should cache strings with special characters correctly")
        void shouldCacheStringsWithSpecialCharactersCorrectly() {
            // Given
            String specialString = "Hello\nWorld\r\n\tSpecial: éñüiños 中文 🌟";
            when(mockProvider.getString()).thenReturn(specialString);
            CachedStringProvider cachedProvider = new CachedStringProvider(mockProvider);

            // When
            String result1 = cachedProvider.getString();
            String result2 = cachedProvider.getString();

            // Then
            verify(mockProvider, times(1)).getString();
            assertThat(result1).isEqualTo(specialString);
            assertThat(result2).isEqualTo(specialString);
            assertThat(result1).isSameAs(result2);
        }

        @Test
        @DisplayName("Should handle provider that throws exception")
        void shouldHandleProviderThatThrowsException() {
            // Given
            RuntimeException expectedException = new RuntimeException("Provider error");
            when(mockProvider.getString()).thenThrow(expectedException);
            CachedStringProvider cachedProvider = new CachedStringProvider(mockProvider);

            // When/Then - First call should throw exception
            assertThatThrownBy(() -> cachedProvider.getString())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Provider error");

            // Second call should also throw exception (no caching of exceptions)
            assertThatThrownBy(() -> cachedProvider.getString())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Provider error");

            // Provider should be called twice
            verify(mockProvider, times(2)).getString();
        }
    }

    @Nested
    @DisplayName("Multiple Instance Behavior")
    class MultipleInstanceBehavior {

        @Test
        @DisplayName("Different cached providers with same underlying provider should be independent")
        void differentCachedProvidersWithSameUnderlyingProviderShouldBeIndependent() {
            // Given
            when(mockProvider.getString()).thenReturn(TEST_STRING);
            CachedStringProvider cachedProvider1 = new CachedStringProvider(mockProvider);
            CachedStringProvider cachedProvider2 = new CachedStringProvider(mockProvider);

            // When
            String result1 = cachedProvider1.getString();
            String result2 = cachedProvider2.getString();

            // Then
            verify(mockProvider, times(2)).getString(); // Called once per cached provider
            assertThat(result1).isEqualTo(TEST_STRING);
            assertThat(result2).isEqualTo(TEST_STRING);
            // Results should be equal but not necessarily the same instance
            assertThat(result1).isEqualTo(result2);
        }

        @Test
        @DisplayName("Cached providers with different underlying providers should work independently")
        void cachedProvidersWithDifferentUnderlyingProvidersShouldWorkIndependently() {
            // Given
            StringProvider mockProvider2 = mock(StringProvider.class);
            String testString2 = "different content";

            when(mockProvider.getString()).thenReturn(TEST_STRING);
            when(mockProvider2.getString()).thenReturn(testString2);

            CachedStringProvider cachedProvider1 = new CachedStringProvider(mockProvider);
            CachedStringProvider cachedProvider2 = new CachedStringProvider(mockProvider2);

            // When
            String result1 = cachedProvider1.getString();
            String result2 = cachedProvider2.getString();

            // Then
            verify(mockProvider, times(1)).getString();
            verify(mockProvider2, times(1)).getString();
            assertThat(result1).isEqualTo(TEST_STRING);
            assertThat(result2).isEqualTo(testString2);
            assertThat(result1).isNotEqualTo(result2);
        }
    }

    @Nested
    @DisplayName("Performance Characteristics")
    class PerformanceCharacteristics {

        @Test
        @DisplayName("Should not call expensive provider multiple times")
        void shouldNotCallExpensiveProviderMultipleTimes() {
            // Given
            StringProvider expensiveProvider = mock(StringProvider.class);
            when(expensiveProvider.getString()).thenAnswer(invocation -> {
                // Simulate expensive operation
                Thread.sleep(1);
                return TEST_STRING;
            });

            CachedStringProvider cachedProvider = new CachedStringProvider(expensiveProvider);

            // When
            long startTime = System.nanoTime();
            cachedProvider.getString(); // First call - expensive
            long afterFirstCall = System.nanoTime();

            cachedProvider.getString(); // Second call - should be fast (cached)
            long afterSecondCall = System.nanoTime();

            // Then
            verify(expensiveProvider, times(1)).getString();

            // Second call should be much faster than first call
            long firstCallTime = afterFirstCall - startTime;
            long secondCallTime = afterSecondCall - afterFirstCall;
            assertThat(secondCallTime).isLessThan(firstCallTime);
        }
    }
}
