package org.suikasoft.jOptions.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.App;
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Unit tests for {@link CommandLineUtils}.
 * 
 * Tests command-line argument parsing, help generation, and application
 * launching for jOptions-based applications.
 * 
 * @author Generated Tests
 */
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("CommandLineUtils")
class CommandLineUtilsTest {

    @TempDir
    File tempDir;

    private CommandLineUtils commandLineUtils;
    private StoreDefinition mockStoreDefinition;
    private DataStore mockDataStore;
    private App mockApp;
    private AppPersistence mockPersistence;
    private DataKey<String> mockStringKey;
    private DataKey<Integer> mockIntKey;
    private StringCodec<String> mockStringCodec;
    private StringCodec<Integer> mockIntCodec;
    private Map<String, DataKey<?>> keyMap;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        mockStoreDefinition = mock(StoreDefinition.class);
        mockDataStore = mock(DataStore.class);
        mockApp = mock(App.class);
        mockPersistence = mock(AppPersistence.class);
        mockStringKey = mock(DataKey.class);
        mockIntKey = mock(DataKey.class);
        mockStringCodec = mock(StringCodec.class);
        mockIntCodec = mock(StringCodec.class);

        commandLineUtils = new CommandLineUtils(mockStoreDefinition);

        // Setup key map
        keyMap = new HashMap<>();
        keyMap.put("stringKey", mockStringKey);
        keyMap.put("intKey", mockIntKey);

        when(mockStoreDefinition.getKeyMap()).thenReturn(keyMap);
        when(mockStoreDefinition.getName()).thenReturn("TestDefinition");
        when(mockStoreDefinition.getKeys()).thenReturn(new ArrayList<>(keyMap.values()));

        // Setup mock keys
        when(mockStringKey.getDecoder()).thenReturn(Optional.of(mockStringCodec));
        when(mockStringKey.toString()).thenReturn("stringKey");
        when(mockStringKey.getLabel()).thenReturn("String Key");

        when(mockIntKey.getDecoder()).thenReturn(Optional.of(mockIntCodec));
        when(mockIntKey.toString()).thenReturn("intKey");
        when(mockIntKey.getLabel()).thenReturn("Integer Key");

        // Setup app
        when(mockApp.getDefinition()).thenReturn(mockStoreDefinition);
        when(mockApp.getName()).thenReturn("TestApp");
        when(mockApp.getPersistence()).thenReturn(mockPersistence);
    }

    @Nested
    @DisplayName("Application Launch")
    class ApplicationLaunchTests {

        @Test
        @DisplayName("launch with empty args shows help and returns false")
        void testLaunch_EmptyArgs_ShowsHelpAndReturnsFalse() {
            List<String> emptyArgs = Collections.emptyList();

            try (MockedStatic<SpecsLogs> logsMock = mockStatic(SpecsLogs.class)) {
                boolean result = CommandLineUtils.launch(mockApp, emptyArgs);

                assertThat(result).isFalse();
                logsMock.verify(() -> SpecsLogs.msgInfo(any(String.class)), times(2)); // App name and help message
            }
        }

        @Test
        @DisplayName("launch with write command creates default config")
        void testLaunch_WriteCommand_CreatesDefaultConfig() {
            List<String> writeArgs = Arrays.asList("write");

            try (MockedStatic<SpecsLogs> logsMock = mockStatic(SpecsLogs.class);
                    MockedStatic<DataStore> dataStoreMock = mockStatic(DataStore.class)) {

                dataStoreMock.when(() -> DataStore.newInstance(mockStoreDefinition))
                        .thenReturn(mockDataStore);

                boolean result = CommandLineUtils.launch(mockApp, writeArgs);

                assertThat(result).isTrue();
                verify(mockPersistence).saveData(any(File.class), eq(mockDataStore), eq(false));
                logsMock.verify(() -> SpecsLogs.msgInfo(any(String.class))); // Success message
            }
        }

        @Test
        @DisplayName("launch with help flag shows help and returns true")
        void testLaunch_HelpFlag_ShowsHelpAndReturnsTrue() {
            List<String> helpArgs = Arrays.asList("--help");

            try (MockedStatic<SpecsLogs> logsMock = mockStatic(SpecsLogs.class)) {
                boolean result = CommandLineUtils.launch(mockApp, helpArgs);

                assertThat(result).isTrue();
                logsMock.verify(() -> SpecsLogs.msgInfo(any(String.class)), times(2)); // App name and help message
            }
        }

        @Test
        @DisplayName("launch with help flag mixed with other args shows help")
        void testLaunch_HelpFlagMixedWithOtherArgs_ShowsHelp() {
            List<String> mixedArgs = Arrays.asList("arg1", "--help", "arg2");

            try (MockedStatic<SpecsLogs> logsMock = mockStatic(SpecsLogs.class)) {
                boolean result = CommandLineUtils.launch(mockApp, mixedArgs);

                assertThat(result).isTrue();
                logsMock.verify(() -> SpecsLogs.msgInfo(any(String.class)), times(2)); // App name and help message
            }
        }

        @Test
        @DisplayName("launch with normal args delegates to AppLauncher")
        void testLaunch_NormalArgs_DelegatesToAppLauncher() {
            List<String> normalArgs = Arrays.asList("stringKey=value", "intKey=42");

            try (MockedConstruction<AppLauncher> launcherMock = mockConstruction(AppLauncher.class,
                    (mock, context) -> {
                        when(mock.launch(normalArgs)).thenReturn(true);
                    })) {

                boolean result = CommandLineUtils.launch(mockApp, normalArgs);

                assertThat(result).isTrue();
                assertThat(launcherMock.constructed()).hasSize(1);
                verify(launcherMock.constructed().get(0)).launch(normalArgs);
            }
        }
    }

    @Nested
    @DisplayName("Command Line Argument Parsing")
    class ArgumentParsingTests {

        @Test
        @DisplayName("addArgs processes valid key-value pairs")
        void testAddArgs_ValidKeyValuePairs_ProcessesCorrectly() {
            List<String> args = Arrays.asList("stringKey=testValue", "intKey=42");

            when(mockStringCodec.decode("testValue")).thenReturn("testValue");
            when(mockIntCodec.decode("42")).thenReturn(42);

            commandLineUtils.addArgs(mockDataStore, args);

            verify(mockDataStore).setRaw(mockStringKey, "testValue");
            verify(mockDataStore).setRaw(mockIntKey, 42);
        }

        @Test
        @DisplayName("addArgs handles malformed key-value pairs")
        void testAddArgs_MalformedKeyValuePairs_HandlesGracefully() {
            List<String> args = Arrays.asList("invalidArg", "stringKey=testValue");

            when(mockStringCodec.decode("testValue")).thenReturn("testValue");

            try (MockedStatic<SpecsLogs> logsMock = mockStatic(SpecsLogs.class)) {
                commandLineUtils.addArgs(mockDataStore, args);

                // Should process valid arg and log warning for invalid
                verify(mockDataStore).setRaw(mockStringKey, "testValue");
                logsMock.verify(() -> SpecsLogs.msgInfo(any(String.class))); // Warning message
            }
        }

        @Test
        @DisplayName("addArgs handles unknown keys")
        void testAddArgs_UnknownKeys_HandlesGracefully() {
            List<String> args = Arrays.asList("unknownKey=value", "stringKey=testValue");

            when(mockStringCodec.decode("testValue")).thenReturn("testValue");

            try (MockedStatic<SpecsLogs> logsMock = mockStatic(SpecsLogs.class)) {
                commandLineUtils.addArgs(mockDataStore, args);

                // Should process valid key and log warning for unknown
                verify(mockDataStore).setRaw(mockStringKey, "testValue");
                logsMock.verify(() -> SpecsLogs.msgInfo(any(String.class))); // Warning message
            }
        }

        @Test
        @DisplayName("addArgs handles keys without decoders")
        void testAddArgs_KeysWithoutDecoders_HandlesGracefully() {
            @SuppressWarnings("unchecked")
            DataKey<String> keyWithoutDecoder = mock(DataKey.class);
            when(keyWithoutDecoder.getDecoder()).thenReturn(Optional.empty());
            keyMap.put("noDecoderKey", keyWithoutDecoder);

            List<String> args = Arrays.asList("noDecoderKey=value");

            try (MockedStatic<SpecsLogs> logsMock = mockStatic(SpecsLogs.class)) {
                commandLineUtils.addArgs(mockDataStore, args);

                logsMock.verify(() -> SpecsLogs.msgInfo(any(String.class))); // Warning message
            }
        }

        @Test
        @DisplayName("addArgs throws exception for empty key string")
        void testAddArgs_EmptyKeyString_ThrowsException() {
            List<String> args = Arrays.asList("=value");

            assertThatThrownBy(() -> commandLineUtils.addArgs(mockDataStore, args))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("empty key string");
        }
    }

    @Nested
    @DisplayName("Help Generation")
    class HelpGenerationTests {

        @Test
        @DisplayName("getHelp generates proper help message")
        void testGetHelp_GeneratesProperHelpMessage() {
            String help = CommandLineUtils.getHelp(mockStoreDefinition);

            assertThat(help)
                    .contains("Use: <OPTION>/<SUBOPTION1>/...=<VALUE>")
                    .contains("Available options:")
                    .contains("stringKey")
                    .contains("intKey");
        }

        @Test
        @DisplayName("getHelp includes key labels when available")
        void testGetHelp_IncludesKeyLabelsWhenAvailable() {
            String help = CommandLineUtils.getHelp(mockStoreDefinition);

            assertThat(help)
                    .contains("String Key")
                    .contains("Integer Key");
        }

        @Test
        @DisplayName("getHelp handles empty store definition")
        void testGetHelp_EmptyStoreDefinition_HandlesGracefully() {
            StoreDefinition emptyDefinition = mock(StoreDefinition.class);
            when(emptyDefinition.getKeys()).thenReturn(Collections.emptyList());

            String help = CommandLineUtils.getHelp(emptyDefinition);

            assertThat(help)
                    .contains("Use: <OPTION>/<SUBOPTION1>/...=<VALUE>")
                    .contains("Available options:");
        }

        @Test
        @DisplayName("getHelp handles keys with empty labels")
        void testGetHelp_KeysWithEmptyLabels_HandlesGracefully() {
            @SuppressWarnings("unchecked")
            DataKey<String> keyWithEmptyLabel = mock(DataKey.class);
            when(keyWithEmptyLabel.toString()).thenReturn("emptyLabelKey");
            when(keyWithEmptyLabel.getLabel()).thenReturn("");

            Collection<DataKey<?>> keysWithEmptyLabel = Arrays.asList(keyWithEmptyLabel);
            StoreDefinition definitionWithEmptyLabel = mock(StoreDefinition.class);
            when(definitionWithEmptyLabel.getKeys()).thenReturn(new ArrayList<>(keysWithEmptyLabel));

            String help = CommandLineUtils.getHelp(definitionWithEmptyLabel);

            assertThat(help).contains("emptyLabelKey");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("CommandLineUtils constructor handles null definition")
        void testConstructor_NullDefinition_AllowsNull() {
            // Constructor doesn't validate inputs, so null is allowed
            CommandLineUtils utils = new CommandLineUtils(null);
            assertThat(utils).isNotNull();
        }

        @Test
        @DisplayName("launch handles null app")
        void testLaunch_NullApp_ThrowsException() {
            List<String> args = Arrays.asList("arg1");

            assertThatThrownBy(() -> CommandLineUtils.launch(null, args))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("App cannot be null");
        }

        @Test
        @DisplayName("launch handles null args")
        void testLaunch_NullArgs_ThrowsException() {
            assertThatThrownBy(() -> CommandLineUtils.launch(mockApp, null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("addArgs handles null dataStore")
        void testAddArgs_NullDataStore_ThrowsException() {
            List<String> args = Arrays.asList("stringKey=value");

            assertThatThrownBy(() -> commandLineUtils.addArgs(null, args))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("addArgs handles null args")
        void testAddArgs_NullArgs_ThrowsException() {
            assertThatThrownBy(() -> commandLineUtils.addArgs(mockDataStore, null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("addArgs handles args with special characters")
        void testAddArgs_ArgsWithSpecialCharacters_HandlesCorrectly() {
            List<String> args = Arrays.asList("stringKey=value with spaces", "intKey=42");

            when(mockStringCodec.decode("value with spaces")).thenReturn("value with spaces");
            when(mockIntCodec.decode("42")).thenReturn(42);

            commandLineUtils.addArgs(mockDataStore, args);

            verify(mockDataStore).setRaw(mockStringKey, "value with spaces");
            verify(mockDataStore).setRaw(mockIntKey, 42);
        }

        @Test
        @DisplayName("addArgs handles args with multiple equals signs")
        void testAddArgs_ArgsWithMultipleEqualsigns_HandlesCorrectly() {
            List<String> args = Arrays.asList("stringKey=value=with=equals");

            when(mockStringCodec.decode("value=with=equals")).thenReturn("value=with=equals");

            commandLineUtils.addArgs(mockDataStore, args);

            verify(mockDataStore).setRaw(mockStringKey, "value=with=equals");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Full command line processing workflow")
        void testFullCommandLineProcessingWorkflow() {
            // Setup comprehensive test scenario
            List<String> complexArgs = Arrays.asList(
                    "stringKey=complexValue",
                    "intKey=123",
                    "unknownKey=ignored",
                    "malformedArg");

            when(mockStringCodec.decode("complexValue")).thenReturn("complexValue");
            when(mockIntCodec.decode("123")).thenReturn(123);

            try (MockedStatic<SpecsLogs> logsMock = mockStatic(SpecsLogs.class)) {
                commandLineUtils.addArgs(mockDataStore, complexArgs);

                // Verify valid args processed
                verify(mockDataStore).setRaw(mockStringKey, "complexValue");
                verify(mockDataStore).setRaw(mockIntKey, 123);

                // Verify warnings logged for invalid args
                logsMock.verify(() -> SpecsLogs.msgInfo(any(String.class)), times(2)); // Warning messages
            }
        }

        @Test
        @DisplayName("Help generation for complex store definition")
        void testHelpGenerationForComplexStoreDefinition() {
            // Add more keys to test comprehensive help generation
            @SuppressWarnings("unchecked")
            DataKey<Boolean> boolKey = mock(DataKey.class);
            when(boolKey.toString()).thenReturn("boolKey");
            when(boolKey.getLabel()).thenReturn("Boolean Option");
            keyMap.put("boolKey", boolKey);

            // Update the mock to return the updated keyMap
            when(mockStoreDefinition.getKeys()).thenReturn(new ArrayList<>(keyMap.values()));

            String help = CommandLineUtils.getHelp(mockStoreDefinition);

            assertThat(help)
                    .contains("stringKey")
                    .contains("String Key")
                    .contains("intKey")
                    .contains("Integer Key")
                    .contains("boolKey")
                    .contains("Boolean Option");
        }
    }
}
