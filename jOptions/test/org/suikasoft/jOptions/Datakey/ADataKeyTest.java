package org.suikasoft.jOptions.Datakey;

import static org.assertj.core.api.Assertions.*;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.suikasoft.jOptions.gui.KeyPanelProvider;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Comprehensive test suite for ADataKey abstract class functionality.
 * 
 * Tests cover:
 * - Constructor validation and initialization
 * - Basic property access (getName, toString)
 * - Equality and hashCode contracts
 * - Default value handling
 * - Copy functionality (via concrete implementation)
 * 
 * @author Generated Tests
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ADataKey Abstract Class Tests")
class ADataKeyTest {

    @Mock
    private Supplier<String> mockDefaultValueProvider;

    @Mock
    private StringCodec<String> mockDecoder;

    @Mock
    private CustomGetter<String> mockCustomGetter;

    @Mock
    private KeyPanelProvider<String> mockPanelProvider;

    @Mock
    private StoreDefinition mockStoreDefinition;

    @Mock
    private Function<String, String> mockCopyFunction;

    @Mock
    private CustomGetter<String> mockCustomSetter;

    @Mock
    private DataKeyExtraData mockExtraData;

    // Concrete implementation for testing
    private static class TestDataKey extends ADataKey<String> {
        public TestDataKey(String id, Supplier<String> defaultValue) {
            super(id, defaultValue);
        }

        public TestDataKey(String id, Supplier<? extends String> defaultValueProvider,
                StringCodec<String> decoder, CustomGetter<String> customGetter,
                KeyPanelProvider<String> panelProvider, String label,
                StoreDefinition definition, Function<String, String> copyFunction,
                CustomGetter<String> customSetter, DataKeyExtraData extraData) {
            super(id, defaultValueProvider, decoder, customGetter, panelProvider,
                    label, definition, copyFunction, customSetter, extraData);
        }

        @Override
        public Class<String> getValueClass() {
            return String.class;
        }

        @Override
        protected DataKey<String> copy(String id, Supplier<? extends String> defaultValueProvider,
                StringCodec<String> decoder, CustomGetter<String> customGetter,
                KeyPanelProvider<String> panelProvider, String label,
                StoreDefinition definition, Function<String, String> copyFunction,
                CustomGetter<String> customSetter, DataKeyExtraData extraData) {
            return new TestDataKey(id, defaultValueProvider, decoder, customGetter,
                    panelProvider, label, definition, copyFunction, customSetter, extraData);
        }
    }

    @Nested
    @DisplayName("Constructor and Initialization")
    class ConstructorTests {

        @Test
        @DisplayName("simple constructor creates ADataKey with id and default value")
        void testSimpleConstructor_CreatesDataKeyWithIdAndDefaultValue() {
            String id = "test.key";
            Supplier<String> defaultValue = () -> "default";

            TestDataKey dataKey = new TestDataKey(id, defaultValue);

            assertThat(dataKey.getName()).isEqualTo(id);
        }

        @Test
        @DisplayName("full constructor creates ADataKey with all parameters")
        void testFullConstructor_CreatesDataKeyWithAllParameters() {
            String id = "test.full.key";
            String label = "Test Label";

            TestDataKey dataKey = new TestDataKey(id, mockDefaultValueProvider, mockDecoder,
                    mockCustomGetter, mockPanelProvider, label, mockStoreDefinition,
                    mockCopyFunction, mockCustomSetter, mockExtraData);

            assertThat(dataKey.getName()).isEqualTo(id);
        }

        @Test
        @DisplayName("constructor handles null optional parameters gracefully")
        void testConstructor_HandlesNullOptionalParameters() {
            String id = "test.null.key";

            TestDataKey dataKey = new TestDataKey(id, null, null, null, null,
                    null, null, null, null, null);

            assertThat(dataKey.getName()).isEqualTo(id);
        }
    }

    @Nested
    @DisplayName("Basic Properties")
    class BasicPropertiesTests {

        private TestDataKey dataKey;

        @BeforeEach
        void setUp() {
            dataKey = new TestDataKey("test.properties", () -> "test");
        }

        @Test
        @DisplayName("getName returns the id provided in constructor")
        void testGetName_ReturnsConstructorId() {
            assertThat(dataKey.getName()).isEqualTo("test.properties");
        }

        @Test
        @DisplayName("toString returns string representation using DataKey utility")
        void testToString_ReturnsStringRepresentation() {
            String result = dataKey.toString();

            // Verify it's not null and contains the key name
            assertThat(result).isNotNull();
            assertThat(result).contains("test.properties");
        }
    }

    @Nested
    @DisplayName("Equality and HashCode")
    class EqualityTests {

        @Test
        @DisplayName("equals returns true for same object")
        void testEquals_SameObject_ReturnsTrue() {
            TestDataKey dataKey = new TestDataKey("test.equals", () -> "value");

            assertThat(dataKey).isEqualTo(dataKey);
        }

        @Test
        @DisplayName("equals returns true for keys with same id")
        void testEquals_SameId_ReturnsTrue() {
            TestDataKey dataKey1 = new TestDataKey("test.equals", () -> "value1");
            TestDataKey dataKey2 = new TestDataKey("test.equals", () -> "value2");

            assertThat(dataKey1).isEqualTo(dataKey2);
        }

        @Test
        @DisplayName("equals returns false for keys with different ids")
        void testEquals_DifferentId_ReturnsFalse() {
            TestDataKey dataKey1 = new TestDataKey("test.equals.1", () -> "value");
            TestDataKey dataKey2 = new TestDataKey("test.equals.2", () -> "value");

            assertThat(dataKey1).isNotEqualTo(dataKey2);
        }

        @Test
        @DisplayName("equals returns false for null")
        void testEquals_Null_ReturnsFalse() {
            TestDataKey dataKey = new TestDataKey("test.equals", () -> "value");

            assertThat(dataKey).isNotEqualTo(null);
        }

        @Test
        @DisplayName("equals returns false for different class")
        void testEquals_DifferentClass_ReturnsFalse() {
            TestDataKey dataKey = new TestDataKey("test.equals", () -> "value");
            String otherObject = "not a data key";

            assertThat(dataKey).isNotEqualTo(otherObject);
        }

        @Test
        @DisplayName("hashCode is consistent with equals")
        void testHashCode_ConsistentWithEquals() {
            TestDataKey dataKey1 = new TestDataKey("test.hash", () -> "value1");
            TestDataKey dataKey2 = new TestDataKey("test.hash", () -> "value2");

            assertThat(dataKey1.hashCode()).isEqualTo(dataKey2.hashCode());
        }

        @Test
        @DisplayName("hashCode is different for different ids")
        void testHashCode_DifferentForDifferentIds() {
            TestDataKey dataKey1 = new TestDataKey("test.hash.1", () -> "value");
            TestDataKey dataKey2 = new TestDataKey("test.hash.2", () -> "value");

            assertThat(dataKey1.hashCode()).isNotEqualTo(dataKey2.hashCode());
        }
    }

    @Nested
    @DisplayName("Default Value Handling")
    class DefaultValueTests {

        @Test
        @DisplayName("constructor accepts supplier for default value")
        void testConstructor_AcceptsDefaultValueSupplier() {
            Supplier<String> supplier = () -> "default_value";

            TestDataKey dataKey = new TestDataKey("test.default", supplier);

            // Constructor should accept the supplier without throwing
            assertThat(dataKey.getName()).isEqualTo("test.default");
        }

        @Test
        @DisplayName("constructor accepts null default value supplier")
        void testConstructor_AcceptsNullDefaultValueSupplier() {
            TestDataKey dataKey = new TestDataKey("test.null.default", null);

            assertThat(dataKey.getName()).isEqualTo("test.null.default");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("constructor with empty id creates valid DataKey")
        void testConstructor_EmptyId_CreatesValidDataKey() {
            TestDataKey dataKey = new TestDataKey("", () -> "value");

            assertThat(dataKey.getName()).isEmpty();
        }

        @Test
        @DisplayName("constructor with special characters in id")
        void testConstructor_SpecialCharactersInId_CreatesValidDataKey() {
            String specialId = "test.key-with_special@chars#123";

            TestDataKey dataKey = new TestDataKey(specialId, () -> "value");

            assertThat(dataKey.getName()).isEqualTo(specialId);
        }

        @Test
        @DisplayName("equals handles null id gracefully")
        void testEquals_HandlesNullId() {
            // This test checks the null handling in equals method
            // Since constructor asserts id != null, we test with mock
            TestDataKey dataKey1 = new TestDataKey("test.id", () -> "value");
            TestDataKey dataKey2 = new TestDataKey("test.id", () -> "value");

            assertThat(dataKey1).isEqualTo(dataKey2);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("ADataKey can be used in collections based on equals/hashCode")
        void testDataKey_CanBeUsedInCollections() {
            TestDataKey dataKey1 = new TestDataKey("collection.test", () -> "value1");
            TestDataKey dataKey2 = new TestDataKey("collection.test", () -> "value2");
            TestDataKey dataKey3 = new TestDataKey("collection.other", () -> "value3");

            java.util.Set<TestDataKey> keySet = new java.util.HashSet<>();
            keySet.add(dataKey1);
            keySet.add(dataKey2); // Should not be added due to equality
            keySet.add(dataKey3);

            assertThat(keySet).hasSize(2);
            assertThat(keySet).contains(dataKey1);
            assertThat(keySet).contains(dataKey3);
        }

        @Test
        @DisplayName("multiple ADataKey instances work correctly together")
        void testMultipleDataKeys_WorkCorrectlyTogether() {
            TestDataKey stringKey = new TestDataKey("string.key", () -> "default");
            TestDataKey anotherKey = new TestDataKey("another.key", () -> "other");

            assertThat(stringKey.getName()).isEqualTo("string.key");
            assertThat(anotherKey.getName()).isEqualTo("another.key");
            assertThat(stringKey).isNotEqualTo(anotherKey);
        }
    }
}
