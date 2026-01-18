/**
 * Copyright 2018 SPeCS.
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

package org.suikasoft.jOptions.Datakey;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.collections.MultiMap;
import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Utility class for common {@link pt.up.fe.specs.util.parsing.StringCodec}
 * implementations for DataKey types.
 *
 * <p>
 * This class provides static methods to create codecs for common types such as
 * File and Map<File, File>.
 */
public class Codecs {

    private static final String FILES_WITH_BASE_FOLDER_SEPARATOR = ";";

    /**
     * Creates a {@link StringCodec} for {@link File} objects.
     *
     * @return a codec for encoding and decoding {@link File} objects
     */
    public static StringCodec<File> file() {
        Function<String, File> decoder = s -> s == null ? new File("") : new File(s);
        return StringCodec.newInstance(Codecs::fileEncoder, decoder);
    }

    /**
     * Encodes a {@link File} object into a normalized path string.
     *
     * @param file the file to encode
     * @return the normalized path string
     */
    private static String fileEncoder(File file) {
        return SpecsIo.normalizePath(file);
    }

    /**
     * Creates a {@link StringCodec} for mapping {@link File} objects to their base
     * folders.
     *
     * @return a codec for encoding and decoding mappings of files to base folders
     */
    public static StringCodec<Map<File, File>> filesWithBaseFolders() {
        return StringCodec.newInstance(Codecs::filesWithBaseFoldersEncoder, Codecs::filesWithBaseFoldersDecoder);
    }

    /**
     * Decodes a string representation of file-to-base-folder mappings into a
     * {@link Map}.
     *
     * @param value the string representation of the mappings
     * @return a map of files to their base folders
     */
    private static Map<File, File> filesWithBaseFoldersDecoder(String value) {
        MultiMap<String, String> basesToPaths = SpecsStrings.parsePathList(value, FILES_WITH_BASE_FOLDER_SEPARATOR);

        Map<File, File> filesWithBases = new HashMap<>();

        for (String base : basesToPaths.keySet()) {
            var paths = basesToPaths.get(base);

            File baseFolder = base.isEmpty() ? null : new File(base);

            for (String path : paths) {
                filesWithBases.put(new File(baseFolder, path), baseFolder);
            }
        }

        return filesWithBases;
    }

    /**
     * Encodes a map of files to their base folders into a string representation.
     *
     * @param value the map of files to base folders
     * @return the string representation of the mappings
     */
    private static String filesWithBaseFoldersEncoder(Map<File, File> value) {
        MultiMap<String, String> basesToPaths = new MultiMap<>();
        for (var entry : value.entrySet()) {
            String base = entry.getValue() == null ? "" : entry.getValue().toString();
            String path = base.isEmpty() ? entry.getKey().toString()
                    : SpecsIo.removeCommonPath(entry.getKey(), entry.getValue()).toString();

            basesToPaths.add(base, path);
        }

        if (basesToPaths.size() == 1 && basesToPaths.containsKey("")) {
            return String.join("", basesToPaths.get(""));
        }

        String pathsNoPrefix = basesToPaths.get("") == null ? ""
                : String.join(FILES_WITH_BASE_FOLDER_SEPARATOR, basesToPaths.get(""));

        String pathsWithPrefix = basesToPaths.entrySet().stream()
                .filter(entry -> !entry.getKey().isEmpty())
                .map(entry -> "$" + entry.getKey() + "$"
                        + String.join(FILES_WITH_BASE_FOLDER_SEPARATOR, entry.getValue()))
                .collect(Collectors.joining());

        return pathsNoPrefix + pathsWithPrefix;
    }
}
