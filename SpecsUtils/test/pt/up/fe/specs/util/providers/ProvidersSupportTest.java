package pt.up.fe.specs.util.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the ProvidersSupport class.
 * 
 * @author Generated Tests
 */
@DisplayName("ProvidersSupport")
class ProvidersSupportTest {

    @Nested
    @DisplayName("getResourcesFromEnumSingle")
    class GetResourcesFromEnumSingle {

        enum TestResourceEnum implements ResourceProvider {
            RESOURCE_A("test/resource/a.txt"),
            RESOURCE_B("test/resource/b.txt"),
            RESOURCE_C("test/resource/c.txt");

            private final String resourcePath;

            TestResourceEnum(String resourcePath) {
                this.resourcePath = resourcePath;
            }

            @Override
            public String getResource() {
                return resourcePath;
            }
        }

        enum EmptyResourceEnum implements ResourceProvider {
            ; // Empty enum with semicolon

            @Override
            public String getResource() {
                // This method is never called for empty enum
                throw new UnsupportedOperationException("Empty enum");
            }
        }

        enum SingleResourceEnum implements ResourceProvider {
            SINGLE("single/resource.txt");

            private final String resourcePath;

            SingleResourceEnum(String resourcePath) {
                this.resourcePath = resourcePath;
            }

            @Override
            public String getResource() {
                return resourcePath;
            }
        }

        @Test
        @DisplayName("should extract resources from enum with multiple values")
        void shouldExtractResourcesFromEnumWithMultipleValues() throws Exception {
            // Given
            Method method = ProvidersSupport.class.getDeclaredMethod("getResourcesFromEnumSingle", Class.class);
            method.setAccessible(true);

            // When
            @SuppressWarnings("unchecked")
            List<ResourceProvider> result = (List<ResourceProvider>) method.invoke(null, TestResourceEnum.class);

            // Then
            assertThat(result).hasSize(3);
            assertThat(result.get(0)).isEqualTo(TestResourceEnum.RESOURCE_A);
            assertThat(result.get(1)).isEqualTo(TestResourceEnum.RESOURCE_B);
            assertThat(result.get(2)).isEqualTo(TestResourceEnum.RESOURCE_C);
        }

        @Test
        @DisplayName("should handle empty enum")
        void shouldHandleEmptyEnum() throws Exception {
            // Given
            Method method = ProvidersSupport.class.getDeclaredMethod("getResourcesFromEnumSingle", Class.class);
            method.setAccessible(true);

            // When
            @SuppressWarnings("unchecked")
            List<ResourceProvider> result = (List<ResourceProvider>) method.invoke(null, EmptyResourceEnum.class);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should handle single value enum")
        void shouldHandleSingleValueEnum() throws Exception {
            // Given
            Method method = ProvidersSupport.class.getDeclaredMethod("getResourcesFromEnumSingle", Class.class);
            method.setAccessible(true);

            // When
            @SuppressWarnings("unchecked")
            List<ResourceProvider> result = (List<ResourceProvider>) method.invoke(null, SingleResourceEnum.class);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0)).isEqualTo(SingleResourceEnum.SINGLE);
        }

        @Test
        @DisplayName("should preserve enum order")
        void shouldPreserveEnumOrder() throws Exception {
            // Given
            Method method = ProvidersSupport.class.getDeclaredMethod("getResourcesFromEnumSingle", Class.class);
            method.setAccessible(true);

            // When
            @SuppressWarnings("unchecked")
            List<ResourceProvider> result = (List<ResourceProvider>) method.invoke(null, TestResourceEnum.class);

            // Then
            assertThat(result).hasSize(3);
            assertThat(result.get(0).getResource()).isEqualTo("test/resource/a.txt");
            assertThat(result.get(1).getResource()).isEqualTo("test/resource/b.txt");
            assertThat(result.get(2).getResource()).isEqualTo("test/resource/c.txt");
        }

        @Test
        @DisplayName("should return new list on each call")
        void shouldReturnNewListOnEachCall() throws Exception {
            // Given
            Method method = ProvidersSupport.class.getDeclaredMethod("getResourcesFromEnumSingle", Class.class);
            method.setAccessible(true);

            // When
            @SuppressWarnings("unchecked")
            List<ResourceProvider> result1 = (List<ResourceProvider>) method.invoke(null, TestResourceEnum.class);
            @SuppressWarnings("unchecked")
            List<ResourceProvider> result2 = (List<ResourceProvider>) method.invoke(null, TestResourceEnum.class);

            // Then
            assertThat(result1).isNotSameAs(result2);
            assertThat(result1).isEqualTo(result2);
        }

        @Test
        @DisplayName("should handle null enum class")
        void shouldHandleNullEnumClass() throws Exception {
            // Given
            Method method = ProvidersSupport.class.getDeclaredMethod("getResourcesFromEnumSingle", Class.class);
            method.setAccessible(true);

            // When/Then
            assertThatThrownBy(() -> method.invoke(null, (Class<?>) null))
                    .hasCauseInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("should reject non-enum class")
        void shouldRejectNonEnumClass() throws Exception {
            // Given
            Method method = ProvidersSupport.class.getDeclaredMethod("getResourcesFromEnumSingle", Class.class);
            method.setAccessible(true);

            // Regular class that implements ResourceProvider
            class RegularClass implements ResourceProvider {
                @Override
                public String getResource() {
                    return "regular";
                }
            }

            // When/Then
            assertThatThrownBy(() -> method.invoke(null, RegularClass.class))
                    .hasRootCauseMessage("Class must be an enum");
        }

        @Test
        @DisplayName("should work with enum implementing multiple interfaces")
        void shouldWorkWithEnumImplementingMultipleInterfaces() throws Exception {
            // Given
            enum MultiInterfaceEnum implements ResourceProvider, KeyProvider<String> {
                MULTI_A("multi/a.txt", "key-a"),
                MULTI_B("multi/b.txt", "key-b");

                private final String resource;
                private final String key;

                MultiInterfaceEnum(String resource, String key) {
                    this.resource = resource;
                    this.key = key;
                }

                @Override
                public String getResource() {
                    return resource;
                }

                @Override
                public String getKey() {
                    return key;
                }
            }

            Method method = ProvidersSupport.class.getDeclaredMethod("getResourcesFromEnumSingle", Class.class);
            method.setAccessible(true);

            // When
            @SuppressWarnings("unchecked")
            List<ResourceProvider> result = (List<ResourceProvider>) method.invoke(null, MultiInterfaceEnum.class);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0)).isEqualTo(MultiInterfaceEnum.MULTI_A);
            assertThat(result.get(1)).isEqualTo(MultiInterfaceEnum.MULTI_B);
            assertThat(result.get(0).getResource()).isEqualTo("multi/a.txt");
            assertThat(result.get(1).getResource()).isEqualTo("multi/b.txt");
        }
    }

    @Nested
    @DisplayName("Class Characteristics")
    class ClassCharacteristics {

        @Test
        @DisplayName("should be utility class with default public constructor")
        void shouldBeUtilityClassWithDefaultPublicConstructor() throws Exception {
            // Given/When
            var constructor = ProvidersSupport.class.getDeclaredConstructor();

            // Then - Java provides default public constructor for public classes
            assertThat(java.lang.reflect.Modifier.isPublic(constructor.getModifiers())).isTrue();
        }

        @Test
        @DisplayName("should have static methods only")
        void shouldHaveStaticMethodsOnly() {
            // Given/When
            Method[] methods = ProvidersSupport.class.getDeclaredMethods();

            // Then
            for (Method method : methods) {
                if (!method.isSynthetic()) { // Ignore synthetic methods
                    assertThat(java.lang.reflect.Modifier.isStatic(method.getModifiers()))
                            .as("Method %s should be static", method.getName())
                            .isTrue();
                }
            }
        }

        @Test
        @DisplayName("should have package-private methods")
        void shouldHavePackagePrivateMethods() throws Exception {
            // Given/When
            Method method = ProvidersSupport.class.getDeclaredMethod("getResourcesFromEnumSingle", Class.class);

            // Then
            assertThat(java.lang.reflect.Modifier.isPublic(method.getModifiers())).isFalse();
            assertThat(java.lang.reflect.Modifier.isPrivate(method.getModifiers())).isFalse();
            assertThat(java.lang.reflect.Modifier.isProtected(method.getModifiers())).isFalse();
            // Package-private methods have no explicit modifier
        }
    }

    @Nested
    @DisplayName("List Implementation Details")
    class ListImplementationDetails {

        enum ListTestEnum implements ResourceProvider {
            ITEM_1("item1.txt"),
            ITEM_2("item2.txt"),
            ITEM_3("item3.txt"),
            ITEM_4("item4.txt"),
            ITEM_5("item5.txt");

            private final String resource;

            ListTestEnum(String resource) {
                this.resource = resource;
            }

            @Override
            public String getResource() {
                return resource;
            }
        }

        @Test
        @DisplayName("should create list with correct initial capacity")
        void shouldCreateListWithCorrectInitialCapacity() throws Exception {
            // Given
            Method method = ProvidersSupport.class.getDeclaredMethod("getResourcesFromEnumSingle", Class.class);
            method.setAccessible(true);

            // When
            @SuppressWarnings("unchecked")
            List<ResourceProvider> result = (List<ResourceProvider>) method.invoke(null, ListTestEnum.class);

            // Then - List should be sized correctly
            assertThat(result).hasSize(5);
            assertThat(result).hasSize(ListTestEnum.values().length);
        }

        @Test
        @DisplayName("should maintain reference equality for enum constants")
        void shouldMaintainReferenceEqualityForEnumConstants() throws Exception {
            // Given
            Method method = ProvidersSupport.class.getDeclaredMethod("getResourcesFromEnumSingle", Class.class);
            method.setAccessible(true);

            // When
            @SuppressWarnings("unchecked")
            List<ResourceProvider> result = (List<ResourceProvider>) method.invoke(null, ListTestEnum.class);

            // Then - Should be the same object references
            assertThat(result.get(0)).isSameAs(ListTestEnum.ITEM_1);
            assertThat(result.get(1)).isSameAs(ListTestEnum.ITEM_2);
            assertThat(result.get(2)).isSameAs(ListTestEnum.ITEM_3);
            assertThat(result.get(3)).isSameAs(ListTestEnum.ITEM_4);
            assertThat(result.get(4)).isSameAs(ListTestEnum.ITEM_5);
        }

        @Test
        @DisplayName("should create mutable list")
        void shouldCreateMutableList() throws Exception {
            // Given
            Method method = ProvidersSupport.class.getDeclaredMethod("getResourcesFromEnumSingle", Class.class);
            method.setAccessible(true);

            // When
            @SuppressWarnings("unchecked")
            List<ResourceProvider> result = (List<ResourceProvider>) method.invoke(null, ListTestEnum.class);

            // Then - Should be modifiable
            assertThat(result).hasSize(5);
            result.remove(0);
            assertThat(result).hasSize(4);
        }
    }

    @Nested
    @DisplayName("Integration")
    class Integration {

        enum IntegrationTestEnum implements ResourceProvider {
            INTEGRATION_A("integration/a.txt"),
            INTEGRATION_B("integration/b.txt");

            private final String resource;

            IntegrationTestEnum(String resource) {
                this.resource = resource;
            }

            @Override
            public String getResource() {
                return resource;
            }
        }

        @Test
        @DisplayName("should work with actual ResourceProvider implementations")
        void shouldWorkWithActualResourceProviderImplementations() throws Exception {
            // Given
            Method method = ProvidersSupport.class.getDeclaredMethod("getResourcesFromEnumSingle", Class.class);
            method.setAccessible(true);

            // When
            @SuppressWarnings("unchecked")
            List<ResourceProvider> result = (List<ResourceProvider>) method.invoke(null, IntegrationTestEnum.class);

            // Then - Should be able to use as ResourceProvider
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getResource()).isEqualTo("integration/a.txt");
            assertThat(result.get(1).getResource()).isEqualTo("integration/b.txt");
        }

        @Test
        @DisplayName("should work consistently across multiple invocations")
        void shouldWorkConsistentlyAcrossMultipleInvocations() throws Exception {
            // Given
            Method method = ProvidersSupport.class.getDeclaredMethod("getResourcesFromEnumSingle", Class.class);
            method.setAccessible(true);

            // When
            @SuppressWarnings("unchecked")
            List<ResourceProvider> result1 = (List<ResourceProvider>) method.invoke(null, IntegrationTestEnum.class);
            @SuppressWarnings("unchecked")
            List<ResourceProvider> result2 = (List<ResourceProvider>) method.invoke(null, IntegrationTestEnum.class);
            @SuppressWarnings("unchecked")
            List<ResourceProvider> result3 = (List<ResourceProvider>) method.invoke(null, IntegrationTestEnum.class);

            // Then - All results should be equivalent but separate instances
            assertThat(result1).isEqualTo(result2).isEqualTo(result3);
            assertThat(result1).isNotSameAs(result2).isNotSameAs(result3);
            assertThat(result2).isNotSameAs(result3);
        }
    }
}
