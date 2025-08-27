package pt.up.fe.specs.util.system;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for the Copyable interface and its implementations.
 * Tests the self-referential generic interface pattern and copy semantics.
 * 
 * @author Generated Tests
 */
public class CopyableTest {

    /**
     * Test implementation of Copyable for testing purposes.
     * Immutable string wrapper that implements proper copy semantics.
     */
    private static class ImmutableString implements Copyable<ImmutableString> {
        private final String value;

        public ImmutableString(String value) {
            this.value = value == null ? "" : value;
        }

        @Override
        public ImmutableString copy() {
            // For immutable objects, can return same instance
            return this;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof ImmutableString))
                return false;
            return value.equals(((ImmutableString) obj).value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return "ImmutableString{" + value + "}";
        }
    }

    /**
     * Test implementation of Copyable for mutable objects.
     * Demonstrates deep copy semantics.
     */
    private static class MutableContainer implements Copyable<MutableContainer> {
        private StringBuilder content;

        public MutableContainer(String initial) {
            this.content = new StringBuilder(initial == null ? "" : initial);
        }

        @Override
        public MutableContainer copy() {
            // Create new instance with copied content
            return new MutableContainer(content.toString());
        }

        public void append(String text) {
            content.append(text);
        }

        public String getContent() {
            return content.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof MutableContainer))
                return false;
            return content.toString().equals(((MutableContainer) obj).content.toString());
        }

        @Override
        public int hashCode() {
            return content.toString().hashCode();
        }

        @Override
        public String toString() {
            return "MutableContainer{" + content + "}";
        }
    }

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Should define copy method that returns same type")
        void testCopyMethodSignature() {
            ImmutableString original = new ImmutableString("test");

            // The copy method should return the same type
            ImmutableString copied = original.copy();

            assertThat(copied).isNotNull();
            assertThat(copied).isInstanceOf(ImmutableString.class);
        }

        @Test
        @DisplayName("Should support generic self-referential type parameter")
        void testGenericTypeParameter() {
            // This test verifies that the generic type parameter works correctly
            // by ensuring implementations can specify their own type

            ImmutableString string = new ImmutableString("value");
            MutableContainer container = new MutableContainer("content");

            // These should compile without warnings due to proper generic typing
            ImmutableString stringCopy = string.copy();
            MutableContainer containerCopy = container.copy();

            assertThat(stringCopy).isInstanceOf(ImmutableString.class);
            assertThat(containerCopy).isInstanceOf(MutableContainer.class);
        }
    }

    @Nested
    @DisplayName("Immutable Implementation Tests")
    class ImmutableImplementationTests {

        @Test
        @DisplayName("Should handle immutable object copy semantics")
        void testImmutableCopy() {
            ImmutableString original = new ImmutableString("immutable");
            ImmutableString copied = original.copy();

            // For immutable objects, same instance can be returned
            assertSame(original, copied);
            assertThat(copied.getValue()).isEqualTo("immutable");
        }

        @Test
        @DisplayName("Should preserve value in immutable copy")
        void testImmutableValuePreservation() {
            String testValue = "preserve_this_value";
            ImmutableString original = new ImmutableString(testValue);
            ImmutableString copied = original.copy();

            assertThat(copied.getValue()).isEqualTo(testValue);
            assertThat(copied).isEqualTo(original);
        }

        @Test
        @DisplayName("Should handle null value in immutable copy")
        void testImmutableNullValue() {
            ImmutableString original = new ImmutableString(null);
            ImmutableString copied = original.copy();

            assertThat(copied.getValue()).isEmpty();
            assertThat(copied).isEqualTo(original);
        }

        @Test
        @DisplayName("Should handle empty string in immutable copy")
        void testImmutableEmptyString() {
            ImmutableString original = new ImmutableString("");
            ImmutableString copied = original.copy();

            assertThat(copied.getValue()).isEmpty();
            assertThat(copied).isEqualTo(original);
        }
    }

    @Nested
    @DisplayName("Mutable Implementation Tests")
    class MutableImplementationTests {

        @Test
        @DisplayName("Should create independent copy of mutable object")
        void testMutableDeepCopy() {
            MutableContainer original = new MutableContainer("initial");
            MutableContainer copied = original.copy();

            // Should be different instances
            assertNotSame(original, copied);

            // But should have same content initially
            assertThat(copied.getContent()).isEqualTo("initial");
            assertThat(copied).isEqualTo(original);
        }

        @Test
        @DisplayName("Should maintain independence after copy")
        void testMutableIndependence() {
            MutableContainer original = new MutableContainer("base");
            MutableContainer copied = original.copy();

            // Modify original
            original.append("_modified");

            // Copied should remain unchanged
            assertThat(original.getContent()).isEqualTo("base_modified");
            assertThat(copied.getContent()).isEqualTo("base");
            assertThat(copied).isNotEqualTo(original);
        }

        @Test
        @DisplayName("Should handle null content in mutable copy")
        void testMutableNullContent() {
            MutableContainer original = new MutableContainer(null);
            MutableContainer copied = original.copy();

            assertThat(copied.getContent()).isEmpty();
            assertThat(copied).isEqualTo(original);

            // Should still be independent
            original.append("new");
            assertThat(copied.getContent()).isEmpty();
            assertThat(original.getContent()).isEqualTo("new");
        }

        @Test
        @DisplayName("Should preserve all content in mutable copy")
        void testMutableContentPreservation() {
            String complexContent = "Line1\nLine2\tTabbed\nSpecial chars: @#$%^&*()";
            MutableContainer original = new MutableContainer(complexContent);
            MutableContainer copied = original.copy();

            assertThat(copied.getContent()).isEqualTo(complexContent);
            assertThat(copied).isEqualTo(original);
        }
    }

    @Nested
    @DisplayName("Copy Semantics Tests")
    class CopySemanticsTests {

        @Test
        @DisplayName("Should support chained copying")
        void testChainedCopying() {
            MutableContainer original = new MutableContainer("start");
            MutableContainer copy1 = original.copy();
            MutableContainer copy2 = copy1.copy();
            MutableContainer copy3 = copy2.copy();

            // All should have same content
            assertThat(copy1.getContent()).isEqualTo("start");
            assertThat(copy2.getContent()).isEqualTo("start");
            assertThat(copy3.getContent()).isEqualTo("start");

            // All should be different instances
            assertNotSame(original, copy1);
            assertNotSame(copy1, copy2);
            assertNotSame(copy2, copy3);
            assertNotSame(original, copy3);

            // Modifications should remain independent
            copy2.append("_copy2");
            assertThat(original.getContent()).isEqualTo("start");
            assertThat(copy1.getContent()).isEqualTo("start");
            assertThat(copy2.getContent()).isEqualTo("start_copy2");
            assertThat(copy3.getContent()).isEqualTo("start");
        }

        @Test
        @DisplayName("Should handle copy of copy operations")
        void testCopyOfCopy() {
            ImmutableString original = new ImmutableString("original");
            ImmutableString firstCopy = original.copy();
            ImmutableString secondCopy = firstCopy.copy();

            // For immutable objects, all should be same instance
            assertSame(original, firstCopy);
            assertSame(firstCopy, secondCopy);
            assertSame(original, secondCopy);
        }

        @Test
        @DisplayName("Should maintain type safety through copy operations")
        void testTypeSafetyInCopying() {
            // This test ensures that copy() returns the correct type
            // and doesn't break the type system

            ImmutableString stringObj = new ImmutableString("type_safe");
            MutableContainer containerObj = new MutableContainer("type_safe");

            // These assignments should be type-safe
            ImmutableString stringCopy = stringObj.copy();
            MutableContainer containerCopy = containerObj.copy();

            assertThat(stringCopy).isInstanceOf(ImmutableString.class);
            assertThat(containerCopy).isInstanceOf(MutableContainer.class);

            // Should not be able to assign cross-types (compile-time check)
            assertThat(stringCopy.getValue()).isEqualTo("type_safe");
            assertThat(containerCopy.getContent()).isEqualTo("type_safe");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle multiple consecutive copy operations")
        void testMultipleCopyOperations() {
            MutableContainer original = new MutableContainer("multi");

            // Perform multiple copy operations in sequence
            MutableContainer result = original;
            for (int i = 0; i < 10; i++) {
                result = result.copy();
            }

            // Final result should have same content but be different instance
            assertThat(result.getContent()).isEqualTo("multi");
            assertNotSame(original, result);

            // Verify independence
            result.append("_final");
            assertThat(original.getContent()).isEqualTo("multi");
            assertThat(result.getContent()).isEqualTo("multi_final");
        }

        @Test
        @DisplayName("Should handle copy with large content")
        void testCopyWithLargeContent() {
            // Create large content
            StringBuilder largeContent = new StringBuilder();
            for (int i = 0; i < 10000; i++) {
                largeContent.append("Line ").append(i).append("\n");
            }

            MutableContainer original = new MutableContainer(largeContent.toString());
            MutableContainer copied = original.copy();

            assertThat(copied.getContent()).isEqualTo(largeContent.toString());
            assertThat(copied.getContent().length()).isEqualTo(largeContent.length());
            assertNotSame(original, copied);
        }

        @Test
        @DisplayName("Should handle copy with special characters")
        void testCopyWithSpecialCharacters() {
            String specialContent = "Unicode: \u03B1\u03B2\u03B3 Emoji: \uD83D\uDE00 Null: \0 Tab: \t Newline: \n";

            ImmutableString immutable = new ImmutableString(specialContent);
            MutableContainer mutable = new MutableContainer(specialContent);

            ImmutableString immutableCopy = immutable.copy();
            MutableContainer mutableCopy = mutable.copy();

            assertThat(immutableCopy.getValue()).isEqualTo(specialContent);
            assertThat(mutableCopy.getContent()).isEqualTo(specialContent);
        }
    }
}
