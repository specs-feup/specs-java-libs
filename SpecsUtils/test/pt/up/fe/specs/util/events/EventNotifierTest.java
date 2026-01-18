package pt.up.fe.specs.util.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Unit tests for {@link EventNotifier}.
 * 
 * Tests the event notification interface implementation and contracts.
 * 
 * @author Generated Tests
 */
@DisplayName("EventNotifier")
class EventNotifierTest {

    @Mock
    private EventReceiver mockReceiver;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Nested
    @DisplayName("Interface Contract")
    class InterfaceContract {

        @Test
        @DisplayName("should be a functional interface")
        void shouldBeAFunctionalInterface() {
            // EventNotifier has only one abstract method: notifyEvent(Event)
            assertThat(EventNotifier.class.isInterface()).isTrue();

            // Check that it can be used as lambda/method reference
            EventNotifier notifier = event -> {
                /* no-op */ };
            assertThat(notifier).isNotNull();
        }

        @Test
        @DisplayName("should have notifyEvent method")
        void shouldHaveNotifyEventMethod() {
            try {
                EventNotifier.class.getMethod("notifyEvent", Event.class);
            } catch (NoSuchMethodException e) {
                fail("EventNotifier should have notifyEvent(Event) method", e);
            }
        }
    }

    @Nested
    @DisplayName("Implementation Examples")
    class ImplementationExamples {

        @Test
        @DisplayName("should work with lambda implementation")
        void shouldWorkWithLambdaImplementation() {
            List<Event> capturedEvents = new ArrayList<>();
            EventNotifier notifier = capturedEvents::add;

            Event testEvent = new SimpleEvent(new TestEventId("test"), "data");
            notifier.notifyEvent(testEvent);

            assertThat(capturedEvents).containsExactly(testEvent);
        }

        @Test
        @DisplayName("should work with method reference implementation")
        void shouldWorkWithMethodReferenceImplementation() {
            List<Event> eventLog = new ArrayList<>();
            EventNotifier notifier = eventLog::add;

            Event event1 = new SimpleEvent(new TestEventId("event1"), "data1");
            Event event2 = new SimpleEvent(new TestEventId("event2"), "data2");

            notifier.notifyEvent(event1);
            notifier.notifyEvent(event2);

            assertThat(eventLog).containsExactly(event1, event2);
        }

        @Test
        @DisplayName("should work with anonymous class implementation")
        void shouldWorkWithAnonymousClassImplementation() {
            List<String> eventNames = new ArrayList<>();
            EventNotifier notifier = new EventNotifier() {
                @Override
                public void notifyEvent(Event event) {
                    eventNames.add(event.getId().toString());
                }
            };

            Event testEvent = new SimpleEvent(new TestEventId("test-event"), "data");
            notifier.notifyEvent(testEvent);

            assertThat(eventNames).containsExactly("TestEventId{test-event}");
        }

        @Test
        @DisplayName("should work with concrete class implementation")
        void shouldWorkWithConcreteClassImplementation() {
            TestEventNotifier notifier = new TestEventNotifier();

            Event testEvent = new SimpleEvent(new TestEventId("test"), "data");
            notifier.notifyEvent(testEvent);

            assertThat(notifier.getLastEvent()).isEqualTo(testEvent);
            assertThat(notifier.getEventCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Notification Behavior")
    class NotificationBehavior {

        @Test
        @DisplayName("should accept any Event implementation")
        void shouldAcceptAnyEventImplementation() {
            List<Event> events = new ArrayList<>();
            EventNotifier notifier = events::add;

            Event simpleEvent = new SimpleEvent(new TestEventId("simple"), "data");
            Event customEvent = new CustomEvent(new TestEventId("custom"), 42);

            notifier.notifyEvent(simpleEvent);
            notifier.notifyEvent(customEvent);

            assertThat(events).containsExactly(simpleEvent, customEvent);
        }

        @Test
        @DisplayName("should handle events with different data types")
        void shouldHandleEventsWithDifferentDataTypes() {
            List<Object> eventData = new ArrayList<>();
            EventNotifier notifier = event -> eventData.add(event.getData());

            Event stringEvent = new SimpleEvent(new TestEventId("string"), "text");
            Event numberEvent = new SimpleEvent(new TestEventId("number"), 123);
            Event objectEvent = new SimpleEvent(new TestEventId("object"), new Object());
            Event nullEvent = new SimpleEvent(new TestEventId("null"), null);

            notifier.notifyEvent(stringEvent);
            notifier.notifyEvent(numberEvent);
            notifier.notifyEvent(objectEvent);
            notifier.notifyEvent(nullEvent);

            assertThat(eventData).hasSize(4);
            assertThat(eventData.get(0)).isEqualTo("text");
            assertThat(eventData.get(1)).isEqualTo(123);
            assertThat(eventData.get(2)).isNotNull();
            assertThat(eventData.get(3)).isNull();
        }

        @Test
        @DisplayName("should be able to delegate to receiver")
        void shouldBeAbleToDelegateToReceiver() {
            EventNotifier notifier = mockReceiver::acceptEvent;

            Event testEvent = new SimpleEvent(new TestEventId("test"), "data");
            notifier.notifyEvent(testEvent);

            verify(mockReceiver).acceptEvent(testEvent);
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandling {

        @Test
        @DisplayName("should handle null events according to implementation")
        void shouldHandleNullEventsAccordingToImplementation() {
            List<Event> events = new ArrayList<>();
            EventNotifier safeNotifier = event -> {
                if (event != null) {
                    events.add(event);
                }
            };

            // Example of an unsafe implementation that does not accept null
            EventNotifier unsafeNotifier = event -> {
                // Will throw NullPointerException if event is null
                event.getId();
                events.add(event);
            };

            // Safe notifier should handle null gracefully
            assertThatCode(() -> safeNotifier.notifyEvent(null))
                    .doesNotThrowAnyException();
            assertThat(events).isEmpty();

            // Unsafe notifier might throw
            assertThatThrownBy(() -> unsafeNotifier.notifyEvent(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("should allow implementations to throw exceptions")
        void shouldAllowImplementationsToThrowExceptions() {
            EventNotifier throwingNotifier = event -> {
                throw new RuntimeException("Notification failed");
            };

            Event testEvent = new SimpleEvent(new TestEventId("test"), "data");

            assertThatThrownBy(() -> throwingNotifier.notifyEvent(testEvent))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Notification failed");
        }
    }

    @Nested
    @DisplayName("Composition and Chaining")
    class CompositionAndChaining {

        @Test
        @DisplayName("should support notifier composition")
        void shouldSupportNotifierComposition() {
            List<String> log1 = new ArrayList<>();
            List<String> log2 = new ArrayList<>();

            EventNotifier notifier1 = event -> log1.add("Notifier1: " + event.getId());
            EventNotifier notifier2 = event -> log2.add("Notifier2: " + event.getId());

            EventNotifier compositeNotifier = event -> {
                notifier1.notifyEvent(event);
                notifier2.notifyEvent(event);
            };

            Event testEvent = new SimpleEvent(new TestEventId("test"), "data");
            compositeNotifier.notifyEvent(testEvent);

            assertThat(log1).containsExactly("Notifier1: TestEventId{test}");
            assertThat(log2).containsExactly("Notifier2: TestEventId{test}");
        }

        @Test
        @DisplayName("should support conditional notification")
        void shouldSupportConditionalNotification() {
            List<Event> importantEvents = new ArrayList<>();
            EventNotifier conditionalNotifier = event -> {
                if (event.getId().toString().contains("important")) {
                    importantEvents.add(event);
                }
            };

            Event importantEvent = new SimpleEvent(new TestEventId("important-update"), "data");
            Event normalEvent = new SimpleEvent(new TestEventId("normal-update"), "data");

            conditionalNotifier.notifyEvent(importantEvent);
            conditionalNotifier.notifyEvent(normalEvent);

            assertThat(importantEvents).containsExactly(importantEvent);
        }
    }

    @Nested
    @DisplayName("Polymorphism")
    class Polymorphism {

        @Test
        @DisplayName("should work with EventController as EventNotifier")
        void shouldWorkWithEventControllerAsEventNotifier() {
            EventController controller = new EventController();
            EventNotifier notifier = controller; // EventController implements EventNotifier

            // Should work without issues
            Event testEvent = new SimpleEvent(new TestEventId("test"), "data");
            assertThatCode(() -> notifier.notifyEvent(testEvent))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should support different notification strategies")
        void shouldSupportDifferentNotificationStrategies() {
            // Immediate notification
            EventNotifier immediateNotifier = event -> System.out.println("Immediate: " + event.getId());

            // Buffered notification
            List<Event> buffer = new ArrayList<>();
            EventNotifier bufferedNotifier = buffer::add;

            // Logging notification
            EventNotifier loggingNotifier = event -> System.out
                    .println("Log: " + event.getId() + " at " + System.currentTimeMillis());

            Event testEvent = new SimpleEvent(new TestEventId("test"), "data");

            assertThatCode(() -> {
                immediateNotifier.notifyEvent(testEvent);
                bufferedNotifier.notifyEvent(testEvent);
                loggingNotifier.notifyEvent(testEvent);
            }).doesNotThrowAnyException();

            assertThat(buffer).containsExactly(testEvent);
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

    private static class TestEventNotifier implements EventNotifier {
        private Event lastEvent;
        private int eventCount = 0;

        @Override
        public void notifyEvent(Event event) {
            this.lastEvent = event;
            this.eventCount++;
        }

        public Event getLastEvent() {
            return lastEvent;
        }

        public int getEventCount() {
            return eventCount;
        }
    }

    private static class CustomEvent implements Event {
        private final EventId id;
        private final Object data;

        public CustomEvent(EventId id, Object data) {
            this.id = id;
            this.data = data;
        }

        @Override
        public EventId getId() {
            return id;
        }

        @Override
        public Object getData() {
            return data;
        }
    }
}
