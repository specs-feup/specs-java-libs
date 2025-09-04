package pt.up.fe.specs.util.function;

import static org.assertj.core.api.Assertions.*;

import java.io.*;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link SerializableSupplier}.
 * 
 * Tests that the interface properly extends both Supplier and Serializable,
 * and that implementations can be serialized and deserialized correctly.
 * 
 * @author Generated Tests
 */
@DisplayName("SerializableSupplier Tests")
class SerializableSupplierTest {

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Should extend Supplier interface")
        void testExtendsSupplier() {
            assertThat(Supplier.class).isAssignableFrom(SerializableSupplier.class);
        }

        @Test
        @DisplayName("Should extend Serializable interface")
        void testExtendsSerializable() {
            assertThat(Serializable.class).isAssignableFrom(SerializableSupplier.class);
        }

        @Test
        @DisplayName("Should be a functional interface")
        void testIsFunctionalInterface() {
            // SerializableSupplier should have only one abstract method (get() from
            // Supplier)
            // This is verified by the compiler if it's properly annotated with
            // @FunctionalInterface or can be used as a lambda target
            SerializableSupplier<String> supplier = () -> "test";
            assertThat(supplier).isNotNull();
            assertThat(supplier.get()).isEqualTo("test");
        }
    }

    @Nested
    @DisplayName("Serialization Tests")
    class SerializationTests {

        @Test
        @DisplayName("Should serialize and deserialize string supplier")
        @SuppressWarnings("unchecked")
        void testSerializeStringSupplier() throws Exception {
            SerializableSupplier<String> originalSupplier = () -> "Hello World";

            // Serialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(originalSupplier);
            }

            // Deserialize
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            SerializableSupplier<String> deserializedSupplier;
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                deserializedSupplier = (SerializableSupplier<String>) ois.readObject();
            }

            // Verify functionality is preserved
            assertThat(deserializedSupplier.get()).isEqualTo("Hello World");
        }

        @Test
        @DisplayName("Should serialize and deserialize integer supplier")
        @SuppressWarnings("unchecked")
        void testSerializeIntegerSupplier() throws Exception {
            SerializableSupplier<Integer> originalSupplier = () -> 42;

            // Serialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(originalSupplier);
            }

            // Deserialize
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            SerializableSupplier<Integer> deserializedSupplier;
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                deserializedSupplier = (SerializableSupplier<Integer>) ois.readObject();
            }

            // Verify functionality is preserved
            assertThat(deserializedSupplier.get()).isEqualTo(42);
        }

        @Test
        @DisplayName("Should serialize supplier with captured variables")
        @SuppressWarnings("unchecked")
        void testSerializeSupplierWithCapturedVariables() throws Exception {
            String capturedValue = "captured";
            SerializableSupplier<String> originalSupplier = () -> "Prefix: " + capturedValue;

            // Serialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(originalSupplier);
            }

            // Deserialize
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            SerializableSupplier<String> deserializedSupplier;
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                deserializedSupplier = (SerializableSupplier<String>) ois.readObject();
            }

            // Verify functionality is preserved
            assertThat(deserializedSupplier.get()).isEqualTo("Prefix: captured");
        }

        @Test
        @DisplayName("Should serialize supplier returning null")
        @SuppressWarnings("unchecked")
        void testSerializeNullReturningSupplier() throws Exception {
            SerializableSupplier<String> originalSupplier = () -> null;

            // Serialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(originalSupplier);
            }

            // Deserialize
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            SerializableSupplier<String> deserializedSupplier;
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                deserializedSupplier = (SerializableSupplier<String>) ois.readObject();
            }

            // Verify functionality is preserved
            assertThat(deserializedSupplier.get()).isNull();
        }
    }

    @Nested
    @DisplayName("Functional Interface Tests")
    class FunctionalInterfaceTests {

        @Test
        @DisplayName("Should work as lambda expression")
        void testAsLambdaExpression() {
            SerializableSupplier<String> supplier = () -> "lambda result";
            assertThat(supplier.get()).isEqualTo("lambda result");
        }

        @Test
        @DisplayName("Should work as method reference")
        void testAsMethodReference() {
            SerializableSupplier<String> supplier = this::getTestValue;
            assertThat(supplier.get()).isEqualTo("method reference result");
        }

        @Test
        @DisplayName("Should work with anonymous class")
        void testAsAnonymousClass() {
            SerializableSupplier<String> supplier = new SerializableSupplier<String>() {
                @Override
                public String get() {
                    return "anonymous class result";
                }
            };
            assertThat(supplier.get()).isEqualTo("anonymous class result");
        }

        @Test
        @DisplayName("Should be assignable to Supplier")
        void testAssignableToSupplier() {
            SerializableSupplier<String> serializableSupplier = () -> "test";
            Supplier<String> supplier = serializableSupplier;
            assertThat(supplier.get()).isEqualTo("test");
        }

        private String getTestValue() {
            return "method reference result";
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle complex object serialization")
        @SuppressWarnings("unchecked")
        void testComplexObjectSerialization() throws Exception {
            SerializableSupplier<java.util.Date> originalSupplier = () -> new java.util.Date(1000000000L);

            // Serialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(originalSupplier);
            }

            // Deserialize
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            SerializableSupplier<java.util.Date> deserializedSupplier;
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                deserializedSupplier = (SerializableSupplier<java.util.Date>) ois.readObject();
            }

            // Verify functionality is preserved
            assertThat(deserializedSupplier.get()).isEqualTo(new java.util.Date(1000000000L));
        }

        @Test
        @DisplayName("Should handle supplier that throws exception")
        @SuppressWarnings("unchecked")
        void testSupplierThrowingException() throws Exception {
            SerializableSupplier<String> originalSupplier = () -> {
                throw new RuntimeException("Test exception");
            };

            // Serialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(originalSupplier);
            }

            // Deserialize
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            SerializableSupplier<String> deserializedSupplier;
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                deserializedSupplier = (SerializableSupplier<String>) ois.readObject();
            }

            // Verify exception behavior is preserved
            assertThatThrownBy(deserializedSupplier::get)
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Test exception");
        }

        private static class StatefulSupplier implements SerializableSupplier<Integer> {
            private static final long serialVersionUID = 1L;
            private int counter = 0;

            @Override
            public Integer get() {
                return ++counter;
            }
        }

        @Test
        @DisplayName("Should handle stateful supplier")
        @SuppressWarnings("unchecked")
        void testStatefulSupplier() throws Exception {
            SerializableSupplier<Integer> originalSupplier = new StatefulSupplier();

            // Use supplier to change state
            assertThat(originalSupplier.get()).isEqualTo(1);
            assertThat(originalSupplier.get()).isEqualTo(2);

            // Serialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(originalSupplier);
            }

            // Deserialize
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            SerializableSupplier<Integer> deserializedSupplier;
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                deserializedSupplier = (SerializableSupplier<Integer>) ois.readObject();
            }

            // Verify state is preserved
            assertThat(deserializedSupplier.get()).isEqualTo(3);
            assertThat(deserializedSupplier.get()).isEqualTo(4);
        }
    }
}
