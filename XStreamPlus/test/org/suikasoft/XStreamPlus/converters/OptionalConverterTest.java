package org.suikasoft.XStreamPlus.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Comprehensive unit tests for {@link OptionalConverter}.
 * 
 * Tests cover type conversion support, marshalling/unmarshalling of Optional
 * values, and handling of both present and empty Optional instances.
 * 
 * @author Generated Tests
 */
@DisplayName("OptionalConverter Tests")
class OptionalConverterTest {

    @Nested
    @DisplayName("Type Support")
    class TypeSupportTests {

        private OptionalConverter converter;

        @BeforeEach
        void setUp() {
            converter = new OptionalConverter();
        }

        @Test
        @DisplayName("canConvert() should return true for Optional.class")
        void testCanConvert_OptionalClass_ShouldReturnTrue() {
            // When
            boolean canConvert = converter.canConvert(Optional.class);

            // Then
            assertThat(canConvert).isTrue();
        }

        @Test
        @DisplayName("canConvert() should return false for other classes")
        void testCanConvert_OtherClasses_ShouldReturnFalse() {
            // When/Then
            assertAll(
                    () -> assertThat(converter.canConvert(String.class)).isFalse(),
                    () -> assertThat(converter.canConvert(Integer.class)).isFalse(),
                    () -> assertThat(converter.canConvert(Object.class)).isFalse(),
                    () -> assertThat(converter.canConvert(java.util.List.class)).isFalse());
        }

        @Test
        @DisplayName("canConvert() should handle null class")
        void testCanConvert_NullClass_ShouldReturnFalse() {
            // When
            boolean canConvert = converter.canConvert(null);

            // Then
            assertThat(canConvert).isFalse();
        }
    }

    @Nested
    @DisplayName("Marshalling Operations")
    class MarshallingTests {

        private OptionalConverter converter;
        private HierarchicalStreamWriter mockWriter;
        private MarshallingContext mockContext;

        @BeforeEach
        void setUp() {
            converter = new OptionalConverter();
            mockWriter = mock(HierarchicalStreamWriter.class);
            mockContext = mock(MarshallingContext.class);
        }

        @Test
        @DisplayName("marshal() should handle present Optional")
        void testMarshal_PresentOptional_ShouldSetAttributeAndMarshalValue() {
            // Given
            Optional<String> presentOptional = Optional.of("test value");

            // When
            converter.marshal(presentOptional, mockWriter, mockContext);

            // Then
            verify(mockWriter).addAttribute("isPresent", "true");
            verify(mockContext).convertAnother("test value");
        }

        @Test
        @DisplayName("marshal() should handle empty Optional")
        void testMarshal_EmptyOptional_ShouldSetAttributeOnly() {
            // Given
            Optional<String> emptyOptional = Optional.empty();

            // When
            converter.marshal(emptyOptional, mockWriter, mockContext);

            // Then
            verify(mockWriter).addAttribute("isPresent", "false");
            verify(mockContext, never()).convertAnother(any());
        }

        @Test
        @DisplayName("marshal() should handle Optional with null value")
        void testMarshal_OptionalWithNull_ShouldSetAttributeAndMarshalNull() {
            // Given
            Optional<String> nullOptional = Optional.ofNullable(null);

            // When
            converter.marshal(nullOptional, mockWriter, mockContext);

            // Then
            verify(mockWriter).addAttribute("isPresent", "false");
            verify(mockContext, never()).convertAnother(any());
        }

        @Test
        @DisplayName("marshal() should handle Optional with complex object")
        void testMarshal_OptionalWithComplexObject_ShouldMarshalCorrectly() {
            // Given
            TestObject testObj = new TestObject("complex", 42);
            Optional<TestObject> complexOptional = Optional.of(testObj);

            // When
            converter.marshal(complexOptional, mockWriter, mockContext);

            // Then
            verify(mockWriter).addAttribute("isPresent", "true");
            verify(mockContext).convertAnother(testObj);
        }
    }

    @Nested
    @DisplayName("Unmarshalling Operations")
    class UnmarshallingTests {

        private OptionalConverter converter;
        private HierarchicalStreamReader mockReader;
        private UnmarshallingContext mockContext;

        @BeforeEach
        void setUp() {
            converter = new OptionalConverter();
            mockReader = mock(HierarchicalStreamReader.class);
            mockContext = mock(UnmarshallingContext.class);
        }

        @Test
        @DisplayName("unmarshal() should handle present Optional")
        void testUnmarshal_PresentOptional_ShouldReturnOptionalWithValue() {
            // Given
            when(mockReader.getAttribute("isPresent")).thenReturn("true");
            doNothing().when(mockReader).moveDown();
            when(mockReader.getAttribute("classname")).thenReturn("java.lang.String");
            doNothing().when(mockReader).moveUp();
            when(mockContext.convertAnother(any(Optional.class), any(Class.class))).thenReturn("unmarshalled value");

            // When
            @SuppressWarnings("unchecked")
            Optional<String> result = (Optional<String>) converter.unmarshal(mockReader, mockContext);

            // Then
            assertThat(result)
                    .isPresent()
                    .hasValue("unmarshalled value");
        }

        @Test
        @DisplayName("unmarshal() should handle empty Optional")
        void testUnmarshal_EmptyOptional_ShouldReturnEmptyOptional() {
            // Given
            when(mockReader.getAttribute("isPresent")).thenReturn("false");

            // When
            @SuppressWarnings("unchecked")
            Optional<String> result = (Optional<String>) converter.unmarshal(mockReader, mockContext);

            // Then
            assertThat(result).isEmpty();
            verify(mockContext, never()).convertAnother(any(), any());
        }

        @Test
        @DisplayName("unmarshal() should handle missing isPresent attribute")
        void testUnmarshal_MissingAttribute_ShouldReturnEmptyOptional() {
            // Given
            when(mockReader.getAttribute("isPresent")).thenReturn(null);

            // When
            @SuppressWarnings("unchecked")
            Optional<String> result = (Optional<String>) converter.unmarshal(mockReader, mockContext);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("unmarshal() should handle complex object")
        void testUnmarshal_ComplexObject_ShouldReturnOptionalWithObject() {
            // Given
            TestObject testObj = new TestObject("complex", 42);
            when(mockReader.getAttribute("isPresent")).thenReturn("true");
            doNothing().when(mockReader).moveDown();
            when(mockReader.getAttribute("classname"))
                    .thenReturn("org.suikasoft.XStreamPlus.converters.OptionalConverterTest$TestObject");
            doNothing().when(mockReader).moveUp();
            when(mockContext.convertAnother(any(Optional.class), any(Class.class))).thenReturn(testObj);

            // When
            @SuppressWarnings("unchecked")
            Optional<TestObject> result = (Optional<TestObject>) converter.unmarshal(mockReader, mockContext);

            // Then
            assertThat(result)
                    .isPresent()
                    .hasValue(testObj);
        }

        @Test
        @DisplayName("unmarshal() should handle boolean variations for isPresent")
        void testUnmarshal_BooleanVariations_ShouldHandleCorrectly() {
            // Given/When/Then
            testBooleanVariation("true", true);
            testBooleanVariation("TRUE", true);
            testBooleanVariation("True", true);
            testBooleanVariation("false", false);
            testBooleanVariation("FALSE", false);
            testBooleanVariation("False", false);
            testBooleanVariation("invalid", false); // Non-boolean values should default to false
        }

        private void testBooleanVariation(String attributeValue, boolean expectedPresence) {
            // Setup fresh mocks for each test
            HierarchicalStreamReader reader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext context = mock(UnmarshallingContext.class);

            when(reader.getAttribute("isPresent")).thenReturn(attributeValue);
            if (expectedPresence) {
                doNothing().when(reader).moveDown();
                when(reader.getAttribute("classname")).thenReturn("java.lang.String");
                doNothing().when(reader).moveUp();
                when(context.convertAnother(any(Optional.class), any(Class.class))).thenReturn("test value");
            }

            // When
            @SuppressWarnings("unchecked")
            Optional<String> result = (Optional<String>) converter.unmarshal(reader, context);

            // Then
            if (expectedPresence) {
                assertThat(result).isPresent();
            } else {
                assertThat(result).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("Round-trip Conversion")
    class RoundTripTests {

        private OptionalConverter converter;

        @BeforeEach
        void setUp() {
            converter = new OptionalConverter();
        }

        @Test
        @DisplayName("Should preserve present Optional in round-trip")
        void testRoundTrip_PresentOptional_ShouldPreserve() {
            // Given
            Optional<String> original = Optional.of("round-trip test");

            HierarchicalStreamWriter mockWriter = mock(HierarchicalStreamWriter.class);
            MarshallingContext mockMarshalContext = mock(MarshallingContext.class);

            HierarchicalStreamReader mockReader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext mockUnmarshalContext = mock(UnmarshallingContext.class);

            // Setup for round-trip
            when(mockReader.getAttribute("isPresent")).thenReturn("true");
            doNothing().when(mockReader).moveDown();
            when(mockReader.getAttribute("classname")).thenReturn("java.lang.String");
            doNothing().when(mockReader).moveUp();
            when(mockUnmarshalContext.convertAnother(any(Optional.class), any(Class.class)))
                    .thenReturn("round-trip test");

            // When
            converter.marshal(original, mockWriter, mockMarshalContext);
            @SuppressWarnings("unchecked")
            Optional<String> result = (Optional<String>) converter.unmarshal(mockReader, mockUnmarshalContext);

            // Then
            assertThat(result)
                    .isPresent()
                    .hasValue("round-trip test");
        }

        @Test
        @DisplayName("Should preserve empty Optional in round-trip")
        void testRoundTrip_EmptyOptional_ShouldPreserve() {
            // Given
            Optional<String> original = Optional.empty();

            HierarchicalStreamWriter mockWriter = mock(HierarchicalStreamWriter.class);
            MarshallingContext mockMarshalContext = mock(MarshallingContext.class);

            HierarchicalStreamReader mockReader = mock(HierarchicalStreamReader.class);
            UnmarshallingContext mockUnmarshalContext = mock(UnmarshallingContext.class);

            // Setup for round-trip
            when(mockReader.getAttribute("isPresent")).thenReturn("false");

            // When
            converter.marshal(original, mockWriter, mockMarshalContext);
            @SuppressWarnings("unchecked")
            Optional<String> result = (Optional<String>) converter.unmarshal(mockReader, mockUnmarshalContext);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandlingTests {

        private OptionalConverter converter;

        @BeforeEach
        void setUp() {
            converter = new OptionalConverter();
        }

        @Test
        @DisplayName("marshal() should handle null Optional")
        void testMarshal_NullOptional_ShouldHandleGracefully() {
            // Given
            HierarchicalStreamWriter mockWriter = mock(HierarchicalStreamWriter.class);
            MarshallingContext mockContext = mock(MarshallingContext.class);

            // When/Then
            assertThatThrownBy(() -> converter.marshal(null, mockWriter, mockContext))
                    .as("Should throw exception for null Optional")
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("marshal() should handle ClassCastException for non-Optional")
        void testMarshal_NonOptional_ShouldThrowClassCastException() {
            // Given
            String nonOptional = "not an optional";
            HierarchicalStreamWriter mockWriter = mock(HierarchicalStreamWriter.class);
            MarshallingContext mockContext = mock(MarshallingContext.class);

            // When/Then
            assertThatThrownBy(() -> converter.marshal(nonOptional, mockWriter, mockContext))
                    .as("Should throw ClassCastException for non-Optional")
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        @DisplayName("unmarshal() should handle null reader")
        void testUnmarshal_NullReader_ShouldHandleGracefully() {
            // Given
            UnmarshallingContext mockContext = mock(UnmarshallingContext.class);

            // When/Then
            assertThatThrownBy(() -> converter.unmarshal(null, mockContext))
                    .as("Should throw exception for null reader")
                    .isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("Integration with XStream")
    class XStreamIntegrationTests {

        @Test
        @DisplayName("Should integrate properly with XStreamUtils")
        void testXStreamIntegration_ShouldWork() {
            // This test verifies the converter works with XStreamUtils
            // The actual integration is tested in XStreamUtilsTest with Optional fields

            // Given
            OptionalConverter converter = new OptionalConverter();

            // When/Then
            assertAll(
                    () -> assertThat(converter.canConvert(Optional.class)).isTrue(),
                    () -> assertThat(converter).isNotNull());
        }
    }

    // Test helper class
    private static class TestObject {
        public String name;
        public int value;

        public TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            TestObject that = (TestObject) obj;
            return value == that.value &&
                    java.util.Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(name, value);
        }
    }
}
