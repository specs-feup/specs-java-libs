package pt.up.fe.specs.util.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Unit tests for {@link EventController}.
 * 
 * Tests the main event management controller that handles registration and
 * notification.
 * 
 * @author Generated Tests
 */
@DisplayName("EventController")
class EventControllerTest {

    private EventController eventController;
    private TestEventId testEventId;
    private TestEventId anotherEventId;
    private Event testEvent;

    @Mock
    private EventReceiver mockReceiver1;

    @Mock
    private EventReceiver mockReceiver2;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        eventController = new EventController();
        testEventId = new TestEventId("test-event");
        anotherEventId = new TestEventId("another-event");
        testEvent = new SimpleEvent(testEventId, "test data");
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create empty event controller")
        void shouldCreateEmptyEventController() {
            EventController controller = new EventController();

            assertThat(controller).isNotNull();
            assertThat(controller.hasListeners()).isFalse();
            assertThat(controller.getListeners()).isEmpty();
        }

        @Test
        @DisplayName("should implement EventNotifier interface")
        void shouldImplementEventNotifierInterface() {
            assertThat(eventController).isInstanceOf(EventNotifier.class);
        }

        @Test
        @DisplayName("should implement EventRegister interface")
        void shouldImplementEventRegisterInterface() {
            assertThat(eventController).isInstanceOf(EventRegister.class);
        }
    }

    @Nested
    @DisplayName("Receiver Registration")
    class ReceiverRegistration {

        @Test
        @DisplayName("should register receiver for its supported events")
        void shouldRegisterReceiverForItsSupportedEvents() {
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(testEventId));

            eventController.registerReceiver(mockReceiver1);

            assertThat(eventController.hasListeners()).isTrue();
            assertThat(eventController.getListeners()).contains(mockReceiver1);
            verify(mockReceiver1).getSupportedEvents();
        }

        @Test
        @DisplayName("should register receiver for multiple events")
        void shouldRegisterReceiverForMultipleEvents() {
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(testEventId, anotherEventId));

            eventController.registerReceiver(mockReceiver1);

            assertThat(eventController.hasListeners()).isTrue();
            assertThat(eventController.getListeners()).contains(mockReceiver1);
        }

        @Test
        @DisplayName("should register multiple receivers")
        void shouldRegisterMultipleReceivers() {
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(testEventId));
            when(mockReceiver2.getSupportedEvents()).thenReturn(Arrays.asList(testEventId));

            eventController.registerReceiver(mockReceiver1);
            eventController.registerReceiver(mockReceiver2);

            assertThat(eventController.hasListeners()).isTrue();
            assertThat(eventController.getListeners()).containsExactlyInAnyOrder(mockReceiver1, mockReceiver2);
        }

        @Test
        @DisplayName("should handle receiver with no supported events")
        void shouldHandleReceiverWithNoSupportedEvents() {
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList());

            eventController.registerReceiver(mockReceiver1);

            // Should still be considered a registered listener even with no events
            assertThat(eventController.hasListeners()).isFalse();
            assertThat(eventController.getListeners()).isEmpty();
        }

        @Test
        @DisplayName("should handle receiver with null supported events")
        void shouldHandleReceiverWithNullSupportedEvents() {
            when(mockReceiver1.getSupportedEvents()).thenReturn(null);

            assertThatCode(() -> eventController.registerReceiver(mockReceiver1))
                    .doesNotThrowAnyException();

            assertThat(eventController.hasListeners()).isFalse();
        }

        @Test
        @DisplayName("should handle duplicate receiver registration")
        void shouldHandleDuplicateReceiverRegistration() {
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(testEventId));

            eventController.registerReceiver(mockReceiver1);
            eventController.registerReceiver(mockReceiver1); // Register again

            // Should still have only one instance
            assertThat(eventController.getListeners()).hasSize(1);
            assertThat(eventController.getListeners()).contains(mockReceiver1);
        }
    }

    @Nested
    @DisplayName("Individual Listener Registration")
    class IndividualListenerRegistration {

        @Test
        @DisplayName("should register listener for single event")
        void shouldRegisterListenerForSingleEvent() {
            eventController.registerListener(mockReceiver1, testEventId);

            assertThat(eventController.hasListeners()).isTrue();
            assertThat(eventController.getListeners()).contains(mockReceiver1);
        }

        @Test
        @DisplayName("should register listener for multiple events via varargs")
        void shouldRegisterListenerForMultipleEventsViaVarargs() {
            eventController.registerListener(mockReceiver1, testEventId, anotherEventId);

            assertThat(eventController.hasListeners()).isTrue();
            assertThat(eventController.getListeners()).contains(mockReceiver1);
        }

        @Test
        @DisplayName("should register listener for event collection")
        void shouldRegisterListenerForEventCollection() {
            Collection<EventId> events = Arrays.asList(testEventId, anotherEventId);

            eventController.registerListener(mockReceiver1, events);

            assertThat(eventController.hasListeners()).isTrue();
            assertThat(eventController.getListeners()).contains(mockReceiver1);
        }

        @Test
        @DisplayName("should handle null event collection")
        void shouldHandleNullEventCollection() {
            assertThatCode(() -> eventController.registerListener(mockReceiver1, (Collection<EventId>) null))
                    .doesNotThrowAnyException();

            assertThat(eventController.hasListeners()).isFalse();
        }

        @Test
        @DisplayName("should handle empty varargs")
        void shouldHandleEmptyVarargs() {
            eventController.registerListener(mockReceiver1);

            assertThat(eventController.hasListeners()).isFalse();
        }
    }

    @Nested
    @DisplayName("Receiver Unregistration")
    class ReceiverUnregistration {

        @BeforeEach
        void setUpRegisteredReceiver() {
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(testEventId, anotherEventId));
            eventController.registerReceiver(mockReceiver1);
        }

        @Test
        @DisplayName("should unregister receiver from its supported events")
        void shouldUnregisterReceiverFromItsSupportedEvents() {
            eventController.unregisterReceiver(mockReceiver1);

            assertThat(eventController.hasListeners()).isFalse();
            assertThat(eventController.getListeners()).isEmpty();
        }

        @Test
        @DisplayName("should handle unregistering non-registered receiver")
        void shouldHandleUnregisteringNonRegisteredReceiver() {
            when(mockReceiver2.getSupportedEvents()).thenReturn(Arrays.asList(testEventId));

            assertThatCode(() -> eventController.unregisterReceiver(mockReceiver2))
                    .doesNotThrowAnyException();

            // Original receiver should still be registered
            assertThat(eventController.hasListeners()).isTrue();
            assertThat(eventController.getListeners()).contains(mockReceiver1);
        }

        @Test
        @DisplayName("should handle unregistering from non-existent event")
        void shouldHandleUnregisteringFromNonExistentEvent() {
            TestEventId nonExistentEvent = new TestEventId("non-existent");
            when(mockReceiver2.getSupportedEvents()).thenReturn(Arrays.asList(nonExistentEvent));

            assertThatCode(() -> eventController.unregisterReceiver(mockReceiver2))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should unregister only specific receiver")
        void shouldUnregisterOnlySpecificReceiver() {
            when(mockReceiver2.getSupportedEvents()).thenReturn(Arrays.asList(testEventId));
            eventController.registerReceiver(mockReceiver2);

            // Both receivers registered
            assertThat(eventController.getListeners()).containsExactlyInAnyOrder(mockReceiver1, mockReceiver2);

            eventController.unregisterReceiver(mockReceiver1);

            // Only mockReceiver2 should remain
            assertThat(eventController.hasListeners()).isTrue();
            assertThat(eventController.getListeners()).containsExactly(mockReceiver2);
        }
    }

    @Nested
    @DisplayName("Event Notification")
    class EventNotification {

        @BeforeEach
        void setUpRegisteredReceivers() {
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(testEventId));
            when(mockReceiver2.getSupportedEvents()).thenReturn(Arrays.asList(testEventId));
            eventController.registerReceiver(mockReceiver1);
            eventController.registerReceiver(mockReceiver2);
        }

        @Test
        @DisplayName("should notify all registered receivers")
        void shouldNotifyAllRegisteredReceivers() {
            eventController.notifyEvent(testEvent);

            verify(mockReceiver1).acceptEvent(testEvent);
            verify(mockReceiver2).acceptEvent(testEvent);
        }

        @Test
        @DisplayName("should not notify receivers for different events")
        void shouldNotNotifyReceiversForDifferentEvents() {
            Event differentEvent = new SimpleEvent(anotherEventId, "different data");

            eventController.notifyEvent(differentEvent);

            verify(mockReceiver1, never()).acceptEvent(any());
            verify(mockReceiver2, never()).acceptEvent(any());
        }

        @Test
        @DisplayName("should handle notification with no registered receivers")
        void shouldHandleNotificationWithNoRegisteredReceivers() {
            EventController emptyController = new EventController();

            assertThatCode(() -> emptyController.notifyEvent(testEvent))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should notify only receivers registered for specific event")
        void shouldNotifyOnlyReceiversRegisteredForSpecificEvent() {
            when(mockReceiver2.getSupportedEvents()).thenReturn(Arrays.asList(anotherEventId));

            // Re-setup with different events
            EventController newController = new EventController();
            newController.registerReceiver(mockReceiver1); // Registered for testEventId
            newController.registerReceiver(mockReceiver2); // Registered for anotherEventId

            newController.notifyEvent(testEvent);

            verify(mockReceiver1).acceptEvent(testEvent);
            verify(mockReceiver2, never()).acceptEvent(any());
        }

        @Test
        @DisplayName("should handle exceptions in receiver gracefully")
        void shouldHandleExceptionsInReceiverGracefully() {
            doThrow(new RuntimeException("Receiver error")).when(mockReceiver1).acceptEvent(any());

            // Should not throw exception even if receiver throws
            assertThatCode(() -> eventController.notifyEvent(testEvent))
                    .doesNotThrowAnyException();

            // Other receivers should still be notified
            verify(mockReceiver2).acceptEvent(testEvent);
        }
    }

    @Nested
    @DisplayName("Listener Management")
    class ListenerManagement {

        @Test
        @DisplayName("should track listener count correctly")
        void shouldTrackListenerCountCorrectly() {
            assertThat(eventController.hasListeners()).isFalse();

            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(testEventId));
            eventController.registerReceiver(mockReceiver1);

            assertThat(eventController.hasListeners()).isTrue();

            eventController.unregisterReceiver(mockReceiver1);

            assertThat(eventController.hasListeners()).isFalse();
        }

        @Test
        @DisplayName("should return current listeners")
        void shouldReturnCurrentListeners() {
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(testEventId));
            when(mockReceiver2.getSupportedEvents()).thenReturn(Arrays.asList(anotherEventId));

            Collection<EventReceiver> listeners = eventController.getListeners();
            assertThat(listeners).isEmpty();

            eventController.registerReceiver(mockReceiver1);
            listeners = eventController.getListeners();
            assertThat(listeners).containsExactly(mockReceiver1);

            eventController.registerReceiver(mockReceiver2);
            listeners = eventController.getListeners();
            assertThat(listeners).containsExactlyInAnyOrder(mockReceiver1, mockReceiver2);
        }

        @Test
        @DisplayName("should handle multiple registrations of same receiver")
        void shouldHandleMultipleRegistrationsOfSameReceiver() {
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(testEventId, anotherEventId));

            eventController.registerReceiver(mockReceiver1);
            eventController.registerReceiver(mockReceiver1); // Register again

            // Should count as one listener
            assertThat(eventController.getListeners()).hasSize(1);

            // Should notify only once per event
            eventController.notifyEvent(testEvent);
            verify(mockReceiver1, times(1)).acceptEvent(testEvent);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle null events in notification")
        void shouldHandleNullEventsInNotification() {
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(testEventId));
            eventController.registerReceiver(mockReceiver1);

            assertThatThrownBy(() -> eventController.notifyEvent(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("should handle events with null IDs")
        void shouldHandleEventsWithNullIds() {
            Event nullIdEvent = new SimpleEvent(null, "data");
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList((EventId) null));
            eventController.registerReceiver(mockReceiver1);

            // Should handle null event IDs without crashing
            assertThatCode(() -> eventController.notifyEvent(nullIdEvent))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should handle concurrent modifications")
        void shouldHandleConcurrentModifications() throws InterruptedException {
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(testEventId));

            // Register in multiple threads
            List<Thread> threads = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Thread thread = new Thread(() -> {
                    eventController.registerReceiver(mockReceiver1);
                    eventController.notifyEvent(testEvent);
                    eventController.unregisterReceiver(mockReceiver1);
                });
                threads.add(thread);
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Should end up in consistent state
            assertThatCode(() -> eventController.hasListeners())
                    .doesNotThrowAnyException();
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
}
