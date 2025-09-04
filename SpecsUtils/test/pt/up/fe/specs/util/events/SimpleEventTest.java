package pt.up.fe.specs.util.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link SimpleEvent}.
 * 
 * Tests the basic implementation of the Event interface.
 * 
 * @author Generated Tests
 */
@DisplayName("SimpleEvent")
class SimpleEventTest {

    private TestEventId testEventId;
    private String testData;

    @BeforeEach
    void setUp() {
        testEventId = new TestEventId("test-event");
        testData = "test data";
    }

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create event with event ID and data")
        void shouldCreateEventWithEventIdAndData() {
            SimpleEvent event = new SimpleEvent(testEventId, testData);

            assertThat(event).isNotNull();
            assertThat(event.getId()).isSameAs(testEventId);
            assertThat(event.getData()).isSameAs(testData);
        }

        @Test
        @DisplayName("should create event with null data")
        void shouldCreateEventWithNullData() {
            SimpleEvent event = new SimpleEvent(testEventId, null);

            assertThat(event).isNotNull();
            assertThat(event.getId()).isSameAs(testEventId);
            assertThat(event.getData()).isNull();
        }

        @Test
        @DisplayName("should create event with null event ID")
        void shouldCreateEventWithNullEventId() {
            SimpleEvent event = new SimpleEvent(null, testData);

            assertThat(event).isNotNull();
            assertThat(event.getId()).isNull();
            assertThat(event.getData()).isSameAs(testData);
        }

        @Test
        @DisplayName("should create event with both null parameters")
        void shouldCreateEventWithBothNullParameters() {
            SimpleEvent event = new SimpleEvent(null, null);

            assertThat(event).isNotNull();
            assertThat(event.getId()).isNull();
            assertThat(event.getData()).isNull();
        }
    }

    @Nested
    @DisplayName("Event Interface Implementation")
    class EventInterfaceImplementation {

        @Test
        @DisplayName("should implement Event interface")
        void shouldImplementEventInterface() {
            SimpleEvent event = new SimpleEvent(testEventId, testData);

            assertThat(event).isInstanceOf(Event.class);
        }

        @Test
        @DisplayName("getId should return the constructor event ID")
        void getIdShouldReturnConstructorEventId() {
            SimpleEvent event = new SimpleEvent(testEventId, testData);

            EventId result = event.getId();

            assertThat(result).isSameAs(testEventId);
        }

        @Test
        @DisplayName("getData should return the constructor data")
        void getDataShouldReturnConstructorData() {
            SimpleEvent event = new SimpleEvent(testEventId, testData);

            Object result = event.getData();

            assertThat(result).isSameAs(testData);
        }

        @Test
        @DisplayName("should maintain immutability of stored data")
        void shouldMaintainImmutabilityOfStoredData() {
            SimpleEvent event = new SimpleEvent(testEventId, testData);

            // Multiple calls should return the same references
            assertThat(event.getId()).isSameAs(event.getId());
            assertThat(event.getData()).isSameAs(event.getData());
        }
    }

    @Nested
    @DisplayName("Data Types")
    class DataTypes {

        @Test
        @DisplayName("should handle string data")
        void shouldHandleStringData() {
            String stringData = "Hello, World!";
            SimpleEvent event = new SimpleEvent(testEventId, stringData);

            assertThat(event.getData()).isEqualTo(stringData);
            assertThat(event.getData()).isInstanceOf(String.class);
        }

        @Test
        @DisplayName("should handle numeric data")
        void shouldHandleNumericData() {
            Integer numericData = 42;
            SimpleEvent event = new SimpleEvent(testEventId, numericData);

            assertThat(event.getData()).isEqualTo(numericData);
            assertThat(event.getData()).isInstanceOf(Integer.class);
        }

        @Test
        @DisplayName("should handle complex object data")
        void shouldHandleComplexObjectData() {
            TestData complexData = new TestData("test", 123);
            SimpleEvent event = new SimpleEvent(testEventId, complexData);

            assertThat(event.getData()).isSameAs(complexData);
            assertThat(event.getData()).isInstanceOf(TestData.class);
        }

        @Test
        @DisplayName("should handle collection data")
        void shouldHandleCollectionData() {
            java.util.List<String> listData = java.util.Arrays.asList("a", "b", "c");
            SimpleEvent event = new SimpleEvent(testEventId, listData);

            assertThat(event.getData()).isSameAs(listData);
            assertThat(event.getData()).isInstanceOf(java.util.List.class);
        }

        @Test
        @DisplayName("should handle array data")
        void shouldHandleArrayData() {
            String[] arrayData = { "x", "y", "z" };
            SimpleEvent event = new SimpleEvent(testEventId, arrayData);

            assertThat(event.getData()).isSameAs(arrayData);
            assertThat(event.getData()).isInstanceOf(String[].class);
        }
    }

    @Nested
    @DisplayName("Event ID Types")
    class EventIdTypes {

        @Test
        @DisplayName("should handle custom EventId implementations")
        void shouldHandleCustomEventIdImplementations() {
            TestEventId customId = new TestEventId("custom");
            SimpleEvent event = new SimpleEvent(customId, testData);

            assertThat(event.getId()).isSameAs(customId);
            assertThat(event.getId()).isInstanceOf(EventId.class);
        }

        @Test
        @DisplayName("should handle enum EventId implementations")
        void shouldHandleEnumEventIdImplementations() {
            TestEventEnum enumId = TestEventEnum.TEST_EVENT;
            SimpleEvent event = new SimpleEvent(enumId, testData);

            assertThat(event.getId()).isSameAs(enumId);
            assertThat(event.getId()).isInstanceOf(EventId.class);
            assertThat(event.getId()).isInstanceOf(Enum.class);
        }
    }

    @Nested
    @DisplayName("Immutability")
    class Immutability {

        @Test
        @DisplayName("should be immutable after construction")
        void shouldBeImmutableAfterConstruction() {
            SimpleEvent event = new SimpleEvent(testEventId, testData);

            EventId originalId = event.getId();
            Object originalData = event.getData();

            // Multiple accesses should return same instances
            assertThat(event.getId()).isSameAs(originalId);
            assertThat(event.getData()).isSameAs(originalData);
        }

        @Test
        @DisplayName("should not expose internal state")
        void shouldNotExposeInternalState() {
            // SimpleEvent should not have setters or methods that modify state
            java.lang.reflect.Method[] methods = SimpleEvent.class.getDeclaredMethods();

            for (java.lang.reflect.Method method : methods) {
                // No setter methods should exist
                assertThat(method.getName()).doesNotStartWith("set");
                // Only getId() and getData() should be public
                if (java.lang.reflect.Modifier.isPublic(method.getModifiers())) {
                    assertThat(method.getName()).isIn("getId", "getData");
                }
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle repeated access")
        void shouldHandleRepeatedAccess() {
            SimpleEvent event = new SimpleEvent(testEventId, testData);

            // Call methods multiple times
            for (int i = 0; i < 1000; i++) {
                assertThat(event.getId()).isSameAs(testEventId);
                assertThat(event.getData()).isSameAs(testData);
            }
        }

        @Test
        @DisplayName("should handle concurrent access")
        void shouldHandleConcurrentAccess() throws InterruptedException {
            SimpleEvent event = new SimpleEvent(testEventId, testData);

            java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(10);
            java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(10);

            try {
                for (int i = 0; i < 10; i++) {
                    executor.submit(() -> {
                        try {
                            // Multiple threads accessing the event
                            assertThat(event.getId()).isSameAs(testEventId);
                            assertThat(event.getData()).isSameAs(testData);
                        } finally {
                            latch.countDown();
                        }
                    });
                }

                boolean completed = latch.await(5, java.util.concurrent.TimeUnit.SECONDS);
                assertThat(completed).isTrue();
            } finally {
                executor.shutdown();
            }
        }
    }

    @Nested
    @DisplayName("Polymorphism")
    class Polymorphism {

        @Test
        @DisplayName("should work as Event interface")
        void shouldWorkAsEventInterface() {
            Event event = new SimpleEvent(testEventId, testData);

            assertThat(event.getId()).isSameAs(testEventId);
            assertThat(event.getData()).isSameAs(testData);
        }

        @Test
        @DisplayName("should work in arrays and collections")
        void shouldWorkInArraysAndCollections() {
            SimpleEvent event1 = new SimpleEvent(testEventId, testData);
            SimpleEvent event2 = new SimpleEvent(new TestEventId("other"), "other data");

            Event[] eventArray = { event1, event2 };
            java.util.List<Event> eventList = java.util.Arrays.asList(event1, event2);

            // Test array access
            assertThat(eventArray[0].getId()).isSameAs(testEventId);
            assertThat(eventArray[1].getId()).isInstanceOf(EventId.class);

            // Test collection access
            assertThat(eventList.get(0).getData()).isSameAs(testData);
            assertThat(eventList.get(1).getData()).isEqualTo("other data");
        }
    }

    // Helper classes
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

    private enum TestEventEnum implements EventId {
        TEST_EVENT,
        OTHER_EVENT
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
