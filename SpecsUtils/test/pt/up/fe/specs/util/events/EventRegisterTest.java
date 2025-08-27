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

import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

/**
 * Unit tests for {@link EventRegister}.
 * 
 * Tests the event registration interface implementation and contracts.
 * 
 * @author Generated Tests
 */
@DisplayName("EventRegister")
class EventRegisterTest {

    @Mock
    private EventReceiver mockReceiver1;

    @Mock
    private EventReceiver mockReceiver2;

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
        @DisplayName("should define registerReceiver method")
        void shouldDefineRegisterReceiverMethod() {
            try {
                EventRegister.class.getMethod("registerReceiver", EventReceiver.class);
            } catch (NoSuchMethodException e) {
                fail("EventRegister should have registerReceiver(EventReceiver) method", e);
            }
        }

        @Test
        @DisplayName("should define unregisterReceiver method")
        void shouldDefineUnregisterReceiverMethod() {
            try {
                EventRegister.class.getMethod("unregisterReceiver", EventReceiver.class);
            } catch (NoSuchMethodException e) {
                fail("EventRegister should have unregisterReceiver(EventReceiver) method", e);
            }
        }

        @Test
        @DisplayName("should be a registration interface")
        void shouldBeARegistrationInterface() {
            assertThat(EventRegister.class.isInterface()).isTrue();

            // Should be focused on registration/unregistration
            assertThat(EventRegister.class.getMethods()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Implementation Examples")
    class ImplementationExamples {

        @Test
        @DisplayName("should work with simple implementation")
        void shouldWorkWithSimpleImplementation() {
            TestEventRegister register = new TestEventRegister();
            TestEventId testEvent = new TestEventId("test");

            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(testEvent));

            register.registerReceiver(mockReceiver1);

            assertThat(register.hasListeners()).isTrue();
            assertThat(register.getListeners()).contains(mockReceiver1);
        }

        @Test
        @DisplayName("should work with EventController implementation")
        void shouldWorkWithEventControllerImplementation() {
            EventController controller = new EventController();
            EventRegister register = controller; // EventController implements EventRegister
            TestEventId testEvent = new TestEventId("test");

            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(testEvent));

            assertThatCode(() -> {
                register.registerReceiver(mockReceiver1);
                register.unregisterReceiver(mockReceiver1);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Receiver Registration")
    class ReceiverRegistration {

        private TestEventRegister register;

        @BeforeEach
        void setUpRegister() {
            register = new TestEventRegister();
        }

        @Test
        @DisplayName("should register receiver based on supported events")
        void shouldRegisterReceiverBasedOnSupportedEvents() {
            TestEventId event1 = new TestEventId("event1");
            TestEventId event2 = new TestEventId("event2");

            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(event1, event2));

            register.registerReceiver(mockReceiver1);

            assertThat(register.hasListeners()).isTrue();
            assertThat(register.getListeners()).contains(mockReceiver1);
            verify(mockReceiver1).getSupportedEvents();
        }

        @Test
        @DisplayName("should handle receiver with no supported events")
        void shouldHandleReceiverWithNoSupportedEvents() {
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList());

            register.registerReceiver(mockReceiver1);

            // Behavior depends on implementation - some might skip, others might register
            verify(mockReceiver1).getSupportedEvents();
        }

        @Test
        @DisplayName("should handle receiver with null supported events")
        void shouldHandleReceiverWithNullSupportedEvents() {
            when(mockReceiver1.getSupportedEvents()).thenReturn(null);

            assertThatCode(() -> register.registerReceiver(mockReceiver1))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should register multiple receivers")
        void shouldRegisterMultipleReceivers() {
            TestEventId event = new TestEventId("event");

            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(event));
            when(mockReceiver2.getSupportedEvents()).thenReturn(Arrays.asList(event));

            register.registerReceiver(mockReceiver1);
            register.registerReceiver(mockReceiver2);

            assertThat(register.hasListeners()).isTrue();
            assertThat(register.getListeners()).containsExactlyInAnyOrder(mockReceiver1, mockReceiver2);
        }
    }

    @Nested
    @DisplayName("Individual Listener Registration")
    class IndividualListenerRegistration {

        private TestEventRegister register;

        @BeforeEach
        void setUpRegister() {
            register = new TestEventRegister();
        }

        @Test
        @DisplayName("should register listener for single event")
        void shouldRegisterListenerForSingleEvent() {
            TestEventId event = new TestEventId("event");

            register.registerListener(mockReceiver1, event);

            assertThat(register.hasListeners()).isTrue();
            assertThat(register.getListeners()).contains(mockReceiver1);
        }

        @Test
        @DisplayName("should register listener for multiple events via varargs")
        void shouldRegisterListenerForMultipleEventsViaVarargs() {
            TestEventId event1 = new TestEventId("event1");
            TestEventId event2 = new TestEventId("event2");

            register.registerListener(mockReceiver1, event1, event2);

            assertThat(register.hasListeners()).isTrue();
            assertThat(register.getListeners()).contains(mockReceiver1);
        }

        @Test
        @DisplayName("should register listener for event collection")
        void shouldRegisterListenerForEventCollection() {
            TestEventId event1 = new TestEventId("event1");
            TestEventId event2 = new TestEventId("event2");
            Collection<EventId> events = Arrays.asList(event1, event2);

            register.registerListener(mockReceiver1, events);

            assertThat(register.hasListeners()).isTrue();
            assertThat(register.getListeners()).contains(mockReceiver1);
        }

        @Test
        @DisplayName("should handle empty varargs")
        void shouldHandleEmptyVarargs() {
            register.registerListener(mockReceiver1);

            // Behavior depends on implementation
            assertThatCode(() -> register.hasListeners()).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should handle null event collection")
        void shouldHandleNullEventCollection() {
            assertThatCode(() -> register.registerListener(mockReceiver1, (Collection<EventId>) null))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Receiver Unregistration")
    class ReceiverUnregistration {

        private TestEventRegister register;

        @BeforeEach
        void setUpRegister() {
            register = new TestEventRegister();
            TestEventId event = new TestEventId("event");
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(event));
            register.registerReceiver(mockReceiver1);
        }

        @Test
        @DisplayName("should unregister receiver")
        void shouldUnregisterReceiver() {
            assertThat(register.hasListeners()).isTrue();

            register.unregisterReceiver(mockReceiver1);

            assertThat(register.hasListeners()).isFalse();
            assertThat(register.getListeners()).isEmpty();
        }

        @Test
        @DisplayName("should handle unregistering non-registered receiver")
        void shouldHandleUnregisteringNonRegisteredReceiver() {
            TestEventId event = new TestEventId("event");
            when(mockReceiver2.getSupportedEvents()).thenReturn(Arrays.asList(event));

            assertThatCode(() -> register.unregisterReceiver(mockReceiver2))
                    .doesNotThrowAnyException();

            // Original receiver should still be registered
            assertThat(register.hasListeners()).isTrue();
            assertThat(register.getListeners()).contains(mockReceiver1);
        }

        @Test
        @DisplayName("should unregister only specific receiver")
        void shouldUnregisterOnlySpecificReceiver() {
            TestEventId event = new TestEventId("event");
            when(mockReceiver2.getSupportedEvents()).thenReturn(Arrays.asList(event));
            register.registerReceiver(mockReceiver2);

            assertThat(register.getListeners()).containsExactlyInAnyOrder(mockReceiver1, mockReceiver2);

            register.unregisterReceiver(mockReceiver1);

            assertThat(register.hasListeners()).isTrue();
            assertThat(register.getListeners()).containsExactly(mockReceiver2);
        }
    }

    @Nested
    @DisplayName("Listener Management")
    class ListenerManagement {

        private TestEventRegister register;

        @BeforeEach
        void setUpRegister() {
            register = new TestEventRegister();
        }

        @Test
        @DisplayName("should track listener state correctly")
        void shouldTrackListenerStateCorrectly() {
            assertThat(register.hasListeners()).isFalse();
            assertThat(register.getListeners()).isEmpty();

            TestEventId event = new TestEventId("event");
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(event));

            register.registerReceiver(mockReceiver1);

            assertThat(register.hasListeners()).isTrue();
            assertThat(register.getListeners()).isNotEmpty();

            register.unregisterReceiver(mockReceiver1);

            assertThat(register.hasListeners()).isFalse();
            assertThat(register.getListeners()).isEmpty();
        }

        @Test
        @DisplayName("should return immutable view of listeners")
        void shouldReturnImmutableViewOfListeners() {
            TestEventId event = new TestEventId("event");
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(event));

            register.registerReceiver(mockReceiver1);
            Collection<EventReceiver> listeners = register.getListeners();

            // Should not be able to modify the returned collection
            assertThatThrownBy(() -> listeners.clear())
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("should handle duplicate registrations")
        void shouldHandleDuplicateRegistrations() {
            TestEventId event = new TestEventId("event");
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(event));

            register.registerReceiver(mockReceiver1);
            register.registerReceiver(mockReceiver1); // Register again

            // Should handle gracefully - behavior depends on implementation
            assertThatCode(() -> register.getListeners()).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandling {

        private TestEventRegister register;

        @BeforeEach
        void setUpRegister() {
            register = new TestEventRegister();
        }

        @Test
        @DisplayName("should handle null receiver registration")
        void shouldHandleNullReceiverRegistration() {
            assertThatThrownBy(() -> register.registerReceiver(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("should handle null receiver unregistration")
        void shouldHandleNullReceiverUnregistration() {
            assertThatThrownBy(() -> register.unregisterReceiver(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("should handle null receiver in listener registration")
        void shouldHandleNullReceiverInListenerRegistration() {
            TestEventId event = new TestEventId("event");

            assertThatThrownBy(() -> register.registerListener(null, event))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("should handle null event in listener registration")
        void shouldHandleNullEventInListenerRegistration() {
            assertThatCode(() -> register.registerListener(mockReceiver1, (EventId) null))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Polymorphism")
    class Polymorphism {

        @Test
        @DisplayName("should work with different EventRegister implementations")
        void shouldWorkWithDifferentEventRegisterImplementations() {
            EventRegister[] registers = {
                    new TestEventRegister(),
                    new EventController()
            };

            TestEventId event = new TestEventId("event");
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(event));

            for (EventRegister register : registers) {
                assertThatCode(() -> {
                    register.registerReceiver(mockReceiver1);
                    register.unregisterReceiver(mockReceiver1);
                }).doesNotThrowAnyException();
            }
        }

        @Test
        @DisplayName("should support different registration strategies")
        void shouldSupportDifferentRegistrationStrategies() {
            TestEventRegister register = new TestEventRegister();
            TestEventId event1 = new TestEventId("event1");
            TestEventId event2 = new TestEventId("event2");

            // Strategy 1: Register receiver based on its supported events
            when(mockReceiver1.getSupportedEvents()).thenReturn(Arrays.asList(event1, event2));
            register.registerReceiver(mockReceiver1);

            // Strategy 2: Register listener for specific events
            register.registerListener(mockReceiver2, event1);

            // Strategy 3: Register listener for event collection
            register.registerListener(mockReceiver2, Arrays.asList(event2));

            assertThat(register.hasListeners()).isTrue();
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

    private static class TestEventRegister implements EventRegister {
        private final Set<EventReceiver> receivers = new HashSet<>();

        @Override
        public void registerReceiver(EventReceiver receiver) {
            if (receiver == null) {
                throw new NullPointerException("Receiver cannot be null");
            }
            Collection<EventId> supportedEvents = receiver.getSupportedEvents();
            if (supportedEvents != null && !supportedEvents.isEmpty()) {
                receivers.add(receiver);
            }
        }

        @Override
        public void unregisterReceiver(EventReceiver receiver) {
            if (receiver == null) {
                throw new NullPointerException("Receiver cannot be null");
            }
            receivers.remove(receiver);
        }

        // Test-specific methods (not part of EventRegister interface)
        public void registerListener(EventReceiver receiver, EventId event) {
            if (receiver == null) {
                throw new NullPointerException("Receiver cannot be null");
            }
            receivers.add(receiver);
        }

        public void registerListener(EventReceiver receiver, EventId... events) {
            if (receiver == null) {
                throw new NullPointerException("Receiver cannot be null");
            }
            if (events != null && events.length > 0) {
                receivers.add(receiver);
            }
        }

        public void registerListener(EventReceiver receiver, Collection<EventId> events) {
            if (receiver == null) {
                throw new NullPointerException("Receiver cannot be null");
            }
            if (events != null && !events.isEmpty()) {
                receivers.add(receiver);
            }
        }

        public boolean hasListeners() {
            return !receivers.isEmpty();
        }

        public Collection<EventReceiver> getListeners() {
            return Collections.unmodifiableCollection(new ArrayList<>(receivers));
        }
    }
}
