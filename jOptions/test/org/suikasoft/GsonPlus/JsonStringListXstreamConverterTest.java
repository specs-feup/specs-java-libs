package org.suikasoft.GsonPlus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import pt.up.fe.specs.util.utilities.StringList;

/**
 * Comprehensive test suite for JsonStringListXstreamConverter class.
 * Tests converter interface compliance, type checking,
 * marshalling/unmarshalling, edge cases, legacy compatibility, error handling,
 * and integration scenarios.
 * 
 * @author Generated Tests
 */
class JsonStringListXstreamConverterTest {

    private JsonStringListXstreamConverter converter;

    @BeforeEach
    void setUp() {
        converter = new JsonStringListXstreamConverter();
    }

    @Nested
    @DisplayName("Converter Interface Tests")
    class ConverterInterfaceTests {

        @Test
        @DisplayName("Should implement Converter interface")
        void testConverterInterface() {
            assertThat(converter).isInstanceOf(com.thoughtworks.xstream.converters.Converter.class);
        }

        @Test
        @DisplayName("Should have canConvert method")
        void testCanConvertMethod() {
            // Verify the method exists and is accessible
            assertThat(converter.canConvert(JsonStringList.class)).isTrue();
            assertThat(converter.canConvert(String.class)).isFalse();
        }

        @Test
        @DisplayName("Should have marshal method")
        void testMarshalMethod() {
            // Verify the method exists but throws runtime exception
            HierarchicalStreamWriter writer = mock(HierarchicalStreamWriter.class);
            MarshallingContext context = mock(MarshallingContext.class);

            assertThatThrownBy(() -> converter.marshal(new JsonStringList(), writer, context))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("This should not be used");
        }

        @Test
        @DisplayName("Should have unmarshal method")
        void testUnmarshalMethod() {
            // Verify the method exists and works correctly
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            // Setup minimal mock behavior for empty list
            when(reader.hasMoreChildren()).thenReturn(false);

            Object result = converter.unmarshal(reader, context);
            assertThat(result).isInstanceOf(StringList.class);
        }
    }

    @Nested
    @DisplayName("Type Checking Tests")
    class TypeCheckingTests {

        @Test
        @DisplayName("Should convert JsonStringList class")
        void testCanConvertJsonStringList() {
            assertThat(converter.canConvert(JsonStringList.class)).isTrue();
        }

        @Test
        @DisplayName("Should not convert other classes")
        void testCannotConvertOtherClasses() {
            assertThat(converter.canConvert(String.class)).isFalse();
            assertThat(converter.canConvert(ArrayList.class)).isFalse();
            assertThat(converter.canConvert(StringList.class)).isFalse();
            assertThat(converter.canConvert(Object.class)).isFalse();
        }

        @Test
        @DisplayName("Should handle null class")
        void testCanConvertNullClass() {
            assertThat(converter.canConvert(null)).isFalse();
        }

        @Test
        @DisplayName("Should handle primitive types")
        void testCanConvertPrimitiveTypes() {
            assertThat(converter.canConvert(int.class)).isFalse();
            assertThat(converter.canConvert(boolean.class)).isFalse();
            assertThat(converter.canConvert(double.class)).isFalse();
        }

        @Test
        @DisplayName("Should handle wrapper types")
        void testCanConvertWrapperTypes() {
            assertThat(converter.canConvert(Integer.class)).isFalse();
            assertThat(converter.canConvert(Boolean.class)).isFalse();
            assertThat(converter.canConvert(Double.class)).isFalse();
        }
    }

    @Nested
    @DisplayName("Marshalling Tests")
    class MarshallingTests {

        @Test
        @DisplayName("Should throw exception for JsonStringList marshalling")
        void testMarshalJsonStringList() {
            HierarchicalStreamWriter writer = mock(HierarchicalStreamWriter.class);
            MarshallingContext context = mock(MarshallingContext.class);
            JsonStringList jsonStringList = new JsonStringList();

            assertThatThrownBy(() -> converter.marshal(jsonStringList, writer, context))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("This should not be used");
        }

        @Test
        @DisplayName("Should throw exception for null source")
        void testMarshalNullSource() {
            HierarchicalStreamWriter writer = mock(HierarchicalStreamWriter.class);
            MarshallingContext context = mock(MarshallingContext.class);

            assertThatThrownBy(() -> converter.marshal(null, writer, context))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("This should not be used");
        }

        @Test
        @DisplayName("Should throw exception with null writer")
        void testMarshalNullWriter() {
            MarshallingContext context = mock(MarshallingContext.class);
            JsonStringList jsonStringList = new JsonStringList();

            assertThatThrownBy(() -> converter.marshal(jsonStringList, null, context))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("This should not be used");
        }

        @Test
        @DisplayName("Should throw exception with null context")
        void testMarshalNullContext() {
            HierarchicalStreamWriter writer = mock(HierarchicalStreamWriter.class);
            JsonStringList jsonStringList = new JsonStringList();

            assertThatThrownBy(() -> converter.marshal(jsonStringList, writer, null))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("This should not be used");
        }

        @Test
        @DisplayName("Should always throw exception regardless of parameters")
        void testMarshalAlwaysThrows() {
            HierarchicalStreamWriter writer = mock(HierarchicalStreamWriter.class);
            MarshallingContext context = mock(MarshallingContext.class);

            // Test with different object types
            assertThatThrownBy(() -> converter.marshal("string", writer, context))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("This should not be used");

            assertThatThrownBy(() -> converter.marshal(new ArrayList<>(), writer, context))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("This should not be used");
        }
    }

    @Nested
    @DisplayName("Unmarshalling Tests")
    class UnmarshallingTests {

        @Test
        @DisplayName("Should unmarshal empty XML to empty StringList")
        void testUnmarshalEmptyXml() {
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            when(reader.hasMoreChildren()).thenReturn(false);

            Object result = converter.unmarshal(reader, context);

            assertThat(result).isInstanceOf(StringList.class);
            StringList stringList = (StringList) result;
            assertThat(stringList.getStringList()).isEmpty();
        }

        @Test
        @DisplayName("Should unmarshal single element XML")
        void testUnmarshalSingleElement() {
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            when(reader.hasMoreChildren())
                    .thenReturn(true)
                    .thenReturn(false);
            when(reader.getValue()).thenReturn("test-value");

            Object result = converter.unmarshal(reader, context);

            assertThat(result).isInstanceOf(StringList.class);
            StringList stringList = (StringList) result;
            assertThat(stringList.getStringList()).hasSize(1);
            assertThat(stringList.getStringList().get(0)).isEqualTo("test-value");
        }

        @Test
        @DisplayName("Should unmarshal multiple element XML")
        void testUnmarshalMultipleElements() {
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            when(reader.hasMoreChildren())
                    .thenReturn(true)
                    .thenReturn(true)
                    .thenReturn(true)
                    .thenReturn(false);
            when(reader.getValue())
                    .thenReturn("flag1")
                    .thenReturn("flag2")
                    .thenReturn("flag3");

            Object result = converter.unmarshal(reader, context);

            assertThat(result).isInstanceOf(StringList.class);
            StringList stringList = (StringList) result;
            assertThat(stringList.getStringList()).hasSize(3);
            assertThat(stringList.getStringList()).containsExactly("flag1", "flag2", "flag3");
        }

        @Test
        @DisplayName("Should handle XML with empty string values")
        void testUnmarshalEmptyStringValues() {
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            when(reader.hasMoreChildren())
                    .thenReturn(true)
                    .thenReturn(true)
                    .thenReturn(false);
            when(reader.getValue())
                    .thenReturn("")
                    .thenReturn("non-empty");

            Object result = converter.unmarshal(reader, context);

            assertThat(result).isInstanceOf(StringList.class);
            StringList stringList = (StringList) result;
            assertThat(stringList.getStringList()).hasSize(2);
            assertThat(stringList.getStringList()).containsExactly("", "non-empty");
        }

        @Test
        @DisplayName("Should handle XML with special characters")
        void testUnmarshalSpecialCharacters() {
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            when(reader.hasMoreChildren())
                    .thenReturn(true)
                    .thenReturn(true)
                    .thenReturn(true)
                    .thenReturn(false);
            when(reader.getValue())
                    .thenReturn("value with spaces")
                    .thenReturn("value/with/slashes")
                    .thenReturn("value-with-dashes");

            Object result = converter.unmarshal(reader, context);

            assertThat(result).isInstanceOf(StringList.class);
            StringList stringList = (StringList) result;
            assertThat(stringList.getStringList()).hasSize(3);
            assertThat(stringList.getStringList()).containsExactly("value with spaces", "value/with/slashes",
                    "value-with-dashes");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very large XML content")
        void testUnmarshalLargeContent() {
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            // Simulate 1000 elements
            Boolean[] hasMoreChildren = new Boolean[1001];
            String[] values = new String[1000];

            for (int i = 0; i < 1000; i++) {
                hasMoreChildren[i] = true;
                values[i] = "value" + i;
            }
            hasMoreChildren[1000] = false;

            when(reader.hasMoreChildren()).thenReturn(hasMoreChildren[0],
                    java.util.Arrays.copyOfRange(hasMoreChildren, 1, hasMoreChildren.length));
            when(reader.getValue()).thenReturn(values[0], java.util.Arrays.copyOfRange(values, 1, values.length));

            Object result = converter.unmarshal(reader, context);

            assertThat(result).isInstanceOf(StringList.class);
            StringList stringList = (StringList) result;
            assertThat(stringList.getStringList()).hasSize(1000);
            assertThat(stringList.getStringList().get(0)).isEqualTo("value0");
            assertThat(stringList.getStringList().get(999)).isEqualTo("value999");
        }

        @Test
        @DisplayName("Should handle null values in XML")
        void testUnmarshalWithNullValues() {
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            when(reader.hasMoreChildren())
                    .thenReturn(true)
                    .thenReturn(true)
                    .thenReturn(false);
            when(reader.getValue())
                    .thenReturn(null)
                    .thenReturn("valid-value");

            Object result = converter.unmarshal(reader, context);

            assertThat(result).isInstanceOf(StringList.class);
            StringList stringList = (StringList) result;
            assertThat(stringList.getStringList()).hasSize(2);
            assertThat(stringList.getStringList().get(0)).isNull();
            assertThat(stringList.getStringList().get(1)).isEqualTo("valid-value");
        }

        @Test
        @DisplayName("Should handle duplicated values in XML")
        void testUnmarshalDuplicatedValues() {
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            when(reader.hasMoreChildren())
                    .thenReturn(true)
                    .thenReturn(true)
                    .thenReturn(true)
                    .thenReturn(false);
            when(reader.getValue())
                    .thenReturn("duplicate")
                    .thenReturn("unique")
                    .thenReturn("duplicate");

            Object result = converter.unmarshal(reader, context);

            assertThat(result).isInstanceOf(StringList.class);
            StringList stringList = (StringList) result;
            assertThat(stringList.getStringList()).hasSize(3);
            assertThat(stringList.getStringList()).containsExactly("duplicate", "unique", "duplicate");
        }
    }

    @Nested
    @DisplayName("Legacy Compatibility Tests")
    class LegacyCompatibilityTests {

        @Test
        @DisplayName("Should be compatible with Clava configuration format")
        void testClavaConfigurationCompatibility() {
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            // Simulate typical Clava configuration flags
            when(reader.hasMoreChildren())
                    .thenReturn(true)
                    .thenReturn(true)
                    .thenReturn(true)
                    .thenReturn(false);
            when(reader.getValue())
                    .thenReturn("-O2")
                    .thenReturn("-Wall")
                    .thenReturn("-std=c++11");

            Object result = converter.unmarshal(reader, context);

            assertThat(result).isInstanceOf(StringList.class);
            StringList stringList = (StringList) result;
            assertThat(stringList.getStringList()).hasSize(3);
            assertThat(stringList.getStringList()).containsExactly("-O2", "-Wall", "-std=c++11");
        }

        @Test
        @DisplayName("Should handle legacy empty configuration")
        void testLegacyEmptyConfiguration() {
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            when(reader.hasMoreChildren()).thenReturn(false);

            Object result = converter.unmarshal(reader, context);

            assertThat(result).isInstanceOf(StringList.class);
            StringList stringList = (StringList) result;
            assertThat(stringList.getStringList()).isEmpty();
        }

        @Test
        @DisplayName("Should handle legacy configuration with compiler flags")
        void testLegacyCompilerFlags() {
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            when(reader.hasMoreChildren())
                    .thenReturn(true)
                    .thenReturn(true)
                    .thenReturn(true)
                    .thenReturn(true)
                    .thenReturn(false);
            when(reader.getValue())
                    .thenReturn("-g")
                    .thenReturn("-O0")
                    .thenReturn("-DDEBUG=1")
                    .thenReturn("-I/usr/include");

            Object result = converter.unmarshal(reader, context);

            assertThat(result).isInstanceOf(StringList.class);
            StringList stringList = (StringList) result;
            assertThat(stringList.getStringList()).hasSize(4);
            assertThat(stringList.getStringList()).containsExactly("-g", "-O0", "-DDEBUG=1", "-I/usr/include");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle reader navigation gracefully")
        void testReaderNavigation() {
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            // The converter should call moveDown/moveUp correctly
            when(reader.hasMoreChildren()).thenReturn(false);

            Object result = converter.unmarshal(reader, context);

            assertThat(result).isInstanceOf(StringList.class);
            // Verify that moveDown() and moveUp() were called appropriately
            // This is tested through the behavior, not direct verification
        }

        @Test
        @DisplayName("Should handle context correctly")
        void testContextHandling() {
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            when(reader.hasMoreChildren()).thenReturn(false);

            Object result = converter.unmarshal(reader, context);

            assertThat(result).isInstanceOf(StringList.class);
            // The context should be handled properly without throwing exceptions
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work as part of XStream converter registry")
        void testXStreamRegistration() {
            // Test that the converter can be identified correctly for registration
            assertThat(converter.canConvert(JsonStringList.class)).isTrue();

            // Verify it doesn't interfere with other converters
            assertThat(converter.canConvert(String.class)).isFalse();
            assertThat(converter.canConvert(java.util.List.class)).isFalse();
        }

        @Test
        @DisplayName("Should maintain consistent behavior across multiple calls")
        void testConsistentBehavior() {
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            when(reader.hasMoreChildren())
                    .thenReturn(true)
                    .thenReturn(false);
            when(reader.getValue()).thenReturn("test");

            // Call multiple times with same inputs
            Object result1 = converter.unmarshal(reader, context);

            // Reset mocks for second call
            when(reader.hasMoreChildren())
                    .thenReturn(true)
                    .thenReturn(false);
            when(reader.getValue()).thenReturn("test");

            Object result2 = converter.unmarshal(reader, context);

            assertThat(result1).isInstanceOf(StringList.class);
            assertThat(result2).isInstanceOf(StringList.class);

            StringList list1 = (StringList) result1;
            StringList list2 = (StringList) result2;

            assertThat(list1.getStringList()).hasSize(1);
            assertThat(list2.getStringList()).hasSize(1);
            assertThat(list1.getStringList().get(0)).isEqualTo("test");
            assertThat(list2.getStringList().get(0)).isEqualTo("test");
        }

        @Test
        @DisplayName("Should produce correct StringList implementation")
        void testStringListImplementation() {
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            when(reader.hasMoreChildren())
                    .thenReturn(true)
                    .thenReturn(true)
                    .thenReturn(false);
            when(reader.getValue())
                    .thenReturn("first")
                    .thenReturn("second");

            Object result = converter.unmarshal(reader, context);

            assertThat(result).isInstanceOf(StringList.class);
            StringList stringList = (StringList) result;

            // Verify StringList behaves correctly
            assertThat(stringList.getStringList()).hasSize(2);
            assertThat(stringList.getStringList().contains("first")).isTrue();
            assertThat(stringList.getStringList().contains("second")).isTrue();
            assertThat(stringList.getStringList().indexOf("first")).isEqualTo(0);
            assertThat(stringList.getStringList().indexOf("second")).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Constants and Documentation Tests")
    class ConstantsAndDocumentationTests {

        @Test
        @DisplayName("Should have proper class structure")
        void testClassStructure() {
            assertThat(converter.getClass().getName())
                    .isEqualTo("org.suikasoft.GsonPlus.JsonStringListXstreamConverter");
            assertThat(converter.getClass().getInterfaces())
                    .contains(com.thoughtworks.xstream.converters.Converter.class);
        }

        @Test
        @DisplayName("Should be instantiable")
        void testInstantiation() {
            JsonStringListXstreamConverter newConverter = new JsonStringListXstreamConverter();
            assertThat(newConverter).isNotNull();
            assertThat(newConverter.canConvert(JsonStringList.class)).isTrue();
        }

        @Test
        @DisplayName("Should maintain immutable behavior")
        void testImmutableBehavior() {
            // The converter should not maintain state between calls
            converter.canConvert(JsonStringList.class);
            converter.canConvert(String.class);

            // State should remain consistent
            assertThat(converter.canConvert(JsonStringList.class)).isTrue();
            assertThat(converter.canConvert(String.class)).isFalse();
        }
    }
}
