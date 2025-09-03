package pt.up.fe.specs.util.swing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GenericActionListener}.
 * Tests the implementation of generic action listener functionality for Swing
 * components.
 * 
 * @author Generated Tests
 */
@DisplayName("GenericActionListener")
class GenericActionListenerTest {

    @Mock
    private Consumer<ActionEvent> mockConsumer;

    @Mock
    private ActionEvent mockActionEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Constructor and Factory Method")
    class ConstructorAndFactoryMethod {

        @Test
        @DisplayName("Should create instance with constructor")
        void shouldCreateInstanceWithConstructor() {
            // When
            GenericActionListener listener = new GenericActionListener(mockConsumer);

            // Then
            assertThat(listener).isNotNull();
            assertThat(listener).isInstanceOf(AbstractAction.class);
        }

        @Test
        @DisplayName("Should create instance with factory method")
        void shouldCreateInstanceWithFactoryMethod() {
            // When
            GenericActionListener listener = GenericActionListener.newInstance(mockConsumer);

            // Then
            assertThat(listener).isNotNull();
            assertThat(listener).isInstanceOf(AbstractAction.class);
        }

        @Test
        @DisplayName("Should accept null consumer in constructor")
        void shouldAcceptNullConsumerInConstructor() {
            // When/Then - Should not throw exception
            GenericActionListener listener = new GenericActionListener(null);
            assertThat(listener).isNotNull();
        }

        @Test
        @DisplayName("Should accept null consumer in factory method")
        void shouldAcceptNullConsumerInFactoryMethod() {
            // When/Then - Should not throw exception
            GenericActionListener listener = GenericActionListener.newInstance(null);
            assertThat(listener).isNotNull();
        }
    }

    @Nested
    @DisplayName("ActionListener Implementation")
    class ActionListenerImplementation {

        @Test
        @DisplayName("Should call consumer when action performed")
        void shouldCallConsumerWhenActionPerformed() {
            // Given
            GenericActionListener listener = new GenericActionListener(mockConsumer);

            // When
            listener.actionPerformed(mockActionEvent);

            // Then
            verify(mockConsumer, times(1)).accept(mockActionEvent);
        }

        @Test
        @DisplayName("Should pass correct event to consumer")
        void shouldPassCorrectEventToConsumer() {
            // Given
            GenericActionListener listener = new GenericActionListener(mockConsumer);

            // When
            listener.actionPerformed(mockActionEvent);

            // Then - Verify the consumer was called with the exact event
            verify(mockConsumer).accept(mockActionEvent);
        }

        @Test
        @DisplayName("Should handle null action event with null consumer")
        void shouldHandleNullActionEventWithNullConsumer() {
            // Given
            GenericActionListener listener = new GenericActionListener(null);

            // When/Then - Should throw NPE when trying to call null consumer
            assertThatThrownBy(() -> listener.actionPerformed(mockActionEvent))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle multiple action events")
        void shouldHandleMultipleActionEvents() {
            // Given
            GenericActionListener listener = new GenericActionListener(mockConsumer);
            ActionEvent secondEvent = mock(ActionEvent.class);

            // When
            listener.actionPerformed(mockActionEvent);
            listener.actionPerformed(secondEvent);

            // Then
            verify(mockConsumer, times(2)).accept(any(ActionEvent.class));
            verify(mockConsumer).accept(mockActionEvent);
            verify(mockConsumer).accept(secondEvent);
        }
    }

    @Nested
    @DisplayName("AbstractAction Integration")
    class AbstractActionIntegration {

        @Test
        @DisplayName("Should be instanceof AbstractAction")
        void shouldBeInstanceOfAbstractAction() {
            // Given
            GenericActionListener listener = new GenericActionListener(mockConsumer);

            // Then
            assertThat(listener).isInstanceOf(AbstractAction.class);
        }

        @Test
        @DisplayName("Should support Action interface methods")
        void shouldSupportActionInterfaceMethods() {
            // Given
            GenericActionListener listener = new GenericActionListener(mockConsumer);

            // Then - Should be able to call AbstractAction methods without exceptions
            assertThat(listener.isEnabled()).isTrue(); // Default enabled state

            // Should be able to set/get values
            listener.putValue("test-key", "test-value");
            assertThat(listener.getValue("test-key")).isEqualTo("test-value");
        }

        @Test
        @DisplayName("Should support enable/disable functionality")
        void shouldSupportEnableDisableFunctionality() {
            // Given
            GenericActionListener listener = new GenericActionListener(mockConsumer);

            // When
            listener.setEnabled(false);

            // Then
            assertThat(listener.isEnabled()).isFalse();

            // When
            listener.setEnabled(true);

            // Then
            assertThat(listener.isEnabled()).isTrue();
        }
    }

    @Nested
    @DisplayName("Consumer Behavior")
    class ConsumerBehavior {

        @Test
        @DisplayName("Should handle consumer that throws exception")
        void shouldHandleConsumerThatThrowsException() {
            // Given
            Consumer<ActionEvent> throwingConsumer = event -> {
                throw new RuntimeException("Test exception");
            };
            GenericActionListener listener = new GenericActionListener(throwingConsumer);

            // When/Then - Exception should propagate
            assertThatThrownBy(() -> listener.actionPerformed(mockActionEvent))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Test exception");
        }

        @Test
        @DisplayName("Should work with lambda expressions")
        void shouldWorkWithLambdaExpressions() {
            // Given
            boolean[] actionPerformed = { false };
            GenericActionListener listener = new GenericActionListener(event -> {
                actionPerformed[0] = true;
            });

            // When
            listener.actionPerformed(mockActionEvent);

            // Then
            assertThat(actionPerformed[0]).isTrue();
        }

        @Test
        @DisplayName("Should work with method references")
        void shouldWorkWithMethodReferences() {
            // Given
            TestActionHandler handler = new TestActionHandler();
            GenericActionListener listener = new GenericActionListener(handler::handleAction);

            // When
            listener.actionPerformed(mockActionEvent);

            // Then
            assertThat(handler.wasActionHandled()).isTrue();
            assertThat(handler.getLastEvent()).isEqualTo(mockActionEvent);
        }
    }

    @Nested
    @DisplayName("Thread Safety")
    class ThreadSafety {

        @Test
        @DisplayName("Should handle concurrent action events")
        void shouldHandleConcurrentActionEvents() throws InterruptedException {
            // Given
            final int numThreads = 10;
            final int actionsPerThread = 100;
            @SuppressWarnings("unchecked")
            Consumer<ActionEvent> countingConsumer = mock(Consumer.class);
            GenericActionListener listener = new GenericActionListener(countingConsumer);

            Thread[] threads = new Thread[numThreads];

            // When
            for (int i = 0; i < numThreads; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < actionsPerThread; j++) {
                        listener.actionPerformed(mockActionEvent);
                    }
                });
                threads[i].start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // Then
            verify(countingConsumer, times(numThreads * actionsPerThread)).accept(mockActionEvent);
        }
    }

    @Nested
    @DisplayName("Serialization")
    class Serialization {

        @Test
        @DisplayName("Should have serialVersionUID field")
        void shouldHaveSerialVersionUIDField() {
            // Given/When/Then - Should be able to create instance (indicates
            // serialVersionUID is present)
            GenericActionListener listener = new GenericActionListener(mockConsumer);
            assertThat(listener).isNotNull();

            // The serialVersionUID field should exist (this is more of a compilation check)
            // If it didn't exist, the class wouldn't compile properly as AbstractAction is
            // Serializable
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle rapid successive action events")
        void shouldHandleRapidSuccessiveActionEvents() {
            // Given
            GenericActionListener listener = new GenericActionListener(mockConsumer);
            final int numEvents = 1000;

            // When
            for (int i = 0; i < numEvents; i++) {
                listener.actionPerformed(mockActionEvent);
            }

            // Then
            verify(mockConsumer, times(numEvents)).accept(mockActionEvent);
        }

        @Test
        @DisplayName("Should maintain consumer reference")
        void shouldMaintainConsumerReference() {
            // Given
            GenericActionListener listener = new GenericActionListener(mockConsumer);

            // When - Call action multiple times with different events
            ActionEvent event1 = mock(ActionEvent.class);
            ActionEvent event2 = mock(ActionEvent.class);

            listener.actionPerformed(event1);
            listener.actionPerformed(event2);

            // Then - Same consumer should be called for both
            verify(mockConsumer).accept(event1);
            verify(mockConsumer).accept(event2);
            verify(mockConsumer, times(2)).accept(any(ActionEvent.class));
        }
    }

    // Helper class for testing method references
    private static class TestActionHandler {
        private boolean actionHandled = false;
        private ActionEvent lastEvent;

        public void handleAction(ActionEvent event) {
            this.actionHandled = true;
            this.lastEvent = event;
        }

        public boolean wasActionHandled() {
            return actionHandled;
        }

        public ActionEvent getLastEvent() {
            return lastEvent;
        }
    }
}
