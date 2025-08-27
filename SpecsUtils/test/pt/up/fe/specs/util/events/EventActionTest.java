package pt.up.fe.specs.util.events;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for EventAction interface and its implementations.
 * 
 * @author Generated Tests
 */
@DisplayName("EventAction")
class EventActionTest {

    private TestEventId testEventId;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        testEventId = new TestEventId("TEST_EVENT");
        testEvent = new SimpleEvent(testEventId, "test data");
    }

    @Nested
    @DisplayName("Interface Contract")
    class InterfaceContract {

        @Test
        @DisplayName("should have correct interface method")
        void shouldHaveCorrectInterfaceMethod() {
            assertThatCode(() -> {
                EventAction.class.getMethod("performAction", Event.class);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should allow implementation to define action behavior")
        void shouldAllowImplementationToDefineActionBehavior() {
            TestEventAction action = new TestEventAction();

            action.performAction(testEvent);

            assertThat(action.getProcessedEvents())
                    .hasSize(1)
                    .containsExactly(testEvent);
        }

        @Test
        @DisplayName("should support functional interface pattern")
        void shouldSupportFunctionalInterfacePattern() {
            List<Event> capturedEvents = new ArrayList<>();
            EventAction lambdaAction = event -> capturedEvents.add(event);

            lambdaAction.performAction(testEvent);

            assertThat(capturedEvents)
                    .hasSize(1)
                    .containsExactly(testEvent);
        }
    }

    @Nested
    @DisplayName("Implementation Behavior")
    class ImplementationBehavior {

        @Test
        @DisplayName("should handle null event gracefully")
        void shouldHandleNullEventGracefully() {
            TestEventAction action = new TestEventAction();

            assertThatCode(() -> action.performAction(null))
                    .doesNotThrowAnyException();

            assertThat(action.getProcessedEvents())
                    .hasSize(1)
                    .containsExactly((Event) null);
        }

        @Test
        @DisplayName("should support multiple action invocations")
        void shouldSupportMultipleActionInvocations() {
            TestEventAction action = new TestEventAction();
            Event event1 = new SimpleEvent(testEventId, "data1");
            Event event2 = new SimpleEvent(testEventId, "data2");

            action.performAction(event1);
            action.performAction(event2);
            action.performAction(event1);

            assertThat(action.getProcessedEvents())
                    .hasSize(3)
                    .containsExactly(event1, event2, event1);
        }

        @Test
        @DisplayName("should allow actions with side effects")
        void shouldAllowActionsWithSideEffects() {
            Counter counter = new Counter();
            EventAction action = event -> counter.increment();

            action.performAction(testEvent);
            action.performAction(testEvent);
            action.performAction(testEvent);

            assertThat(counter.getValue()).isEqualTo(3);
        }

        @Test
        @DisplayName("should support stateless actions")
        void shouldSupportStatelessActions() {
            EventAction statelessAction = event -> {
                // Stateless action - do nothing or perform pure operations
            };

            assertThatCode(() -> {
                statelessAction.performAction(testEvent);
                statelessAction.performAction(testEvent);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Polymorphism")
    class Polymorphism {

        @Test
        @DisplayName("should work with different implementations")
        void shouldWorkWithDifferentImplementations() {
            List<Event> capturedEvents = new ArrayList<>();

            EventAction classAction = new TestEventAction();
            EventAction lambdaAction = event -> capturedEvents.add(event);
            EventAction mockAction = mock(EventAction.class);

            classAction.performAction(testEvent);
            lambdaAction.performAction(testEvent);
            mockAction.performAction(testEvent);

            assertThat(((TestEventAction) classAction).getProcessedEvents()).hasSize(1);
            assertThat(capturedEvents).hasSize(1);
            verify(mockAction).performAction(testEvent);
        }

        @Test
        @DisplayName("should support actions array processing")
        void shouldSupportActionsArrayProcessing() {
            TestEventAction action1 = new TestEventAction();
            TestEventAction action2 = new TestEventAction();
            EventAction[] actions = { action1, action2 };

            for (EventAction action : actions) {
                action.performAction(testEvent);
            }

            assertThat(action1.getProcessedEvents()).containsExactly(testEvent);
            assertThat(action2.getProcessedEvents()).containsExactly(testEvent);
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandling {

        @Test
        @DisplayName("should propagate exceptions from action implementation")
        void shouldPropagateExceptionsFromActionImplementation() {
            EventAction throwingAction = event -> {
                throw new RuntimeException("Action failed");
            };

            assertThatCode(() -> throwingAction.performAction(testEvent))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Action failed");
        }

        @Test
        @DisplayName("should handle checked exceptions properly")
        void shouldHandleCheckedExceptionsProperly() {
            EventAction action = event -> {
                try {
                    throw new Exception("Checked exception");
                } catch (Exception e) {
                    throw new RuntimeException("Wrapped exception", e);
                }
            };

            assertThatCode(() -> action.performAction(testEvent))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Wrapped exception")
                    .hasCauseInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("should not interfere with subsequent actions on exception")
        void shouldNotInterfereWithSubsequentActionsOnException() {
            TestEventAction normalAction = new TestEventAction();
            EventAction throwingAction = event -> {
                throw new RuntimeException("Action failed");
            };

            // First action should work normally
            normalAction.performAction(testEvent);

            // Second action throws exception
            assertThatCode(() -> throwingAction.performAction(testEvent))
                    .isInstanceOf(RuntimeException.class);

            // First action should still work after exception in second
            normalAction.performAction(testEvent);

            assertThat(normalAction.getProcessedEvents()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Integration")
    class Integration {

        @Test
        @DisplayName("should work with ActionsMap")
        void shouldWorkWithActionsMap() {
            ActionsMap actionsMap = new ActionsMap();
            TestEventAction action = new TestEventAction();

            actionsMap.putAction(testEventId, action);
            actionsMap.performAction(testEvent);

            assertThat(action.getProcessedEvents()).containsExactly(testEvent);
        }

        @Test
        @DisplayName("should support chaining through composition")
        void shouldSupportChainingThroughComposition() {
            TestEventAction action1 = new TestEventAction();
            TestEventAction action2 = new TestEventAction();

            EventAction composedAction = event -> {
                action1.performAction(event);
                action2.performAction(event);
            };

            composedAction.performAction(testEvent);

            assertThat(action1.getProcessedEvents()).containsExactly(testEvent);
            assertThat(action2.getProcessedEvents()).containsExactly(testEvent);
        }

        @Test
        @DisplayName("should support conditional action execution")
        void shouldSupportConditionalActionExecution() {
            TestEventAction action = new TestEventAction();
            EventId conditionalEventId = new TestEventId("CONDITIONAL_EVENT");

            EventAction conditionalAction = event -> {
                if (conditionalEventId.equals(event.getId())) {
                    action.performAction(event);
                }
            };

            // Should execute for matching event
            Event matchingEvent = new SimpleEvent(conditionalEventId, "data");
            conditionalAction.performAction(matchingEvent);

            // Should not execute for non-matching event
            conditionalAction.performAction(testEvent);

            assertThat(action.getProcessedEvents()).containsExactly(matchingEvent);
        }
    }

    @Nested
    @DisplayName("Performance")
    class Performance {

        @Test
        @DisplayName("should support repeated action execution")
        void shouldSupportRepeatedActionExecution() {
            Counter counter = new Counter();
            EventAction action = event -> counter.increment();

            // Execute action many times
            for (int i = 0; i < 1000; i++) {
                action.performAction(testEvent);
            }

            assertThat(counter.getValue()).isEqualTo(1000);
        }

        @Test
        @DisplayName("should not retain references unnecessarily")
        void shouldNotRetainReferencesUnnecessarily() {
            List<Event> events = new ArrayList<>();
            EventAction action = event -> {
                // Process but don't retain reference
                event.getData().toString(); // Use data but don't store event
            };

            for (int i = 0; i < 100; i++) {
                Event event = new SimpleEvent(testEventId, "data" + i);
                events.add(event);
                action.performAction(event);
            }

            // Action should not prevent garbage collection of events
            assertThatCode(() -> System.gc()).doesNotThrowAnyException();
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
     * Simple counter for testing side effects.
     */
    private static class Counter {
        private int value = 0;

        public void increment() {
            value++;
        }

        public int getValue() {
            return value;
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
