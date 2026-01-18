package pt.up.fe.specs.util.events;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for EventReceiverTemplate abstract class.
 * 
 * @author Generated Tests
 */
@DisplayName("EventReceiverTemplate")
class EventReceiverTemplateTest {

    private TestEventId testEventId1;
    private TestEventId testEventId2;
    private Event testEvent1;
    private Event testEvent2;
    private ActionsMap mockActionsMap;

    @BeforeEach
    void setUp() {
        testEventId1 = new TestEventId("TEST_EVENT_1");
        testEventId2 = new TestEventId("TEST_EVENT_2");
        testEvent1 = new SimpleEvent(testEventId1, "data1");
        testEvent2 = new SimpleEvent(testEventId2, "data2");
        mockActionsMap = mock(ActionsMap.class);
    }

    @Nested
    @DisplayName("Event Acceptance")
    class EventAcceptance {

        @Test
        @DisplayName("should delegate event acceptance to actions map")
        void shouldDelegateEventAcceptanceToActionsMap() {
            TestEventReceiverTemplate receiver = new TestEventReceiverTemplate(mockActionsMap);

            receiver.acceptEvent(testEvent1);

            verify(mockActionsMap).performAction(testEvent1);
        }

        @Test
        @DisplayName("should handle null actions map gracefully")
        void shouldHandleNullActionsMapGracefully() {
            TestEventReceiverTemplate receiver = new TestEventReceiverTemplate(null);

            assertThatCode(() -> receiver.acceptEvent(testEvent1))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should not call actions map when it is null")
        void shouldNotCallActionsMapWhenItIsNull() {
            TestEventReceiverTemplate receiver = new TestEventReceiverTemplate(null);

            receiver.acceptEvent(testEvent1);

            // No exception should be thrown, and no further verification needed
            // since actionsMap is null
        }

        @Test
        @DisplayName("should handle null event with actions map")
        void shouldHandleNullEventWithActionsMap() {
            TestEventReceiverTemplate receiver = new TestEventReceiverTemplate(mockActionsMap);

            assertThatCode(() -> receiver.acceptEvent(null))
                    .doesNotThrowAnyException();

            verify(mockActionsMap).performAction(null);
        }

        @Test
        @DisplayName("should handle null event with null actions map")
        void shouldHandleNullEventWithNullActionsMap() {
            TestEventReceiverTemplate receiver = new TestEventReceiverTemplate(null);

            assertThatCode(() -> receiver.acceptEvent(null))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Supported Events")
    class SupportedEvents {

        @Test
        @DisplayName("should delegate supported events to actions map")
        void shouldDelegateSupportedEventsToActionsMap() {
            Set<EventId> expectedEvents = new HashSet<>(Arrays.asList(testEventId1, testEventId2));
            when(mockActionsMap.getSupportedEvents()).thenReturn(expectedEvents);
            TestEventReceiverTemplate receiver = new TestEventReceiverTemplate(mockActionsMap);

            Collection<EventId> supportedEvents = receiver.getSupportedEvents();

            assertThat(supportedEvents).isEqualTo(expectedEvents);
            verify(mockActionsMap).getSupportedEvents();
        }

        @Test
        @DisplayName("should return empty list when actions map is null")
        void shouldReturnEmptyListWhenActionsMapIsNull() {
            TestEventReceiverTemplate receiver = new TestEventReceiverTemplate(null);

            Collection<EventId> supportedEvents = receiver.getSupportedEvents();

            assertThat(supportedEvents)
                    .isNotNull()
                    .isEmpty();
        }

        @Test
        @DisplayName("should handle empty supported events from actions map")
        void shouldHandleEmptySupportedEventsFromActionsMap() {
            when(mockActionsMap.getSupportedEvents()).thenReturn(Collections.emptySet());
            TestEventReceiverTemplate receiver = new TestEventReceiverTemplate(mockActionsMap);

            Collection<EventId> supportedEvents = receiver.getSupportedEvents();

            assertThat(supportedEvents)
                    .isNotNull()
                    .isEmpty();
        }

        @Test
        @DisplayName("should handle null supported events from actions map")
        void shouldHandleNullSupportedEventsFromActionsMap() {
            when(mockActionsMap.getSupportedEvents()).thenReturn(null);
            TestEventReceiverTemplate receiver = new TestEventReceiverTemplate(mockActionsMap);

            Collection<EventId> supportedEvents = receiver.getSupportedEvents();

            assertThat(supportedEvents).isNull();
        }
    }

    @Nested
    @DisplayName("Template Pattern")
    class TemplatePattern {

        @Test
        @DisplayName("should work with different actions map implementations")
        void shouldWorkWithDifferentActionsMapImplementations() {
            ActionsMap realActionsMap = new ActionsMap();
            TestEventReceiverTemplate receiver = new TestEventReceiverTemplate(realActionsMap);

            assertThat(receiver.getSupportedEvents())
                    .isNotNull()
                    .isEmpty(); // Real ActionsMap starts empty
        }

        @Test
        @DisplayName("should allow subclasses to provide different actions maps")
        void shouldAllowSubclassesToProvideDifferentActionsMaps() {
            TestEventReceiverTemplate receiver1 = new TestEventReceiverTemplate(mockActionsMap);
            TestEventReceiverTemplate receiver2 = new TestEventReceiverTemplate(null);

            when(mockActionsMap.getSupportedEvents()).thenReturn(new HashSet<>(Arrays.asList(testEventId1)));

            assertThat(receiver1.getSupportedEvents()).containsExactly(testEventId1);
            assertThat(receiver2.getSupportedEvents()).isEmpty();
        }

        @Test
        @DisplayName("should support abstract template method pattern")
        void shouldSupportAbstractTemplateMethodPattern() {
            // Test that the getActionsMap method is properly called
            TestEventReceiverTemplate receiver = new TestEventReceiverTemplate(mockActionsMap);

            receiver.acceptEvent(testEvent1);
            receiver.getSupportedEvents();

            // Verify that the actions map was used for both operations
            verify(mockActionsMap).performAction(testEvent1);
            verify(mockActionsMap).getSupportedEvents();
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandling {

        @Test
        @DisplayName("should handle actions map exceptions during event acceptance")
        void shouldHandleActionsMapExceptionsDuringEventAcceptance() {
            doThrow(new RuntimeException("Actions map error"))
                    .when(mockActionsMap).performAction(testEvent1);
            TestEventReceiverTemplate receiver = new TestEventReceiverTemplate(mockActionsMap);

            assertThatCode(() -> receiver.acceptEvent(testEvent1))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Actions map error");
        }

        @Test
        @DisplayName("should handle actions map exceptions during supported events retrieval")
        void shouldHandleActionsMapExceptionsDuringSupportedEventsRetrieval() {
            when(mockActionsMap.getSupportedEvents())
                    .thenThrow(new RuntimeException("Supported events error"));
            TestEventReceiverTemplate receiver = new TestEventReceiverTemplate(mockActionsMap);

            assertThatCode(() -> receiver.getSupportedEvents())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Supported events error");
        }
    }

    @Nested
    @DisplayName("Integration")
    class Integration {

        @Test
        @DisplayName("should work as EventReceiver interface")
        void shouldWorkAsEventReceiverInterface() {
            EventReceiver receiver = new TestEventReceiverTemplate(mockActionsMap);

            // Should be usable as EventReceiver
            assertThatCode(() -> {
                receiver.acceptEvent(testEvent1);
                receiver.getSupportedEvents();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should support multiple events through actions map")
        void shouldSupportMultipleEventsThroughActionsMap() {
            TestEventReceiverTemplate receiver = new TestEventReceiverTemplate(mockActionsMap);

            receiver.acceptEvent(testEvent1);
            receiver.acceptEvent(testEvent2);

            verify(mockActionsMap).performAction(testEvent1);
            verify(mockActionsMap).performAction(testEvent2);
        }

        @Test
        @DisplayName("should not interfere with actions map state")
        void shouldNotInterfereWithActionsMapState() {
            TestEventReceiverTemplate receiver = new TestEventReceiverTemplate(mockActionsMap);

            receiver.acceptEvent(testEvent1);
            receiver.getSupportedEvents();

            // Verify only expected calls were made
            verify(mockActionsMap).performAction(testEvent1);
            verify(mockActionsMap).getSupportedEvents();
            verify(mockActionsMap, never()).putAction(testEventId1, null);
        }
    }

    /**
     * Test implementation of EventReceiverTemplate for testing purposes.
     */
    private static class TestEventReceiverTemplate extends EventReceiverTemplate {
        private final ActionsMap actionsMap;

        public TestEventReceiverTemplate(ActionsMap actionsMap) {
            this.actionsMap = actionsMap;
        }

        @Override
        protected ActionsMap getActionsMap() {
            return actionsMap;
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
