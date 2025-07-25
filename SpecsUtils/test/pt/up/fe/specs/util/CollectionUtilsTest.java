/**
 * Copyright 2014 SPeCS Research Group.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test suite for SpecsCollections utility class.
 * 
 * This test class covers collection functionality including:
 * - Type-based index finding
 * - Collection utilities
 * - Edge cases for collection operations
 */
@DisplayName("SpecsCollections Tests")
public class CollectionUtilsTest {

    @Nested
    @DisplayName("Index Finding Operations")
    class IndexFindingOperations {

        @Test
        @DisplayName("getFirstIndex should find correct index for different types")
        void testGetFirstIndex_DifferentTypes_ReturnsCorrectIndex() {
            List<Number> numbers = Arrays.asList(Integer.valueOf(2), Double.valueOf(3.5));

            assertThat(SpecsCollections.getFirstIndex(numbers, Integer.class)).isEqualTo(0);
            assertThat(SpecsCollections.getFirstIndex(numbers, Double.class)).isEqualTo(1);
            assertThat(SpecsCollections.getFirstIndex(numbers, Number.class)).isEqualTo(0);
            assertThat(SpecsCollections.getFirstIndex(numbers, Float.class)).isEqualTo(-1);
        }

        @Test
        @DisplayName("getFirstIndex should handle empty list")
        void testGetFirstIndex_EmptyList_ReturnsMinusOne() {
            List<Number> emptyList = Collections.emptyList();
            assertThat(SpecsCollections.getFirstIndex(emptyList, Integer.class)).isEqualTo(-1);
        }

        @Test
        @DisplayName("getFirstIndex should handle null list gracefully")
        void testGetFirstIndex_NullList_ShouldHandleGracefully() {
            assertThatCode(() -> {
                SpecsCollections.getFirstIndex(null, Integer.class);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("getFirstIndex should handle null class gracefully")
        void testGetFirstIndex_NullClass_ShouldHandleGracefully() {
            List<Number> numbers = Arrays.asList(Integer.valueOf(2), null, Double.valueOf(3.5));
            assertThatCode(() -> {
                SpecsCollections.getFirstIndex(numbers, null);
            }).doesNotThrowAnyException();
            assertThat(SpecsCollections.getFirstIndex(numbers, null)).isEqualTo(1);
        }

        @Test
        @DisplayName("getFirstIndex should find first occurrence in complex hierarchy")
        void testGetFirstIndex_ComplexHierarchy_ReturnsFirstOccurrence() {
            List<Number> numbers = Arrays.asList(
                Integer.valueOf(1), 
                Float.valueOf(2.5f), 
                Integer.valueOf(3), 
                Double.valueOf(4.5)
            );

            // Should find first Integer at index 0
            assertThat(SpecsCollections.getFirstIndex(numbers, Integer.class)).isEqualTo(0);
            // Should find first Float at index 1
            assertThat(SpecsCollections.getFirstIndex(numbers, Float.class)).isEqualTo(1);
            // Should find first Double at index 3
            assertThat(SpecsCollections.getFirstIndex(numbers, Double.class)).isEqualTo(3);
            // Should find first Number (base class) at index 0
            assertThat(SpecsCollections.getFirstIndex(numbers, Number.class)).isEqualTo(0);
        }
    }

}
