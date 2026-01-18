/**
 * Copyright 2022 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.collections;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for SpecsArray array manipulation utility.
 * Tests array length detection and last element retrieval functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsArray Tests")
public class SpecsArrayTest {

    @Nested
    @DisplayName("Array Length Detection")
    class ArrayLengthDetection {

        @Test
        @DisplayName("Should get length of object arrays")
        void testGetLengthObjectArrays() {
            String[] stringArray = { "A", "B", "C" };
            Integer[] integerArray = { 1, 2, 3, 4, 5 };
            Object[] objectArray = new Object[10];
            String[] emptyStringArray = {};

            assertThat(SpecsArray.getLength(stringArray)).isEqualTo(3);
            assertThat(SpecsArray.getLength(integerArray)).isEqualTo(5);
            assertThat(SpecsArray.getLength(objectArray)).isEqualTo(10);
            assertThat(SpecsArray.getLength(emptyStringArray)).isEqualTo(0);
        }

        @Test
        @DisplayName("Should get length of primitive int arrays")
        void testGetLengthIntArrays() {
            int[] intArray = { 1, 2, 3, 4 };
            int[] emptyIntArray = {};
            int[] singleElementArray = { 42 };

            assertThat(SpecsArray.getLength(intArray)).isEqualTo(4);
            assertThat(SpecsArray.getLength(emptyIntArray)).isEqualTo(0);
            assertThat(SpecsArray.getLength(singleElementArray)).isEqualTo(1);
        }

        @Test
        @DisplayName("Should get length of primitive long arrays")
        void testGetLengthLongArrays() {
            long[] longArray = { 1L, 2L, 3L };
            long[] emptyLongArray = {};

            assertThat(SpecsArray.getLength(longArray)).isEqualTo(3);
            assertThat(SpecsArray.getLength(emptyLongArray)).isEqualTo(0);
        }

        @Test
        @DisplayName("Should get length of primitive double arrays")
        void testGetLengthDoubleArrays() {
            double[] doubleArray = { 1.1, 2.2, 3.3, 4.4, 5.5 };
            double[] emptyDoubleArray = {};

            assertThat(SpecsArray.getLength(doubleArray)).isEqualTo(5);
            assertThat(SpecsArray.getLength(emptyDoubleArray)).isEqualTo(0);
        }

        @Test
        @DisplayName("Should get length of primitive float arrays")
        void testGetLengthFloatArrays() {
            float[] floatArray = { 1.1f, 2.2f };
            float[] emptyFloatArray = {};

            assertThat(SpecsArray.getLength(floatArray)).isEqualTo(2);
            assertThat(SpecsArray.getLength(emptyFloatArray)).isEqualTo(0);
        }

        @Test
        @DisplayName("Should get length of primitive boolean arrays")
        void testGetLengthBooleanArrays() {
            boolean[] booleanArray = { true, false, true, false, true, false };
            boolean[] emptyBooleanArray = {};

            assertThat(SpecsArray.getLength(booleanArray)).isEqualTo(6);
            assertThat(SpecsArray.getLength(emptyBooleanArray)).isEqualTo(0);
        }

        @Test
        @DisplayName("Should get length of primitive char arrays")
        void testGetLengthCharArrays() {
            char[] charArray = { 'a', 'b', 'c', 'd', 'e', 'f', 'g' };
            char[] emptyCharArray = {};

            assertThat(SpecsArray.getLength(charArray)).isEqualTo(7);
            assertThat(SpecsArray.getLength(emptyCharArray)).isEqualTo(0);
        }

        @Test
        @DisplayName("Should get length of primitive byte arrays")
        void testGetLengthByteArrays() {
            byte[] byteArray = { 1, 2, 3, 4, 5, 6, 7, 8 };
            byte[] emptyByteArray = {};

            assertThat(SpecsArray.getLength(byteArray)).isEqualTo(8);
            assertThat(SpecsArray.getLength(emptyByteArray)).isEqualTo(0);
        }

        @Test
        @DisplayName("Should get length of primitive short arrays")
        void testGetLengthShortArrays() {
            short[] shortArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
            short[] emptyShortArray = {};

            assertThat(SpecsArray.getLength(shortArray)).isEqualTo(9);
            assertThat(SpecsArray.getLength(emptyShortArray)).isEqualTo(0);
        }

        @Test
        @DisplayName("Should return -1 for non-array objects")
        void testGetLengthNonArrays() {
            String string = "not an array";
            Integer integer = 42;
            Object object = new Object();

            assertThat(SpecsArray.getLength(string)).isEqualTo(-1);
            assertThat(SpecsArray.getLength(integer)).isEqualTo(-1);
            assertThat(SpecsArray.getLength(object)).isEqualTo(-1);
        }

        @Test
        @DisplayName("Should handle null input")
        void testGetLengthNull() {
            assertThatThrownBy(() -> SpecsArray.getLength(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle multidimensional arrays")
        void testGetLengthMultidimensionalArrays() {
            int[][] intMatrix = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
            String[][] stringMatrix = { { "A", "B" }, { "C", "D" } };
            Object[][][] cube = new Object[3][4][5];

            assertThat(SpecsArray.getLength(intMatrix)).isEqualTo(3);
            assertThat(SpecsArray.getLength(stringMatrix)).isEqualTo(2);
            assertThat(SpecsArray.getLength(cube)).isEqualTo(3);
        }

        // Legacy tests (maintain compatibility)
        @Test
        @DisplayName("Should return -1 for non-array objects")
        public void notArray() {
            assertThat(SpecsArray.getLength("hello")).isEqualTo(-1);
        }

        @Test
        @DisplayName("Should return correct length for int arrays")
        public void intArray() {
            assertThat(SpecsArray.getLength(new int[10])).isEqualTo(10);
        }

        @Test
        @DisplayName("Should return correct length for object arrays")
        public void objectArray() {
            assertThat(SpecsArray.getLength(new String[9])).isEqualTo(9);
        }
    }

    @Nested
    @DisplayName("Last Element Retrieval")
    class LastElementRetrieval {

        @Test
        @DisplayName("Should get last element from non-empty arrays")
        void testLastNonEmptyArrays() {
            String[] stringArray = { "A", "B", "C", "D" };
            Integer[] integerArray = { 10, 20, 30 };
            Boolean[] booleanArray = { true, false, true };

            assertThat(SpecsArray.last(stringArray)).isEqualTo("D");
            assertThat(SpecsArray.last(integerArray)).isEqualTo(30);
            assertThat(SpecsArray.last(booleanArray)).isTrue();
        }

        @Test
        @DisplayName("Should return null for empty arrays")
        void testLastEmptyArrays() {
            String[] emptyStringArray = {};
            Integer[] emptyIntegerArray = {};
            Object[] emptyObjectArray = {};

            assertThat(SpecsArray.last(emptyStringArray)).isNull();
            assertThat(SpecsArray.last(emptyIntegerArray)).isNull();
            assertThat(SpecsArray.last(emptyObjectArray)).isNull();
        }

        @Test
        @DisplayName("Should handle single element arrays")
        void testLastSingleElementArrays() {
            String[] singleStringArray = { "ONLY" };
            Integer[] singleIntegerArray = { 999 };
            Object[] singleObjectArray = { new Object() };

            assertThat(SpecsArray.last(singleStringArray)).isEqualTo("ONLY");
            assertThat(SpecsArray.last(singleIntegerArray)).isEqualTo(999);
            assertThat(SpecsArray.last(singleObjectArray)).isNotNull();
        }

        @Test
        @DisplayName("Should handle arrays with null elements")
        void testLastArraysWithNulls() {
            String[] arrayWithNulls = { "A", null, "C", null };
            Integer[] arrayEndingWithNull = { 1, 2, 3, null };
            Object[] allNulls = { null, null, null };

            assertThat(SpecsArray.last(arrayWithNulls)).isNull();
            assertThat(SpecsArray.last(arrayEndingWithNull)).isNull();
            assertThat(SpecsArray.last(allNulls)).isNull();
        }

        @Test
        @DisplayName("Should handle arrays of different object types")
        void testLastDifferentObjectTypes() {
            class Person {
                String name;

                Person(String name) {
                    this.name = name;
                }

                @Override
                public boolean equals(Object obj) {
                    return obj instanceof Person p && name.equals(p.name);
                }

                @Override
                public int hashCode() {
                    return name.hashCode();
                }
            }

            Person[] people = { new Person("Alice"), new Person("Bob"), new Person("Charlie") };
            Double[] doubles = { 1.1, 2.2, 3.3, 4.4 };
            Character[] chars = { 'x', 'y', 'z' };

            assertThat(SpecsArray.last(people)).isEqualTo(new Person("Charlie"));
            assertThat(SpecsArray.last(doubles)).isEqualTo(4.4);
            assertThat(SpecsArray.last(chars)).isEqualTo('z');
        }

        @Test
        @DisplayName("Should handle null array parameter")
        void testLastNullArray() {
            assertThatThrownBy(() -> SpecsArray.last(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle large arrays")
        void testLastLargeArrays() {
            // Create large array
            String[] largeArray = new String[1000];
            for (int i = 0; i < largeArray.length; i++) {
                largeArray[i] = "Element" + i;
            }

            assertThat(SpecsArray.last(largeArray)).isEqualTo("Element999");
        }
    }

    @Nested
    @DisplayName("Type Safety and Generics")
    class TypeSafetyAndGenerics {

        @Test
        @DisplayName("Should maintain type safety for last element")
        void testLastTypeSafety() {
            String[] stringArray = { "A", "B", "C" };
            Integer[] integerArray = { 1, 2, 3 };
            Double[] doubleArray = { 1.1, 2.2, 3.3 };

            String lastString = SpecsArray.last(stringArray);
            Integer lastInteger = SpecsArray.last(integerArray);
            Double lastDouble = SpecsArray.last(doubleArray);

            assertThat(lastString).isInstanceOf(String.class).isEqualTo("C");
            assertThat(lastInteger).isInstanceOf(Integer.class).isEqualTo(3);
            assertThat(lastDouble).isInstanceOf(Double.class).isEqualTo(3.3);
        }

        @Test
        @DisplayName("Should work with inheritance hierarchy")
        void testLastInheritanceHierarchy() {
            class Animal {
                String name;

                Animal(String name) {
                    this.name = name;
                }
            }
            class Dog extends Animal {
                Dog(String name) {
                    super(name);
                }
            }
            class Cat extends Animal {
                Cat(String name) {
                    super(name);
                }
            }

            Animal[] animals = { new Dog("Buddy"), new Cat("Whiskers"), new Dog("Max") };
            Dog[] dogs = { new Dog("Rex"), new Dog("Spot") };

            Animal lastAnimal = SpecsArray.last(animals);
            Dog lastDog = SpecsArray.last(dogs);

            assertThat(lastAnimal).isInstanceOf(Dog.class);
            assertThat(lastAnimal.name).isEqualTo("Max");
            assertThat(lastDog.name).isEqualTo("Spot");
        }

        @Test
        @DisplayName("Should work with wildcard arrays")
        void testLastWildcardArrays() {
            Number[] numbers = { 1, 2.5, 3L, 4.7f };
            String[] comparables = { "apple", "banana", "cherry" };

            Number lastNumber = SpecsArray.last(numbers);
            String lastComparable = SpecsArray.last(comparables);

            assertThat(lastNumber).isInstanceOf(Float.class).isEqualTo(4.7f);
            assertThat(lastComparable).isEqualTo("cherry");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should handle arrays with extreme values")
        void testArraysWithExtremeValues() {
            // Test with maximum values
            Integer[] maxInts = { Integer.MAX_VALUE, Integer.MIN_VALUE };
            Long[] maxLongs = { Long.MAX_VALUE, Long.MIN_VALUE };
            Double[] specialDoubles = { Double.MAX_VALUE, Double.MIN_VALUE, Double.POSITIVE_INFINITY,
                    Double.NEGATIVE_INFINITY, Double.NaN };

            assertThat(SpecsArray.getLength(maxInts)).isEqualTo(2);
            assertThat(SpecsArray.getLength(maxLongs)).isEqualTo(2);
            assertThat(SpecsArray.getLength(specialDoubles)).isEqualTo(5);

            assertThat(SpecsArray.last(maxInts)).isEqualTo(Integer.MIN_VALUE);
            assertThat(SpecsArray.last(maxLongs)).isEqualTo(Long.MIN_VALUE);
            assertThat(SpecsArray.last(specialDoubles)).isNaN();
        }

        @Test
        @DisplayName("Should handle arrays with Unicode characters")
        void testArraysWithUnicodeCharacters() {
            String[] unicodeStrings = { "Hello", "こんにちは", "🌟", "Unicode" };
            Character[] unicodeChars = { 'A', 'α', '中', 'Z' };

            assertThat(SpecsArray.getLength(unicodeStrings)).isEqualTo(4);
            assertThat(SpecsArray.getLength(unicodeChars)).isEqualTo(4);

            assertThat(SpecsArray.last(unicodeStrings)).isEqualTo("Unicode");
            assertThat(SpecsArray.last(unicodeChars)).isEqualTo('Z');
        }

        @Test
        @DisplayName("Should handle arrays of wrapper types")
        void testWrapperTypeArrays() {
            Byte[] byteWrappers = { 1, 2, 3 };
            Short[] shortWrappers = { 10, 20, 30 };
            Character[] charWrappers = { 'a', 'b', 'c' };
            Boolean[] booleanWrappers = { true, false, true };

            assertThat(SpecsArray.getLength(byteWrappers)).isEqualTo(3);
            assertThat(SpecsArray.getLength(shortWrappers)).isEqualTo(3);
            assertThat(SpecsArray.getLength(charWrappers)).isEqualTo(3);
            assertThat(SpecsArray.getLength(booleanWrappers)).isEqualTo(3);

            assertThat(SpecsArray.last(byteWrappers)).isEqualTo((byte) 3);
            assertThat(SpecsArray.last(shortWrappers)).isEqualTo((short) 30);
            assertThat(SpecsArray.last(charWrappers)).isEqualTo('c');
            assertThat(SpecsArray.last(booleanWrappers)).isTrue();
        }
    }

    @Nested
    @DisplayName("Integration and Workflow Tests")
    class IntegrationAndWorkflowTests {

        @Test
        @DisplayName("Should support array processing pipeline")
        void testArrayProcessingPipeline() {
            String[] data = { "apple", "banana", "cherry", "date", "elderberry" };

            // Get array length
            int length = SpecsArray.getLength(data);
            assertThat(length).isEqualTo(5);

            // Get last element
            String lastElement = SpecsArray.last(data);
            assertThat(lastElement).isEqualTo("elderberry");

            // Use in conditional logic
            if (length > 0) {
                String result = "Array has " + length + " elements, last is: " + lastElement;
                assertThat(result).isEqualTo("Array has 5 elements, last is: elderberry");
            }
        }

        @Test
        @DisplayName("Should support array validation workflow")
        void testArrayValidationWorkflow() {
            Integer[] validArray = { 1, 2, 3 };
            Object notAnArray = "string";
            Integer[] emptyArray = {};

            // Validation pipeline
            int validLength = SpecsArray.getLength(validArray);
            int invalidLength = SpecsArray.getLength(notAnArray);
            int emptyLength = SpecsArray.getLength(emptyArray);

            assertThat(validLength).isGreaterThan(0);
            assertThat(invalidLength).isEqualTo(-1);
            assertThat(emptyLength).isEqualTo(0);

            // Safe last element retrieval
            Integer lastValid = (Integer) SpecsArray.last((Object[]) validArray);
            Integer lastEmpty = (Integer) SpecsArray.last((Object[]) emptyArray);

            assertThat(lastValid).isEqualTo(3);
            assertThat(lastEmpty).isNull();
        }

        @Test
        @DisplayName("Should support generic array utility functions")
        void testGenericArrayUtilities() {
            // Helper function using SpecsArray utilities
            class ArrayHelper {
                static <T> String describe(T[] array) {
                    if (array == null)
                        return "null array";

                    int length = SpecsArray.getLength(array);
                    T last = SpecsArray.last(array);

                    return String.format("Array[length=%d, last=%s]", length, last);
                }
            }

            String[] strings = { "A", "B", "C" };
            Integer[] integers = { 1, 2, 3, 4 };
            Object[] empty = {};

            assertThat(ArrayHelper.describe(strings)).isEqualTo("Array[length=3, last=C]");
            assertThat(ArrayHelper.describe(integers)).isEqualTo("Array[length=4, last=4]");
            assertThat(ArrayHelper.describe(empty)).isEqualTo("Array[length=0, last=null]");
        }
    }
}
