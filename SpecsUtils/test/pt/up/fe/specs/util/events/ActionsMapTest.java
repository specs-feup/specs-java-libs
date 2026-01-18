package pt.up.fe.specs.util.events;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ActionsMap class.
 * 
 * @author Generated Tests
 */
@DisplayName("ActionsMap")
class ActionsMapTest {

    private ActionsMap actionsMap;
    private TestEventId testEventId1;
    private TestEventId testEventId2;
    private Event testEvent1;
    private Event testEvent2;
    private EventAction mockAction1;
    private EventAction mockAction2;

    @BeforeEach
    void setUp() {
        actionsMap = new ActionsMap();
        testEventId1 = new TestEventId("TEST_EVENT_1");
        testEventId2 = new TestEventId("TEST_EVENT_2");
        testEvent1 = new SimpleEvent(testEventId1, "data1");
        testEvent2 = new SimpleEvent(testEventId2, "data2");
        mockAction1 = mock(EventAction.class);
        mockAction2 = mock(EventAction.class);
    }

    @Nested
    @DisplayName("Action Registration")
    class ActionRegistration {

        @Test
        @DisplayName("should register action for event ID")
        void shouldRegisterActionForEventId() {
            EventAction result = actionsMap.putAction(testEventId1, mockAction1);

            assertThat(result).isNull(); // No previous action
            assertThat(actionsMap.getSupportedEvents()).containsExactly(testEventId1);
        }

        @Test
        @DisplayName("should register multiple actions for different event IDs")
        void shouldRegisterMultipleActionsForDifferentEventIds() {
            actionsMap.putAction(testEventId1, mockAction1);
            actionsMap.putAction(testEventId2, mockAction2);

            assertThat(actionsMap.getSupportedEvents())
                    .hasSize(2)
                    .containsExactlyInAnyOrder(testEventId1, testEventId2);
        }

        @Test
        @DisplayName("should return previous action when replacing")
        void shouldReturnPreviousActionWhenReplacing() {
            actionsMap.putAction(testEventId1, mockAction1);
            EventAction result = actionsMap.putAction(testEventId1, mockAction2);

            assertThat(result).isEqualTo(mockAction1);
            assertThat(actionsMap.getSupportedEvents()).containsExactly(testEventId1);
        }

        @Test
        @DisplayName("should handle null action registration")
        void shouldHandleNullActionRegistration() {
            assertThatCode(() -> actionsMap.putAction(testEventId1, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("EventAction cannot be null");
        }

        @Test
        @DisplayName("should handle null event ID registration")
        void shouldHandleNullEventIdRegistration() {
            assertThatCode(() -> actionsMap.putAction(null, mockAction1))
                    .doesNotThrowAnyException();

            assertThat(actionsMap.getSupportedEvents()).containsExactly((EventId) null);
        }
    }

    @Nested
    @DisplayName("Action Execution")
    class ActionExecution {

        @Test
        @DisplayName("should execute action for registered event")
        void shouldExecuteActionForRegisteredEvent() {
            actionsMap.putAction(testEventId1, mockAction1);

            actionsMap.performAction(testEvent1);

            verify(mockAction1).performAction(testEvent1);
        }

        @Test
        @DisplayName("should execute correct action for multiple registered events")
        void shouldExecuteCorrectActionForMultipleRegisteredEvents() {
            actionsMap.putAction(testEventId1, mockAction1);
            actionsMap.putAction(testEventId2, mockAction2);

            actionsMap.performAction(testEvent1);
            actionsMap.performAction(testEvent2);

            verify(mockAction1).performAction(testEvent1);
            verify(mockAction2).performAction(testEvent2);
            verify(mockAction1, never()).performAction(testEvent2);
            verify(mockAction2, never()).performAction(testEvent1);
        }

        @Test
        @DisplayName("should handle execution for unregistered event gracefully")
        void shouldHandleExecutionForUnregisteredEventGracefully() {
            // No actions registered
            assertThatCode(() -> actionsMap.performAction(testEvent1))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should handle null event during execution")
        void shouldHandleNullEventDuringExecution() {
            actionsMap.putAction(testEventId1, mockAction1);

            assertThatCode(() -> actionsMap.performAction(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("should not allow null actions to be registered")
        void shouldNotAllowNullActionsToBeRegistered() {
            assertThatCode(() -> actionsMap.putAction(testEventId1, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("EventAction cannot be null");

            // Event should not be in supported events since registration failed
            assertThat(actionsMap.getSupportedEvents()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Supported Events")
    class SupportedEvents {

        @Test
        @DisplayName("should return empty set initially")
        void shouldReturnEmptySetInitially() {
            Set<EventId> supportedEvents = actionsMap.getSupportedEvents();

            assertThat(supportedEvents)
                    .isNotNull()
                    .isEmpty();
        }

        @Test
        @DisplayName("should return set of registered event IDs")
        void shouldReturnSetOfRegisteredEventIds() {
            actionsMap.putAction(testEventId1, mockAction1);
            actionsMap.putAction(testEventId2, mockAction2);

            Set<EventId> supportedEvents = actionsMap.getSupportedEvents();

            assertThat(supportedEvents)
                    .hasSize(2)
                    .containsExactlyInAnyOrder(testEventId1, testEventId2);
        }

        @Test
        @DisplayName("should return key set view of internal map")
        void shouldReturnKeySetViewOfInternalMap() {
            actionsMap.putAction(testEventId1, mockAction1);
            Set<EventId> supportedEvents1 = actionsMap.getSupportedEvents();

            actionsMap.putAction(testEventId2, mockAction2);
            Set<EventId> supportedEvents2 = actionsMap.getSupportedEvents();

            // Should reflect current state
            assertThat(supportedEvents1).hasSize(2); // Updated view
            assertThat(supportedEvents2).hasSize(2);
            assertThat(supportedEvents1).isEqualTo(supportedEvents2);
        }

        @Test
        @DisplayName("should handle duplicate registrations correctly")
        void shouldHandleDuplicateRegistrationsCorrectly() {
            actionsMap.putAction(testEventId1, mockAction1);
            actionsMap.putAction(testEventId1, mockAction2); // Replace

            Set<EventId> supportedEvents = actionsMap.getSupportedEvents();

            assertThat(supportedEvents)
                    .hasSize(1)
                    .containsExactly(testEventId1);
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandling {

        @Test
        @DisplayName("should handle exceptions in action execution")
        void shouldHandleExceptionsInActionExecution() {
            doThrow(new RuntimeException("Action failed"))
                    .when(mockAction1).performAction(testEvent1);
            actionsMap.putAction(testEventId1, mockAction1);

            assertThatCode(() -> actionsMap.performAction(testEvent1))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Action failed");
        }

        @Test
        @DisplayName("should not affect other actions when one throws exception")
        void shouldNotAffectOtherActionsWhenOneThrowsException() {
            doThrow(new RuntimeException("Action failed"))
                    .when(mockAction1).performAction(testEvent1);
            actionsMap.putAction(testEventId1, mockAction1);
            actionsMap.putAction(testEventId2, mockAction2);

            // First action throws exception
            assertThatCode(() -> actionsMap.performAction(testEvent1))
                    .isInstanceOf(RuntimeException.class);

            // Second action should still work
            assertThatCode(() -> actionsMap.performAction(testEvent2))
                    .doesNotThrowAnyException();

            verify(mockAction2).performAction(testEvent2);
        }

        @Test
        @DisplayName("should handle event with null ID")
        void shouldHandleEventWithNullId() {
            Event eventWithNullId = new SimpleEvent(null, "data");
            actionsMap.putAction(null, mockAction1);

            actionsMap.performAction(eventWithNullId);

            verify(mockAction1).performAction(eventWithNullId);
        }
    }

    @Nested
    @DisplayName("Integration")
    class Integration {

        @Test
        @DisplayName("should work with EventReceiverTemplate")
        void shouldWorkWithEventReceiverTemplate() {
            actionsMap.putAction(testEventId1, mockAction1);

            EventReceiverTemplate receiver = new EventReceiverTemplate() {
                @Override
                protected ActionsMap getActionsMap() {
                    return actionsMap;
                }
            };

            receiver.acceptEvent(testEvent1);

            verify(mockAction1).performAction(testEvent1);
            assertThat(receiver.getSupportedEvents()).containsExactly(testEventId1);
        }

        @Test
        @DisplayName("should support different EventAction implementations")
        void shouldSupportDifferentEventActionImplementations() {
            TestEventAction testAction = new TestEventAction();
            EventAction lambdaAction = event -> {
                /* do nothing */ };

            actionsMap.putAction(testEventId1, testAction);
            actionsMap.putAction(testEventId2, lambdaAction);

            actionsMap.performAction(testEvent1);
            actionsMap.performAction(testEvent2);

            assertThat(testAction.getProcessedEvents()).containsExactly(testEvent1);
        }

        @Test
        @DisplayName("should work with EventUtils created collections")
        void shouldWorkWithEventUtilsCreatedCollections() {
            var eventIds = EventUtils.getEventIds(testEventId1, testEventId2);

            for (EventId eventId : eventIds) {
                actionsMap.putAction(eventId, mock(EventAction.class));
            }

            assertThat(actionsMap.getSupportedEvents())
                    .containsExactlyInAnyOrderElementsOf(eventIds);
        }
    }

    @Nested
    @DisplayName("State Management")
    class StateManagement {

        @Test
        @DisplayName("should maintain state correctly across operations")
        void shouldMaintainStateCorrectlyAcrossOperations() {
            // Initial state
            assertThat(actionsMap.getSupportedEvents()).isEmpty();

            // Add first action
            actionsMap.putAction(testEventId1, mockAction1);
            assertThat(actionsMap.getSupportedEvents()).hasSize(1);

            // Add second action
            actionsMap.putAction(testEventId2, mockAction2);
            assertThat(actionsMap.getSupportedEvents()).hasSize(2);

            // Replace first action
            actionsMap.putAction(testEventId1, mockAction2);
            assertThat(actionsMap.getSupportedEvents()).hasSize(2);

            // Execute actions
            actionsMap.performAction(testEvent1);
            actionsMap.performAction(testEvent2);

            verify(mockAction2).performAction(testEvent1);
            verify(mockAction2).performAction(testEvent2);
            verify(mockAction1, never()).performAction(testEvent1);
        }

        @Test
        @DisplayName("should handle repeated action executions")
        void shouldHandleRepeatedActionExecutions() {
            TestEventAction action = new TestEventAction();
            actionsMap.putAction(testEventId1, action);

            actionsMap.performAction(testEvent1);
            actionsMap.performAction(testEvent1);
            actionsMap.performAction(testEvent1);

            assertThat(action.getProcessedEvents()).hasSize(3);
        }

        @Test
        @DisplayName("should support concurrent-like usage patterns")
        void shouldSupportConcurrentLikeUsagePatterns() {
            // Simulate rapid registration and execution
            for (int i = 0; i < 100; i++) {
                TestEventId eventId = new TestEventId("EVENT_" + i);
                TestEventAction action = new TestEventAction();
                Event event = new SimpleEvent(eventId, "data" + i);

                actionsMap.putAction(eventId, action);
                actionsMap.performAction(event);

                assertThat(action.getProcessedEvents()).hasSize(1);
            }

            assertThat(actionsMap.getSupportedEvents()).hasSize(100);
        }
    }

    @Nested
    @DisplayName("Warning Logging Behavior")
    class WarningLoggingBehavior {

        @Test
        @DisplayName("should warn when replacing existing action")
        void shouldWarnWhenReplacingExistingAction() {
            // First registration
            actionsMap.putAction(testEventId1, mockAction1);

            // Second registration should trigger warning (we can't easily test logging)
            EventAction result = actionsMap.putAction(testEventId1, mockAction2);

            assertThat(result).isEqualTo(mockAction1);
            // Note: We can't easily test the warning log without additional setup
        }

        @Test
        @DisplayName("should warn when no action found for event")
        void shouldWarnWhenNoActionFoundForEvent() {
            // No action registered for this event
            assertThatCode(() -> actionsMap.performAction(testEvent1))
                    .doesNotThrowAnyException();

            // Note: We can't easily test the warning log without additional setup
        }
    }

    /**
     * Test implementation of EventAction for testing purposes.
     */
    private static class TestEventAction implements EventAction {
        private final List<Event> processedEvents = new ArrayList<>();

        @Override
        public void performAction(Event event) {
            processedEvents.add(event);
        }

        public List<Event> getProcessedEvents() {
            return processedEvents;
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
