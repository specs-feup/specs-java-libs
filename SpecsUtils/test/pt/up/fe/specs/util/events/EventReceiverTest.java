package pt.up.fe.specs.util.events;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for EventReceiver interface and its implementations.
 * 
 * @author Generated Tests
 */
@DisplayName("EventReceiver")
class EventReceiverTest {

    private TestEventId testEventId1;
    private TestEventId testEventId2;
    private Event testEvent1;
    private Event testEvent2;

    @BeforeEach
    void setUp() {
        testEventId1 = new TestEventId("TEST_EVENT_1");
        testEventId2 = new TestEventId("TEST_EVENT_2");
        testEvent1 = new SimpleEvent(testEventId1, "data1");
        testEvent2 = new SimpleEvent(testEventId2, "data2");
    }

    @Nested
    @DisplayName("Interface Contract")
    class InterfaceContract {

        @Test
        @DisplayName("should have correct interface methods")
        void shouldHaveCorrectInterfaceMethods() {
            assertThatCode(() -> {
                EventReceiver.class.getMethod("acceptEvent", Event.class);
                EventReceiver.class.getMethod("getSupportedEvents");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should allow implementations to define supported events")
        void shouldAllowImplementationsToDefineSupportedEvents() {
            EventReceiver receiver = new TestEventReceiver(Arrays.asList(testEventId1, testEventId2));

            Collection<EventId> supportedEvents = receiver.getSupportedEvents();

            assertThat(supportedEvents)
                    .isNotNull()
                    .hasSize(2)
                    .containsExactlyInAnyOrder(testEventId1, testEventId2);
        }

        @Test
        @DisplayName("should allow implementations to accept events")
        void shouldAllowImplementationsToAcceptEvents() {
            TestEventReceiver receiver = new TestEventReceiver(Arrays.asList(testEventId1));

            assertThatCode(() -> receiver.acceptEvent(testEvent1))
                    .doesNotThrowAnyException();

            assertThat(receiver.getReceivedEvents())
                    .hasSize(1)
                    .containsExactly(testEvent1);
        }
    }

    @Nested
    @DisplayName("Implementation Behavior")
    class ImplementationBehavior {

        @Test
        @DisplayName("should handle empty supported events collection")
        void shouldHandleEmptySupportedEventsCollection() {
            EventReceiver receiver = new TestEventReceiver(Collections.emptyList());

            Collection<EventId> supportedEvents = receiver.getSupportedEvents();

            assertThat(supportedEvents)
                    .isNotNull()
                    .isEmpty();
        }

        @Test
        @DisplayName("should handle null event gracefully")
        void shouldHandleNullEventGracefully() {
            TestEventReceiver receiver = new TestEventReceiver(Arrays.asList(testEventId1));

            assertThatCode(() -> receiver.acceptEvent(null))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should handle events not in supported list")
        void shouldHandleEventsNotInSupportedList() {
            TestEventReceiver receiver = new TestEventReceiver(Arrays.asList(testEventId1));

            assertThatCode(() -> receiver.acceptEvent(testEvent2))
                    .doesNotThrowAnyException();

            assertThat(receiver.getReceivedEvents())
                    .hasSize(1)
                    .containsExactly(testEvent2);
        }

        @Test
        @DisplayName("should allow multiple event processing")
        void shouldAllowMultipleEventProcessing() {
            TestEventReceiver receiver = new TestEventReceiver(Arrays.asList(testEventId1, testEventId2));

            receiver.acceptEvent(testEvent1);
            receiver.acceptEvent(testEvent2);
            receiver.acceptEvent(testEvent1);

            assertThat(receiver.getReceivedEvents())
                    .hasSize(3)
                    .containsExactly(testEvent1, testEvent2, testEvent1);
        }
    }

    @Nested
    @DisplayName("Polymorphism")
    class Polymorphism {

        @Test
        @DisplayName("should work with different implementations")
        void shouldWorkWithDifferentImplementations() {
            EventReceiver testReceiver = new TestEventReceiver(Arrays.asList(testEventId1));
            EventReceiver mockReceiver = mock(EventReceiver.class);
            when(mockReceiver.getSupportedEvents()).thenReturn(Arrays.asList(testEventId2));

            assertThat(testReceiver.getSupportedEvents()).containsExactly(testEventId1);
            assertThat(mockReceiver.getSupportedEvents()).containsExactly(testEventId2);
        }

        @Test
        @DisplayName("should support interface-based programming")
        void shouldSupportInterfaceBasedProgramming() {
            List<EventReceiver> receivers = Arrays.asList(
                    new TestEventReceiver(Arrays.asList(testEventId1)),
                    new TestEventReceiver(Arrays.asList(testEventId2)));

            for (EventReceiver receiver : receivers) {
                assertThat(receiver.getSupportedEvents()).isNotEmpty();
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle receiver with null supported events")
        void shouldHandleReceiverWithNullSupportedEvents() {
            EventReceiver receiver = new EventReceiver() {
                @Override
                public void acceptEvent(Event event) {
                    // Do nothing
                }

                @Override
                public Collection<EventId> getSupportedEvents() {
                    return null;
                }
            };

            assertThat(receiver.getSupportedEvents()).isNull();
        }

        @Test
        @DisplayName("should allow immutable supported events collection")
        void shouldAllowImmutableSupportedEventsCollection() {
            EventReceiver receiver = new TestEventReceiver(Arrays.asList(testEventId1));
            Collection<EventId> supportedEvents = receiver.getSupportedEvents();

            assertThatCode(() -> {
                // Should not modify the original collection
                supportedEvents.size();
            }).doesNotThrowAnyException();
        }
    }

    /**
     * Test implementation of EventReceiver for testing purposes.
     */
    private static class TestEventReceiver implements EventReceiver {
        private final Collection<EventId> supportedEvents;
        private final List<Event> receivedEvents;

        public TestEventReceiver(Collection<EventId> supportedEvents) {
            this.supportedEvents = supportedEvents;
            this.receivedEvents = new java.util.ArrayList<>();
        }

        @Override
        public void acceptEvent(Event event) {
            receivedEvents.add(event);
        }

        @Override
        public Collection<EventId> getSupportedEvents() {
            return supportedEvents;
        }

        public List<Event> getReceivedEvents() {
            return receivedEvents;
        }
    }

    /**
     * Test implementation of EventId for testing purposes.
     */
    private static class TestEventId implements EventId {
        private final String name;

        public TestEventId(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            TestEventId that = (TestEventId) obj;
            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}
