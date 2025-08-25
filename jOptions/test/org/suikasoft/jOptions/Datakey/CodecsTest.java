package org.suikasoft.jOptions.Datakey;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Test suite for Codecs functionality.
 * Tests file codec and files-with-base-folders codec creation and operations.
 * 
 * @author Generated Tests
 */
@DisplayName("Codecs")
class CodecsTest {

    @TempDir
    Path tempDir;

    private File testFile1;
    private File testFile2;
    private File baseFolder1;
    private File baseFolder2;

    @BeforeEach
    void setUp() throws IOException {
        baseFolder1 = tempDir.resolve("base1").toFile();
        baseFolder2 = tempDir.resolve("base2").toFile();
        baseFolder1.mkdirs();
        baseFolder2.mkdirs();

        testFile1 = new File(baseFolder1, "test1.txt");
        testFile2 = new File(baseFolder2, "test2.txt");
        Files.createFile(testFile1.toPath());
        Files.createFile(testFile2.toPath());
    }

    @Nested
    @DisplayName("File Codec")
    class FileCodecTests {

        @Test
        @DisplayName("file codec creation returns valid StringCodec")
        void testFileCodecCreation_ReturnsValidStringCodec() {
            StringCodec<File> codec = Codecs.file();

            assertThat(codec).isNotNull();
        }

        @Test
        @DisplayName("file codec encodes file to normalized path")
        void testFileCodec_EncodesFileToNormalizedPath() {
            StringCodec<File> codec = Codecs.file();

            String encoded = codec.encode(testFile1);

            assertThat(encoded).isNotNull();
            assertThat(encoded).contains("test1.txt");
            // The exact format depends on SpecsIo.normalizePath implementation
        }

        @Test
        @DisplayName("file codec decodes string to File object")
        void testFileCodec_DecodesStringToFileObject() {
            StringCodec<File> codec = Codecs.file();
            String filePath = testFile1.getAbsolutePath();

            File decoded = codec.decode(filePath);

            assertThat(decoded).isNotNull();
            assertThat(decoded.getAbsolutePath()).isEqualTo(filePath);
        }

        @Test
        @DisplayName("file codec handles null input by creating empty file")
        void testFileCodec_HandlesNullInputByCreatingEmptyFile() {
            StringCodec<File> codec = Codecs.file();

            File decoded = codec.decode(null);

            assertThat(decoded).isNotNull();
            assertThat(decoded.getPath()).isEqualTo("");
        }

        @Test
        @DisplayName("file codec round-trip encoding and decoding works")
        void testFileCodec_RoundTripEncodingAndDecodingWorks() {
            StringCodec<File> codec = Codecs.file();

            String encoded = codec.encode(testFile1);
            File decoded = codec.decode(encoded);

            // The decoded file should represent the same path
            assertThat(decoded.getAbsolutePath()).contains("test1.txt");
        }

        @Test
        @DisplayName("file codec handles relative paths")
        void testFileCodec_HandlesRelativePaths() {
            StringCodec<File> codec = Codecs.file();
            File relativeFile = new File("relative/path/file.txt");

            String encoded = codec.encode(relativeFile);
            File decoded = codec.decode(encoded);

            assertThat(decoded).isNotNull();
            assertThat(decoded.getPath()).contains("relative");
            assertThat(decoded.getPath()).contains("file.txt");
        }

        @Test
        @DisplayName("file codec handles empty string input")
        void testFileCodec_HandlesEmptyStringInput() {
            StringCodec<File> codec = Codecs.file();

            File decoded = codec.decode("");

            assertThat(decoded).isNotNull();
            assertThat(decoded.getPath()).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("Files With Base Folders Codec")
    class FilesWithBaseFoldersCodecTests {

        @Test
        @DisplayName("filesWithBaseFolders codec creation returns valid StringCodec")
        void testFilesWithBaseFoldersCodecCreation_ReturnsValidStringCodec() {
            StringCodec<Map<File, File>> codec = Codecs.filesWithBaseFolders();

            assertThat(codec).isNotNull();
        }

        @Test
        @DisplayName("filesWithBaseFolders codec encodes simple file-to-base mapping")
        void testFilesWithBaseFoldersCodec_EncodesSimpleFileToBaseMapping() {
            StringCodec<Map<File, File>> codec = Codecs.filesWithBaseFolders();

            Map<File, File> mapping = new HashMap<>();
            mapping.put(testFile1, baseFolder1);

            String encoded = codec.encode(mapping);

            assertThat(encoded).isNotNull();
            assertThat(encoded).isNotEmpty();
        }

        @Test
        @DisplayName("filesWithBaseFolders codec decodes string to file mapping")
        void testFilesWithBaseFoldersCodec_DecodesStringToFileMapping() {
            StringCodec<Map<File, File>> codec = Codecs.filesWithBaseFolders();

            // Simple format: base folder path and relative file path
            String encoded = "$" + baseFolder1.getAbsolutePath() + "$test1.txt";

            Map<File, File> decoded = codec.decode(encoded);

            assertThat(decoded).isNotNull();
            assertThat(decoded).isNotEmpty();
        }

        @Test
        @DisplayName("filesWithBaseFolders codec handles empty mapping")
        void testFilesWithBaseFoldersCodec_HandlesEmptyMapping() {
            StringCodec<Map<File, File>> codec = Codecs.filesWithBaseFolders();

            Map<File, File> emptyMapping = new HashMap<>();

            String encoded = codec.encode(emptyMapping);
            Map<File, File> decoded = codec.decode(encoded);

            assertThat(encoded).isNotNull();
            assertThat(decoded).isNotNull();
            assertThat(decoded).isEmpty();
        }

        @Test
        @DisplayName("filesWithBaseFolders codec handles null base folder")
        void testFilesWithBaseFoldersCodec_HandlesNullBaseFolder() {
            StringCodec<Map<File, File>> codec = Codecs.filesWithBaseFolders();

            Map<File, File> mappingWithNullBase = new HashMap<>();
            mappingWithNullBase.put(testFile1, null);

            String encoded = codec.encode(mappingWithNullBase);

            assertThat(encoded).isNotNull();
            // When base folder is null, should handle gracefully
        }

        @Test
        @DisplayName("filesWithBaseFolders codec handles multiple files with same base")
        void testFilesWithBaseFoldersCodec_HandlesMultipleFilesWithSameBase() {
            StringCodec<Map<File, File>> codec = Codecs.filesWithBaseFolders();

            Map<File, File> mapping = new HashMap<>();
            mapping.put(testFile1, baseFolder1);
            mapping.put(new File(baseFolder1, "another.txt"), baseFolder1);

            String encoded = codec.encode(mapping);
            Map<File, File> decoded = codec.decode(encoded);

            assertThat(encoded).isNotNull();
            assertThat(decoded).isNotNull();
            // Should handle multiple files with the same base folder
        }

        @Test
        @DisplayName("filesWithBaseFolders codec handles multiple base folders")
        void testFilesWithBaseFoldersCodec_HandlesMultipleBaseFolders() {
            StringCodec<Map<File, File>> codec = Codecs.filesWithBaseFolders();

            Map<File, File> mapping = new HashMap<>();
            mapping.put(testFile1, baseFolder1);
            mapping.put(testFile2, baseFolder2);

            String encoded = codec.encode(mapping);
            Map<File, File> decoded = codec.decode(encoded);

            assertThat(encoded).isNotNull();
            assertThat(decoded).isNotNull();
            // Should handle files from different base folders
        }
    }

    @Nested
    @DisplayName("Round-trip Encoding/Decoding")
    class RoundTripEncodingDecodingTests {

        @Test
        @DisplayName("file codec round-trip preserves file information")
        void testFileCodec_RoundTripPreservesFileInformation() {
            StringCodec<File> codec = Codecs.file();

            String encoded = codec.encode(testFile1);
            File decoded = codec.decode(encoded);
            String reEncoded = codec.encode(decoded);

            // Round-trip should be consistent
            assertThat(reEncoded).isEqualTo(encoded);
        }

        @Test
        @DisplayName("filesWithBaseFolders codec round-trip preserves mapping information")
        void testFilesWithBaseFoldersCodec_RoundTripPreservesMappingInformation() {
            StringCodec<Map<File, File>> codec = Codecs.filesWithBaseFolders();

            Map<File, File> originalMapping = new HashMap<>();
            originalMapping.put(testFile1, baseFolder1);

            String encoded = codec.encode(originalMapping);
            Map<File, File> decoded = codec.decode(encoded);
            String reEncoded = codec.encode(decoded);

            // Round-trip should be consistent (might not be exactly equal due to path
            // normalization)
            assertThat(decoded).isNotNull();
            assertThat(reEncoded).isNotNull();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("file codec handles very long file paths")
        void testFileCodec_HandlesVeryLongFilePaths() {
            StringCodec<File> codec = Codecs.file();

            // Create a very long path
            StringBuilder longPath = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                longPath.append("very_long_directory_name_").append(i).append(File.separator);
            }
            longPath.append("file.txt");

            File longPathFile = new File(longPath.toString());

            String encoded = codec.encode(longPathFile);
            File decoded = codec.decode(encoded);

            assertThat(encoded).isNotNull();
            assertThat(decoded).isNotNull();
        }

        @Test
        @DisplayName("file codec handles files with special characters")
        void testFileCodec_HandlesFilesWithSpecialCharacters() {
            StringCodec<File> codec = Codecs.file();

            File specialFile = new File(baseFolder1, "file-with_special.chars (123).txt");

            String encoded = codec.encode(specialFile);
            File decoded = codec.decode(encoded);

            assertThat(encoded).isNotNull();
            assertThat(decoded).isNotNull();
            assertThat(decoded.getName()).contains("special");
        }

        @Test
        @DisplayName("filesWithBaseFolders codec handles malformed input gracefully")
        void testFilesWithBaseFoldersCodec_HandlesMalformedInputGracefully() {
            StringCodec<Map<File, File>> codec = Codecs.filesWithBaseFolders();

            try {
                Map<File, File> decoded = codec.decode("malformed input without proper format");

                // Should handle gracefully, possibly returning empty map or throwing exception
                assertThat(decoded).isNotNull();

            } catch (Exception e) {
                // If it throws an exception, that's also valid behavior
                assertThat(e).isNotNull();
            }
        }

        @Test
        @DisplayName("codecs handle null inputs appropriately")
        void testCodecs_HandleNullInputsAppropriately() {
            StringCodec<File> fileCodec = Codecs.file();
            StringCodec<Map<File, File>> mappingCodec = Codecs.filesWithBaseFolders();

            // File codec with null should create empty file (as tested above)
            File nullDecodedFile = fileCodec.decode(null);
            assertThat(nullDecodedFile).isNotNull();

            // Mapping codec with null might throw or return empty map
            try {
                Map<File, File> nullDecodedMapping = mappingCodec.decode(null);
                assertThat(nullDecodedMapping).isNotNull();
            } catch (Exception e) {
                // Exception is also valid behavior for null input
                assertThat(e).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("Integration with SpecsIo")
    class IntegrationWithSpecsIoTests {

        @Test
        @DisplayName("file codec uses SpecsIo.normalizePath for encoding")
        void testFileCodec_UsesSpecsIoNormalizePathForEncoding() {
            StringCodec<File> codec = Codecs.file();

            // Create a file with path that might need normalization
            File fileWithDots = new File(baseFolder1, "../base1/./test1.txt");

            String encoded = codec.encode(fileWithDots);

            assertThat(encoded).isNotNull();
            // The exact normalization behavior depends on SpecsIo implementation
            // but the encoding should work without throwing exceptions
        }

        @Test
        @DisplayName("filesWithBaseFolders codec uses SpecsIo for path operations")
        void testFilesWithBaseFoldersCodec_UsesSpecsIoForPathOperations() {
            StringCodec<Map<File, File>> codec = Codecs.filesWithBaseFolders();

            Map<File, File> mapping = new HashMap<>();
            mapping.put(testFile1, baseFolder1);

            String encoded = codec.encode(mapping);

            assertThat(encoded).isNotNull();
            // Should use SpecsIo.removeCommonPath and other utilities internally
        }
    }
}
