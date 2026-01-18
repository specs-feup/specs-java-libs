package pt.up.fe.specs.util.swing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GenericMouseListener}.
 * Tests the implementation of generic mouse listener functionality for Swing
 * components.
 * 
 * @author Generated Tests
 */
@DisplayName("GenericMouseListener")
class GenericMouseListenerTest {

    @Mock
    private Consumer<MouseEvent> mockConsumer;

    @Mock
    private MouseEvent mockMouseEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Constructor and Factory Methods")
    class ConstructorAndFactoryMethods {

        @Test
        @DisplayName("Should create instance with default constructor")
        void shouldCreateInstanceWithDefaultConstructor() {
            // When
            GenericMouseListener listener = new GenericMouseListener();

            // Then
            assertThat(listener).isNotNull();
            assertThat(listener).isInstanceOf(MouseListener.class);
        }

        @Test
        @DisplayName("Should create instance with click factory method")
        void shouldCreateInstanceWithClickFactoryMethod() {
            // When
            GenericMouseListener listener = GenericMouseListener.click(mockConsumer);

            // Then
            assertThat(listener).isNotNull();
            assertThat(listener).isInstanceOf(MouseListener.class);
        }

        @Test
        @DisplayName("Should accept null consumer in click factory method")
        void shouldAcceptNullConsumerInClickFactoryMethod() {
            // When/Then - Should not throw exception
            GenericMouseListener listener = GenericMouseListener.click(null);
            assertThat(listener).isNotNull();
        }
    }

    @Nested
    @DisplayName("Event Handler Configuration")
    class EventHandlerConfiguration {

        @Test
        @DisplayName("Should set click handler and return self")
        void shouldSetClickHandlerAndReturnSelf() {
            // Given
            GenericMouseListener listener = new GenericMouseListener();

            // When
            GenericMouseListener result = listener.onClick(mockConsumer);

            // Then
            assertThat(result).isSameAs(listener);
        }

        @Test
        @DisplayName("Should set press handler and return self")
        void shouldSetPressHandlerAndReturnSelf() {
            // Given
            GenericMouseListener listener = new GenericMouseListener();

            // When
            GenericMouseListener result = listener.onPressed(mockConsumer);

            // Then
            assertThat(result).isSameAs(listener);
        }

        @Test
        @DisplayName("Should set release handler and return self")
        void shouldSetReleaseHandlerAndReturnSelf() {
            // Given
            GenericMouseListener listener = new GenericMouseListener();

            // When
            GenericMouseListener result = listener.onRelease(mockConsumer);

            // Then
            assertThat(result).isSameAs(listener);
        }

        @Test
        @DisplayName("Should set entered handler and return self")
        void shouldSetEnteredHandlerAndReturnSelf() {
            // Given
            GenericMouseListener listener = new GenericMouseListener();

            // When
            GenericMouseListener result = listener.onEntered(mockConsumer);

            // Then
            assertThat(result).isSameAs(listener);
        }

        @Test
        @DisplayName("Should set exited handler and return self")
        void shouldSetExitedHandlerAndReturnSelf() {
            // Given
            GenericMouseListener listener = new GenericMouseListener();

            // When
            GenericMouseListener result = listener.onExited(mockConsumer);

            // Then
            assertThat(result).isSameAs(listener);
        }

        @Test
        @DisplayName("Should allow method chaining")
        void shouldAllowMethodChaining() {
            // Given
            @SuppressWarnings("unchecked")
            Consumer<MouseEvent> clickConsumer = mock(Consumer.class);
            @SuppressWarnings("unchecked")
            Consumer<MouseEvent> pressConsumer = mock(Consumer.class);
            @SuppressWarnings("unchecked")
            Consumer<MouseEvent> releaseConsumer = mock(Consumer.class);

            // When
            GenericMouseListener listener = new GenericMouseListener()
                    .onClick(clickConsumer)
                    .onPressed(pressConsumer)
                    .onRelease(releaseConsumer);

            // Then
            assertThat(listener).isNotNull();

            // Verify handlers work
            listener.mouseClicked(mockMouseEvent);
            listener.mousePressed(mockMouseEvent);
            listener.mouseReleased(mockMouseEvent);

            verify(clickConsumer).accept(mockMouseEvent);
            verify(pressConsumer).accept(mockMouseEvent);
            verify(releaseConsumer).accept(mockMouseEvent);
        }
    }

    @Nested
    @DisplayName("Mouse Event Handling")
    class MouseEventHandling {

        @Test
        @DisplayName("Should call click handler on mouse clicked")
        void shouldCallClickHandlerOnMouseClicked() {
            // Given
            GenericMouseListener listener = new GenericMouseListener().onClick(mockConsumer);

            // When
            listener.mouseClicked(mockMouseEvent);

            // Then
            verify(mockConsumer).accept(mockMouseEvent);
        }

        @Test
        @DisplayName("Should call press handler on mouse pressed")
        void shouldCallPressHandlerOnMousePressed() {
            // Given
            GenericMouseListener listener = new GenericMouseListener().onPressed(mockConsumer);

            // When
            listener.mousePressed(mockMouseEvent);

            // Then
            verify(mockConsumer).accept(mockMouseEvent);
        }

        @Test
        @DisplayName("Should call release handler on mouse released")
        void shouldCallReleaseHandlerOnMouseReleased() {
            // Given
            GenericMouseListener listener = new GenericMouseListener().onRelease(mockConsumer);

            // When
            listener.mouseReleased(mockMouseEvent);

            // Then
            verify(mockConsumer).accept(mockMouseEvent);
        }

        @Test
        @DisplayName("Should call entered handler on mouse entered")
        void shouldCallEnteredHandlerOnMouseEntered() {
            // Given
            GenericMouseListener listener = new GenericMouseListener().onEntered(mockConsumer);

            // When
            listener.mouseEntered(mockMouseEvent);

            // Then
            verify(mockConsumer).accept(mockMouseEvent);
        }

        @Test
        @DisplayName("Should call exited handler on mouse exited")
        void shouldCallExitedHandlerOnMouseExited() {
            // Given
            GenericMouseListener listener = new GenericMouseListener().onExited(mockConsumer);

            // When
            listener.mouseExited(mockMouseEvent);

            // Then
            verify(mockConsumer).accept(mockMouseEvent);
        }

        @Test
        @DisplayName("Should handle multiple events correctly")
        void shouldHandleMultipleEventsCorrectly() {
            // Given
            @SuppressWarnings("unchecked")
            Consumer<MouseEvent> clickConsumer = mock(Consumer.class);
            @SuppressWarnings("unchecked")
            Consumer<MouseEvent> pressConsumer = mock(Consumer.class);
            GenericMouseListener listener = new GenericMouseListener()
                    .onClick(clickConsumer)
                    .onPressed(pressConsumer);

            // When
            listener.mouseClicked(mockMouseEvent);
            listener.mousePressed(mockMouseEvent);
            listener.mouseReleased(mockMouseEvent); // Should do nothing

            // Then
            verify(clickConsumer).accept(mockMouseEvent);
            verify(pressConsumer).accept(mockMouseEvent);
        }
    }

    @Nested
    @DisplayName("Default Behavior")
    class DefaultBehavior {

        @Test
        @DisplayName("Should do nothing when no handlers set")
        void shouldDoNothingWhenNoHandlersSet() {
            // Given
            GenericMouseListener listener = new GenericMouseListener();

            // When/Then - Should not throw exceptions
            listener.mouseClicked(mockMouseEvent);
            listener.mousePressed(mockMouseEvent);
            listener.mouseReleased(mockMouseEvent);
            listener.mouseEntered(mockMouseEvent);
            listener.mouseExited(mockMouseEvent);
        }

        @Test
        @DisplayName("Should handle null events gracefully with default handlers")
        void shouldHandleNullEventsGracefullyWithDefaultHandlers() {
            // Given
            GenericMouseListener listener = new GenericMouseListener();

            // When/Then - Should not throw exceptions
            listener.mouseClicked(null);
            listener.mousePressed(null);
            listener.mouseReleased(null);
            listener.mouseEntered(null);
            listener.mouseExited(null);
        }

        @Test
        @DisplayName("Should handle null events with custom handlers")
        void shouldHandleNullEventsWithCustomHandlers() {
            // Given
            GenericMouseListener listener = new GenericMouseListener().onClick(mockConsumer);

            // When
            listener.mouseClicked(null);

            // Then
            verify(mockConsumer).accept(null);
        }
    }

    @Nested
    @DisplayName("Handler Replacement")
    class HandlerReplacement {

        @Test
        @DisplayName("Should replace click handler when set multiple times")
        void shouldReplaceClickHandlerWhenSetMultipleTimes() {
            // Given
            @SuppressWarnings("unchecked")
            Consumer<MouseEvent> firstConsumer = mock(Consumer.class);
            @SuppressWarnings("unchecked")
            Consumer<MouseEvent> secondConsumer = mock(Consumer.class);
            GenericMouseListener listener = new GenericMouseListener()
                    .onClick(firstConsumer)
                    .onClick(secondConsumer);

            // When
            listener.mouseClicked(mockMouseEvent);

            // Then
            verify(secondConsumer).accept(mockMouseEvent);
            verify(firstConsumer, never()).accept(any());
        }

        @Test
        @DisplayName("Should allow setting handler to null")
        void shouldAllowSettingHandlerToNull() {
            // Given
            GenericMouseListener listener = new GenericMouseListener()
                    .onClick(mockConsumer)
                    .onClick(null);

            // When/Then - Should throw NPE when null handler is called
            assertThatThrownBy(() -> listener.mouseClicked(mockMouseEvent))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Factory Method Behavior")
    class FactoryMethodBehavior {

        @Test
        @DisplayName("Should create listener with click handler via factory")
        void shouldCreateListenerWithClickHandlerViaFactory() {
            // When
            GenericMouseListener listener = GenericMouseListener.click(mockConsumer);

            // Then
            listener.mouseClicked(mockMouseEvent);
            verify(mockConsumer).accept(mockMouseEvent);
        }

        @Test
        @DisplayName("Should allow further configuration after factory creation")
        void shouldAllowFurtherConfigurationAfterFactoryCreation() {
            // Given
            @SuppressWarnings("unchecked")
            Consumer<MouseEvent> pressConsumer = mock(Consumer.class);

            // When
            GenericMouseListener listener = GenericMouseListener.click(mockConsumer)
                    .onPressed(pressConsumer);

            // Then
            listener.mouseClicked(mockMouseEvent);
            listener.mousePressed(mockMouseEvent);

            verify(mockConsumer).accept(mockMouseEvent);
            verify(pressConsumer).accept(mockMouseEvent);
        }
    }

    @Nested
    @DisplayName("Exception Handling")
    class ExceptionHandling {

        @Test
        @DisplayName("Should propagate exceptions from click handler")
        void shouldPropagateExceptionsFromClickHandler() {
            // Given
            Consumer<MouseEvent> throwingConsumer = event -> {
                throw new RuntimeException("Test exception");
            };
            GenericMouseListener listener = new GenericMouseListener().onClick(throwingConsumer);

            // When/Then
            assertThatThrownBy(() -> listener.mouseClicked(mockMouseEvent))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Test exception");
        }

        @Test
        @DisplayName("Should propagate exceptions from press handler")
        void shouldPropagateExceptionsFromPressHandler() {
            // Given
            Consumer<MouseEvent> throwingConsumer = event -> {
                throw new IllegalStateException("Press error");
            };
            GenericMouseListener listener = new GenericMouseListener().onPressed(throwingConsumer);

            // When/Then
            assertThatThrownBy(() -> listener.mousePressed(mockMouseEvent))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Press error");
        }
    }

    @Nested
    @DisplayName("Lambda and Method Reference Support")
    class LambdaAndMethodReferenceSupport {

        @Test
        @DisplayName("Should work with lambda expressions")
        void shouldWorkWithLambdaExpressions() {
            // Given
            boolean[] eventHandled = { false };
            GenericMouseListener listener = new GenericMouseListener()
                    .onClick(event -> eventHandled[0] = true);

            // When
            listener.mouseClicked(mockMouseEvent);

            // Then
            assertThat(eventHandled[0]).isTrue();
        }

        @Test
        @DisplayName("Should work with method references")
        void shouldWorkWithMethodReferences() {
            // Given
            TestMouseHandler handler = new TestMouseHandler();
            GenericMouseListener listener = new GenericMouseListener()
                    .onClick(handler::handleClick)
                    .onPressed(handler::handlePress);

            // When
            listener.mouseClicked(mockMouseEvent);
            listener.mousePressed(mockMouseEvent);

            // Then
            assertThat(handler.getClickCount()).isEqualTo(1);
            assertThat(handler.getPressCount()).isEqualTo(1);
            assertThat(handler.getLastClickEvent()).isEqualTo(mockMouseEvent);
            assertThat(handler.getLastPressEvent()).isEqualTo(mockMouseEvent);
        }
    }

    @Nested
    @DisplayName("Thread Safety")
    class ThreadSafety {

        @Test
        @DisplayName("Should handle concurrent mouse events")
        void shouldHandleConcurrentMouseEvents() throws InterruptedException {
            // Given
            final int numThreads = 10;
            final int eventsPerThread = 50;
            @SuppressWarnings("unchecked")
            Consumer<MouseEvent> countingConsumer = mock(Consumer.class);
            GenericMouseListener listener = new GenericMouseListener().onClick(countingConsumer);

            Thread[] threads = new Thread[numThreads];

            // When
            for (int i = 0; i < numThreads; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < eventsPerThread; j++) {
                        listener.mouseClicked(mockMouseEvent);
                    }
                });
                threads[i].start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // Then
            verify(countingConsumer, times(numThreads * eventsPerThread)).accept(mockMouseEvent);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle rapid successive events")
        void shouldHandleRapidSuccessiveEvents() {
            // Given
            GenericMouseListener listener = new GenericMouseListener().onClick(mockConsumer);
            final int numEvents = 1000;

            // When
            for (int i = 0; i < numEvents; i++) {
                listener.mouseClicked(mockMouseEvent);
            }

            // Then
            verify(mockConsumer, times(numEvents)).accept(mockMouseEvent);
        }

        @Test
        @DisplayName("Should handle mixed event types in sequence")
        void shouldHandleMixedEventTypesInSequence() {
            // Given
            @SuppressWarnings("unchecked")
            Consumer<MouseEvent> clickConsumer = mock(Consumer.class);
            @SuppressWarnings("unchecked")
            Consumer<MouseEvent> pressConsumer = mock(Consumer.class);
            @SuppressWarnings("unchecked")
            Consumer<MouseEvent> releaseConsumer = mock(Consumer.class);
            @SuppressWarnings("unchecked")
            Consumer<MouseEvent> enteredConsumer = mock(Consumer.class);
            @SuppressWarnings("unchecked")
            Consumer<MouseEvent> exitedConsumer = mock(Consumer.class);

            GenericMouseListener listener = new GenericMouseListener()
                    .onClick(clickConsumer)
                    .onPressed(pressConsumer)
                    .onRelease(releaseConsumer)
                    .onEntered(enteredConsumer)
                    .onExited(exitedConsumer);

            // When
            listener.mouseEntered(mockMouseEvent);
            listener.mousePressed(mockMouseEvent);
            listener.mouseReleased(mockMouseEvent);
            listener.mouseClicked(mockMouseEvent);
            listener.mouseExited(mockMouseEvent);

            // Then
            verify(enteredConsumer).accept(mockMouseEvent);
            verify(pressConsumer).accept(mockMouseEvent);
            verify(releaseConsumer).accept(mockMouseEvent);
            verify(clickConsumer).accept(mockMouseEvent);
            verify(exitedConsumer).accept(mockMouseEvent);
        }
    }

    // Helper class for testing method references
    private static class TestMouseHandler {
        private int clickCount = 0;
        private int pressCount = 0;
        private MouseEvent lastClickEvent;
        private MouseEvent lastPressEvent;

        public void handleClick(MouseEvent event) {
            clickCount++;
            lastClickEvent = event;
        }

        public void handlePress(MouseEvent event) {
            pressCount++;
            lastPressEvent = event;
        }

        public int getClickCount() {
            return clickCount;
        }

        public int getPressCount() {
            return pressCount;
        }

        public MouseEvent getLastClickEvent() {
            return lastClickEvent;
        }

        public MouseEvent getLastPressEvent() {
            return lastPressEvent;
        }
    }
}
