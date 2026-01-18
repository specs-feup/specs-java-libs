package org.suikasoft.jOptions.app;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * Comprehensive test suite for the AppPersistence interface.
 * Tests all interface methods and typical implementation scenarios.
 * 
 * @author Generated Tests
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AppPersistence Interface Tests")
class AppPersistenceTest {

    @Mock
    private DataStore mockDataStore;

    @TempDir
    private Path tempDir;

    /**
     * Test implementation of AppPersistence for testing purposes.
     */
    private static class TestAppPersistence implements AppPersistence {
        private final DataStore loadResult;
        private final boolean saveResult;
        private File lastLoadFile;
        private File lastSaveFile;
        private DataStore lastSaveData;
        private Boolean lastKeepConfigFile;
        private boolean loadWasCalled = false;
        private boolean saveWasCalled = false;

        public TestAppPersistence(DataStore loadResult, boolean saveResult) {
            this.loadResult = loadResult;
            this.saveResult = saveResult;
        }

        @Override
        public DataStore loadData(File file) {
            this.loadWasCalled = true;
            this.lastLoadFile = file;
            return loadResult;
        }

        @Override
        public boolean saveData(File file, DataStore data, boolean keepConfigFile) {
            this.saveWasCalled = true;
            this.lastSaveFile = file;
            this.lastSaveData = data;
            this.lastKeepConfigFile = keepConfigFile;
            return saveResult;
        }

        public File getLastLoadFile() {
            return lastLoadFile;
        }

        public File getLastSaveFile() {
            return lastSaveFile;
        }

        public DataStore getLastSaveData() {
            return lastSaveData;
        }

        public Boolean getLastKeepConfigFile() {
            return lastKeepConfigFile;
        }

        public boolean wasLoadCalled() {
            return loadWasCalled;
        }

        public boolean wasSaveCalled() {
            return saveWasCalled;
        }
    }

    /**
     * Test implementation that throws exceptions.
     */
    private static class ExceptionThrowingAppPersistence implements AppPersistence {
        private final RuntimeException loadException;
        private final RuntimeException saveException;

        public ExceptionThrowingAppPersistence(RuntimeException loadException, RuntimeException saveException) {
            this.loadException = loadException;
            this.saveException = saveException;
        }

        @Override
        public DataStore loadData(File file) {
            if (loadException != null) {
                throw loadException;
            }
            return null;
        }

        @Override
        public boolean saveData(File file, DataStore data, boolean keepConfigFile) {
            if (saveException != null) {
                throw saveException;
            }
            return false;
        }
    }

    private TestAppPersistence testPersistence;
    private File testFile;

    @BeforeEach
    void setUp() throws IOException {
        testPersistence = new TestAppPersistence(mockDataStore, true);
        testFile = tempDir.resolve("test-config.xml").toFile();
        testFile.createNewFile();
    }

    @Nested
    @DisplayName("Load Data Tests")
    class LoadDataTests {

        @Test
        @DisplayName("Should load data from valid file")
        void testLoadData_WithValidFile_ReturnsDataStore() {
            // when
            DataStore result = testPersistence.loadData(testFile);

            // then
            assertThat(result).isSameAs(mockDataStore);
            assertThat(testPersistence.wasLoadCalled()).isTrue();
            assertThat(testPersistence.getLastLoadFile()).isSameAs(testFile);
        }

        @Test
        @DisplayName("Should handle null file")
        void testLoadData_WithNullFile_HandlesNullGracefully() {
            // when
            DataStore result = testPersistence.loadData(null);

            // then
            assertThat(result).isSameAs(mockDataStore);
            assertThat(testPersistence.wasLoadCalled()).isTrue();
            assertThat(testPersistence.getLastLoadFile()).isNull();
        }

        @Test
        @DisplayName("Should handle non-existent file")
        void testLoadData_WithNonExistentFile_CallsLoadMethod() {
            // given
            File nonExistentFile = tempDir.resolve("non-existent.xml").toFile();

            // when
            DataStore result = testPersistence.loadData(nonExistentFile);

            // then
            assertThat(result).isSameAs(mockDataStore);
            assertThat(testPersistence.wasLoadCalled()).isTrue();
            assertThat(testPersistence.getLastLoadFile()).isSameAs(nonExistentFile);
        }

        @Test
        @DisplayName("Should handle load returning null")
        void testLoadData_ReturningNull_ReturnsNull() {
            // given
            TestAppPersistence nullPersistence = new TestAppPersistence(null, true);

            // when
            DataStore result = nullPersistence.loadData(testFile);

            // then
            assertThat(result).isNull();
            assertThat(nullPersistence.wasLoadCalled()).isTrue();
        }

        @Test
        @DisplayName("Should propagate exceptions during load")
        void testLoadData_ThrowsException_PropagatesException() {
            // given
            RuntimeException testException = new RuntimeException("Load failed");
            ExceptionThrowingAppPersistence exceptionPersistence = new ExceptionThrowingAppPersistence(testException,
                    null);

            // when & then
            assertThatThrownBy(() -> exceptionPersistence.loadData(testFile))
                    .isSameAs(testException)
                    .hasMessage("Load failed");
        }
    }

    @Nested
    @DisplayName("Save Data Tests - Full Method")
    class SaveDataFullMethodTests {

        @Test
        @DisplayName("Should save data with keep config file true")
        void testSaveData_WithKeepConfigFileTrue_SavesSuccessfully() {
            // when
            boolean result = testPersistence.saveData(testFile, mockDataStore, true);

            // then
            assertThat(result).isTrue();
            assertThat(testPersistence.wasSaveCalled()).isTrue();
            assertThat(testPersistence.getLastSaveFile()).isSameAs(testFile);
            assertThat(testPersistence.getLastSaveData()).isSameAs(mockDataStore);
            assertThat(testPersistence.getLastKeepConfigFile()).isTrue();
        }

        @Test
        @DisplayName("Should save data with keep config file false")
        void testSaveData_WithKeepConfigFileFalse_SavesSuccessfully() {
            // when
            boolean result = testPersistence.saveData(testFile, mockDataStore, false);

            // then
            assertThat(result).isTrue();
            assertThat(testPersistence.wasSaveCalled()).isTrue();
            assertThat(testPersistence.getLastSaveFile()).isSameAs(testFile);
            assertThat(testPersistence.getLastSaveData()).isSameAs(mockDataStore);
            assertThat(testPersistence.getLastKeepConfigFile()).isFalse();
        }

        @Test
        @DisplayName("Should handle save failure")
        void testSaveData_SaveFails_ReturnsFalse() {
            // given
            TestAppPersistence failingPersistence = new TestAppPersistence(mockDataStore, false);

            // when
            boolean result = failingPersistence.saveData(testFile, mockDataStore, true);

            // then
            assertThat(result).isFalse();
            assertThat(failingPersistence.wasSaveCalled()).isTrue();
        }

        @Test
        @DisplayName("Should handle null file parameter")
        void testSaveData_WithNullFile_HandlesNullGracefully() {
            // when
            boolean result = testPersistence.saveData(null, mockDataStore, true);

            // then
            assertThat(result).isTrue();
            assertThat(testPersistence.wasSaveCalled()).isTrue();
            assertThat(testPersistence.getLastSaveFile()).isNull();
            assertThat(testPersistence.getLastSaveData()).isSameAs(mockDataStore);
            assertThat(testPersistence.getLastKeepConfigFile()).isTrue();
        }

        @Test
        @DisplayName("Should handle null data parameter")
        void testSaveData_WithNullData_HandlesNullGracefully() {
            // when
            boolean result = testPersistence.saveData(testFile, null, false);

            // then
            assertThat(result).isTrue();
            assertThat(testPersistence.wasSaveCalled()).isTrue();
            assertThat(testPersistence.getLastSaveFile()).isSameAs(testFile);
            assertThat(testPersistence.getLastSaveData()).isNull();
            assertThat(testPersistence.getLastKeepConfigFile()).isFalse();
        }

        @Test
        @DisplayName("Should propagate exceptions during save")
        void testSaveData_ThrowsException_PropagatesException() {
            // given
            RuntimeException testException = new RuntimeException("Save failed");
            ExceptionThrowingAppPersistence exceptionPersistence = new ExceptionThrowingAppPersistence(null,
                    testException);

            // when & then
            assertThatThrownBy(() -> exceptionPersistence.saveData(testFile, mockDataStore, true))
                    .isSameAs(testException)
                    .hasMessage("Save failed");
        }
    }

    @Nested
    @DisplayName("Save Data Tests - Default Method")
    class SaveDataDefaultMethodTests {

        @Test
        @DisplayName("Should use default method with keep config file false")
        void testSaveData_DefaultMethod_UsesKeepConfigFileFalse() {
            // when
            boolean result = testPersistence.saveData(testFile, mockDataStore);

            // then
            assertThat(result).isTrue();
            assertThat(testPersistence.wasSaveCalled()).isTrue();
            assertThat(testPersistence.getLastSaveFile()).isSameAs(testFile);
            assertThat(testPersistence.getLastSaveData()).isSameAs(mockDataStore);
            assertThat(testPersistence.getLastKeepConfigFile()).isFalse();
        }

        @Test
        @DisplayName("Should handle save failure in default method")
        void testSaveData_DefaultMethodSaveFails_ReturnsFalse() {
            // given
            TestAppPersistence failingPersistence = new TestAppPersistence(mockDataStore, false);

            // when
            boolean result = failingPersistence.saveData(testFile, mockDataStore);

            // then
            assertThat(result).isFalse();
            assertThat(failingPersistence.wasSaveCalled()).isTrue();
            assertThat(failingPersistence.getLastKeepConfigFile()).isFalse();
        }

        @Test
        @DisplayName("Should handle null parameters in default method")
        void testSaveData_DefaultMethodWithNulls_HandlesNullsGracefully() {
            // when
            boolean result = testPersistence.saveData(null, null);

            // then
            assertThat(result).isTrue();
            assertThat(testPersistence.wasSaveCalled()).isTrue();
            assertThat(testPersistence.getLastSaveFile()).isNull();
            assertThat(testPersistence.getLastSaveData()).isNull();
            assertThat(testPersistence.getLastKeepConfigFile()).isFalse();
        }
    }

    @Nested
    @DisplayName("Multiple Operations Tests")
    class MultipleOperationsTests {

        @Test
        @DisplayName("Should handle load followed by save")
        void testLoadThenSave_BothOperations_WorkCorrectly() {
            // when
            DataStore loadResult = testPersistence.loadData(testFile);
            boolean saveResult = testPersistence.saveData(testFile, loadResult, true);

            // then
            assertThat(loadResult).isSameAs(mockDataStore);
            assertThat(saveResult).isTrue();
            assertThat(testPersistence.wasLoadCalled()).isTrue();
            assertThat(testPersistence.wasSaveCalled()).isTrue();
            assertThat(testPersistence.getLastSaveData()).isSameAs(mockDataStore);
        }

        @Test
        @DisplayName("Should handle multiple load operations")
        void testMultipleLoads_DifferentFiles_WorkCorrectly() throws IOException {
            // given
            File secondFile = tempDir.resolve("second-config.xml").toFile();
            secondFile.createNewFile();

            // when
            DataStore result1 = testPersistence.loadData(testFile);
            DataStore result2 = testPersistence.loadData(secondFile);

            // then
            assertThat(result1).isSameAs(mockDataStore);
            assertThat(result2).isSameAs(mockDataStore);
            assertThat(testPersistence.getLastLoadFile()).isSameAs(secondFile);
        }

        @Test
        @DisplayName("Should handle multiple save operations")
        void testMultipleSaves_DifferentFiles_WorkCorrectly() throws IOException {
            // given
            File secondFile = tempDir.resolve("second-config.xml").toFile();
            DataStore secondDataStore = mock(DataStore.class);

            // when
            boolean result1 = testPersistence.saveData(testFile, mockDataStore, true);
            boolean result2 = testPersistence.saveData(secondFile, secondDataStore, false);

            // then
            assertThat(result1).isTrue();
            assertThat(result2).isTrue();
            assertThat(testPersistence.getLastSaveFile()).isSameAs(secondFile);
            assertThat(testPersistence.getLastSaveData()).isSameAs(secondDataStore);
            assertThat(testPersistence.getLastKeepConfigFile()).isFalse();
        }
    }

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Should work with lambda implementation")
        void testAppPersistence_AsLambda_WorksCorrectly() {
            // given
            DataStore testData = mock(DataStore.class);
            AppPersistence lambdaPersistence = new AppPersistence() {
                @Override
                public DataStore loadData(File file) {
                    return testData;
                }

                @Override
                public boolean saveData(File file, DataStore data, boolean keepConfigFile) {
                    return true;
                }
            };

            // when
            DataStore loadResult = lambdaPersistence.loadData(testFile);
            boolean saveResult = lambdaPersistence.saveData(testFile, testData, true);
            boolean defaultSaveResult = lambdaPersistence.saveData(testFile, testData);

            // then
            assertThat(loadResult).isSameAs(testData);
            assertThat(saveResult).isTrue();
            assertThat(defaultSaveResult).isTrue();
        }

        @Test
        @DisplayName("Should maintain state between calls")
        void testAppPersistence_StatefulImplementation_MaintainsState() {
            // given
            StatefulAppPersistence statefulPersistence = new StatefulAppPersistence();

            // when
            statefulPersistence.saveData(testFile, mockDataStore, true);
            DataStore loadResult = statefulPersistence.loadData(testFile);

            // then
            assertThat(loadResult).isSameAs(mockDataStore);
            assertThat(statefulPersistence.getSaveCount()).isEqualTo(1);
            assertThat(statefulPersistence.getLoadCount()).isEqualTo(1);
        }
    }

    /**
     * Helper class for stateful testing.
     */
    private static class StatefulAppPersistence implements AppPersistence {
        private DataStore lastSavedData;
        private int saveCount = 0;
        private int loadCount = 0;

        @Override
        public DataStore loadData(File file) {
            loadCount++;
            return lastSavedData;
        }

        @Override
        public boolean saveData(File file, DataStore data, boolean keepConfigFile) {
            saveCount++;
            lastSavedData = data;
            return true;
        }

        public int getSaveCount() {
            return saveCount;
        }

        public int getLoadCount() {
            return loadCount;
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with file system operations")
        void testAppPersistence_WithFileSystem_WorksCorrectly() throws IOException {
            // given
            File inputFile = tempDir.resolve("input.xml").toFile();
            File outputFile = tempDir.resolve("output.xml").toFile();
            inputFile.createNewFile();

            // when
            DataStore loadedData = testPersistence.loadData(inputFile);
            boolean saved = testPersistence.saveData(outputFile, loadedData, true);

            // then
            assertThat(loadedData).isNotNull();
            assertThat(saved).isTrue();
            assertThat(testPersistence.getLastLoadFile()).isSameAs(inputFile);
            assertThat(testPersistence.getLastSaveFile()).isSameAs(outputFile);
        }

        @Test
        @DisplayName("Should handle complete persistence workflow")
        void testAppPersistence_CompleteWorkflow_WorksCorrectly() {
            // given
            DataStore originalData = mock(DataStore.class);
            TestAppPersistence workflowPersistence = new TestAppPersistence(originalData, true);

            // when
            // 1. Save original data
            boolean saveResult = workflowPersistence.saveData(testFile, originalData, true);
            // 2. Load data back
            DataStore loadedData = workflowPersistence.loadData(testFile);
            // 3. Save with different configuration
            boolean secondSaveResult = workflowPersistence.saveData(testFile, loadedData, false);

            // then
            assertThat(saveResult).isTrue();
            assertThat(loadedData).isSameAs(originalData);
            assertThat(secondSaveResult).isTrue();
            assertThat(workflowPersistence.getLastKeepConfigFile()).isFalse();
        }
    }
}
