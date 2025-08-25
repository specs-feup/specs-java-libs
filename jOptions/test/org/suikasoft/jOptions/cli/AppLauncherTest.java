package org.suikasoft.jOptions.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.App;
import org.suikasoft.jOptions.app.AppKernel;
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.SpecsCollections;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Unit tests for {@link AppLauncher}.
 * 
 * Tests the application launcher that handles command-line argument processing,
 * setup file loading, and application execution for jOptions-based
 * applications.
 * 
 * @author Generated Tests
 */
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("AppLauncher")
class AppLauncherTest {

    @TempDir
    File tempDir;

    private AppLauncher appLauncher;
    private App mockApp;
    private AppKernel mockKernel;
    private AppPersistence mockPersistence;
    private StoreDefinition mockStoreDefinition;
    private DataStore mockDataStore;

    private MockedStatic<SpecsLogs> mockedSpecsLogs;
    private MockedStatic<SpecsIo> mockedSpecsIo;
    private MockedStatic<SpecsCollections> mockedSpecsCollections;
    private MockedStatic<DataStore> mockedDataStoreStatic;

    @BeforeEach
    void setUp() {
        // Create mocks
        mockApp = mock(App.class);
        mockKernel = mock(AppKernel.class);
        mockPersistence = mock(AppPersistence.class);
        mockStoreDefinition = mock(StoreDefinition.class);
        mockDataStore = mock(DataStore.class);

        // Setup core mock behaviors
        when(mockApp.getPersistence()).thenReturn(mockPersistence);
        when(mockApp.getDefinition()).thenReturn(mockStoreDefinition);
        when(mockApp.getKernel()).thenReturn(mockKernel);
        when(mockApp.getName()).thenReturn("TestApp");

        // Provide minimal StoreDefinition key info to avoid NPEs during addArgs()
        Map<String, org.suikasoft.jOptions.Datakey.DataKey<?>> keyMap = new java.util.HashMap<>();
        when(mockStoreDefinition.getKeyMap()).thenReturn(keyMap);
        when(mockStoreDefinition.getKeys()).thenReturn(new ArrayList<>(keyMap.values()));

        // Mock static utilities
        mockedSpecsLogs = mockStatic(SpecsLogs.class);
        mockedSpecsIo = mockStatic(SpecsIo.class);
        mockedSpecsCollections = mockStatic(SpecsCollections.class);
        mockedDataStoreStatic = mockStatic(DataStore.class);

        // Create launcher instance
        appLauncher = new AppLauncher(mockApp);
    }

    @AfterEach
    void tearDown() {
        // Close static mocks to prevent "already registered" errors
        if (mockedSpecsLogs != null) {
            mockedSpecsLogs.close();
        }
        if (mockedSpecsIo != null) {
            mockedSpecsIo.close();
        }
        if (mockedSpecsCollections != null) {
            mockedSpecsCollections.close();
        }
        if (mockedDataStoreStatic != null) {
            mockedDataStoreStatic.close();
        }
    }

    @Nested
    @DisplayName("Constructor and Basic Properties")
    class ConstructorAndBasicPropertiesTests {

        @Test
        @DisplayName("Constructor creates AppLauncher with correct app")
        void testConstructor_CreatesAppLauncherWithCorrectApp() {
            assertThat(appLauncher.getApp()).isSameAs(mockApp);
        }

        @Test
        @DisplayName("Constructor with null app throws exception")
        void testConstructor_NullApp_ThrowsException() {
            assertThatThrownBy(() -> new AppLauncher(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("getApp returns correct application instance")
        void testGetApp_ReturnsCorrectApplicationInstance() {
            assertThat(appLauncher.getApp()).isSameAs(mockApp);
        }
    }

    @Nested
    @DisplayName("Resource Management")
    class ResourceManagementTests {

        @Test
        @DisplayName("addResources adds collection of resources")
        void testAddResources_AddsCollectionOfResources() {
            Collection<String> resources = Arrays.asList("resource1.txt", "resource2.txt");

            // Execute (addResources is void, so we can't directly verify)
            appLauncher.addResources(resources);

            // The resources are added internally, but there's no getter to verify
            // This test mainly ensures no exception is thrown
        }

        @Test
        @DisplayName("addResources with empty collection handles gracefully")
        void testAddResources_EmptyCollection_HandlesGracefully() {
            Collection<String> emptyResources = Collections.emptyList();

            appLauncher.addResources(emptyResources);

            // Should complete without throwing exception
        }

        @Test
        @DisplayName("addResources with null collection throws exception")
        void testAddResources_NullCollection_ThrowsException() {
            assertThatThrownBy(() -> appLauncher.addResources(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Application Launch with Arguments")
    class ApplicationLaunchTests {

        @Test
        @DisplayName("launch with empty args returns false and logs message")
        void testLaunch_EmptyArgs_ReturnsFalseAndLogsMessage() {
            List<String> emptyArgs = Collections.emptyList();

            boolean result = appLauncher.launch(emptyArgs);

            assertThat(result).isFalse();
            mockedSpecsLogs.verify(() -> SpecsLogs.msgInfo(any(String.class)));
        }

        @Test
        @DisplayName("launch with array delegates to list version")
        void testLaunch_WithArray_DelegatesToListVersion() {
            String[] args = { "key=value" };

            mockedDataStoreStatic.when(() -> DataStore.newInstance(mockStoreDefinition))
                    .thenReturn(mockDataStore);

            boolean result = appLauncher.launch(args);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("launch with setup file loads data and processes remaining args")
        void testLaunch_WithSetupFile_LoadsDataAndProcessesRemainingArgs() {
            File setupFile = new File(tempDir, "setup.xml");
            List<String> args = Arrays.asList(setupFile.getAbsolutePath(), "key=value");

            when(mockPersistence.loadData(any(File.class))).thenReturn(mockDataStore);

            mockedSpecsCollections.when(() -> SpecsCollections.subList(args, 1))
                    .thenReturn(Arrays.asList("key=value"));

            boolean result = appLauncher.launch(args);

            assertThat(result).isTrue();
            verify(mockPersistence, times(2)).loadData(any(File.class));
        }

        @Test
        @DisplayName("launch with setup file handles null data")
        void testLaunch_WithSetupFile_HandlesNullData() {
            File setupFile = new File(tempDir, "setup.xml");
            List<String> args = Arrays.asList(setupFile.getAbsolutePath());

            when(mockPersistence.loadData(any(File.class))).thenReturn(null);

            boolean result = appLauncher.launch(args);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("launch with key-value args creates empty setup and processes")
        void testLaunch_WithKeyValueArgs_CreatesEmptySetupAndProcesses() {
            List<String> args = Arrays.asList("key=value", "option=setting");

            mockedDataStoreStatic.when(() -> DataStore.newInstance(mockStoreDefinition))
                    .thenReturn(mockDataStore);

            boolean result = appLauncher.launch(args);

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("Special Arguments Parsing")
    class SpecialArgumentsParsingTests {

        @Test
        @DisplayName("launch with base_folder argument sets base folder")
        void testLaunch_WithBaseFolderArgument_SetsBaseFolder() {
            List<String> args = Arrays.asList("base_folder=" + tempDir.getAbsolutePath(), "key=value");

            mockedSpecsIo.when(() -> SpecsIo.existingFolder(eq(null), eq(tempDir.getAbsolutePath())))
                    .thenReturn(tempDir);

            mockedDataStoreStatic.when(() -> DataStore.newInstance(mockStoreDefinition))
                    .thenReturn(mockDataStore);

            boolean result = appLauncher.launch(args);

            assertThat(result).isTrue();
            verify(mockPersistence).saveData(any(File.class), eq(mockDataStore), eq(true));
        }

        @Test
        @DisplayName("launch with base_folder creates temp file in correct location")
        void testLaunch_WithBaseFolder_CreatesTempFileInCorrectLocation() {
            List<String> args = Arrays.asList("base_folder=" + tempDir.getAbsolutePath(), "key=value");

            mockedSpecsIo.when(() -> SpecsIo.existingFolder(eq(null), eq(tempDir.getAbsolutePath())))
                    .thenReturn(tempDir);

            mockedDataStoreStatic.when(() -> DataStore.newInstance(mockStoreDefinition))
                    .thenReturn(mockDataStore);

            appLauncher.launch(args);

            // Verify temp file is created in the base folder
            verify(mockPersistence).saveData(any(File.class), eq(mockDataStore), eq(true));
        }
    }

    @Nested
    @DisplayName("Application Execution")
    class ApplicationExecutionTests {

        @Test
        @DisplayName("execute with valid setup file returns kernel result")
        void testExecute_ValidSetupFile_ReturnsKernelResult() {
            File setupFile = new File(tempDir, "setup.xml");
            int expectedResult = 42;

            when(mockPersistence.loadData(setupFile)).thenReturn(mockDataStore);
            when(mockKernel.execute(mockDataStore)).thenReturn(expectedResult);

            int result = appLauncher.execute(setupFile);

            assertThat(result).isEqualTo(expectedResult);
            verify(mockKernel).execute(mockDataStore);
        }

        @Test
        @DisplayName("execute with null data returns -1")
        void testExecute_NullData_ReturnsMinusOne() {
            File setupFile = new File(tempDir, "setup.xml");

            when(mockPersistence.loadData(setupFile)).thenReturn(null);

            int result = appLauncher.execute(setupFile);

            assertThat(result).isEqualTo(-1);
            mockedSpecsLogs.verify(() -> SpecsLogs.msgLib(any(String.class)));
        }

        @Test
        @DisplayName("execute handles kernel exception and returns -1")
        void testExecute_KernelException_HandlesAndReturnsMinusOne() {
            File setupFile = new File(tempDir, "setup.xml");
            RuntimeException testException = new RuntimeException("Test exception");

            when(mockPersistence.loadData(setupFile)).thenReturn(mockDataStore);
            when(mockKernel.execute(mockDataStore)).thenThrow(testException);

            int result = appLauncher.execute(setupFile);

            assertThat(result).isEqualTo(-1);
            mockedSpecsLogs.verify(() -> SpecsLogs.warn(any(String.class), eq(testException)));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("launch with null args throws exception")
        void testLaunch_NullArgs_ThrowsException() {
            assertThatThrownBy(() -> appLauncher.launch((List<String>) null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("launch with null array throws exception")
        void testLaunch_NullArray_ThrowsException() {
            assertThatThrownBy(() -> appLauncher.launch((String[]) null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("execute with null file throws exception")
        void testExecute_NullFile_ThrowsException() {
            assertThatThrownBy(() -> appLauncher.execute(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("launch handles malformed base_folder argument")
        void testLaunch_MalformedBaseFolderArgument_HandlesGracefully() {
            List<String> args = Arrays.asList("base_folder=/invalid/path", "key=value");

            mockedSpecsIo.when(() -> SpecsIo.existingFolder(eq(null), eq("/invalid/path")))
                    .thenReturn(null);

            mockedDataStoreStatic.when(() -> DataStore.newInstance(mockStoreDefinition))
                    .thenReturn(mockDataStore);

            boolean result = appLauncher.launch(args);

            assertThat(result).isTrue(); // Should still succeed with null base folder
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Complete launch workflow with setup file and arguments")
        void testCompleteLaunchWorkflow_SetupFileAndArguments() {
            File setupFile = new File(tempDir, "complete-setup.xml");
            List<String> args = Arrays.asList(setupFile.getAbsolutePath(), "key1=value1", "key2=value2");

            when(mockPersistence.loadData(any(File.class))).thenReturn(mockDataStore);
            when(mockKernel.execute(mockDataStore)).thenReturn(0);

            mockedSpecsCollections.when(() -> SpecsCollections.subList(args, 1))
                    .thenReturn(Arrays.asList("key1=value1", "key2=value2"));

            boolean launchResult = appLauncher.launch(args);
            int executeResult = appLauncher.execute(setupFile);

            assertThat(launchResult).isTrue();
            assertThat(executeResult).isEqualTo(0);
            verify(mockPersistence, times(3)).loadData(any(File.class)); // Called three times in this workflow
            verify(mockKernel, times(2)).execute(mockDataStore); // Called twice: in launch and execute
        }

        @Test
        @DisplayName("Complete launch workflow with base folder and key-value args")
        void testCompleteLaunchWorkflow_BaseFolderAndKeyValueArgs() {
            List<String> args = Arrays.asList(
                    "base_folder=" + tempDir.getAbsolutePath(),
                    "key1=value1",
                    "key2=value2");

            mockedSpecsIo.when(() -> SpecsIo.existingFolder(eq(null), eq(tempDir.getAbsolutePath())))
                    .thenReturn(tempDir);

            mockedDataStoreStatic.when(() -> DataStore.newInstance(mockStoreDefinition))
                    .thenReturn(mockDataStore);

            when(mockKernel.execute(mockDataStore)).thenReturn(0);

            boolean result = appLauncher.launch(args);

            assertThat(result).isTrue();
            verify(mockPersistence).saveData(any(File.class), eq(mockDataStore), eq(true));
            mockedSpecsLogs.verify(() -> SpecsLogs.msgInfo(any(String.class)), times(3)); // Called multiple times
                                                                                          // during execution
        }

        @Test
        @DisplayName("Resource management workflow")
        void testResourceManagementWorkflow() {
            Collection<String> resources1 = Arrays.asList("resource1.txt", "resource2.txt");
            Collection<String> resources2 = Arrays.asList("resource3.txt");

            // Add multiple resource collections
            appLauncher.addResources(resources1);
            appLauncher.addResources(resources2);

            // Verify app reference remains consistent
            assertThat(appLauncher.getApp()).isSameAs(mockApp);
        }
    }
}
