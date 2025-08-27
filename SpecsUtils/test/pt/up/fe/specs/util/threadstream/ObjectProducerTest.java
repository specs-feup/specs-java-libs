package pt.up.fe.specs.util.threadstream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for the ObjectProducer interface.
 * Tests the interface contract and default method implementations.
 * 
 * @author Generated Tests
 */
@DisplayName("ObjectProducer Tests")
public class ObjectProducerTest {

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Should implement AutoCloseable")
        void testImplementsAutoCloseable() {
            try (var producer = new TestObjectProducer()) {
                // Given/Then
                assertThat(producer).isInstanceOf(AutoCloseable.class);
            } catch (Exception e) {
                // Ignore close exceptions in tests
            }
        }

        @Test
        @DisplayName("Should have default getPoison method")
        void testDefaultGetPoisonMethod() {
            try (var producer = new TestObjectProducer()) {
                // When
                var poison = producer.getPoison();

                // Then
                assertThat(poison).isNull(); // Default implementation returns null
            } catch (Exception e) {
                // Ignore close exceptions in tests
            }
        }
    }

    @Nested
    @DisplayName("Default Method Implementation Tests")
    class DefaultMethodTests {

        @Test
        @DisplayName("Should allow overriding getPoison method")
        void testOverrideGetPoison() {
            try (var producer = new CustomPoisonProducer()) {
                // When
                var poison = producer.getPoison();

                // Then
                assertThat(poison).isEqualTo("CUSTOM_POISON");
            } catch (Exception e) {
                // Ignore close exceptions in tests
            }
        }

        @Test
        @DisplayName("Should support different poison types")
        void testDifferentPoisonTypes() {
            try (var stringProducer = new StringPoisonProducer();
                    var integerProducer = new IntegerPoisonProducer()) {

                // When
                var stringPoison = stringProducer.getPoison();
                var integerPoison = integerProducer.getPoison();

                // Then
                assertThat(stringPoison).isEqualTo("END");
                assertThat(integerPoison).isEqualTo(-1);
            } catch (Exception e) {
                // Ignore close exceptions in tests
            }
        }
    }

    @Nested
    @DisplayName("Concrete Implementation Tests")
    class ConcreteImplementationTests {

        @Test
        @DisplayName("Should allow custom close implementation")
        void testCustomCloseImplementation() {
            // Given
            var producer = new TrackingCloseProducer();
            assertThat(producer.isClosed()).isFalse();

            // When
            assertThatCode(() -> producer.close()).doesNotThrowAnyException();

            // Then
            assertThat(producer.isClosed()).isTrue();
        }

        @Test
        @DisplayName("Should handle close exceptions gracefully")
        void testCloseWithException() {
            // Given
            var producer = new ExceptionThrowingProducer();

            // When/Then
            assertThatThrownBy(() -> producer.close())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Close failed");
        }
    }

    @Nested
    @DisplayName("Type Safety Tests")
    class TypeSafetyTests {

        @Test
        @DisplayName("Should handle generic types correctly")
        void testGenericTypes() {
            try (var stringProducer = new GenericTestProducer<String>("string_poison");
                    var integerProducer = new GenericTestProducer<Integer>(999)) {

                // When
                var stringPoison = stringProducer.getPoison();
                var integerPoison = integerProducer.getPoison();

                // Then
                assertThat(stringPoison).isInstanceOf(String.class).isEqualTo("string_poison");
                assertThat(integerPoison).isInstanceOf(Integer.class).isEqualTo(999);
            } catch (Exception e) {
                // Ignore close exceptions in tests
            }
        }
    }

    // Test implementations
    private static class TestObjectProducer implements ObjectProducer<String> {
        @Override
        public void close() throws Exception {
            // Simple test implementation
        }
    }

    private static class CustomPoisonProducer implements ObjectProducer<String> {
        @Override
        public String getPoison() {
            return "CUSTOM_POISON";
        }

        @Override
        public void close() throws Exception {
            // Simple test implementation
        }
    }

    private static class StringPoisonProducer implements ObjectProducer<String> {
        @Override
        public String getPoison() {
            return "END";
        }

        @Override
        public void close() throws Exception {
            // Simple test implementation
        }
    }

    private static class IntegerPoisonProducer implements ObjectProducer<Integer> {
        @Override
        public Integer getPoison() {
            return -1;
        }

        @Override
        public void close() throws Exception {
            // Simple test implementation
        }
    }

    private static class TrackingCloseProducer implements ObjectProducer<String> {
        private boolean closed = false;

        @Override
        public void close() throws Exception {
            this.closed = true;
        }

        public boolean isClosed() {
            return closed;
        }
    }

    private static class ExceptionThrowingProducer implements ObjectProducer<String> {
        @Override
        public void close() throws Exception {
            throw new RuntimeException("Close failed");
        }
    }

    private static class GenericTestProducer<T> implements ObjectProducer<T> {
        private final T poison;

        public GenericTestProducer(T poison) {
            this.poison = poison;
        }

        @Override
        public T getPoison() {
            return poison;
        }

        @Override
        public void close() throws Exception {
            // Simple test implementation
        }
    }
}
