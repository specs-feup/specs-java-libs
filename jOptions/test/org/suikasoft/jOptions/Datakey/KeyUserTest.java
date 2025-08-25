package org.suikasoft.jOptions.Datakey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Comprehensive test suite for the {@link KeyUser} interface.
 * Tests key management, validation logic, and default interface behavior.
 * 
 * @author Generated Tests
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("KeyUser Tests")
class KeyUserTest {

    @Mock
    private DataStore mockDataStore;
    @Mock
    private StoreDefinition mockStoreDefinition;
    @Mock
    private DataKey<String> mockReadKey;
    @Mock
    private DataKey<String> mockWriteKey;
    @Mock
    private DataKey<Integer> mockKeyWithDefault;
    @Mock
    private DataKey<Boolean> mockKeyWithoutDefault;

    private DataKey<String> testReadKey;
    private DataKey<String> testWriteKey;
    private DataKey<Integer> testKeyWithDefault;
    private DataKey<Boolean> testKeyWithoutDefault;

    @BeforeEach
    void setUp() {
        testReadKey = KeyFactory.string("read_key");
        testWriteKey = KeyFactory.string("write_key");
        testKeyWithDefault = KeyFactory.integer("key_with_default", 42);
        testKeyWithoutDefault = KeyFactory.bool("key_without_default");
    }

    @Nested
    @DisplayName("Default Interface Methods Tests")
    class DefaultMethodsTests {

        @Test
        @DisplayName("Should return empty collections for default methods")
        void testDefaultMethods() {
            // given - basic implementation with defaults
            KeyUser keyUser = new KeyUser() {
            };

            // when
            Collection<DataKey<?>> readKeys = keyUser.getReadKeys();
            Collection<DataKey<?>> writeKeys = keyUser.getWriteKeys();

            // then
            assertThat(readKeys).isEmpty();
            assertThat(writeKeys).isEmpty();
        }

        @Test
        @DisplayName("Should support empty implementation")
        void testEmptyImplementation() {
            // given
            KeyUser emptyKeyUser = new KeyUser() {
            };

            // when/then - should not throw any exceptions
            assertThat(emptyKeyUser.getReadKeys()).isNotNull();
            assertThat(emptyKeyUser.getWriteKeys()).isNotNull();
            assertThat(emptyKeyUser.getReadKeys()).hasSize(0);
            assertThat(emptyKeyUser.getWriteKeys()).hasSize(0);
        }
    }

    @Nested
    @DisplayName("Custom Implementation Tests")
    class CustomImplementationTests {

        @Test
        @DisplayName("Should support custom read keys")
        void testCustomReadKeys() {
            // given
            KeyUser readKeyUser = new KeyUser() {
                @Override
                public Collection<DataKey<?>> getReadKeys() {
                    return Arrays.asList(testReadKey, testKeyWithDefault);
                }
            };

            // when
            Collection<DataKey<?>> readKeys = readKeyUser.getReadKeys();

            // then
            assertThat(readKeys).hasSize(2);
            assertThat(readKeys).contains(testReadKey, testKeyWithDefault);
        }

        @Test
        @DisplayName("Should support custom write keys")
        void testCustomWriteKeys() {
            // given
            KeyUser writeKeyUser = new KeyUser() {
                @Override
                public Collection<DataKey<?>> getWriteKeys() {
                    return Arrays.asList(testWriteKey, testKeyWithoutDefault);
                }
            };

            // when
            Collection<DataKey<?>> writeKeys = writeKeyUser.getWriteKeys();

            // then
            assertThat(writeKeys).hasSize(2);
            assertThat(writeKeys).contains(testWriteKey, testKeyWithoutDefault);
        }

        @Test
        @DisplayName("Should support both read and write keys")
        void testBothReadAndWriteKeys() {
            // given
            KeyUser fullKeyUser = new KeyUser() {
                @Override
                public Collection<DataKey<?>> getReadKeys() {
                    return Arrays.asList(testReadKey);
                }

                @Override
                public Collection<DataKey<?>> getWriteKeys() {
                    return Arrays.asList(testWriteKey);
                }
            };

            // when
            Collection<DataKey<?>> readKeys = fullKeyUser.getReadKeys();
            Collection<DataKey<?>> writeKeys = fullKeyUser.getWriteKeys();

            // then
            assertThat(readKeys).containsExactly(testReadKey);
            assertThat(writeKeys).containsExactly(testWriteKey);
        }

        @Test
        @DisplayName("Should support immutable collections")
        void testImmutableCollections() {
            // given
            KeyUser immutableKeyUser = new KeyUser() {
                @Override
                public Collection<DataKey<?>> getReadKeys() {
                    return Collections.unmodifiableList(Arrays.asList(testReadKey));
                }
            };

            // when
            Collection<DataKey<?>> readKeys = immutableKeyUser.getReadKeys();

            // then
            assertThat(readKeys).containsExactly(testReadKey);
            assertThatThrownBy(() -> readKeys.add(testWriteKey))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should fail validation when DataStore has no StoreDefinition")
        void testValidationFailsWithoutStoreDefinition() {
            // given
            KeyUser keyUser = new KeyUser() {
            };
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.empty());

            // when/then
            assertThatThrownBy(() -> keyUser.check(mockDataStore, false))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("This method requires that the DataStore has a StoreDefinition");
        }

        @Test
        @DisplayName("Should pass validation when all keys have values")
        void testValidationPassesWithAllValues() {
            // given
            KeyUser keyUser = new KeyUser() {
            };
            List<DataKey<?>> keys = Arrays.asList(testReadKey, testWriteKey);

            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys()).thenReturn(keys);
            when(mockDataStore.hasValue(testReadKey)).thenReturn(true);
            when(mockDataStore.hasValue(testWriteKey)).thenReturn(true);

            // when/then - should not throw
            keyUser.check(mockDataStore, false);

            verify(mockDataStore).hasValue(testReadKey);
            verify(mockDataStore).hasValue(testWriteKey);
        }

        @Test
        @DisplayName("Should pass validation when missing keys have defaults and defaults allowed")
        void testValidationPassesWithDefaults() {
            // given
            KeyUser keyUser = new KeyUser() {
            };
            List<DataKey<?>> keys = Arrays.asList(mockKeyWithDefault);

            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys()).thenReturn(keys);
            when(mockDataStore.hasValue(mockKeyWithDefault)).thenReturn(false);
            when(mockKeyWithDefault.hasDefaultValue()).thenReturn(true);

            // when/then - should not throw
            keyUser.check(mockDataStore, false);

            verify(mockDataStore).hasValue(mockKeyWithDefault);
        }

        @Test
        @DisplayName("Should fail validation when missing keys have no defaults")
        void testValidationFailsWithoutDefaults() {
            // given
            KeyUser keyUser = new KeyUser() {
            };
            List<DataKey<?>> keys = Arrays.asList(mockKeyWithoutDefault);

            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys()).thenReturn(keys);
            when(mockDataStore.hasValue(mockKeyWithoutDefault)).thenReturn(false);
            when(mockKeyWithoutDefault.hasDefaultValue()).thenReturn(false);
            when(mockKeyWithoutDefault.getName()).thenReturn("key_without_default");

            // when/then
            assertThatThrownBy(() -> keyUser.check(mockDataStore, false))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("DataStore check failed")
                    .hasMessageContaining("key_without_default")
                    .hasMessageContaining("defaults are enabled");
        }

        @Test
        @DisplayName("Should fail validation when defaults disabled and key missing")
        void testValidationFailsWithDefaultsDisabled() {
            // given
            KeyUser keyUser = new KeyUser() {
            };
            List<DataKey<?>> keys = Arrays.asList(mockKeyWithDefault);

            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys()).thenReturn(keys);
            when(mockDataStore.hasValue(mockKeyWithDefault)).thenReturn(false);
            when(mockKeyWithDefault.getName()).thenReturn("key_with_default");

            // when/then
            assertThatThrownBy(() -> keyUser.check(mockDataStore, true))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("DataStore check failed")
                    .hasMessageContaining("key_with_default")
                    .hasMessageContaining("defaults are disabled");
        }

        @Test
        @DisplayName("Should validate mixed scenarios correctly")
        void testMixedValidationScenario() {
            // given
            KeyUser keyUser = new KeyUser() {
            };
            List<DataKey<?>> keys = Arrays.asList(mockReadKey, mockKeyWithDefault, mockKeyWithoutDefault);

            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys()).thenReturn(keys);
            when(mockDataStore.hasValue(mockReadKey)).thenReturn(true);
            when(mockDataStore.hasValue(mockKeyWithDefault)).thenReturn(false);
            when(mockDataStore.hasValue(mockKeyWithoutDefault)).thenReturn(false);
            when(mockKeyWithDefault.hasDefaultValue()).thenReturn(true);
            when(mockKeyWithoutDefault.hasDefaultValue()).thenReturn(false);
            when(mockKeyWithoutDefault.getName()).thenReturn("key_without_default");

            // when/then - should fail because mockKeyWithoutDefault has no value and no
            // default
            assertThatThrownBy(() -> keyUser.check(mockDataStore, false))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("key_without_default");
        }
    }

    @Nested
    @DisplayName("Real-world Usage Tests")
    class RealWorldUsageTests {

        @Test
        @DisplayName("Should work with concrete implementation")
        void testConcreteImplementation() {
            // given - simulate a real class that uses keys
            class ConfigurationProcessor implements KeyUser {
                private final DataKey<String> inputFileKey = KeyFactory.string("input_file");
                private final DataKey<String> outputFileKey = KeyFactory.string("output_file");
                private final DataKey<Boolean> verboseKey = KeyFactory.bool("verbose");

                @Override
                public Collection<DataKey<?>> getReadKeys() {
                    return Arrays.asList(inputFileKey, verboseKey);
                }

                @Override
                public Collection<DataKey<?>> getWriteKeys() {
                    return Arrays.asList(outputFileKey);
                }
            }

            ConfigurationProcessor processor = new ConfigurationProcessor();

            // when
            Collection<DataKey<?>> readKeys = processor.getReadKeys();
            Collection<DataKey<?>> writeKeys = processor.getWriteKeys();

            // then
            assertThat(readKeys).hasSize(2);
            assertThat(writeKeys).hasSize(1);
            assertThat(readKeys.stream().map(DataKey::getName))
                    .contains("input_file", "verbose");
            assertThat(writeKeys.stream().map(DataKey::getName))
                    .contains("output_file");
        }

        @Test
        @DisplayName("Should support inheritance")
        void testInheritance() {
            // given
            abstract class BaseProcessor implements KeyUser {
                protected final DataKey<String> baseKey = KeyFactory.string("base_key");

                @Override
                public Collection<DataKey<?>> getReadKeys() {
                    return Arrays.asList(baseKey);
                }
            }

            class ExtendedProcessor extends BaseProcessor {
                private final DataKey<String> extendedKey = KeyFactory.string("extended_key");

                @Override
                public Collection<DataKey<?>> getReadKeys() {
                    List<DataKey<?>> keys = new java.util.ArrayList<>(super.getReadKeys());
                    keys.add(extendedKey);
                    return keys;
                }
            }

            ExtendedProcessor processor = new ExtendedProcessor();

            // when
            Collection<DataKey<?>> readKeys = processor.getReadKeys();

            // then
            assertThat(readKeys).hasSize(2);
            assertThat(readKeys.stream().map(DataKey::getName))
                    .contains("base_key", "extended_key");
        }

        @Test
        @DisplayName("Should support composition")
        void testComposition() {
            // given
            class ComponentA implements KeyUser {
                @Override
                public Collection<DataKey<?>> getReadKeys() {
                    return Arrays.asList(KeyFactory.string("component_a_key"));
                }
            }

            class ComponentB implements KeyUser {
                @Override
                public Collection<DataKey<?>> getReadKeys() {
                    return Arrays.asList(KeyFactory.string("component_b_key"));
                }
            }

            class CompositeProcessor implements KeyUser {
                private final ComponentA componentA = new ComponentA();
                private final ComponentB componentB = new ComponentB();

                @Override
                public Collection<DataKey<?>> getReadKeys() {
                    List<DataKey<?>> keys = new java.util.ArrayList<>();
                    keys.addAll(componentA.getReadKeys());
                    keys.addAll(componentB.getReadKeys());
                    return keys;
                }
            }

            CompositeProcessor processor = new CompositeProcessor();

            // when
            Collection<DataKey<?>> readKeys = processor.getReadKeys();

            // then
            assertThat(readKeys).hasSize(2);
            assertThat(readKeys.stream().map(DataKey::getName))
                    .contains("component_a_key", "component_b_key");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty key collections in validation")
        void testEmptyKeyCollections() {
            // given
            KeyUser keyUser = new KeyUser() {
            };
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys()).thenReturn(Collections.emptyList());

            // when/then - should not throw
            keyUser.check(mockDataStore, false);
            keyUser.check(mockDataStore, true);
        }

        @Test
        @DisplayName("Should handle null collections gracefully")
        void testNullCollections() {
            // given
            KeyUser keyUser = new KeyUser() {
                @Override
                public Collection<DataKey<?>> getReadKeys() {
                    return null;
                }
            };

            // when
            Collection<DataKey<?>> readKeys = keyUser.getReadKeys();

            // then
            assertThat(readKeys).isNull();
        }

        @Test
        @DisplayName("Should handle large number of keys")
        void testLargeNumberOfKeys() {
            // given
            List<DataKey<?>> manyKeys = new java.util.ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                manyKeys.add(KeyFactory.string("key_" + i));
            }

            KeyUser keyUser = new KeyUser() {
                @Override
                public Collection<DataKey<?>> getReadKeys() {
                    return manyKeys;
                }
            };

            // when
            Collection<DataKey<?>> readKeys = keyUser.getReadKeys();

            // then
            assertThat(readKeys).hasSize(1000);
        }
    }
}
