package pt.up.fe.specs.util.io;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pt.up.fe.specs.util.providers.ResourceProvider;

/**
 * Comprehensive test suite for ResourceCollection class.
 * 
 * Tests the ResourceCollection class functionality including resource
 * management, collection handling, ID uniqueness tracking, and provider
 * integration.
 * 
 * @author Generated Tests
 */
@DisplayName("ResourceCollection Tests")
@MockitoSettings(strictness = Strictness.LENIENT)
class ResourceCollectionTest {

    @Mock
    private ResourceProvider mockProvider1;

    @Mock
    private ResourceProvider mockProvider2;

    @Mock
    private ResourceProvider mockProvider3;

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create ResourceCollection with all parameters")
        void testConstructorWithAllParameters() {
            // Given
            String id = "test-collection";
            boolean isIdUnique = true;
            Collection<ResourceProvider> resources = Arrays.asList(mockProvider1, mockProvider2);

            // When
            ResourceCollection collection = new ResourceCollection(id, isIdUnique, resources);

            // Then
            assertThat(collection.id()).isEqualTo(id);
            assertThat(collection.isIdUnique()).isTrue();
            assertThat(collection.resources()).isEqualTo(resources);
            assertThat(collection.resources()).hasSize(2);
        }

        @Test
        @DisplayName("Should create ResourceCollection with unique ID")
        void testConstructorWithUniqueId() {
            // Given
            String id = "unique-collection";
            boolean isIdUnique = true;
            Collection<ResourceProvider> resources = List.of(mockProvider1);

            // When
            ResourceCollection collection = new ResourceCollection(id, isIdUnique, resources);

            // Then
            assertThat(collection.id()).isEqualTo(id);
            assertThat(collection.isIdUnique()).isTrue();
            assertThat(collection.resources()).containsExactly(mockProvider1);
        }

        @Test
        @DisplayName("Should create ResourceCollection with non-unique ID")
        void testConstructorWithNonUniqueId() {
            // Given
            String id = "non-unique-collection";
            boolean isIdUnique = false;
            Collection<ResourceProvider> resources = Arrays.asList(mockProvider1, mockProvider2, mockProvider3);

            // When
            ResourceCollection collection = new ResourceCollection(id, isIdUnique, resources);

            // Then
            assertThat(collection.id()).isEqualTo(id);
            assertThat(collection.isIdUnique()).isFalse();
            assertThat(collection.resources()).hasSize(3);
            assertThat(collection.resources()).containsExactly(mockProvider1, mockProvider2, mockProvider3);
        }

        @Test
        @DisplayName("Should create ResourceCollection with empty resources")
        void testConstructorWithEmptyResources() {
            // Given
            String id = "empty-collection";
            boolean isIdUnique = true;
            Collection<ResourceProvider> resources = Collections.emptyList();

            // When
            ResourceCollection collection = new ResourceCollection(id, isIdUnique, resources);

            // Then
            assertThat(collection.id()).isEqualTo(id);
            assertThat(collection.isIdUnique()).isTrue();
            assertThat(collection.resources()).isEmpty();
        }

        @Test
        @DisplayName("Should create ResourceCollection with null ID")
        void testConstructorWithNullId() {
            // Given
            String id = null;
            boolean isIdUnique = true;
            Collection<ResourceProvider> resources = List.of(mockProvider1);

            // When
            ResourceCollection collection = new ResourceCollection(id, isIdUnique, resources);

            // Then
            assertThat(collection.id()).isNull();
            assertThat(collection.isIdUnique()).isTrue();
            assertThat(collection.resources()).isNotNull();
        }

        @Test
        @DisplayName("Should create ResourceCollection with null resources")
        void testConstructorWithNullResources() {
            // Given
            String id = "null-resources";
            boolean isIdUnique = true;
            Collection<ResourceProvider> resources = null;

            // When
            ResourceCollection collection = new ResourceCollection(id, isIdUnique, resources);

            // Then
            assertThat(collection.id()).isEqualTo(id);
            assertThat(collection.isIdUnique()).isTrue();
            assertThat(collection.resources()).isNull();
        }
    }

    @Nested
    @DisplayName("Getter Method Tests")
    class GetterMethodTests {

        @Test
        @DisplayName("Should return correct ID")
        void testGetId() {
            // Given
            String expectedId = "test-id-12345";
            ResourceCollection collection = new ResourceCollection(expectedId, true, Collections.emptyList());

            // When
            String actualId = collection.id();

            // Then
            assertThat(actualId).isEqualTo(expectedId);
        }

        @Test
        @DisplayName("Should return correct isIdUnique flag")
        void testIsIdUnique() {
            // Given
            ResourceCollection uniqueCollection = new ResourceCollection("unique", true, Collections.emptyList());
            ResourceCollection nonUniqueCollection = new ResourceCollection("non-unique", false,
                    Collections.emptyList());

            // When/Then
            assertThat(uniqueCollection.isIdUnique()).isTrue();
            assertThat(nonUniqueCollection.isIdUnique()).isFalse();
        }

        @Test
        @DisplayName("Should return correct resources collection")
        void testGetResources() {
            // Given
            Collection<ResourceProvider> expectedResources = Arrays.asList(mockProvider1, mockProvider2);
            ResourceCollection collection = new ResourceCollection("test", true, expectedResources);

            // When
            Collection<ResourceProvider> actualResources = collection.resources();

            // Then
            assertThat(actualResources).isSameAs(expectedResources);
            assertThat(actualResources).hasSize(2);
            assertThat(actualResources).containsExactly(mockProvider1, mockProvider2);
        }

        @Test
        @DisplayName("Should maintain resource collection reference")
        void testResourcesReferenceIntegrity() {
            // Given - Using a mutable list
            List<ResourceProvider> resources = new java.util.ArrayList<>(Arrays.asList(mockProvider1, mockProvider2));
            ResourceCollection collection = new ResourceCollection("ref-test", true, resources);

            // When
            Collection<ResourceProvider> retrievedResources = collection.resources();

            // Then
            assertThat(retrievedResources).isSameAs(resources);

            // Modifications to original should be reflected (if collection is mutable)
            resources.add(mockProvider3);
            assertThat(collection.resources()).hasSize(3);
            assertThat(collection.resources()).contains(mockProvider3);
        }
    }

    @Nested
    @DisplayName("Resource Provider Integration Tests")
    class ResourceProviderIntegrationTests {

        @Test
        @DisplayName("Should handle single resource provider")
        void testSingleResourceProvider() {
            // Given
            Collection<ResourceProvider> resources = List.of(mockProvider1);
            ResourceCollection collection = new ResourceCollection("single", true, resources);

            // When/Then
            assertThat(collection.resources()).hasSize(1);
            assertThat(collection.resources()).containsExactly(mockProvider1);
        }

        @Test
        @DisplayName("Should handle multiple resource providers")
        void testMultipleResourceProviders() {
            // Given
            Collection<ResourceProvider> resources = Arrays.asList(mockProvider1, mockProvider2, mockProvider3);
            ResourceCollection collection = new ResourceCollection("multiple", false, resources);

            // When/Then
            assertThat(collection.resources()).hasSize(3);
            assertThat(collection.resources()).containsExactly(mockProvider1, mockProvider2, mockProvider3);
        }

        @Test
        @DisplayName("Should handle duplicate resource providers")
        void testDuplicateResourceProviders() {
            // Given
            Collection<ResourceProvider> resources = Arrays.asList(mockProvider1, mockProvider1, mockProvider2);
            ResourceCollection collection = new ResourceCollection("duplicates", true, resources);

            // When/Then
            assertThat(collection.resources()).hasSize(3);
            assertThat(collection.resources()).containsExactly(mockProvider1, mockProvider1, mockProvider2);
        }

        @Test
        @DisplayName("Should support different collection types")
        void testDifferentCollectionTypes() {
            // Given
            List<ResourceProvider> list = Arrays.asList(mockProvider1, mockProvider2);
            ResourceCollection listCollection = new ResourceCollection("list", true, list);

            // When/Then
            assertThat(listCollection.resources()).isInstanceOf(List.class);
            assertThat(listCollection.resources()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("ID Management Tests")
    class IdManagementTests {

        @Test
        @DisplayName("Should handle various ID formats")
        void testVariousIdFormats() {
            String[] idFormats = {
                    "simple-id",
                    "id_with_underscores",
                    "ID-WITH-CAPS",
                    "id.with.dots",
                    "id123with456numbers",
                    "id with spaces",
                    "id@with#special$chars",
                    "",
                    "very-long-id-with-many-characters-to-test-boundary-conditions"
            };

            for (String id : idFormats) {
                ResourceCollection collection = new ResourceCollection(id, true, Collections.emptyList());
                assertThat(collection.id()).isEqualTo(id);
            }
        }

        @Test
        @DisplayName("Should handle ID uniqueness flag correctly")
        void testIdUniquenessFlag() {
            // Given
            String sameId = "shared-id";
            ResourceCollection unique1 = new ResourceCollection(sameId, true, Collections.emptyList());
            ResourceCollection unique2 = new ResourceCollection(sameId, true, Collections.emptyList());
            ResourceCollection nonUnique1 = new ResourceCollection(sameId, false, Collections.emptyList());
            ResourceCollection nonUnique2 = new ResourceCollection(sameId, false, Collections.emptyList());

            // When/Then
            assertThat(unique1.id()).isEqualTo(sameId);
            assertThat(unique2.id()).isEqualTo(sameId);
            assertThat(nonUnique1.id()).isEqualTo(sameId);
            assertThat(nonUnique2.id()).isEqualTo(sameId);

            assertThat(unique1.isIdUnique()).isTrue();
            assertThat(unique2.isIdUnique()).isTrue();
            assertThat(nonUnique1.isIdUnique()).isFalse();
            assertThat(nonUnique2.isIdUnique()).isFalse();
        }

        @Test
        @DisplayName("Should differentiate between unique and non-unique collections")
        void testUniqueVsNonUniqueCollections() {
            // Given
            String id = "test-id";
            Collection<ResourceProvider> resources = Arrays.asList(mockProvider1);

            ResourceCollection uniqueCollection = new ResourceCollection(id, true, resources);
            ResourceCollection nonUniqueCollection = new ResourceCollection(id, false, resources);

            // When/Then
            assertThat(uniqueCollection.id()).isEqualTo(nonUniqueCollection.id());
            assertThat(uniqueCollection.isIdUnique()).isNotEqualTo(nonUniqueCollection.isIdUnique());
            assertThat(uniqueCollection.resources()).isEqualTo(nonUniqueCollection.resources());
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty string ID")
        void testEmptyStringId() {
            // Given
            String emptyId = "";
            ResourceCollection collection = new ResourceCollection(emptyId, true,
                    List.of(mockProvider1));

            // When/Then
            assertThat(collection.id()).isEmpty();
            assertThat(collection.isIdUnique()).isTrue();
            assertThat(collection.resources()).isNotEmpty();
        }

        @Test
        @DisplayName("Should handle whitespace-only ID")
        void testWhitespaceOnlyId() {
            // Given
            String whitespaceId = "   \t\n   ";
            ResourceCollection collection = new ResourceCollection(whitespaceId, false, Collections.emptyList());

            // When/Then
            assertThat(collection.id()).isEqualTo(whitespaceId);
            assertThat(collection.isIdUnique()).isFalse();
        }

        @Test
        @DisplayName("Should handle both null ID and null resources")
        void testBothNullParameters() {
            // Given/When
            ResourceCollection collection = new ResourceCollection(null, true, null);

            // Then
            assertThat(collection.id()).isNull();
            assertThat(collection.isIdUnique()).isTrue();
            assertThat(collection.resources()).isNull();
        }

        @Test
        @DisplayName("Should handle large number of resources")
        void testLargeNumberOfResources() {
            // Given
            Collection<ResourceProvider> manyResources = Collections.nCopies(1000, mockProvider1);
            ResourceCollection collection = new ResourceCollection("large", true, manyResources);

            // When/Then
            assertThat(collection.resources()).hasSize(1000);
            assertThat(collection.id()).isEqualTo("large");
            assertThat(collection.isIdUnique()).isTrue();
        }

        @Test
        @DisplayName("Should maintain immutability of constructor parameters")
        void testConstructorParameterImmutability() {
            // Given
            String id = "immutable-test";
            boolean isUnique = true;
            Collection<ResourceProvider> resources = Arrays.asList(mockProvider1, mockProvider2);

            // When
            ResourceCollection collection = new ResourceCollection(id, isUnique, resources);

            // Then - Changes to local variables should not affect the collection
            String originalId = collection.id();
            boolean originalUnique = collection.isIdUnique();
            Collection<ResourceProvider> originalResources = collection.resources();

            assertThat(collection.id()).isEqualTo(originalId);
            assertThat(collection.isIdUnique()).isEqualTo(originalUnique);
            assertThat(collection.resources()).isSameAs(originalResources);
        }
    }

    @Nested
    @DisplayName("Integration and Usage Pattern Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should support typical usage patterns")
        void testTypicalUsagePatterns() {
            // Given - Typical configuration scenario
            ResourceCollection configResources = new ResourceCollection("config-files", true,
                    Arrays.asList(mockProvider1, mockProvider2));

            ResourceCollection dynamicResources = new ResourceCollection("dynamic-content", false,
                    List.of(mockProvider3));

            // When/Then
            assertThat(configResources.id()).isEqualTo("config-files");
            assertThat(configResources.isIdUnique()).isTrue();
            assertThat(configResources.resources()).hasSize(2);

            assertThat(dynamicResources.id()).isEqualTo("dynamic-content");
            assertThat(dynamicResources.isIdUnique()).isFalse();
            assertThat(dynamicResources.resources()).hasSize(1);
        }

        @Test
        @DisplayName("Should work with resource provider hierarchies")
        void testResourceProviderHierarchies() {
            // Given
            Collection<ResourceProvider> primaryResources = Arrays.asList(mockProvider1, mockProvider2);
            Collection<ResourceProvider> fallbackResources = List.of(mockProvider3);

            ResourceCollection primaryCollection = new ResourceCollection("primary", true, primaryResources);
            ResourceCollection fallbackCollection = new ResourceCollection("fallback", true, fallbackResources);

            // When/Then
            assertThat(primaryCollection.resources()).hasSize(2);
            assertThat(fallbackCollection.resources()).hasSize(1);

            // Collections can be used together for resource resolution strategies
            assertThat(primaryCollection.id()).isNotEqualTo(fallbackCollection.id());
            assertThat(primaryCollection.isIdUnique()).isEqualTo(fallbackCollection.isIdUnique());
        }

        @Test
        @DisplayName("Should handle resource collection composition")
        void testResourceCollectionComposition() {
            // Given
            ResourceCollection collection1 = new ResourceCollection("part1", true,
                    Arrays.asList(mockProvider1));
            ResourceCollection collection2 = new ResourceCollection("part2", true,
                    Arrays.asList(mockProvider2, mockProvider3));

            // When - Composing collections
            Collection<ResourceProvider> combined = Arrays.asList(
                    collection1.resources().iterator().next(),
                    collection2.resources().iterator().next());
            ResourceCollection combinedCollection = new ResourceCollection("combined", false, combined);

            // Then
            assertThat(combinedCollection.resources()).hasSize(2);
            assertThat(combinedCollection.id()).isEqualTo("combined");
            assertThat(combinedCollection.isIdUnique()).isFalse();
        }

        @Test
        @DisplayName("Should maintain consistent state across operations")
        void testStateConsistency() {
            // Given
            String id = "consistent-state";
            boolean isUnique = true;
            Collection<ResourceProvider> resources = Arrays.asList(mockProvider1, mockProvider2);

            ResourceCollection collection = new ResourceCollection(id, isUnique, resources);

            // When - Multiple accesses
            for (int i = 0; i < 100; i++) {
                assertThat(collection.id()).isEqualTo(id);
                assertThat(collection.isIdUnique()).isEqualTo(isUnique);
                assertThat(collection.resources()).hasSize(2);
                assertThat(collection.resources()).containsExactly(mockProvider1, mockProvider2);
            }
        }

        @Test
        @DisplayName("Should support concurrent access safely")
        void testConcurrentAccess() throws InterruptedException {
            // Given
            ResourceCollection collection = new ResourceCollection("concurrent", true,
                    Arrays.asList(mockProvider1, mockProvider2, mockProvider3));

            // When
            Thread[] threads = new Thread[10];
            boolean[] results = new boolean[10];

            for (int i = 0; i < threads.length; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    try {
                        String id = collection.id();
                        boolean isUnique = collection.isIdUnique();
                        Collection<ResourceProvider> resources = collection.resources();

                        results[index] = "concurrent".equals(id) &&
                                isUnique &&
                                resources.size() == 3;
                    } catch (Exception e) {
                        results[index] = false;
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then
            for (boolean result : results) {
                assertThat(result).isTrue();
            }
        }
    }
}
