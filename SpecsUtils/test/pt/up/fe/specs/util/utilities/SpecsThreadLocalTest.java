package pt.up.fe.specs.util.utilities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Unit tests for {@link SpecsThreadLocal}.
 * 
 * Tests thread-local storage with additional validation and warning
 * capabilities.
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsThreadLocal")
class SpecsThreadLocalTest {

    private SpecsThreadLocal<String> stringThreadLocal;
    private SpecsThreadLocal<Integer> integerThreadLocal;

    @BeforeEach
    void setUp() {
        stringThreadLocal = new SpecsThreadLocal<>(String.class);
        integerThreadLocal = new SpecsThreadLocal<>(Integer.class);
    }

    @AfterEach
    void tearDown() {
        // Clean up any thread local values
        if (stringThreadLocal.isSet()) {
            stringThreadLocal.removeWithWarning();
        }
        if (integerThreadLocal.isSet()) {
            integerThreadLocal.removeWithWarning();
        }
    }

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create with class type")
        void shouldCreateWithClassType() {
            SpecsThreadLocal<Double> doubleThreadLocal = new SpecsThreadLocal<>(Double.class);

            assertThat(doubleThreadLocal).isNotNull();
            assertThat(doubleThreadLocal.isSet()).isFalse();
        }

        @Test
        @DisplayName("should handle null class type")
        void shouldHandleNullClassType() {
            // Constructor accepts null class type
            assertThatCode(() -> new SpecsThreadLocal<String>(null))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Value Setting")
    class ValueSetting {

        @Test
        @DisplayName("should set value when not set")
        void shouldSetValueWhenNotSet() {
            stringThreadLocal.set("test value");

            assertThat(stringThreadLocal.isSet()).isTrue();
            assertThat(stringThreadLocal.get()).isEqualTo("test value");
        }

        @Test
        @DisplayName("should throw exception when setting value twice")
        void shouldThrowExceptionWhenSettingValueTwice() {
            stringThreadLocal.set("first value");

            assertThatThrownBy(() -> stringThreadLocal.set("second value"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("already a value present");
        }

        @Test
        @DisplayName("should set null value")
        void shouldSetNullValue() {
            stringThreadLocal.set(null);

            assertThat(stringThreadLocal.isSet()).isFalse(); // null means not set
        }

        @Test
        @DisplayName("should set with warning when value exists")
        void shouldSetWithWarningWhenValueExists() {
            stringThreadLocal.set("original value");

            // This should not throw, but log a warning
            assertThatCode(() -> stringThreadLocal.setWithWarning("new value"))
                    .doesNotThrowAnyException();

            assertThat(stringThreadLocal.get()).isEqualTo("new value");
        }

        @Test
        @DisplayName("should set with warning when no previous value")
        void shouldSetWithWarningWhenNoPreviousValue() {
            assertThatCode(() -> stringThreadLocal.setWithWarning("test value"))
                    .doesNotThrowAnyException();

            assertThat(stringThreadLocal.get()).isEqualTo("test value");
        }
    }

    @Nested
    @DisplayName("Value Getting")
    class ValueGetting {

        @Test
        @DisplayName("should get value when set")
        void shouldGetValueWhenSet() {
            stringThreadLocal.set("test value");

            assertThat(stringThreadLocal.get()).isEqualTo("test value");
        }

        @Test
        @DisplayName("should throw exception when getting unset value")
        void shouldThrowExceptionWhenGettingUnsetValue() {
            assertThatThrownBy(() -> stringThreadLocal.get())
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("no value set");
        }

        @Test
        @DisplayName("should return correct type")
        void shouldReturnCorrectType() {
            integerThreadLocal.set(42);

            Integer value = integerThreadLocal.get();
            assertThat(value).isEqualTo(42);
            assertThat(value).isInstanceOf(Integer.class);
        }
    }

    @Nested
    @DisplayName("Value Removal")
    class ValueRemoval {

        @Test
        @DisplayName("should remove value when set")
        void shouldRemoveValueWhenSet() {
            stringThreadLocal.set("test value");
            stringThreadLocal.remove();

            assertThat(stringThreadLocal.isSet()).isFalse();
        }

        @Test
        @DisplayName("should throw exception when removing unset value")
        void shouldThrowExceptionWhenRemovingUnsetValue() {
            assertThatThrownBy(() -> stringThreadLocal.remove())
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("no value set");
        }

        @Test
        @DisplayName("should remove with warning when value set")
        void shouldRemoveWithWarningWhenValueSet() {
            stringThreadLocal.set("test value");

            assertThatCode(() -> stringThreadLocal.removeWithWarning())
                    .doesNotThrowAnyException();

            assertThat(stringThreadLocal.isSet()).isFalse();
        }

        @Test
        @DisplayName("should remove with warning when no value set")
        void shouldRemoveWithWarningWhenNoValueSet() {
            // Should not throw, but log a warning
            assertThatCode(() -> stringThreadLocal.removeWithWarning())
                    .doesNotThrowAnyException();

            assertThat(stringThreadLocal.isSet()).isFalse();
        }
    }

    @Nested
    @DisplayName("State Checking")
    class StateChecking {

        @Test
        @DisplayName("should report false when not set")
        void shouldReportFalseWhenNotSet() {
            assertThat(stringThreadLocal.isSet()).isFalse();
        }

        @Test
        @DisplayName("should report true when set")
        void shouldReportTrueWhenSet() {
            stringThreadLocal.set("test value");

            assertThat(stringThreadLocal.isSet()).isTrue();
        }

        @Test
        @DisplayName("should report false after removal")
        void shouldReportFalseAfterRemoval() {
            stringThreadLocal.set("test value");
            stringThreadLocal.remove();

            assertThat(stringThreadLocal.isSet()).isFalse();
        }
    }

    @Nested
    @DisplayName("Thread Isolation")
    class ThreadIsolation {

        @Test
        @DisplayName("should isolate values between threads")
        void shouldIsolateValuesBetweenThreads() throws InterruptedException {
            stringThreadLocal.set("main thread value");

            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<String> otherThreadValue = new AtomicReference<>();
            AtomicBoolean otherThreadIsSet = new AtomicBoolean();

            Thread otherThread = new Thread(() -> {
                try {
                    // Should not see main thread's value
                    otherThreadIsSet.set(stringThreadLocal.isSet());

                    // Set own value
                    stringThreadLocal.set("other thread value");
                    otherThreadValue.set(stringThreadLocal.get());
                } finally {
                    if (stringThreadLocal.isSet()) {
                        stringThreadLocal.remove();
                    }
                    latch.countDown();
                }
            });

            otherThread.start();
            latch.await();

            // Other thread should not see main thread's value
            assertThat(otherThreadIsSet.get()).isFalse();
            assertThat(otherThreadValue.get()).isEqualTo("other thread value");

            // Main thread should still have its value
            assertThat(stringThreadLocal.get()).isEqualTo("main thread value");
        }

        @Test
        @DisplayName("should handle multiple thread locals independently")
        void shouldHandleMultipleThreadLocalsIndependently() {
            stringThreadLocal.set("string value");
            integerThreadLocal.set(42);

            assertThat(stringThreadLocal.get()).isEqualTo("string value");
            assertThat(integerThreadLocal.get()).isEqualTo(42);

            stringThreadLocal.remove();

            assertThat(stringThreadLocal.isSet()).isFalse();
            assertThat(integerThreadLocal.isSet()).isTrue();
            assertThat(integerThreadLocal.get()).isEqualTo(42);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle complex object types")
        void shouldHandleComplexObjectTypes() {
            SpecsThreadLocal<StringBuilder> builderThreadLocal = new SpecsThreadLocal<>(StringBuilder.class);
            StringBuilder builder = new StringBuilder("test");

            builderThreadLocal.set(builder);

            assertThat(builderThreadLocal.get()).isSameAs(builder);
            assertThat(builderThreadLocal.get().toString()).isEqualTo("test");

            builderThreadLocal.remove();
        }

        @Test
        @DisplayName("should handle rapid set and remove cycles")
        void shouldHandleRapidSetAndRemoveCycles() {
            for (int i = 0; i < 100; i++) {
                stringThreadLocal.set("value" + i);
                assertThat(stringThreadLocal.get()).isEqualTo("value" + i);
                stringThreadLocal.remove();
                assertThat(stringThreadLocal.isSet()).isFalse();
            }
        }

        @Test
        @DisplayName("should handle setWithWarning after removeWithWarning")
        void shouldHandleSetWithWarningAfterRemoveWithWarning() {
            stringThreadLocal.removeWithWarning(); // Remove from empty state
            stringThreadLocal.setWithWarning("test value"); // Set after warning removal

            assertThat(stringThreadLocal.get()).isEqualTo("test value");
        }
    }
}
