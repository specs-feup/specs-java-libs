/**
 * Copyright 2012 SPeCS Research Group.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util;

import static org.assertj.core.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test suite for SpecsIo utility class.
 * 
 * This test class covers I/O functionality including:
 * - Relative path calculation
 * - URL parsing and query parsing
 * - Path list parsing
 * - File operations
 */
@DisplayName("SpecsIo Tests")
public class IoUtilsTest {

    @Nested
    @DisplayName("Relative Path Operations")
    class RelativePathOperations {

        @Test
        @DisplayName("getRelativePath should calculate correct relative path from file to file")
        void testGetRelativePath_FileToFile_ReturnsCorrectPath() {
            File target = new File("Windows/Boot/Fonts/chs_boot.ttf");
            File base = new File("Windows/Speech/Common/sapisvr.exe");

            String relPath = SpecsIo.getRelativePath(target, base);
            assertThat(relPath).isEqualTo("../../Boot/Fonts/chs_boot.ttf");
        }

        @Test
        @DisplayName("getRelativePath should handle relative files correctly")
        void testGetRelativePath_RelativeFile_ReturnsCorrectPath() {
            File target = new File("SharedLibrary/../a/b/test.dat");
            File base = new File("./");

            String relPath = SpecsIo.getRelativePath(target, base);
            assertThat(relPath).isEqualTo("a/b/test.dat");
        }

        @Test
        @DisplayName("getRelativePath should handle files in same folder")
        void testGetRelativePath_SameFolder_ReturnsCorrectPath() {
            File target = new File("lib/b.h");
            File base = new File("lib/a.h");

            String relPath = SpecsIo.getRelativePath(target, base);
            assertThat(relPath).isEqualTo("b.h");
        }

        @Test
        @DisplayName("getRelativePath should handle files in different folders")
        void testGetRelativePath_DifferentFolders_ReturnsCorrectPath() {
            File target = new File("lib/b.h");
            File base = new File("lib2/a.h");

            String relPath = SpecsIo.getRelativePath(target, base);
            assertThat(relPath).isEqualTo("../lib/b.h");
        }

        @Test
        @DisplayName("getRelativePath should handle null inputs gracefully")
        void testGetRelativePath_NullInputs_ShouldHandleGracefully() {
            assertThatCode(() -> {
                SpecsIo.getRelativePath(null, new File("test"));
            }).doesNotThrowAnyException();

            assertThatCode(() -> {
                SpecsIo.getRelativePath(new File("test"), null);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Path Parsing Operations")
    class PathParsingOperations {

        @Test
        @DisplayName("parsePathList should parse simple path list correctly")
        void testParsePathList_SimplePaths_ReturnsCorrectMap() {
            var result = SpecsStrings.parsePathList("path1; path2  ; path3", ";");
            assertThat(result.toString()).isEqualTo("{=[path1, path2, path3]}");
        }

        @Test
        @DisplayName("parsePathList should parse path list with prefixes correctly")
        void testParsePathList_WithPrefixes_ReturnsCorrectMap() {
            var result = SpecsStrings.parsePathList("path1$prefix/$path2;path3$$path4", ";");
            assertThat(result.toString()).isEqualTo("{=[path1, path4], prefix/=[path2, path3]}");
        }

        @Test
        @DisplayName("parsePathList should handle empty paths gracefully")
        void testParsePathList_EmptyPaths_ShouldHandleGracefully() {
            assertThatCode(() -> {
                SpecsStrings.parsePathList("", ";");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("parsePathList should handle null inputs gracefully")
        void testParsePathList_NullInputs_ShouldHandleGracefully() {
            assertThatCode(() -> {
                SpecsStrings.parsePathList(null, ";");
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("URL Parsing Operations")
    class UrlParsingOperations {

        @Test
        @DisplayName("parseUrl and parseUrlQuery should parse URL with query parameters correctly")
        void testParseUrl_WithQueryParameters_ReturnsCorrectComponents() {
            var urlString = "https://github.com/specs-feup/clava.git?folder=benchmarks/NAS&another=stuff";

            var urlOptional = SpecsIo.parseUrl(urlString);
            assertThat(urlOptional).isPresent();

            var url = urlOptional.get();
            var query = SpecsIo.parseUrlQuery(url);

            assertThat(url.getProtocol()).isEqualTo("https");
            assertThat(url.getHost()).isEqualTo("github.com");
            assertThat(url.getPath()).isEqualTo("/specs-feup/clava.git");
            assertThat(query.get("folder")).isEqualTo("benchmarks/NAS");
            assertThat(query.get("another")).isEqualTo("stuff");
        }

        @Test
        @DisplayName("parseUrl should handle invalid URLs gracefully")
        void testParseUrl_InvalidUrl_ReturnsEmpty() {
            var result = SpecsIo.parseUrl("not-a-valid-url");
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("parseUrl should handle null input gracefully")
        void testParseUrl_NullInput_ShouldHandleGracefully() {
            assertThatCode(() -> {
                SpecsIo.parseUrl(null);
            }).doesNotThrowAnyException();
        }
    }
}
