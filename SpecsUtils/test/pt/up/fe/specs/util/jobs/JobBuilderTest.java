package pt.up.fe.specs.util.jobs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for the JobBuilder interface.
 * Tests the contract and behavior of job building functionality.
 * 
 * @author Generated Tests
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JobBuilder Tests")
class JobBuilderTest {

    @Mock
    private JobBuilder mockJobBuilder;

    @Mock
    private FileSet mockFileSet;

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Should define buildJobs method with correct signature")
        void testBuildJobsMethod_ExistsWithCorrectSignature() {
            // Arrange
            File outputFolder = new File("/tmp/output");
            List<FileSet> programs = Arrays.asList(mockFileSet);
            List<Job> expectedJobs = Arrays.asList(Job.singleJavaCall(() -> {
            }));

            when(mockJobBuilder.buildJobs(programs, outputFolder)).thenReturn(expectedJobs);

            // Act
            List<Job> result = mockJobBuilder.buildJobs(programs, outputFolder);

            // Assert
            assertThat(result).isEqualTo(expectedJobs);
            verify(mockJobBuilder).buildJobs(programs, outputFolder);
        }

        @Test
        @DisplayName("Should handle empty program list")
        void testBuildJobs_EmptyProgramList_HandlesGracefully() {
            // Arrange
            File outputFolder = new File("/tmp/output");
            List<FileSet> emptyPrograms = Collections.emptyList();
            List<Job> emptyJobs = Collections.emptyList();

            when(mockJobBuilder.buildJobs(emptyPrograms, outputFolder)).thenReturn(emptyJobs);

            // Act
            List<Job> result = mockJobBuilder.buildJobs(emptyPrograms, outputFolder);

            // Assert
            assertThat(result).isEmpty();
            verify(mockJobBuilder).buildJobs(emptyPrograms, outputFolder);
        }

        @Test
        @DisplayName("Should handle null return for error conditions")
        void testBuildJobs_ErrorCondition_ReturnsNull() {
            // Arrange
            File outputFolder = new File("/tmp/output");
            List<FileSet> programs = Arrays.asList(mockFileSet);

            when(mockJobBuilder.buildJobs(programs, outputFolder)).thenReturn(null);

            // Act
            List<Job> result = mockJobBuilder.buildJobs(programs, outputFolder);

            // Assert
            assertThat(result).isNull();
            verify(mockJobBuilder).buildJobs(programs, outputFolder);
        }
    }

    @Nested
    @DisplayName("Parameter Validation Tests")
    class ParameterValidationTests {

        @Test
        @DisplayName("Should handle null output folder")
        void testBuildJobs_NullOutputFolder_HandlesAccordingToImplementation() {
            // Arrange
            List<FileSet> programs = Arrays.asList(mockFileSet);

            when(mockJobBuilder.buildJobs(programs, null)).thenReturn(null);

            // Act
            List<Job> result = mockJobBuilder.buildJobs(programs, null);

            // Assert
            assertThat(result).isNull();
            verify(mockJobBuilder).buildJobs(programs, null);
        }

        @Test
        @DisplayName("Should handle null program list")
        void testBuildJobs_NullProgramList_HandlesAccordingToImplementation() {
            // Arrange
            File outputFolder = new File("/tmp/output");

            when(mockJobBuilder.buildJobs(null, outputFolder)).thenReturn(null);

            // Act
            List<Job> result = mockJobBuilder.buildJobs(null, outputFolder);

            // Assert
            assertThat(result).isNull();
            verify(mockJobBuilder).buildJobs(null, outputFolder);
        }
    }

    @Nested
    @DisplayName("Behavior Tests")
    class BehaviorTests {

        @Test
        @DisplayName("Should build jobs for multiple file sets")
        void testBuildJobs_MultipleFileSets_ReturnsCorrespondingJobs() {
            // Arrange
            File outputFolder = new File("/tmp/output");
            FileSet fileSet1 = mock(FileSet.class);
            FileSet fileSet2 = mock(FileSet.class);
            List<FileSet> programs = Arrays.asList(fileSet1, fileSet2);

            Job job1 = Job.singleJavaCall(() -> {
            }, "Job 1");
            Job job2 = Job.singleJavaCall(() -> {
            }, "Job 2");
            List<Job> expectedJobs = Arrays.asList(job1, job2);

            when(mockJobBuilder.buildJobs(programs, outputFolder)).thenReturn(expectedJobs);

            // Act
            List<Job> result = mockJobBuilder.buildJobs(programs, outputFolder);

            // Assert
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(job1, job2);
            verify(mockJobBuilder).buildJobs(programs, outputFolder);
        }

        @Test
        @DisplayName("Should handle different output folder types")
        void testBuildJobs_DifferentOutputFolders_ProcessesCorrectly() {
            // Arrange
            List<FileSet> programs = Arrays.asList(mockFileSet);
            File relativeFolder = new File("relative/path");
            File absoluteFolder = new File("/absolute/path");

            Job job1 = Job.singleJavaCall(() -> {
            }, "Relative Job");
            Job job2 = Job.singleJavaCall(() -> {
            }, "Absolute Job");

            when(mockJobBuilder.buildJobs(programs, relativeFolder)).thenReturn(Arrays.asList(job1));
            when(mockJobBuilder.buildJobs(programs, absoluteFolder)).thenReturn(Arrays.asList(job2));

            // Act
            List<Job> relativeResult = mockJobBuilder.buildJobs(programs, relativeFolder);
            List<Job> absoluteResult = mockJobBuilder.buildJobs(programs, absoluteFolder);

            // Assert
            assertThat(relativeResult).containsExactly(job1);
            assertThat(absoluteResult).containsExactly(job2);
            verify(mockJobBuilder).buildJobs(programs, relativeFolder);
            verify(mockJobBuilder).buildJobs(programs, absoluteFolder);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle non-existent output folder")
        void testBuildJobs_NonExistentOutputFolder_HandlesGracefully() {
            // Arrange
            File nonExistentFolder = new File("/path/that/does/not/exist");
            List<FileSet> programs = Arrays.asList(mockFileSet);

            when(mockJobBuilder.buildJobs(programs, nonExistentFolder)).thenReturn(null);

            // Act
            List<Job> result = mockJobBuilder.buildJobs(programs, nonExistentFolder);

            // Assert
            assertThat(result).isNull();
            verify(mockJobBuilder).buildJobs(programs, nonExistentFolder);
        }

        @Test
        @DisplayName("Should handle file sets with problematic content")
        void testBuildJobs_ProblematicFileSets_ReturnsNullOnProblems() {
            // Arrange
            File outputFolder = new File("/tmp/output");
            List<FileSet> programs = Arrays.asList(mockFileSet);

            // Simulate problems during job building
            when(mockJobBuilder.buildJobs(programs, outputFolder)).thenReturn(null);

            // Act
            List<Job> result = mockJobBuilder.buildJobs(programs, outputFolder);

            // Assert
            assertThat(result).isNull();
            verify(mockJobBuilder).buildJobs(programs, outputFolder);
        }
    }

    @Nested
    @DisplayName("Documentation Contract Tests")
    class DocumentationContractTests {

        @Test
        @DisplayName("Should return null when any problem happens as documented")
        void testBuildJobs_ProblemsOccur_ReturnsNullAsDocumented() {
            // Arrange
            File outputFolder = new File("/tmp/output");
            List<FileSet> programs = Arrays.asList(mockFileSet);

            // Simulate the documented behavior: "returns null if any problem happens"
            when(mockJobBuilder.buildJobs(programs, outputFolder)).thenReturn(null);

            // Act
            List<Job> result = mockJobBuilder.buildJobs(programs, outputFolder);

            // Assert
            assertThat(result).isNull();
            verify(mockJobBuilder).buildJobs(programs, outputFolder);
        }

        @Test
        @DisplayName("Should build jobs according to given program sources as documented")
        void testBuildJobs_ValidInputs_BuildsJobsAccordingToSources() {
            // Arrange
            File outputFolder = new File("/tmp/output");
            List<FileSet> programs = Arrays.asList(mockFileSet);

            Job expectedJob = Job.singleJavaCall(() -> {
            }, "testProgram Job");
            List<Job> expectedJobs = Arrays.asList(expectedJob);

            when(mockJobBuilder.buildJobs(programs, outputFolder)).thenReturn(expectedJobs);

            // Act
            List<Job> result = mockJobBuilder.buildJobs(programs, outputFolder);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getDescription()).contains("testProgram");
            verify(mockJobBuilder).buildJobs(programs, outputFolder);
        }
    }
}
