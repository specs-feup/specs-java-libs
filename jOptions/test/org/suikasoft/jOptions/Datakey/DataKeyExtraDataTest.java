package org.suikasoft.jOptions.Datakey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for the {@link DataKeyExtraData} class.
 * Tests data class functionality, inheritance from ADataClass, and basic
 * operations.
 * 
 * @author Generated Tests
 */
@DisplayName("DataKeyExtraData Tests")
class DataKeyExtraDataTest {

    private DataKeyExtraData extraData;

    @BeforeEach
    void setUp() {
        extraData = new DataKeyExtraData();
    }

    @Nested
    @DisplayName("Construction Tests")
    class ConstructionTests {

        @Test
        @DisplayName("Should create with default constructor")
        void testDefaultConstructor() {
            // when
            DataKeyExtraData data = new DataKeyExtraData();

            // then
            assertThat(data).isNotNull();
            assertThat(data.getStoreDefinitionTry()).isPresent();
        }

        @Test
        @DisplayName("Should have proper class name")
        void testClassName() {
            // when
            String className = extraData.getDataClassName();

            // then
            assertThat(className).isNotNull();
            assertThat(className).contains("DataKeyExtraData");
        }
    }

    @Nested
    @DisplayName("Basic Functionality Tests")
    class BasicFunctionalityTests {

        @Test
        @DisplayName("Should create and lock instance")
        void testLocking() {
            // when
            DataKeyExtraData lockedData = extraData.lock();

            // then
            assertThat(lockedData).isSameAs(extraData);
        }

        @Test
        @DisplayName("Should have string representation")
        void testStringRepresentation() {
            // when
            String stringRep = extraData.toString();
            String getString = extraData.getString();

            // then
            assertThat(stringRep).isNotNull();
            assertThat(getString).isEqualTo(stringRep);
        }

        @Test
        @DisplayName("Should support equality comparison")
        void testEquality() {
            // given
            DataKeyExtraData other = new DataKeyExtraData();

            // when/then
            assertThat(extraData).isEqualTo(other);
            assertThat(extraData.hashCode()).isEqualTo(other.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to null")
        void testNullEquality() {
            // when/then
            assertThat(extraData).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different class")
        void testDifferentClassEquality() {
            // when/then
            assertThat(extraData).isNotEqualTo("string");
        }

        @Test
        @DisplayName("Should be equal to itself")
        void testReflexiveEquality() {
            // when/then
            assertThat(extraData).isEqualTo(extraData);
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should work with inheritance")
        void testInheritance() {
            // given - create a subclass
            class SpecialExtraData extends DataKeyExtraData {
                public void performSpecialAction() {
                    // Special behavior
                }
            }

            SpecialExtraData specialData = new SpecialExtraData();

            // when/then
            assertThat(specialData).isNotNull();
            assertThat(specialData).isInstanceOf(DataKeyExtraData.class);
            specialData.performSpecialAction(); // should not throw
        }

        @Test
        @DisplayName("Should support polymorphism")
        void testPolymorphism() {
            // given
            Object objectRef = extraData;

            // when/then
            assertThat(objectRef).isInstanceOf(DataKeyExtraData.class);
            DataKeyExtraData casted = (DataKeyExtraData) objectRef;
            assertThat(casted).isEqualTo(extraData);
        }
    }

    @Nested
    @DisplayName("Copy Operations Tests")
    class CopyOperationsTests {

        @Test
        @DisplayName("Should copy from another instance")
        void testCopyingValues() {
            // given
            DataKeyExtraData sourceData = new DataKeyExtraData();

            // when
            DataKeyExtraData result = extraData.set(sourceData);

            // then
            assertThat(result).isSameAs(extraData);
            assertThat(extraData).isEqualTo(sourceData);
        }

        @Test
        @DisplayName("Should prevent copying when locked")
        void testCopyingWhenLocked() {
            // given
            DataKeyExtraData sourceData = new DataKeyExtraData();
            extraData.lock();

            // when/then
            assertThatThrownBy(() -> extraData.set(sourceData))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("is locked");
        }
    }

    @Nested
    @DisplayName("Advanced Usage Tests")
    class AdvancedUsageTests {

        @Test
        @DisplayName("Should handle complex operations")
        void testComplexOperations() {
            // given
            DataKeyExtraData data1 = new DataKeyExtraData();
            DataKeyExtraData data2 = new DataKeyExtraData();

            // when - complex operations
            data2.set(data1) // copy from data1
                    .lock(); // lock it

            // then
            assertThat(data2).isEqualTo(data1);
            assertThatThrownBy(() -> data2.set(data1))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should maintain object identity after operations")
        void testObjectIdentity() {
            // given
            DataKeyExtraData original = extraData;

            // when
            DataKeyExtraData result = extraData.lock();

            // then
            assertThat(result).isSameAs(original);
            assertThat(result).isSameAs(extraData);
        }
    }

    @Nested
    @DisplayName("Store Definition Tests")
    class StoreDefinitionTests {

        @Test
        @DisplayName("Should have store definition")
        void testStoreDefinition() {
            // when/then
            assertThat(extraData.getStoreDefinitionTry()).isPresent();
        }

        @Test
        @DisplayName("Should provide data class name")
        void testDataClassName() {
            // when
            String name = extraData.getDataClassName();

            // then
            assertThat(name).isNotNull();
            assertThat(name).isNotEmpty();
        }
    }
}
