package pt.up.fe.specs.util.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link EventId}.
 * 
 * Tests the marker interface for event identification.
 * 
 * @author Generated Tests
 */
@DisplayName("EventId")
class EventIdTest {

    @Nested
    @DisplayName("Interface Contract")
    class InterfaceContract {

        @Test
        @DisplayName("should be a marker interface")
        void shouldBeMarkerInterface() {
            // EventId should be an interface with no methods
            assertThat(EventId.class.isInterface()).isTrue();
            assertThat(EventId.class.getDeclaredMethods()).isEmpty();
        }

        @Test
        @DisplayName("should allow implementation by classes")
        void shouldAllowImplementationByClasses() {
            TestEventId eventId = new TestEventId("test");

            assertThat(eventId).isInstanceOf(EventId.class);
        }

        @Test
        @DisplayName("should allow implementation by enums")
        void shouldAllowImplementationByEnums() {
            TestEventEnum eventId = TestEventEnum.FIRST_EVENT;

            assertThat(eventId).isInstanceOf(EventId.class);
            assertThat(eventId).isInstanceOf(Enum.class);
        }
    }

    @Nested
    @DisplayName("Implementation Patterns")
    class ImplementationPatterns {

        @Test
        @DisplayName("should support class-based implementations")
        void shouldSupportClassBasedImplementations() {
            TestEventId eventId1 = new TestEventId("event1");
            TestEventId eventId2 = new TestEventId("event2");

            assertThat(eventId1).isInstanceOf(EventId.class);
            assertThat(eventId2).isInstanceOf(EventId.class);
            assertThat(eventId1).isNotEqualTo(eventId2);
        }

        @Test
        @DisplayName("should support enum-based implementations")
        void shouldSupportEnumBasedImplementations() {
            TestEventEnum event1 = TestEventEnum.FIRST_EVENT;
            TestEventEnum event2 = TestEventEnum.SECOND_EVENT;

            assertThat(event1).isInstanceOf(EventId.class);
            assertThat(event2).isInstanceOf(EventId.class);
            assertThat(event1).isNotEqualTo(event2);

            // Enums provide natural equality and hash code
            assertThat(event1.name()).isEqualTo("FIRST_EVENT");
            assertThat(event2.name()).isEqualTo("SECOND_EVENT");
        }

        @Test
        @DisplayName("should support string-based implementations")
        void shouldSupportStringBasedImplementations() {
            StringEventId eventId = new StringEventId("string-event-id");

            assertThat(eventId).isInstanceOf(EventId.class);
            assertThat(eventId.getValue()).isEqualTo("string-event-id");
        }

        @Test
        @DisplayName("should support anonymous implementations")
        void shouldSupportAnonymousImplementations() {
            EventId anonymousId = new EventId() {
                @Override
                public String toString() {
                    return "AnonymousEventId";
                }
            };

            assertThat(anonymousId).isInstanceOf(EventId.class);
            assertThat(anonymousId.toString()).isEqualTo("AnonymousEventId");
        }
    }

    @Nested
    @DisplayName("Polymorphism")
    class Polymorphism {

        @Test
        @DisplayName("should work in collections")
        void shouldWorkInCollections() {
            java.util.List<EventId> eventIds = java.util.Arrays.asList(
                    new TestEventId("id1"),
                    TestEventEnum.FIRST_EVENT,
                    new StringEventId("id2"),
                    TestEventEnum.SECOND_EVENT);

            assertThat(eventIds).hasSize(4);
            assertThat(eventIds).allMatch(id -> id instanceof EventId);
        }

        @Test
        @DisplayName("should work as map keys")
        void shouldWorkAsMapKeys() {
            java.util.Map<EventId, String> eventMap = new java.util.HashMap<>();

            TestEventId classId = new TestEventId("class-id");
            TestEventEnum enumId = TestEventEnum.FIRST_EVENT;

            eventMap.put(classId, "class-based");
            eventMap.put(enumId, "enum-based");

            assertThat(eventMap).hasSize(2);
            assertThat(eventMap.get(classId)).isEqualTo("class-based");
            assertThat(eventMap.get(enumId)).isEqualTo("enum-based");
        }

        @Test
        @DisplayName("should work with type checking")
        void shouldWorkWithTypeChecking() {
            EventId[] eventIds = {
                    new TestEventId("test"),
                    TestEventEnum.FIRST_EVENT,
                    new StringEventId("string")
            };

            for (EventId eventId : eventIds) {
                assertThat(eventId).isInstanceOf(EventId.class);

                if (eventId instanceof TestEventId) {
                    TestEventId testId = (TestEventId) eventId;
                    assertThat(testId.getName()).isNotNull();
                } else if (eventId instanceof TestEventEnum) {
                    TestEventEnum enumId = (TestEventEnum) eventId;
                    assertThat(enumId.name()).isNotNull();
                } else if (eventId instanceof StringEventId) {
                    StringEventId stringId = (StringEventId) eventId;
                    assertThat(stringId.getValue()).isNotNull();
                }
            }
        }
    }

    @Nested
    @DisplayName("Equality and Identity")
    class EqualityAndIdentity {

        @Test
        @DisplayName("should support proper equality for custom implementations")
        void shouldSupportProperEqualityForCustomImplementations() {
            TestEventId id1 = new TestEventId("same");
            TestEventId id2 = new TestEventId("same");
            TestEventId id3 = new TestEventId("different");

            assertThat(id1).isEqualTo(id2);
            assertThat(id1).isNotEqualTo(id3);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("should support enum identity")
        void shouldSupportEnumIdentity() {
            TestEventEnum enum1 = TestEventEnum.FIRST_EVENT;
            TestEventEnum enum2 = TestEventEnum.FIRST_EVENT;

            assertThat(enum1).isSameAs(enum2);
            assertThat(enum1).isEqualTo(enum2);
            assertThat(enum1.hashCode()).isEqualTo(enum2.hashCode());
        }

        @Test
        @DisplayName("should handle null comparisons")
        void shouldHandleNullComparisons() {
            TestEventId eventId = new TestEventId("test");

            assertThat(eventId).isNotEqualTo(null);
            assertThat(eventId.equals(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("Use Cases")
    class UseCases {

        @Test
        @DisplayName("should work with events")
        void shouldWorkWithEvents() {
            TestEventId eventId = new TestEventId("test-event");
            Event event = new SimpleEvent(eventId, "test data");

            assertThat(event.getId()).isSameAs(eventId);
            assertThat(event.getId()).isInstanceOf(EventId.class);
        }

        @Test
        @DisplayName("should provide meaningful string representation")
        void shouldProvideMeaningfulStringRepresentation() {
            TestEventId classId = new TestEventId("class-event");
            TestEventEnum enumId = TestEventEnum.FIRST_EVENT;
            StringEventId stringId = new StringEventId("string-event");

            assertThat(classId.toString()).contains("class-event");
            assertThat(enumId.toString()).isEqualTo("FIRST_EVENT");
            assertThat(stringId.toString()).contains("string-event");
        }

        @Test
        @DisplayName("should be serializable when implementation supports it")
        void shouldBeSerializableWhenImplementationSupportsIt() {
            // Test enum serialization (enums are serializable by default)
            TestEventEnum enumId = TestEventEnum.FIRST_EVENT;
            assertThat(enumId).isInstanceOf(java.io.Serializable.class);

            // Test custom serializable implementation
            SerializableEventId serializableId = new SerializableEventId("serializable");
            assertThat(serializableId).isInstanceOf(java.io.Serializable.class);
        }
    }

    // Helper classes
    private static class TestEventId implements EventId {
        private final String name;

        public TestEventId(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
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

    private enum TestEventEnum implements EventId {
        FIRST_EVENT,
        SECOND_EVENT,
        THIRD_EVENT
    }

    private static class StringEventId implements EventId {
        private final String value;

        public StringEventId(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "StringEventId{" + value + "}";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof StringEventId))
                return false;
            StringEventId other = (StringEventId) obj;
            return value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }

    private static class SerializableEventId implements EventId, java.io.Serializable {
        private static final long serialVersionUID = 1L;

        private final String id;

        public SerializableEventId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "SerializableEventId{" + id + "}";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof SerializableEventId))
                return false;
            SerializableEventId other = (SerializableEventId) obj;
            return id.equals(other.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }
}
