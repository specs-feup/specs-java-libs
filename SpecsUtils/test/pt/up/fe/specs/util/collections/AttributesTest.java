package pt.up.fe.specs.util.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link Attributes} interface.
 * Tests generic attribute storage and retrieval functionality.
 * 
 * @author Generated Tests
 */
class AttributesTest {

    private TestAttributes attributes;

    @BeforeEach
    void setUp() {
        attributes = new TestAttributes();
    }

    @Nested
    @DisplayName("Basic Attribute Operations")
    class BasicOperationsTests {

        @Test
        @DisplayName("Should return empty attributes initially")
        void testInitialAttributes() {
            assertThat(attributes.getAttributes()).isEmpty();
        }

        @Test
        @DisplayName("Should add and retrieve string attribute")
        void testStringAttribute() {
            attributes.putObject("name", "John");

            assertThat(attributes.getAttributes()).contains("name");
            assertThat(attributes.getObject("name")).isEqualTo("John");
        }

        @Test
        @DisplayName("Should add and retrieve numeric attribute")
        void testNumericAttribute() {
            attributes.putObject("age", 25);

            assertThat(attributes.getAttributes()).contains("age");
            assertThat(attributes.getObject("age")).isEqualTo(25);
        }

        @Test
        @DisplayName("Should add and retrieve boolean attribute")
        void testBooleanAttribute() {
            attributes.putObject("active", true);

            assertThat(attributes.getAttributes()).contains("active");
            assertThat(attributes.getObject("active")).isEqualTo(true);
        }

        @Test
        @DisplayName("Should handle null values")
        void testNullValue() {
            attributes.putObject("nullValue", null);

            assertThat(attributes.getAttributes()).contains("nullValue");
            assertThat(attributes.getObject("nullValue")).isNull();
        }

        @Test
        @DisplayName("Should replace existing attribute")
        void testReplaceAttribute() {
            attributes.putObject("value", "old");
            Object oldValue = attributes.putObject("value", "new");

            assertThat(oldValue).isEqualTo("old");
            assertThat(attributes.getObject("value")).isEqualTo("new");
            assertThat(attributes.getAttributes()).hasSize(1);
        }

        @Test
        @DisplayName("Should return null for first-time attribute assignment")
        void testFirstTimeAssignment() {
            Object oldValue = attributes.putObject("new", "value");

            assertThat(oldValue).isNull();
            assertThat(attributes.getObject("new")).isEqualTo("value");
        }
    }

    @Nested
    @DisplayName("Attribute Existence Checks")
    class AttributeExistenceTests {

        @Test
        @DisplayName("Should return false for non-existent attribute")
        void testNonExistentAttribute() {
            assertThat(attributes.hasAttribute("nonexistent")).isFalse();
        }

        @Test
        @DisplayName("Should return true for existing attribute")
        void testExistingAttribute() {
            attributes.putObject("exists", "value");

            assertThat(attributes.hasAttribute("exists")).isTrue();
        }

        @Test
        @DisplayName("Should return true for attribute with null value")
        void testAttributeWithNullValue() {
            attributes.putObject("nullAttribute", null);

            assertThat(attributes.hasAttribute("nullAttribute")).isTrue();
        }

        @Test
        @DisplayName("Should handle empty string attribute name")
        void testEmptyStringAttributeName() {
            attributes.putObject("", "value");

            assertThat(attributes.hasAttribute("")).isTrue();
        }
    }

    @Nested
    @DisplayName("Type-Safe Attribute Retrieval")
    class TypeSafeRetrievalTests {

        @Test
        @DisplayName("Should retrieve and cast string attribute")
        void testGetStringAttribute() {
            attributes.putObject("name", "John");

            String name = attributes.getObject("name", String.class);
            assertThat(name).isEqualTo("John");
        }

        @Test
        @DisplayName("Should retrieve and cast integer attribute")
        void testGetIntegerAttribute() {
            attributes.putObject("age", 25);

            Integer age = attributes.getObject("age", Integer.class);
            assertThat(age).isEqualTo(25);
        }

        @Test
        @DisplayName("Should retrieve and cast boolean attribute")
        void testGetBooleanAttribute() {
            attributes.putObject("active", true);

            Boolean active = attributes.getObject("active", Boolean.class);
            assertThat(active).isTrue();
        }

        @Test
        @DisplayName("Should throw ClassCastException for invalid cast")
        void testInvalidCast() {
            attributes.putObject("text", "not a number");

            assertThatThrownBy(() -> attributes.getObject("text", Integer.class))
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        @DisplayName("Should handle null cast correctly")
        void testNullCast() {
            attributes.putObject("nullValue", null);

            String value = attributes.getObject("nullValue", String.class);
            assertThat(value).isNull();
        }
    }

    @Nested
    @DisplayName("List Conversion")
    class ListConversionTests {

        @Test
        @DisplayName("Should convert array to list")
        void testArrayToList() {
            String[] array = { "item1", "item2", "item3" };
            attributes.putObject("array", array);

            List<Object> list = attributes.getObjectAsList("array");
            assertThat(list).containsExactly("item1", "item2", "item3");
        }

        @Test
        @DisplayName("Should convert collection to list")
        void testCollectionToList() {
            Set<String> set = new HashSet<>(Arrays.asList("item1", "item2", "item3"));
            attributes.putObject("set", set);

            List<Object> list = attributes.getObjectAsList("set");
            assertThat(list).hasSize(3);
            assertThat(list).containsExactlyInAnyOrder("item1", "item2", "item3");
        }

        @Test
        @DisplayName("Should convert existing list")
        void testListToList() {
            List<String> originalList = Arrays.asList("item1", "item2", "item3");
            attributes.putObject("list", originalList);

            List<Object> list = attributes.getObjectAsList("list");
            assertThat(list).containsExactly("item1", "item2", "item3");
            assertThat(list).isNotSameAs(originalList); // Should be a new list
        }

        @Test
        @DisplayName("Should throw exception for non-convertible type")
        void testNonConvertibleType() {
            attributes.putObject("string", "not a list");

            assertThatThrownBy(() -> attributes.getObjectAsList("string"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not convert object of class");
        }

        @Test
        @DisplayName("Should convert typed list with element class")
        void testTypedListConversion() {
            String[] array = { "item1", "item2", "item3" };
            attributes.putObject("array", array);

            List<String> list = attributes.getObjectAsList("array", String.class);
            assertThat(list).containsExactly("item1", "item2", "item3");
        }

        @Test
        @DisplayName("Should handle empty array conversion")
        void testEmptyArrayConversion() {
            String[] emptyArray = {};
            attributes.putObject("empty", emptyArray);

            List<Object> list = attributes.getObjectAsList("empty");
            assertThat(list).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty collection conversion")
        void testEmptyCollectionConversion() {
            List<String> emptyList = Collections.emptyList();
            attributes.putObject("empty", emptyList);

            List<Object> list = attributes.getObjectAsList("empty");
            assertThat(list).isEmpty();
        }
    }

    @Nested
    @DisplayName("Optional Attribute Retrieval")
    class OptionalRetrievalTests {

        @Test
        @DisplayName("Should return empty optional for non-existent attribute")
        void testNonExistentOptional() {
            Optional<Object> result = attributes.getOptionalObject("nonexistent");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return present optional for existing attribute")
        void testExistingOptional() {
            attributes.putObject("exists", "value");

            Optional<Object> result = attributes.getOptionalObject("exists");
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo("value");
        }

        @Test
        @DisplayName("Should return empty optional for null value")
        void testNullValueOptional() {
            attributes.putObject("nullValue", null);

            Optional<Object> result = attributes.getOptionalObject("nullValue");
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should work with different value types")
        void testOptionalWithDifferentTypes() {
            attributes.putObject("string", "text");
            attributes.putObject("number", 42);
            attributes.putObject("boolean", true);

            assertThat(attributes.getOptionalObject("string")).contains("text");
            assertThat(attributes.getOptionalObject("number")).contains(42);
            assertThat(attributes.getOptionalObject("boolean")).contains(true);
        }
    }

    @Nested
    @DisplayName("Multiple Attributes Management")
    class MultipleAttributesTests {

        @Test
        @DisplayName("Should handle multiple attributes")
        void testMultipleAttributes() {
            attributes.putObject("name", "John");
            attributes.putObject("age", 30);
            attributes.putObject("active", true);
            attributes.putObject("score", 95.5);

            assertThat(attributes.getAttributes()).hasSize(4);
            assertThat(attributes.getAttributes()).containsExactlyInAnyOrder("name", "age", "active", "score");

            assertThat(attributes.getObject("name")).isEqualTo("John");
            assertThat(attributes.getObject("age")).isEqualTo(30);
            assertThat(attributes.getObject("active")).isEqualTo(true);
            assertThat(attributes.getObject("score")).isEqualTo(95.5);
        }

        @Test
        @DisplayName("Should maintain attribute independence")
        void testAttributeIndependence() {
            attributes.putObject("attr1", "value1");
            attributes.putObject("attr2", "value2");

            attributes.putObject("attr1", "newValue1");

            assertThat(attributes.getObject("attr1")).isEqualTo("newValue1");
            assertThat(attributes.getObject("attr2")).isEqualTo("value2"); // Should remain unchanged
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle complex object types")
        void testComplexObjectTypes() {
            Map<String, Integer> map = new HashMap<>();
            map.put("key1", 1);
            map.put("key2", 2);

            attributes.putObject("map", map);

            @SuppressWarnings("unchecked")
            Map<String, Integer> retrieved = (Map<String, Integer>) attributes.getObject("map");
            assertThat(retrieved).isEqualTo(map);
        }

        @Test
        @DisplayName("Should handle nested collections")
        void testNestedCollections() {
            List<List<String>> nestedList = Arrays.asList(
                    Arrays.asList("a", "b"),
                    Arrays.asList("c", "d"));

            attributes.putObject("nested", nestedList);

            @SuppressWarnings("unchecked")
            List<List<String>> retrieved = (List<List<String>>) attributes.getObject("nested");
            assertThat(retrieved).isEqualTo(nestedList);
        }

        @Test
        @DisplayName("Should handle very long attribute names")
        void testLongAttributeName() {
            String longName = "a".repeat(1000);
            attributes.putObject(longName, "value");

            assertThat(attributes.hasAttribute(longName)).isTrue();
            assertThat(attributes.getObject(longName)).isEqualTo("value");
        }

        @Test
        @DisplayName("Should handle special characters in attribute names")
        void testSpecialCharacters() {
            String specialName = "attr-with_special.chars@123";
            attributes.putObject(specialName, "specialValue");

            assertThat(attributes.hasAttribute(specialName)).isTrue();
            assertThat(attributes.getObject(specialName)).isEqualTo("specialValue");
        }
    }

    /**
     * Simple test implementation of Attributes interface for testing purposes.
     */
    private static class TestAttributes implements Attributes {
        private final Map<String, Object> attributeMap = new HashMap<>();

        @Override
        public Collection<String> getAttributes() {
            return new ArrayList<>(attributeMap.keySet());
        }

        @Override
        public Object getObject(String attribute) {
            if (!hasAttribute(attribute)) {
                throw new RuntimeException("Attribute '" + attribute + "' not found");
            }
            return attributeMap.get(attribute);
        }

        @Override
        public Object putObject(String attribute, Object value) {
            return attributeMap.put(attribute, value);
        }

        @Override
        public boolean hasAttribute(String attribute) {
            return attributeMap.containsKey(attribute);
        }
    }
}
