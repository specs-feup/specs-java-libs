package pt.up.fe.specs.util.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link Event}.
 * 
 * Tests the base event interface which all events must implement.
 * 
 * @author Generated Tests
 */
@DisplayName("Event")
class EventTest {

    @Nested
    @DisplayName("Interface Contract")
    class InterfaceContract {

        @Test
        @DisplayName("should be a functional interface with required methods")
        void shouldBeFunctionalInterfaceWithRequiredMethods() {
            // Event interface should define two methods: getId() and getData()
            assertThat(Event.class.isInterface()).isTrue();
            assertThat(Event.class.getMethods()).hasSize(2);

            // Check method names exist
            assertThatCode(() -> Event.class.getMethod("getId")).doesNotThrowAnyException();
            assertThatCode(() -> Event.class.getMethod("getData")).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should allow implementation by concrete classes")
        void shouldAllowImplementationByConcreteClasses() {
            // Test with a simple implementation
            TestEventId testId = new TestEventId("test");
            String testData = "test data";

            Event event = new Event() {
                @Override
                public EventId getId() {
                    return testId;
                }

                @Override
                public Object getData() {
                    return testData;
                }
            };

            assertThat(event.getId()).isSameAs(testId);
            assertThat(event.getData()).isSameAs(testData);
        }
    }

    @Nested
    @DisplayName("Method Contracts")
    class MethodContracts {

        @Test
        @DisplayName("getId should return EventId instance")
        void getIdShouldReturnEventIdInstance() {
            TestEventId eventId = new TestEventId("test-id");
            Event event = createTestEvent(eventId, "data");

            EventId result = event.getId();

            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(EventId.class);
            assertThat(result).isSameAs(eventId);
        }

        @Test
        @DisplayName("getData should return event data")
        void getDataShouldReturnEventData() {
            String testData = "test data";
            Event event = createTestEvent(new TestEventId("id"), testData);

            Object result = event.getData();

            assertThat(result).isSameAs(testData);
        }

        @Test
        @DisplayName("getData should handle null data")
        void getDataShouldHandleNullData() {
            Event event = createTestEvent(new TestEventId("id"), null);

            Object result = event.getData();

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("Event Implementation Examples")
    class EventImplementationExamples {

        @Test
        @DisplayName("should support string data")
        void shouldSupportStringData() {
            TestEventId eventId = new TestEventId("string-event");
            String data = "Hello, World!";
            Event event = createTestEvent(eventId, data);

            assertThat(event.getId()).isSameAs(eventId);
            assertThat(event.getData()).isEqualTo(data);
            assertThat(event.getData()).isInstanceOf(String.class);
        }

        @Test
        @DisplayName("should support complex object data")
        void shouldSupportComplexObjectData() {
            TestEventId eventId = new TestEventId("complex-event");
            TestData complexData = new TestData("name", 42);
            Event event = createTestEvent(eventId, complexData);

            assertThat(event.getId()).isSameAs(eventId);
            assertThat(event.getData()).isSameAs(complexData);
            assertThat(event.getData()).isInstanceOf(TestData.class);
        }

        @Test
        @DisplayName("should support primitive wrapper data")
        void shouldSupportPrimitiveWrapperData() {
            TestEventId eventId = new TestEventId("number-event");
            Integer numberData = 123;
            Event event = createTestEvent(eventId, numberData);

            assertThat(event.getId()).isSameAs(eventId);
            assertThat(event.getData()).isEqualTo(numberData);
            assertThat(event.getData()).isInstanceOf(Integer.class);
        }
    }

    @Nested
    @DisplayName("Polymorphism")
    class Polymorphism {

        @Test
        @DisplayName("should work with different event implementations")
        void shouldWorkWithDifferentEventImplementations() {
            TestEventId eventId = new TestEventId("poly-test");
            String data = "polymorphism test";

            Event[] events = {
                    createTestEvent(eventId, data),
                    new SimpleEvent(eventId, data)
            };

            for (Event event : events) {
                assertThat(event.getId()).isSameAs(eventId);
                assertThat(event.getData()).isEqualTo(data);
            }
        }

        @Test
        @DisplayName("should allow type-safe casting")
        void shouldAllowTypeSafeCasting() {
            TestEventId eventId = new TestEventId("cast-test");
            String stringData = "cast me";
            Event event = createTestEvent(eventId, stringData);

            // Type-safe casting
            if (event.getData() instanceof String) {
                String castedData = (String) event.getData();
                assertThat(castedData).isEqualTo(stringData);
            }
        }
    }

    // Helper methods and classes
    private Event createTestEvent(EventId id, Object data) {
        return new Event() {
            @Override
            public EventId getId() {
                return id;
            }

            @Override
            public Object getData() {
                return data;
            }
        };
    }

    private static class TestEventId implements EventId {
        private final String name;

        public TestEventId(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "TestEventId{" + name + "}";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof TestEventId))
                return false;
            TestEventId other = (TestEventId) obj;
            return name.equals(other.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    private static class TestData {
        private final String name;
        private final int value;

        public TestData(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return "TestData{name='" + name + "', value=" + value + "}";
        }
    }
}
