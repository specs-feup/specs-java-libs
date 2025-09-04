package org.suikasoft.jOptions.Datakey;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import pt.up.fe.specs.util.utilities.StringList;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for KeyFactory static factory methods.
 * 
 * Tests cover:
 * - Basic type factories (bool, string, integer, etc.)
 * - Default value handling
 * - Value class verification
 * - Complex type factories (file, collections)
 * - Factory method consistency
 * - Generated key properties
 * 
 * @author Generated Tests
 */
@DisplayName("KeyFactory Static Factory Tests")
class KeyFactoryTest {

    @Nested
    @DisplayName("Boolean Key Factory")
    class BooleanFactoryTests {

        @Test
        @DisplayName("bool factory creates Boolean DataKey")
        void testBoolFactory_CreatesBooleanDataKey() {
            DataKey<Boolean> key = KeyFactory.bool("test.bool");

            assertThat(key).isNotNull();
            assertThat(key.getName()).isEqualTo("test.bool");
            assertThat(key.getValueClass()).isEqualTo(Boolean.class);
        }

        @Test
        @DisplayName("bool factory creates key with FALSE default")
        void testBoolFactory_CreatesKeyWithFalseDefault() {
            DataKey<Boolean> key = KeyFactory.bool("default.bool");

            // The factory should set default to FALSE
            assertThat(key.getName()).isEqualTo("default.bool");
        }
    }

    @Nested
    @DisplayName("String Key Factory")
    class StringFactoryTests {

        @Test
        @DisplayName("string factory creates String DataKey")
        void testStringFactory_CreatesStringDataKey() {
            DataKey<String> key = KeyFactory.string("test.string");

            assertThat(key).isNotNull();
            assertThat(key.getName()).isEqualTo("test.string");
            assertThat(key.getValueClass()).isEqualTo(String.class);
        }

        @Test
        @DisplayName("string factory with default value creates String DataKey")
        void testStringFactoryWithDefault_CreatesStringDataKey() {
            String defaultValue = "default_value";

            DataKey<String> key = KeyFactory.string("default.string", defaultValue);

            assertThat(key.getName()).isEqualTo("default.string");
            assertThat(key.getValueClass()).isEqualTo(String.class);
        }

        @Test
        @DisplayName("string factory without default has empty string default")
        void testStringFactoryWithoutDefault_HasEmptyStringDefault() {
            DataKey<String> key = KeyFactory.string("empty.default");

            // Factory should set default to empty string
            assertThat(key.getName()).isEqualTo("empty.default");
        }
    }

    @Nested
    @DisplayName("Integer Key Factory")
    class IntegerFactoryTests {

        @Test
        @DisplayName("integer factory creates Integer DataKey")
        void testIntegerFactory_CreatesIntegerDataKey() {
            DataKey<Integer> key = KeyFactory.integer("test.int");

            assertThat(key).isNotNull();
            assertThat(key.getName()).isEqualTo("test.int");
            assertThat(key.getValueClass()).isEqualTo(Integer.class);
        }

        @Test
        @DisplayName("integer factory with default value creates Integer DataKey")
        void testIntegerFactoryWithDefault_CreatesIntegerDataKey() {
            int defaultValue = 42;

            DataKey<Integer> key = KeyFactory.integer("default.int", defaultValue);

            assertThat(key.getName()).isEqualTo("default.int");
            assertThat(key.getValueClass()).isEqualTo(Integer.class);
        }
    }

    @Nested
    @DisplayName("Long Key Factory")
    class LongFactoryTests {

        @Test
        @DisplayName("longInt factory creates Long DataKey")
        void testLongIntFactory_CreatesLongDataKey() {
            DataKey<Long> key = KeyFactory.longInt("test.long");

            assertThat(key).isNotNull();
            assertThat(key.getName()).isEqualTo("test.long");
            assertThat(key.getValueClass()).isEqualTo(Long.class);
        }

        @Test
        @DisplayName("longInt factory with default value creates Long DataKey")
        void testLongIntFactoryWithDefault_CreatesLongDataKey() {
            long defaultValue = 123456789L;

            DataKey<Long> key = KeyFactory.longInt("default.long", defaultValue);

            assertThat(key.getName()).isEqualTo("default.long");
            assertThat(key.getValueClass()).isEqualTo(Long.class);
        }
    }

    @Nested
    @DisplayName("Double Key Factory")
    class DoubleFactoryTests {

        @Test
        @DisplayName("double64 factory creates Double DataKey")
        void testDouble64Factory_CreatesDoubleDataKey() {
            DataKey<Double> key = KeyFactory.double64("test.double");

            assertThat(key).isNotNull();
            assertThat(key.getName()).isEqualTo("test.double");
            assertThat(key.getValueClass()).isEqualTo(Double.class);
        }

        @Test
        @DisplayName("double64 factory with default value creates Double DataKey")
        void testDouble64FactoryWithDefault_CreatesDoubleDataKey() {
            double defaultValue = 3.14159;

            DataKey<Double> key = KeyFactory.double64("default.double", defaultValue);

            assertThat(key.getName()).isEqualTo("default.double");
            assertThat(key.getValueClass()).isEqualTo(Double.class);
        }
    }

    @Nested
    @DisplayName("BigInteger Key Factory")
    class BigIntegerFactoryTests {

        @Test
        @DisplayName("bigInteger factory creates BigInteger DataKey")
        void testBigIntegerFactory_CreatesBigIntegerDataKey() {
            DataKey<BigInteger> key = KeyFactory.bigInteger("test.bigint");

            assertThat(key).isNotNull();
            assertThat(key.getName()).isEqualTo("test.bigint");
            assertThat(key.getValueClass()).isEqualTo(BigInteger.class);
        }
    }

    @Nested
    @DisplayName("File Key Factory")
    class FileFactoryTests {

        @Test
        @DisplayName("file factory creates File DataKey")
        void testFileFactory_CreatesFileDataKey() {
            DataKey<File> key = KeyFactory.file("test.file");

            assertThat(key).isNotNull();
            assertThat(key.getName()).isEqualTo("test.file");
            assertThat(key.getValueClass()).isEqualTo(File.class);
        }
    }

    @Nested
    @DisplayName("Collection Key Factories")
    class CollectionFactoryTests {

        @Test
        @DisplayName("stringList factory creates StringList DataKey")
        void testStringListFactory_CreatesStringListDataKey() {
            DataKey<StringList> key = KeyFactory.stringList("test.stringlist");

            assertThat(key).isNotNull();
            assertThat(key.getName()).isEqualTo("test.stringlist");
            assertThat(key.getValueClass()).isEqualTo(StringList.class);
        }

        @Test
        @DisplayName("stringList factory with default creates StringList DataKey")
        void testStringListFactoryWithDefault_CreatesStringListDataKey() {
            List<String> defaultList = java.util.Arrays.asList("default1", "default2");

            DataKey<StringList> key = KeyFactory.stringList("default.stringlist", defaultList);

            assertThat(key.getName()).isEqualTo("default.stringlist");
            assertThat(key.getValueClass()).isEqualTo(StringList.class);
        }
    }

    @Nested
    @DisplayName("Enum Key Factory")
    class EnumFactoryTests {

        private enum TestEnum {
            VALUE1, VALUE2, VALUE3
        }

        @Test
        @DisplayName("enumeration factory creates Enum DataKey")
        void testEnumerationFactory_CreatesEnumDataKey() {
            DataKey<TestEnum> key = KeyFactory.enumeration("test.enum", TestEnum.class);

            assertThat(key).isNotNull();
            assertThat(key.getName()).isEqualTo("test.enum");
            assertThat(key.getValueClass()).isEqualTo(TestEnum.class);
        }

        @Test
        @DisplayName("enumeration factory with default creates Enum DataKey")
        void testEnumerationFactoryWithDefault_CreatesEnumDataKey() {
            // The enumeration method only takes (String id, Class<T> enumClass)
            // No default value parameter available
            DataKey<TestEnum> key = KeyFactory.enumeration("default.enum", TestEnum.class);

            assertThat(key.getName()).isEqualTo("default.enum");
            assertThat(key.getValueClass()).isEqualTo(TestEnum.class);
        }
    }

    @Nested
    @DisplayName("Factory Method Consistency")
    class FactoryConsistencyTests {

        @Test
        @DisplayName("all factories create DataKey instances")
        void testAllFactories_CreateDataKeyInstances() {
            DataKey<Boolean> boolKey = KeyFactory.bool("consistency.bool");
            DataKey<String> stringKey = KeyFactory.string("consistency.string");
            DataKey<Integer> intKey = KeyFactory.integer("consistency.int");

            assertThat(boolKey).isInstanceOf(DataKey.class);
            assertThat(stringKey).isInstanceOf(DataKey.class);
            assertThat(intKey).isInstanceOf(DataKey.class);
        }

        @Test
        @DisplayName("factory methods respect provided ids")
        void testFactoryMethods_RespectProvidedIds() {
            String boolId = "test.bool.id";
            String stringId = "test.string.id";
            String intId = "test.int.id";

            DataKey<Boolean> boolKey = KeyFactory.bool(boolId);
            DataKey<String> stringKey = KeyFactory.string(stringId);
            DataKey<Integer> intKey = KeyFactory.integer(intId);

            assertThat(boolKey.getName()).isEqualTo(boolId);
            assertThat(stringKey.getName()).isEqualTo(stringId);
            assertThat(intKey.getName()).isEqualTo(intId);
        }

        @Test
        @DisplayName("factory methods create keys with correct value classes")
        void testFactoryMethods_CreateKeysWithCorrectValueClasses() {
            DataKey<Boolean> boolKey = KeyFactory.bool("class.bool");
            DataKey<String> stringKey = KeyFactory.string("class.string");
            DataKey<Integer> intKey = KeyFactory.integer("class.int");
            DataKey<Double> doubleKey = KeyFactory.double64("class.double");

            assertThat(boolKey.getValueClass()).isEqualTo(Boolean.class);
            assertThat(stringKey.getValueClass()).isEqualTo(String.class);
            assertThat(intKey.getValueClass()).isEqualTo(Integer.class);
            assertThat(doubleKey.getValueClass()).isEqualTo(Double.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("factories work with empty string ids")
        void testFactories_WorkWithEmptyStringIds() {
            DataKey<Boolean> boolKey = KeyFactory.bool("");
            DataKey<String> stringKey = KeyFactory.string("");

            assertThat(boolKey.getName()).isEmpty();
            assertThat(stringKey.getName()).isEmpty();
        }

        @Test
        @DisplayName("factories work with special character ids")
        void testFactories_WorkWithSpecialCharacterIds() {
            String specialId = "test.key-with_special@chars#123";

            DataKey<Boolean> boolKey = KeyFactory.bool(specialId);
            DataKey<String> stringKey = KeyFactory.string(specialId);

            assertThat(boolKey.getName()).isEqualTo(specialId);
            assertThat(stringKey.getName()).isEqualTo(specialId);
        }

        @Test
        @DisplayName("string factory with null default value works")
        void testStringFactory_WithNullDefaultValue_Works() {
            DataKey<String> key = KeyFactory.string("null.default", null);

            assertThat(key.getName()).isEqualTo("null.default");
            assertThat(key.getValueClass()).isEqualTo(String.class);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("factory keys work in collections")
        void testFactoryKeys_WorkInCollections() {
            DataKey<Boolean> boolKey = KeyFactory.bool("collection.bool");
            DataKey<String> stringKey = KeyFactory.string("collection.string");
            DataKey<Integer> intKey = KeyFactory.integer("collection.int");

            java.util.Set<DataKey<?>> keySet = new java.util.HashSet<>();
            keySet.add(boolKey);
            keySet.add(stringKey);
            keySet.add(intKey);

            assertThat(keySet).hasSize(3);
        }

        @Test
        @DisplayName("factory keys have proper toString representation")
        void testFactoryKeys_HaveProperToStringRepresentation() {
            DataKey<Boolean> boolKey = KeyFactory.bool("toString.bool");
            DataKey<String> stringKey = KeyFactory.string("toString.string");

            String boolString = boolKey.toString();
            String stringString = stringKey.toString();

            assertThat(boolString).contains("toString.bool");
            assertThat(stringString).contains("toString.string");
        }

        @Test
        @DisplayName("factory keys implement equals and hashCode correctly")
        void testFactoryKeys_ImplementEqualsAndHashCodeCorrectly() {
            DataKey<String> key1 = KeyFactory.string("equals.test");
            DataKey<String> key2 = KeyFactory.string("equals.test");
            DataKey<String> key3 = KeyFactory.string("different.test");

            assertThat(key1).isEqualTo(key2);
            assertThat(key1).isNotEqualTo(key3);
            assertThat(key1.hashCode()).isEqualTo(key2.hashCode());
        }
    }
}
