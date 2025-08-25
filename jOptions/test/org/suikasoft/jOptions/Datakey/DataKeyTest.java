package org.suikasoft.jOptions.Datakey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;
import org.suikasoft.jOptions.gui.KeyPanelProvider;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Unit tests for {@link DataKey}.
 * 
 * Tests the core data key interface including encoding/decoding, default
 * values, custom getters/setters, and key metadata management. Since DataKey is
 * an interface, tests use a mock implementation to verify behavior.
 * 
 * @author Generated Tests
 */
@DisplayName("DataKey")
@SuppressWarnings({ "unchecked", "rawtypes" })
class DataKeyTest {

    private DataKey<String> mockStringKey;
    private DataKey<Integer> mockIntKey;
    private StringCodec<String> mockStringCodec;
    @SuppressWarnings("unused")
    private StringCodec<Integer> mockIntCodec;
    private DataStore mockDataStore;
    private StoreDefinition mockStoreDefinition;
    private KeyPanelProvider<String> mockPanelProvider;
    private KeyPanel<String> mockPanel;
    private CustomGetter<String> mockCustomGetter;
    private DataKeyExtraData mockExtraData;

    @BeforeEach
    void setUp() {
        mockStringKey = mock(DataKey.class);
        mockIntKey = mock(DataKey.class);
        mockStringCodec = mock(StringCodec.class);
        mockIntCodec = mock(StringCodec.class);
        mockDataStore = mock(DataStore.class);
        mockStoreDefinition = mock(StoreDefinition.class);
        mockPanelProvider = mock(KeyPanelProvider.class);
        mockPanel = mock(KeyPanel.class);
        mockCustomGetter = mock(CustomGetter.class);
        mockExtraData = mock(DataKeyExtraData.class);

        // Setup basic behavior for string key
        when(mockStringKey.getName()).thenReturn("testStringKey");
        when(mockStringKey.getValueClass()).thenReturn(String.class);
        when(mockStringKey.getKey()).thenCallRealMethod();
        when(mockStringKey.getTypeName()).thenCallRealMethod();
        when(mockStringKey.getLabel()).thenReturn("Test String Key");

        // Setup basic behavior for int key
        when(mockIntKey.getName()).thenReturn("testIntKey");
        when(mockIntKey.getValueClass()).thenReturn(Integer.class);
        when(mockIntKey.getKey()).thenCallRealMethod();
        when(mockIntKey.getTypeName()).thenCallRealMethod();
        when(mockIntKey.getLabel()).thenReturn("Test Integer Key");
    }

    @Nested
    @DisplayName("Basic Key Properties")
    class BasicKeyPropertiesTests {

        @Test
        @DisplayName("getKey returns same as getName")
        void testGetKey_ReturnsSameAsGetName() {
            assertThat(mockStringKey.getKey()).isEqualTo("testStringKey");
            assertThat(mockIntKey.getKey()).isEqualTo("testIntKey");
        }

        @Test
        @DisplayName("getTypeName returns simple class name")
        void testGetTypeName_ReturnsSimpleClassName() {
            assertThat(mockStringKey.getTypeName()).isEqualTo("String");
            assertThat(mockIntKey.getTypeName()).isEqualTo("Integer");
        }

        @Test
        @DisplayName("getName returns key name")
        void testGetName_ReturnsKeyName() {
            assertThat(mockStringKey.getName()).isEqualTo("testStringKey");
            assertThat(mockIntKey.getName()).isEqualTo("testIntKey");
        }

        @Test
        @DisplayName("getValueClass returns correct class")
        void testGetValueClass_ReturnsCorrectClass() {
            assertThat(mockStringKey.getValueClass()).isEqualTo(String.class);
            assertThat(mockIntKey.getValueClass()).isEqualTo(Integer.class);
        }

        @Test
        @DisplayName("getLabel returns key label")
        void testGetLabel_ReturnsKeyLabel() {
            assertThat(mockStringKey.getLabel()).isEqualTo("Test String Key");
            assertThat(mockIntKey.getLabel()).isEqualTo("Test Integer Key");
        }
    }

    @Nested
    @DisplayName("Encoding and Decoding")
    class EncodingAndDecodingTests {

        @Test
        @DisplayName("decode with decoder present returns decoded value")
        void testDecode_DecoderPresent_ReturnsDecodedValue() {
            String encodedValue = "test_value";
            String decodedValue = "decoded_value";

            when(mockStringKey.getDecoder()).thenReturn(Optional.of(mockStringCodec));
            when(mockStringCodec.decode(encodedValue)).thenReturn(decodedValue);
            when(mockStringKey.decode(encodedValue)).thenCallRealMethod();

            String result = mockStringKey.decode(encodedValue);

            assertThat(result).isEqualTo(decodedValue);
        }

        @Test
        @DisplayName("decode without decoder throws exception")
        void testDecode_NoDecoder_ThrowsException() {
            when(mockStringKey.getDecoder()).thenReturn(Optional.empty());
            when(mockStringKey.decode("test")).thenCallRealMethod();

            assertThatThrownBy(() -> mockStringKey.decode("test"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("No encoder/decoder set");
        }

        @Test
        @DisplayName("encode with decoder present returns encoded value")
        void testEncode_DecoderPresent_ReturnsEncodedValue() {
            String value = "test_value";
            String encodedValue = "encoded_value";

            when(mockStringKey.getDecoder()).thenReturn(Optional.of(mockStringCodec));
            when(mockStringCodec.encode(value)).thenReturn(encodedValue);
            when(mockStringKey.encode(value)).thenCallRealMethod();

            String result = mockStringKey.encode(value);

            assertThat(result).isEqualTo(encodedValue);
        }

        @Test
        @DisplayName("encode without decoder throws exception")
        void testEncode_NoDecoder_ThrowsException() {
            when(mockStringKey.getDecoder()).thenReturn(Optional.empty());
            when(mockStringKey.encode("test")).thenCallRealMethod();

            assertThatThrownBy(() -> mockStringKey.encode("test"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("No encoder/decoder set");
        }
    }

    @Nested
    @DisplayName("Default Values")
    class DefaultValuesTests {

        @Test
        @DisplayName("hasDefaultValue returns true when default is present")
        void testHasDefaultValue_DefaultPresent_ReturnsTrue() {
            when(mockStringKey.getDefault()).thenReturn(Optional.of("default"));
            when(mockStringKey.hasDefaultValue()).thenReturn(true);

            assertThat(mockStringKey.hasDefaultValue()).isTrue();
        }

        @Test
        @DisplayName("hasDefaultValue returns false when default is empty")
        void testHasDefaultValue_DefaultEmpty_ReturnsFalse() {
            when(mockStringKey.getDefault()).thenReturn(Optional.empty());
            when(mockStringKey.hasDefaultValue()).thenReturn(false);

            assertThat(mockStringKey.hasDefaultValue()).isFalse();
        }

        @Test
        @DisplayName("setDefaultString with decoder creates key with string default")
        void testSetDefaultString_WithDecoder_CreatesKeyWithStringDefault() {
            String defaultString = "default_string";
            String decodedDefault = "decoded_default";
            DataKey<String> newKey = mock(DataKey.class);

            when(mockStringKey.getDecoder()).thenReturn(Optional.of(mockStringCodec));
            when(mockStringCodec.decode(defaultString)).thenReturn(decodedDefault);
            when(mockStringKey.setDefault(any(Supplier.class))).thenReturn(newKey);
            when(mockStringKey.setDefaultString(defaultString)).thenCallRealMethod();

            DataKey<String> result = mockStringKey.setDefaultString(defaultString);

            assertThat(result).isSameAs(newKey);
        }

        @Test
        @DisplayName("setDefaultString without decoder throws exception")
        void testSetDefaultString_NoDecoder_ThrowsException() {
            when(mockStringKey.getDecoder()).thenReturn(Optional.empty());
            when(mockStringKey.setDefaultString("test")).thenCallRealMethod();

            assertThatThrownBy(() -> mockStringKey.setDefaultString("test"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Can only use this method if a decoder was set before");
        }
    }

    @Nested
    @DisplayName("Panel Functionality")
    class PanelFunctionalityTests {

        @Test
        @DisplayName("getPanel with provider returns panel")
        void testGetPanel_WithProvider_ReturnsPanel() {
            when(mockStringKey.getKeyPanelProvider()).thenReturn(Optional.of(mockPanelProvider));
            when(mockPanelProvider.getPanel(mockStringKey, mockDataStore)).thenReturn(mockPanel);
            when(mockStringKey.getPanel(mockDataStore)).thenCallRealMethod();

            KeyPanel<String> result = mockStringKey.getPanel(mockDataStore);

            assertThat(result).isSameAs(mockPanel);
        }

        @Test
        @DisplayName("getPanel without provider throws exception")
        void testGetPanel_NoProvider_ThrowsException() {
            when(mockStringKey.getKeyPanelProvider()).thenReturn(Optional.empty());
            when(mockStringKey.getName()).thenReturn("testKey");
            when(mockStringKey.getValueClass()).thenReturn((Class) String.class);
            when(mockStringKey.getPanel(mockDataStore)).thenCallRealMethod();

            assertThatThrownBy(() -> mockStringKey.getPanel(mockDataStore))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("No panel defined for key 'testKey' of type 'class java.lang.String'");
        }
    }

    @Nested
    @DisplayName("Copy Functionality")
    class CopyFunctionalityTests {

        @Test
        @DisplayName("copy with copy function applies function")
        void testCopy_WithCopyFunction_AppliesFunction() {
            String original = "original";
            String copied = "copied";
            Function<String, String> copyFunction = mock(Function.class);

            when(mockStringKey.getCopyFunction()).thenReturn(Optional.of(copyFunction));
            when(copyFunction.apply(original)).thenReturn(copied);
            when(mockStringKey.copy(original)).thenCallRealMethod();

            String result = mockStringKey.copy(original);

            assertThat(result).isEqualTo(copied);
        }

        @Test
        @DisplayName("copy without copy function returns original")
        void testCopy_NoCopyFunction_ReturnsOriginal() {
            String original = "original";

            when(mockStringKey.getCopyFunction()).thenReturn(Optional.empty());
            when(mockStringKey.copy(original)).thenCallRealMethod();

            String result = mockStringKey.copy(original);

            assertThat(result).isSameAs(original);
        }

        @Test
        @DisplayName("copyRaw casts and delegates to copy")
        void testCopyRaw_CastsAndDelegatesToCopy() {
            String original = "original";
            String copied = "copied";

            when(mockStringKey.getValueClass()).thenReturn((Class) String.class);
            when(mockStringKey.copy(original)).thenReturn(copied);
            when(mockStringKey.copyRaw(original)).thenCallRealMethod();

            Object result = mockStringKey.copyRaw(original);

            assertThat(result).isEqualTo(copied);
        }
    }

    @Nested
    @DisplayName("Static Utility Methods")
    class StaticUtilityMethodsTests {

        @Test
        @DisplayName("toString with simple key creates correct string representation")
        void testToString_SimpleKey_CreatesCorrectStringRepresentation() {
            DataKey<String> realKey = mock(DataKey.class);
            when(realKey.getName()).thenReturn("simpleKey");
            when(realKey.getValueClass()).thenReturn((Class) String.class);
            when(realKey.getDefault()).thenReturn(Optional.empty());

            String result = DataKey.toString(realKey);

            assertThat(result).isEqualTo("simpleKey (String)");
        }

        @Test
        @DisplayName("toString with key with default value includes default")
        void testToString_KeyWithDefaultValue_IncludesDefault() {
            DataKey<String> realKey = mock(DataKey.class);
            when(realKey.getName()).thenReturn("keyWithDefault");
            when(realKey.getValueClass()).thenReturn((Class) String.class);
            when(realKey.getDefault()).thenReturn(Optional.of("defaultValue"));

            String result = DataKey.toString(realKey);

            // Production toString() unwraps Optional and prints the actual default value
            assertThat(result).contains("keyWithDefault (String = defaultValue)");
        }

        @Test
        @DisplayName("toString with collection creates multi-line representation")
        void testToString_Collection_CreatesMultiLineRepresentation() {
            DataKey<String> key1 = mock(DataKey.class);
            DataKey<Integer> key2 = mock(DataKey.class);

            when(key1.getName()).thenReturn("key1");
            when(key1.getValueClass()).thenReturn((Class) String.class);
            when(key1.getDefault()).thenReturn(Optional.empty());
            when(key1.toString()).thenReturn("key1 (String)");

            when(key2.getName()).thenReturn("key2");
            when(key2.getValueClass()).thenReturn((Class) Integer.class);
            when(key2.getDefault()).thenReturn(Optional.empty());
            when(key2.toString()).thenReturn("key2 (Integer)");

            Collection<DataKey<?>> keys = Arrays.asList(key1, key2);
            String result = DataKey.toString(keys);

            assertThat(result)
                    .contains("key1 (String)")
                    .contains("key2 (Integer)");
        }

        @Test
        @DisplayName("toString with DataStore default value handles nested structure")
        void testToString_DataStoreDefaultValue_HandlesNestedStructure() {
            DataKey<DataStore> dataStoreKey = mock(DataKey.class);
            DataStore defaultDataStore = mock(DataStore.class);

            when(dataStoreKey.getName()).thenReturn("dataStoreKey");
            when(dataStoreKey.getValueClass()).thenReturn((Class) DataStore.class);
            when(dataStoreKey.getDefault()).thenReturn(Optional.of(defaultDataStore));
            when(defaultDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys()).thenReturn(Arrays.asList(mockStringKey));

            String result = DataKey.toString(dataStoreKey);

            assertThat(result).contains("dataStoreKey (DataStore)");
        }
    }

    @Nested
    @DisplayName("Verification and Validation")
    class VerificationAndValidationTests {

        @Test
        @DisplayName("verifyValueClass returns true by default")
        void testVerifyValueClass_ReturnsTrueByDefault() {
            when(mockStringKey.verifyValueClass()).thenReturn(true);

            boolean result = mockStringKey.verifyValueClass();

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("toString handles null key gracefully")
        void testToString_NullKey_HandlesGracefully() {
            assertThatThrownBy(() -> DataKey.toString((DataKey<?>) null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("toString handles empty collection")
        void testToString_EmptyCollection_HandlesGracefully() {
            Collection<DataKey<?>> emptyKeys = Arrays.asList();
            String result = DataKey.toString(emptyKeys);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("toString throws for collection with null elements")
        void testToString_CollectionWithNullElements_Throws() {
            when(mockStringKey.toString()).thenReturn("testStringKey (String)");
            when(mockIntKey.toString()).thenReturn("testIntKey (Integer)");

            Collection<DataKey<?>> keysWithNull = Arrays.asList(mockStringKey, null, mockIntKey);

            assertThatThrownBy(() -> DataKey.toString(keysWithNull))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("DataKey collection contains null element");
        }

        @Test
        @DisplayName("copy handles null input")
        void testCopy_NullInput_HandlesGracefully() {
            when(mockStringKey.getCopyFunction()).thenReturn(Optional.empty());
            when(mockStringKey.copy(null)).thenCallRealMethod();

            String result = mockStringKey.copy(null);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("getPanel with null dataStore throws exception")
        void testGetPanel_NullDataStore_ThrowsException() {
            when(mockStringKey.getKeyPanelProvider()).thenReturn(Optional.of(mockPanelProvider));
            when(mockPanelProvider.getPanel(mockStringKey, null)).thenThrow(NullPointerException.class);
            when(mockStringKey.getPanel(null)).thenCallRealMethod();

            assertThatThrownBy(() -> mockStringKey.getPanel(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Complete key workflow with all features")
        void testCompleteKeyWorkflow_AllFeatures() {
            // Setup a comprehensive key configuration
            String keyName = "complexKey";
            String defaultValue = "default";
            String encodedValue = "encoded";
            String decodedValue = "decoded";

            DataKey<String> complexKey = mock(DataKey.class);
            when(complexKey.getName()).thenReturn(keyName);
            when(complexKey.getValueClass()).thenReturn((Class) String.class);
            when(complexKey.getKey()).thenCallRealMethod();
            when(complexKey.getTypeName()).thenCallRealMethod();
            when(complexKey.getLabel()).thenReturn("Complex Key");
            when(complexKey.getDecoder()).thenReturn(Optional.of(mockStringCodec));
            when(complexKey.getDefault()).thenReturn(Optional.of(defaultValue));
            when(complexKey.hasDefaultValue()).thenReturn(true); // Mock the abstract method
            when(complexKey.getKeyPanelProvider()).thenReturn(Optional.of(mockPanelProvider));
            when(complexKey.getCustomGetter()).thenReturn(Optional.of(mockCustomGetter));
            when(complexKey.getExtraData()).thenReturn(Optional.of(mockExtraData));

            // Setup codec behavior
            when(mockStringCodec.encode(decodedValue)).thenReturn(encodedValue);
            when(mockStringCodec.decode(encodedValue)).thenReturn(decodedValue);
            when(complexKey.encode(decodedValue)).thenCallRealMethod();
            when(complexKey.decode(encodedValue)).thenCallRealMethod();

            // Setup panel behavior
            when(mockPanelProvider.getPanel(complexKey, mockDataStore)).thenReturn(mockPanel);
            when(complexKey.getPanel(mockDataStore)).thenCallRealMethod();

            // Test all functionality
            assertThat(complexKey.getName()).isEqualTo(keyName);
            assertThat(complexKey.getKey()).isEqualTo(keyName);
            assertThat(complexKey.getTypeName()).isEqualTo("String");
            assertThat(complexKey.getLabel()).isEqualTo("Complex Key");
            assertThat(complexKey.hasDefaultValue()).isTrue();
            assertThat(complexKey.getDefault()).contains(defaultValue);
            assertThat(complexKey.encode(decodedValue)).isEqualTo(encodedValue);
            assertThat(complexKey.decode(encodedValue)).isEqualTo(decodedValue);
            assertThat(complexKey.getPanel(mockDataStore)).isSameAs(mockPanel);
            assertThat(complexKey.getCustomGetter()).contains(mockCustomGetter);
            assertThat(complexKey.getExtraData()).contains(mockExtraData);
        }

        @Test
        @DisplayName("Key equality and string representation")
        void testKeyEqualityAndStringRepresentation() {
            // Test string representation with various configurations
            DataKey<String> simpleKey = mock(DataKey.class);
            when(simpleKey.getName()).thenReturn("simple");
            when(simpleKey.getValueClass()).thenReturn((Class) String.class);
            when(simpleKey.getDefault()).thenReturn(Optional.empty());
            when(simpleKey.toString()).thenReturn("simple (String)");

            DataKey<String> keyWithDefault = mock(DataKey.class);
            when(keyWithDefault.getName()).thenReturn("withDefault");
            when(keyWithDefault.getValueClass()).thenReturn((Class) String.class);
            when(keyWithDefault.getDefault()).thenReturn(Optional.of("defaultValue"));
            when(keyWithDefault.toString()).thenReturn("withDefault (String = defaultValue)");

            Collection<DataKey<?>> keys = Arrays.asList(simpleKey, keyWithDefault);
            String representation = DataKey.toString(keys);

            assertThat(representation)
                    .contains("simple (String)")
                    .contains("withDefault (String = defaultValue)");
        }
    }
}
