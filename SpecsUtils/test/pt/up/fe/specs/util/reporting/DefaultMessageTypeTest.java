package pt.up.fe.specs.util.reporting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link DefaultMessageType}.
 * Tests the implementation of default message types in the reporting framework.
 * 
 * @author Generated Tests
 */
@DisplayName("DefaultMessageType")
class DefaultMessageTypeTest {

    private DefaultMessageType errorType;
    private DefaultMessageType warningType;
    private DefaultMessageType infoType;

    @BeforeEach
    void setUp() {
        errorType = new DefaultMessageType("Error", ReportCategory.ERROR);
        warningType = new DefaultMessageType("Warning", ReportCategory.WARNING);
        infoType = new DefaultMessageType("Information", ReportCategory.INFORMATION);
    }

    @Nested
    @DisplayName("Constructor and Initialization")
    class ConstructorAndInitialization {

        @Test
        @DisplayName("Should create instance with name and category")
        void shouldCreateInstanceWithNameAndCategory() {
            // When
            DefaultMessageType customType = new DefaultMessageType("Custom", ReportCategory.ERROR);

            // Then
            assertThat(customType).isNotNull();
            assertThat(customType).isInstanceOf(MessageType.class);
        }

        @Test
        @DisplayName("Should store provided name")
        void shouldStoreProvidedName() {
            // When/Then
            assertThat(errorType.getName()).isEqualTo("Error");
            assertThat(warningType.getName()).isEqualTo("Warning");
            assertThat(infoType.getName()).isEqualTo("Information");
        }

        @Test
        @DisplayName("Should store provided category")
        void shouldStoreProvidedCategory() {
            // When/Then
            assertThat(errorType.getMessageCategory()).isEqualTo(ReportCategory.ERROR);
            assertThat(warningType.getMessageCategory()).isEqualTo(ReportCategory.WARNING);
            assertThat(infoType.getMessageCategory()).isEqualTo(ReportCategory.INFORMATION);
        }

        @Test
        @DisplayName("Should handle null name")
        void shouldHandleNullName() {
            // When
            DefaultMessageType nullNameType = new DefaultMessageType(null, ReportCategory.ERROR);

            // Then
            assertThat(nullNameType.getName()).isNull();
            assertThat(nullNameType.getMessageCategory()).isEqualTo(ReportCategory.ERROR);
        }

        @Test
        @DisplayName("Should handle null category")
        void shouldHandleNullCategory() {
            // When
            DefaultMessageType nullCategoryType = new DefaultMessageType("Test", null);

            // Then
            assertThat(nullCategoryType.getName()).isEqualTo("Test");
            assertThat(nullCategoryType.getMessageCategory()).isNull();
        }

        @Test
        @DisplayName("Should handle both null name and category")
        void shouldHandleBothNullNameAndCategory() {
            // When
            DefaultMessageType nullType = new DefaultMessageType(null, null);

            // Then
            assertThat(nullType.getName()).isNull();
            assertThat(nullType.getMessageCategory()).isNull();
        }
    }

    @Nested
    @DisplayName("Name Operations")
    class NameOperations {

        @Test
        @DisplayName("Should return exact name provided in constructor")
        void shouldReturnExactNameProvidedInConstructor() {
            // Given
            String[] testNames = {
                    "Simple",
                    "Complex Name With Spaces",
                    "name-with-dashes",
                    "name_with_underscores",
                    "name.with.dots",
                    "123NumericName",
                    "SpecialChars!@#$%",
                    ""
            };

            // When/Then
            for (String name : testNames) {
                DefaultMessageType type = new DefaultMessageType(name, ReportCategory.INFORMATION);
                assertThat(type.getName()).isEqualTo(name);
            }
        }

        @Test
        @DisplayName("Should handle empty string name")
        void shouldHandleEmptyStringName() {
            // When
            DefaultMessageType emptyNameType = new DefaultMessageType("", ReportCategory.WARNING);

            // Then
            assertThat(emptyNameType.getName()).isEqualTo("");
        }

        @Test
        @DisplayName("Should handle very long names")
        void shouldHandleVeryLongNames() {
            // Given
            String longName = "A".repeat(1000);

            // When
            DefaultMessageType longNameType = new DefaultMessageType(longName, ReportCategory.ERROR);

            // Then
            assertThat(longNameType.getName()).isEqualTo(longName);
            assertThat(longNameType.getName()).hasSize(1000);
        }

        @Test
        @DisplayName("Should handle unicode characters in name")
        void shouldHandleUnicodeCharactersInName() {
            // Given
            String unicodeName = "测试类型 🚨 Тест Ώμέγα";

            // When
            DefaultMessageType unicodeType = new DefaultMessageType(unicodeName, ReportCategory.WARNING);

            // Then
            assertThat(unicodeType.getName()).isEqualTo(unicodeName);
        }
    }

    @Nested
    @DisplayName("Category Operations")
    class CategoryOperations {

        @Test
        @DisplayName("Should return exact category provided in constructor")
        void shouldReturnExactCategoryProvidedInConstructor() {
            // Given
            ReportCategory[] categories = ReportCategory.values();

            // When/Then
            for (ReportCategory category : categories) {
                DefaultMessageType type = new DefaultMessageType("Test", category);
                assertThat(type.getMessageCategory()).isEqualTo(category);
            }
        }

        @Test
        @DisplayName("Should maintain category independence from name")
        void shouldMaintainCategoryIndependenceFromName() {
            // Given
            DefaultMessageType errorWithWarningName = new DefaultMessageType("Warning", ReportCategory.ERROR);
            DefaultMessageType warningWithErrorName = new DefaultMessageType("Error", ReportCategory.WARNING);

            // When/Then
            assertThat(errorWithWarningName.getName()).isEqualTo("Warning");
            assertThat(errorWithWarningName.getMessageCategory()).isEqualTo(ReportCategory.ERROR);

            assertThat(warningWithErrorName.getName()).isEqualTo("Error");
            assertThat(warningWithErrorName.getMessageCategory()).isEqualTo(ReportCategory.WARNING);
        }
    }

    @Nested
    @DisplayName("MessageType Interface Compliance")
    class MessageTypeInterfaceCompliance {

        @Test
        @DisplayName("Should implement MessageType interface")
        void shouldImplementMessageTypeInterface() {
            // When/Then
            assertThat(errorType).isInstanceOf(MessageType.class);
            assertThat(warningType).isInstanceOf(MessageType.class);
            assertThat(infoType).isInstanceOf(MessageType.class);
        }

        @Test
        @DisplayName("Should override default getName behavior")
        void shouldOverrideDefaultGetNameBehavior() {
            // Given
            DefaultMessageType type = new DefaultMessageType("CustomName", ReportCategory.ERROR);

            // When
            String name = type.getName();
            String toString = type.toString();

            // Then
            assertThat(name).isEqualTo("CustomName");
            // The name should be explicitly set, not relying on toString()
            assertThat(name).isNotEqualTo(toString);
        }

        @Test
        @DisplayName("Should properly implement getMessageCategory")
        void shouldProperlyImplementGetMessageCategory() {
            // When/Then
            assertThat(errorType.getMessageCategory()).isEqualTo(ReportCategory.ERROR);
            assertThat(warningType.getMessageCategory()).isEqualTo(ReportCategory.WARNING);
            assertThat(infoType.getMessageCategory()).isEqualTo(ReportCategory.INFORMATION);
        }
    }

    @Nested
    @DisplayName("Object Behavior")
    class ObjectBehavior {

        @Test
        @DisplayName("Should have meaningful toString")
        void shouldHaveMeaningfulToString() {
            // When/Then
            assertThat(errorType.toString()).isNotNull();
            assertThat(warningType.toString()).isNotNull();
            assertThat(infoType.toString()).isNotNull();

            // toString should use default Object implementation (class name + hashcode)
            assertThat(errorType.toString()).contains("DefaultMessageType");
        }

        @Test
        @DisplayName("Should follow equals contract")
        void shouldFollowEqualsContract() {
            // Given
            DefaultMessageType sameError1 = new DefaultMessageType("Error", ReportCategory.ERROR);
            DefaultMessageType sameError2 = new DefaultMessageType("Error", ReportCategory.ERROR);
            DefaultMessageType differentName = new DefaultMessageType("Different", ReportCategory.ERROR);
            DefaultMessageType differentCategory = new DefaultMessageType("Error", ReportCategory.WARNING);

            // When/Then - Reflexive
            assertThat(errorType).isEqualTo(errorType);

            // Different instances with same values are not equal (no equals override)
            assertThat(sameError1).isNotEqualTo(sameError2);
            assertThat(errorType).isNotEqualTo(sameError1);

            // Different values are not equal
            assertThat(errorType).isNotEqualTo(differentName);
            assertThat(errorType).isNotEqualTo(differentCategory);

            // Null comparison
            assertThat(errorType).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should have consistent hashCode")
        void shouldHaveConsistentHashCode() {
            // When/Then
            assertThat(errorType.hashCode()).isEqualTo(errorType.hashCode());
            assertThat(warningType.hashCode()).isEqualTo(warningType.hashCode());
            assertThat(infoType.hashCode()).isEqualTo(infoType.hashCode());

            // Different instances should have different hash codes (default Object
            // behavior)
            DefaultMessageType sameError = new DefaultMessageType("Error", ReportCategory.ERROR);
            assertThat(errorType.hashCode()).isNotEqualTo(sameError.hashCode());
        }
    }

    @Nested
    @DisplayName("Immutability")
    class Immutability {

        @Test
        @DisplayName("Should be immutable after construction")
        void shouldBeImmutableAfterConstruction() {
            // Given
            DefaultMessageType type = new DefaultMessageType("Original", ReportCategory.ERROR);
            String originalName = type.getName();
            ReportCategory originalCategory = type.getMessageCategory();

            // When - No setters available, so immutability is enforced by design
            // Multiple calls should return same values
            String name1 = type.getName();
            String name2 = type.getName();
            ReportCategory category1 = type.getMessageCategory();
            ReportCategory category2 = type.getMessageCategory();

            // Then
            assertThat(name1).isEqualTo(originalName);
            assertThat(name2).isEqualTo(originalName);
            assertThat(name1).isEqualTo(name2);

            assertThat(category1).isEqualTo(originalCategory);
            assertThat(category2).isEqualTo(originalCategory);
            assertThat(category1).isEqualTo(category2);
        }
    }

    @Nested
    @DisplayName("Thread Safety")
    class ThreadSafety {

        @Test
        @DisplayName("Should be thread-safe for read operations")
        void shouldBeThreadSafeForReadOperations() throws InterruptedException {
            // Given
            DefaultMessageType sharedType = new DefaultMessageType("SharedType", ReportCategory.ERROR);
            final int numThreads = 10;
            Thread[] threads = new Thread[numThreads];
            String[] names = new String[numThreads];
            ReportCategory[] categories = new ReportCategory[numThreads];

            // When
            for (int i = 0; i < numThreads; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    names[index] = sharedType.getName();
                    categories[index] = sharedType.getMessageCategory();
                });
                threads[i].start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // Then
            for (int i = 0; i < numThreads; i++) {
                assertThat(names[i]).isEqualTo("SharedType");
                assertThat(categories[i]).isEqualTo(ReportCategory.ERROR);
            }
        }
    }

    @Nested
    @DisplayName("Integration with Standard MessageTypes")
    class IntegrationWithStandardMessageTypes {

        @Test
        @DisplayName("Should be compatible with MessageType constants")
        void shouldBeCompatibleWithMessageTypeConstants() {
            // Given
            MessageType infoConstant = MessageType.INFO_TYPE;
            MessageType warningConstant = MessageType.WARNING_TYPE;
            MessageType errorConstant = MessageType.ERROR_TYPE;

            // When/Then - The constants should be DefaultMessageType instances
            assertThat(infoConstant).isInstanceOf(DefaultMessageType.class);
            assertThat(warningConstant).isInstanceOf(DefaultMessageType.class);
            assertThat(errorConstant).isInstanceOf(DefaultMessageType.class);

            // Should have correct categories
            assertThat(infoConstant.getMessageCategory()).isEqualTo(ReportCategory.INFORMATION);
            assertThat(warningConstant.getMessageCategory()).isEqualTo(ReportCategory.WARNING);
            assertThat(errorConstant.getMessageCategory()).isEqualTo(ReportCategory.ERROR);
        }

        @Test
        @DisplayName("Should work interchangeably with MessageType interface")
        void shouldWorkInterchangeablyWithMessageTypeInterface() {
            // Given
            MessageType[] types = {
                    errorType,
                    warningType,
                    infoType,
                    MessageType.ERROR_TYPE,
                    MessageType.WARNING_TYPE,
                    MessageType.INFO_TYPE
            };

            // When/Then
            for (MessageType type : types) {
                assertThat(type.getName()).isNotNull();
                assertThat(type.getMessageCategory()).isNotNull();
                assertThat(type.getMessageCategory()).isIn(
                        ReportCategory.ERROR,
                        ReportCategory.WARNING,
                        ReportCategory.INFORMATION);
            }
        }
    }
}
