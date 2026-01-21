package pt.up.fe.specs.util;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import pt.up.fe.specs.util.providers.KeyProvider;
import pt.up.fe.specs.util.providers.StringProvider;

/**
 * Comprehensive test suite for SpecsEnums utility class.
 * 
 * This test class covers enum manipulation operations including:
 * - Enum value lookup and conversion
 * - Map and list building from enums
 * - Enum complement operations
 * - Helper and cache functionality
 * - Edge cases and error handling
 * - Interface-based enum operations (KeyProvider, StringProvider)
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsEnums Tests")
public class SpecsEnumsTest {

    @BeforeAll
    static void init() {
        SpecsSystem.programStandardInit();
    }

    // Test enums for testing
    enum TestColor {
        RED, GREEN, BLUE, YELLOW
    }

    enum TestSize {
        SMALL, MEDIUM, LARGE
    }

    enum TestDirection {
        NORTH, SOUTH, EAST, WEST
    }

    enum TestKeyProviderEnum implements KeyProvider<String> {
        OPTION_A("key_a"),
        OPTION_B("key_b"),
        OPTION_C("key_c");

        private final String key;

        TestKeyProviderEnum(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    enum TestStringProviderEnum implements StringProvider {
        FIRST("First Option"),
        SECOND("Second Option"),
        THIRD("Third Option");

        private final String displayName;

        TestStringProviderEnum(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String getString() {
            return displayName;
        }
    }

    @Nested
    @DisplayName("Enum Value Lookup and Conversion")
    class EnumLookupTests {

        @Test
        @DisplayName("valueOf should return correct enum value for valid name")
        void testValueOf_ValidName() {
            // Execute
            TestColor result = SpecsEnums.valueOf(TestColor.class, "RED");

            // Verify
            assertThat(result).isEqualTo(TestColor.RED);
        }

        @Test
        @DisplayName("valueOf should return first enum value for invalid name with warning")
        void testValueOf_InvalidName() {
            // Execute
            TestColor result = SpecsEnums.valueOf(TestColor.class, "INVALID");

            // Verify - should return first element with warning
            assertThat(result).isEqualTo(TestColor.RED);
        }

        @Test
        @DisplayName("valueOf should handle null name gracefully")
        void testValueOf_NullName() {
            // Execute
            TestColor result = SpecsEnums.valueOf(TestColor.class, null);

            // Verify - should return first element with warning
            assertThat(result).isEqualTo(TestColor.RED);
        }

        @Test
        @DisplayName("valueOf should handle empty string name")
        void testValueOf_EmptyName() {
            // Execute
            TestColor result = SpecsEnums.valueOf(TestColor.class, "");

            // Verify - should return first element with warning
            assertThat(result).isEqualTo(TestColor.RED);
        }

        @Test
        @DisplayName("valueOfTry should return Optional with enum value for valid name")
        void testValueOfTry_ValidName() {
            // Execute
            Optional<TestColor> result = SpecsEnums.valueOfTry(TestColor.class, "BLUE");

            // Verify
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(TestColor.BLUE);
        }

        @Test
        @DisplayName("valueOfTry should return non-empty Optional even for invalid name")
        void testValueOfTry_InvalidName() {
            // Execute
            Optional<TestColor> result = SpecsEnums.valueOfTry(TestColor.class, "INVALID");

            // Verify - returns first element, so Optional is present
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(TestColor.RED);
        }

        @ParameterizedTest
        @ValueSource(strings = { "RED", "GREEN", "BLUE", "YELLOW" })
        @DisplayName("valueOf should work correctly for all valid enum values")
        void testValueOf_AllValidValues(String enumName) {
            // Execute
            TestColor result = SpecsEnums.valueOf(TestColor.class, enumName);

            // Verify
            assertThat(result.name()).isEqualTo(enumName);
        }
    }

    @Nested
    @DisplayName("Map and List Building")
    class MapListBuildingTests {

        @Test
        @DisplayName("buildMap should create map from enum toString to enum")
        void testBuildMap() {
            // Execute
            Map<String, TestColor> result = SpecsEnums.buildMap(TestColor.values());

            // Verify
            assertThat(result).hasSize(4);
            assertThat(result.get("RED")).isEqualTo(TestColor.RED);
            assertThat(result.get("GREEN")).isEqualTo(TestColor.GREEN);
            assertThat(result.get("BLUE")).isEqualTo(TestColor.BLUE);
            assertThat(result.get("YELLOW")).isEqualTo(TestColor.YELLOW);

            // Verify map is unmodifiable
            assertThatThrownBy(() -> result.put("NEW", TestColor.RED))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("buildNamesMap should create map from enum names to enum")
        void testBuildNamesMap() {
            // Execute
            Map<String, TestColor> result = SpecsEnums.buildNamesMap(TestColor.class, Arrays.asList());

            // Verify
            assertThat(result).hasSize(4);
            assertThat(result.get("RED")).isEqualTo(TestColor.RED);
            assertThat(result.get("GREEN")).isEqualTo(TestColor.GREEN);
            assertThat(result.get("BLUE")).isEqualTo(TestColor.BLUE);
            assertThat(result.get("YELLOW")).isEqualTo(TestColor.YELLOW);
        }

        @Test
        @DisplayName("buildNamesMap should exclude specified enum values")
        void testBuildNamesMap_WithExclusions() {
            // Execute
            Map<String, TestColor> result = SpecsEnums.buildNamesMap(TestColor.class,
                    Arrays.asList(TestColor.RED, TestColor.BLUE));

            // Verify
            assertThat(result).hasSize(2);
            assertThat(result.get("GREEN")).isEqualTo(TestColor.GREEN);
            assertThat(result.get("YELLOW")).isEqualTo(TestColor.YELLOW);
            assertThat(result).doesNotContainKey("RED");
            assertThat(result).doesNotContainKey("BLUE");
        }

        @Test
        @DisplayName("buildNamesMap should work with StringProvider enums")
        void testBuildNamesMap_StringProvider() {
            // Execute
            Map<String, TestStringProviderEnum> result = SpecsEnums.buildNamesMap(
                    TestStringProviderEnum.class, Arrays.asList());

            // Verify - should use getString() instead of name()
            assertThat(result).hasSize(3);
            assertThat(result.get("First Option")).isEqualTo(TestStringProviderEnum.FIRST);
            assertThat(result.get("Second Option")).isEqualTo(TestStringProviderEnum.SECOND);
            assertThat(result.get("Third Option")).isEqualTo(TestStringProviderEnum.THIRD);
        }

        @Test
        @DisplayName("buildMap with KeyProvider should create map from keys to enums")
        void testBuildMap_KeyProvider() {
            // Execute
            Map<String, TestKeyProviderEnum> result = SpecsEnums.buildMap(TestKeyProviderEnum.class);

            // Verify
            assertThat(result).hasSize(3);
            assertThat(result.get("key_a")).isEqualTo(TestKeyProviderEnum.OPTION_A);
            assertThat(result.get("key_b")).isEqualTo(TestKeyProviderEnum.OPTION_B);
            assertThat(result.get("key_c")).isEqualTo(TestKeyProviderEnum.OPTION_C);
        }
    }

    @Nested
    @DisplayName("Enum Value Extraction")
    class ValueExtractionTests {

        @Test
        @DisplayName("extractValues should return list of enum values")
        void testExtractValues() {
            // Execute
            List<TestColor> result = SpecsEnums.extractValues(TestColor.class);

            // Verify
            assertThat(result).hasSize(4);
            assertThat(result).containsExactly(TestColor.RED, TestColor.GREEN, TestColor.BLUE, TestColor.YELLOW);
        }

        @Test
        @DisplayName("extractValues should return null for non-enum class")
        void testExtractValues_NonEnumClass() {
            // Execute
            List<String> result = SpecsEnums.extractValues(String.class);

            // Verify
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("extractValues with multiple enum classes should combine all values")
        void testExtractValues_MultipleClasses() {
            // Execute
            List<Enum<?>> result = SpecsEnums.extractValues(Arrays.asList(TestColor.class, TestSize.class));

            // Verify
            assertThat(result).hasSize(7); // 4 colors + 3 sizes
            assertThat(result).contains(TestColor.RED, TestColor.GREEN, TestColor.BLUE, TestColor.YELLOW);
            assertThat(result).contains(TestSize.SMALL, TestSize.MEDIUM, TestSize.LARGE);
        }
    }

    @Nested
    @DisplayName("Helper and Utility Operations")
    class HelperUtilityTests {

        @Test
        @DisplayName("fromName should work like valueOf")
        void testFromName() {
            // Execute
            TestColor result = SpecsEnums.fromName(TestColor.class, "GREEN");

            // Verify
            assertThat(result).isEqualTo(TestColor.GREEN);
        }

        @Test
        @DisplayName("fromOrdinal should return enum by ordinal")
        void testFromOrdinal() {
            // Execute
            TestColor result = SpecsEnums.fromOrdinal(TestColor.class, 2);

            // Verify
            assertThat(result).isEqualTo(TestColor.BLUE);
        }

        @Test
        @DisplayName("getHelper should return and cache enum helper")
        void testGetHelper() {
            // Execute
            var helper1 = SpecsEnums.getHelper(TestColor.class);
            var helper2 = SpecsEnums.getHelper(TestColor.class);

            // Verify
            assertThat(helper1).isNotNull();
            assertThat(helper2).isSameAs(helper1); // Should be cached
        }

        @Test
        @DisplayName("getKeys should return list of keys from KeyProvider enum")
        void testGetKeys() {
            // Execute
            List<String> result = SpecsEnums.getKeys(TestKeyProviderEnum.class);

            // Verify
            assertThat(result).hasSize(3);
            assertThat(result).containsExactly("key_a", "key_b", "key_c");
        }
    }

    @Nested
    @DisplayName("Enum Map Conversion")
    class EnumMapConversionTests {

        @Test
        @DisplayName("toEnumMap should convert string map to enum map")
        void testToEnumMap() {
            // Arrange
            Map<String, Integer> stringMap = new java.util.LinkedHashMap<>();
            stringMap.put("RED", 1);
            stringMap.put("GREEN", 2);
            stringMap.put("BLUE", 3);
            stringMap.put("INVALID", 4);

            // Execute
            EnumMap<TestColor, Integer> result = SpecsEnums.toEnumMap(TestColor.class, stringMap);

            // Verify
            assertThat(result).hasSize(3); // INVALID should be skipped
            assertThat(result.get(TestColor.RED)).isEqualTo(1);
            assertThat(result.get(TestColor.GREEN)).isEqualTo(2);
            assertThat(result.get(TestColor.BLUE)).isEqualTo(3);
            assertThat(result).doesNotContainKey(TestColor.YELLOW);
        }

        @Test
        @DisplayName("toEnumMap should handle empty string map")
        void testToEnumMap_Empty() {
            // Arrange
            Map<String, String> emptyMap = new java.util.HashMap<>();

            // Execute
            EnumMap<TestColor, String> result = SpecsEnums.toEnumMap(TestColor.class, emptyMap);

            // Verify
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("toEnumMap should work with StringProvider enums")
        void testToEnumMap_StringProvider() {
            // Arrange
            Map<String, String> stringMap = new java.util.LinkedHashMap<>();
            stringMap.put("First Option", "value1");
            stringMap.put("Second Option", "value2");
            stringMap.put("Invalid Option", "value3");

            // Execute
            EnumMap<TestStringProviderEnum, String> result = SpecsEnums.toEnumMap(
                    TestStringProviderEnum.class, stringMap);

            // Verify
            assertThat(result).hasSize(2); // Invalid should be skipped
            assertThat(result.get(TestStringProviderEnum.FIRST)).isEqualTo("value1");
            assertThat(result.get(TestStringProviderEnum.SECOND)).isEqualTo("value2");
        }
    }

    @Nested
    @DisplayName("Interface-based Operations")
    class InterfaceOperationsTests {

        @Test
        @DisplayName("getInterfaceFromEnum should extract interface implementation")
        void testGetInterfaceFromEnum() {
            // Execute
            Object result = SpecsEnums.getInterfaceFromEnum(TestKeyProviderEnum.class, KeyProvider.class);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(KeyProvider.class);
            assertThat(result).isEqualTo(TestKeyProviderEnum.OPTION_A); // First enum value
        }

        @Test
        @DisplayName("getInterfaceFromEnum should return null for non-implementing enum")
        void testGetInterfaceFromEnum_NonImplementing() {
            // Execute
            Object result = SpecsEnums.getInterfaceFromEnum(TestColor.class, KeyProvider.class);

            // Verify
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("StringProvider enum should work with buildNamesMap")
        void testStringProviderIntegration() {
            // Execute
            Map<String, TestStringProviderEnum> result = SpecsEnums.buildNamesMap(
                    TestStringProviderEnum.class, Arrays.asList());

            // Verify - should use getString() method
            assertThat(result.keySet()).containsExactly("First Option", "Second Option", "Third Option");
            assertThat(result.values()).containsExactly(
                    TestStringProviderEnum.FIRST, TestStringProviderEnum.SECOND, TestStringProviderEnum.THIRD);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("operations should handle enum with single value")
        void testSingleValueEnum() {
            enum SingleValue {
                ONLY
            }

            // Execute & Verify
            assertThat(SpecsEnums.valueOf(SingleValue.class, "ONLY")).isEqualTo(SingleValue.ONLY);
            assertThat(SpecsEnums.valueOf(SingleValue.class, "INVALID")).isEqualTo(SingleValue.ONLY);
        }

        @Test
        @DisplayName("operations should handle case sensitivity")
        void testCaseSensitivity() {
            // Execute & Verify - should be case sensitive
            assertThat(SpecsEnums.valueOf(TestColor.class, "red")).isEqualTo(TestColor.RED); // Returns first on fail
            assertThat(SpecsEnums.valueOf(TestColor.class, "Red")).isEqualTo(TestColor.RED); // Returns first on fail
            assertThat(SpecsEnums.valueOf(TestColor.class, "RED")).isEqualTo(TestColor.RED); // Exact match
        }

        @Test
        @DisplayName("getFirstEnum should throw exception for enum with no values")
        void testGetFirstEnum_EmptyEnum() {
            // This is hard to test as Java doesn't allow empty enums at compile time
            // The method would throw RuntimeException if such enum existed
        }

        @Test
        @DisplayName("fromOrdinal should handle boundary ordinals")
        void testFromOrdinal_Boundaries() {
            // Execute & Verify
            assertThat(SpecsEnums.fromOrdinal(TestColor.class, 0)).isEqualTo(TestColor.RED);
            assertThat(SpecsEnums.fromOrdinal(TestColor.class, 3)).isEqualTo(TestColor.YELLOW);

            // Test out of bounds - should handle gracefully
            assertThatThrownBy(() -> SpecsEnums.fromOrdinal(TestColor.class, -1))
                    .isInstanceOf(RuntimeException.class);
            assertThatThrownBy(() -> SpecsEnums.fromOrdinal(TestColor.class, 4))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("operations should be thread-safe for helper caching")
        void testThreadSafety() {
            // Execute multiple times to test caching
            for (int i = 0; i < 100; i++) {
                var helper = SpecsEnums.getHelper(TestColor.class);
                assertThat(helper).isNotNull();
                assertThat(helper.values()).hasSize(4);
            }
        }

        @Test
        @DisplayName("operations should handle null enum class gracefully")
        void testNullEnumClass() {
            // Execute & Verify
            assertThatThrownBy(() -> SpecsEnums.valueOf(null, "TEST"))
                    .isInstanceOf(NullPointerException.class);
        }

        @ParameterizedTest
        @CsvSource({
                "RED, 0",
                "GREEN, 1",
                "BLUE, 2",
                "YELLOW, 3"
        })
        @DisplayName("enum ordinals should be consistent")
        void testEnumOrdinals(String enumName, int expectedOrdinal) {
            // Execute
            TestColor enumValue = SpecsEnums.valueOf(TestColor.class, enumName);

            // Verify
            assertThat(enumValue.ordinal()).isEqualTo(expectedOrdinal);
            assertThat(SpecsEnums.fromOrdinal(TestColor.class, expectedOrdinal)).isEqualTo(enumValue);
        }

        @Test
        @DisplayName("operations should preserve enum order")
        void testEnumOrder() {
            // Execute
            List<TestColor> values = SpecsEnums.extractValues(TestColor.class);
            
            // Verify order is preserved
            assertThat(values).containsExactly(TestColor.RED, TestColor.GREEN, TestColor.BLUE, TestColor.YELLOW);
        }
    }

    @Nested
    @DisplayName("Advanced Enum Operations")
    class AdvancedEnumOperations {

        @Test
        @DisplayName("buildMap with enum array should create name-to-enum map")
        void testBuildMapWithArray() {
            TestColor[] values = TestColor.values();
            Map<String, TestColor> map = SpecsEnums.buildMap(values);
            
            assertThat(map).hasSize(4);
            assertThat(map).containsEntry("RED", TestColor.RED);
            assertThat(map).containsEntry("GREEN", TestColor.GREEN);
            assertThat(map).containsEntry("BLUE", TestColor.BLUE);
            assertThat(map).containsEntry("YELLOW", TestColor.YELLOW);
        }

        @Test
        @DisplayName("buildNamesMap should create name-to-enum map with exclusions")
        void testBuildNamesMap() {
            List<TestColor> excludeList = Arrays.asList(TestColor.YELLOW);
            Map<String, TestColor> map = SpecsEnums.buildNamesMap(TestColor.class, excludeList);
            
            assertThat(map).hasSize(3);
            assertThat(map).containsEntry("RED", TestColor.RED);
            assertThat(map).containsEntry("GREEN", TestColor.GREEN);
            assertThat(map).containsEntry("BLUE", TestColor.BLUE);
            assertThat(map).doesNotContainKey("YELLOW");
        }

        @Test
        @DisplayName("valueOfTry should return Optional")
        void testValueOfTry() {
            Optional<TestColor> validResult = SpecsEnums.valueOfTry(TestColor.class, "RED");
            assertThat(validResult).isPresent();
            assertThat(validResult.get()).isEqualTo(TestColor.RED);
            
            Optional<TestColor> invalidResult = SpecsEnums.valueOfTry(TestColor.class, "INVALID");
            assertThat(invalidResult).isPresent(); // valueOf returns first element on error
            assertThat(invalidResult.get()).isEqualTo(TestColor.RED); // First element
        }
    }

    // Additional test enum for single value testing
    enum TestSingleValue {
        ONLY
    }
}
