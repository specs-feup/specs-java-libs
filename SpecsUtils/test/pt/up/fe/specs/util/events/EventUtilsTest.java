package pt.up.fe.specs.util.events;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for EventUtils utility class.
 * 
 * @author Generated Tests
 */
@DisplayName("EventUtils")
class EventUtilsTest {

    private TestEventId testEventId1;
    private TestEventId testEventId2;
    private TestEventId testEventId3;

    @BeforeEach
    void setUp() {
        testEventId1 = new TestEventId("TEST_EVENT_1");
        testEventId2 = new TestEventId("TEST_EVENT_2");
        testEventId3 = new TestEventId("TEST_EVENT_3");
    }

    @Nested
    @DisplayName("Event ID Collection Creation")
    class EventIdCollectionCreation {

        @Test
        @DisplayName("should create collection with single event ID")
        void shouldCreateCollectionWithSingleEventId() {
            Collection<EventId> eventIds = EventUtils.getEventIds(testEventId1);

            assertThat(eventIds)
                    .isNotNull()
                    .hasSize(1)
                    .containsExactly(testEventId1);
        }

        @Test
        @DisplayName("should create collection with multiple event IDs")
        void shouldCreateCollectionWithMultipleEventIds() {
            Collection<EventId> eventIds = EventUtils.getEventIds(testEventId1, testEventId2, testEventId3);

            assertThat(eventIds)
                    .isNotNull()
                    .hasSize(3)
                    .containsExactly(testEventId1, testEventId2, testEventId3);
        }

        @Test
        @DisplayName("should create empty collection with no event IDs")
        void shouldCreateEmptyCollectionWithNoEventIds() {
            Collection<EventId> eventIds = EventUtils.getEventIds();

            assertThat(eventIds)
                    .isNotNull()
                    .isEmpty();
        }

        @Test
        @DisplayName("should preserve order of event IDs")
        void shouldPreserveOrderOfEventIds() {
            Collection<EventId> eventIds = EventUtils.getEventIds(testEventId3, testEventId1, testEventId2);

            assertThat(eventIds)
                    .containsExactly(testEventId3, testEventId1, testEventId2);
        }

        @Test
        @DisplayName("should handle duplicate event IDs")
        void shouldHandleDuplicateEventIds() {
            Collection<EventId> eventIds = EventUtils.getEventIds(testEventId1, testEventId2, testEventId1);

            assertThat(eventIds)
                    .hasSize(3)
                    .containsExactly(testEventId1, testEventId2, testEventId1);
        }
    }

    @Nested
    @DisplayName("Null Handling")
    class NullHandling {

        @Test
        @DisplayName("should handle single null event ID")
        void shouldHandleSingleNullEventId() {
            Collection<EventId> eventIds = EventUtils.getEventIds((EventId) null);

            assertThat(eventIds)
                    .isNotNull()
                    .hasSize(1)
                    .containsExactly((EventId) null);
        }

        @Test
        @DisplayName("should handle null event IDs mixed with valid ones")
        void shouldHandleNullEventIdsMixedWithValidOnes() {
            Collection<EventId> eventIds = EventUtils.getEventIds(testEventId1, null, testEventId2);

            assertThat(eventIds)
                    .hasSize(3)
                    .containsExactly(testEventId1, null, testEventId2);
        }

        @Test
        @DisplayName("should handle multiple null event IDs")
        void shouldHandleMultipleNullEventIds() {
            Collection<EventId> eventIds = EventUtils.getEventIds((EventId) null, (EventId) null);

            assertThat(eventIds)
                    .hasSize(2)
                    .containsExactly((EventId) null, (EventId) null);
        }

        @Test
        @DisplayName("should handle null varargs array")
        void shouldHandleNullVarargsArray() {
            EventId[] nullArray = null;

            assertThatCode(() -> EventUtils.getEventIds(nullArray))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Collection Properties")
    class CollectionProperties {

        @Test
        @DisplayName("should return mutable collection")
        void shouldReturnMutableCollection() {
            Collection<EventId> eventIds = EventUtils.getEventIds(testEventId1, testEventId2);

            assertThatCode(() -> {
                eventIds.add(testEventId3);
                eventIds.remove(testEventId1);
            }).doesNotThrowAnyException();

            assertThat(eventIds).containsExactly(testEventId2, testEventId3);
        }

        @Test
        @DisplayName("should return ArrayList implementation")
        void shouldReturnArrayListImplementation() {
            Collection<EventId> eventIds = EventUtils.getEventIds(testEventId1);

            // Should be ArrayList as per SpecsFactory.newArrayList()
            assertThat(eventIds).isInstanceOf(java.util.ArrayList.class);
        }

        @Test
        @DisplayName("should support collection operations")
        void shouldSupportCollectionOperations() {
            Collection<EventId> eventIds = EventUtils.getEventIds(testEventId1, testEventId2);

            assertThat(eventIds.contains(testEventId1)).isTrue();
            assertThat(eventIds.contains(testEventId3)).isFalse();
            assertThat(eventIds.size()).isEqualTo(2);
            assertThat(eventIds.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("should support iteration")
        void shouldSupportIteration() {
            Collection<EventId> eventIds = EventUtils.getEventIds(testEventId1, testEventId2, testEventId3);

            int count = 0;
            for (EventId eventId : eventIds) {
                assertThat(eventId).isIn(testEventId1, testEventId2, testEventId3);
                count++;
            }

            assertThat(count).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle large number of event IDs")
        void shouldHandleLargeNumberOfEventIds() {
            EventId[] manyEventIds = new EventId[1000];
            for (int i = 0; i < 1000; i++) {
                manyEventIds[i] = new TestEventId("EVENT_" + i);
            }

            Collection<EventId> eventIds = EventUtils.getEventIds(manyEventIds);

            assertThat(eventIds)
                    .hasSize(1000)
                    .containsExactly(manyEventIds);
        }

        @Test
        @DisplayName("should create new collection each time")
        void shouldCreateNewCollectionEachTime() {
            Collection<EventId> eventIds1 = EventUtils.getEventIds(testEventId1);
            Collection<EventId> eventIds2 = EventUtils.getEventIds(testEventId1);

            assertThat(eventIds1).isNotSameAs(eventIds2);
            assertThat(eventIds1).isEqualTo(eventIds2);
        }

        @Test
        @DisplayName("should not share references between calls")
        void shouldNotShareReferencesBetweenCalls() {
            Collection<EventId> eventIds1 = EventUtils.getEventIds(testEventId1);
            Collection<EventId> eventIds2 = EventUtils.getEventIds(testEventId1);

            eventIds1.add(testEventId2);

            assertThat(eventIds1).hasSize(2);
            assertThat(eventIds2).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Integration")
    class Integration {

        @Test
        @DisplayName("should work with EventReceiver implementations")
        void shouldWorkWithEventReceiverImplementations() {
            Collection<EventId> supportedEvents = EventUtils.getEventIds(testEventId1, testEventId2);

            EventReceiver receiver = new EventReceiver() {
                @Override
                public void acceptEvent(Event event) {
                    // Implementation not needed for this test
                }

                @Override
                public Collection<EventId> getSupportedEvents() {
                    return supportedEvents;
                }
            };

            assertThat(receiver.getSupportedEvents()).isEqualTo(supportedEvents);
        }

        @Test
        @DisplayName("should work with ActionsMap")
        void shouldWorkWithActionsMap() {
            ActionsMap actionsMap = new ActionsMap();
            Collection<EventId> eventIds = EventUtils.getEventIds(testEventId1, testEventId2);

            for (EventId eventId : eventIds) {
                actionsMap.putAction(eventId, event -> {
                    // Test action
                });
            }

            assertThat(actionsMap.getSupportedEvents()).containsExactlyInAnyOrderElementsOf(eventIds);
        }

        @Test
        @DisplayName("should be compatible with existing event framework")
        void shouldBeCompatibleWithExistingEventFramework() {
            // Create event IDs using utility
            Collection<EventId> eventIds = EventUtils.getEventIds(testEventId1, testEventId2);

            // Use with EventController-like registration
            for (EventId eventId : eventIds) {
                Event event = new SimpleEvent(eventId, "test data");
                assertThat(event.getId()).isEqualTo(eventId);
            }
        }
    }

    @Nested
    @DisplayName("Utility Class Properties")
    class UtilityClassProperties {

        @Test
        @DisplayName("should be a utility class with static methods")
        void shouldBeUtilityClassWithStaticMethods() {
            assertThatCode(() -> {
                // Should be able to call static method without instantiation
                EventUtils.getEventIds(testEventId1);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should have public constructor for instantiation")
        void shouldHavePublicConstructorForInstantiation() {
            assertThatCode(() -> {
                new EventUtils();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should behave consistently across multiple calls")
        void shouldBehaveConsistentlyAcrossMultipleCalls() {
            for (int i = 0; i < 10; i++) {
                Collection<EventId> eventIds = EventUtils.getEventIds(testEventId1, testEventId2);
                assertThat(eventIds)
                        .hasSize(2)
                        .containsExactly(testEventId1, testEventId2);
            }
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
