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
 * specific language governing permissions and limitations under the License. under the License.
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

public class Codecs {

    private static final String FILES_WITH_BASE_FOLDER_SEPARATOR = ";";

    public static StringCodec<File> file() {
        // Function<File, String> encoder = f -> SpecsIo.normalizePath(f);
        Function<String, File> decoder = s -> s == null ? new File("") : new File(s);

        return StringCodec.newInstance(Codecs::fileEncoder, decoder);
    }

    private static String fileEncoder(File file) {
        System.out.println("ENCODING FILE:" + SpecsIo.normalizePath(file));
        return SpecsIo.normalizePath(file);
    }

    public static StringCodec<Map<File, File>> filesWithBaseFolders() {
        return StringCodec.newInstance(Codecs::filesWithBaseFoldersEncoder, Codecs::filesWithBaseFoldersDecoder);
    }

    private static Map<File, File> filesWithBaseFoldersDecoder(String value) {
        MultiMap<String, String> basesToPaths = SpecsStrings.parsePathList(value, FILES_WITH_BASE_FOLDER_SEPARATOR);

        Map<File, File> filesWithBases = new HashMap<>();

        for (String base : basesToPaths.keySet()) {
            var paths = basesToPaths.get(base);

            // For base folder
            File baseFolder = base.isEmpty() ? null : new File(base);

            for (String path : paths) {
                filesWithBases.put(new File(baseFolder, path), baseFolder);
            }
        }

        return filesWithBases;
    }

    private static String filesWithBaseFoldersEncoder(Map<File, File> value) {
        MultiMap<String, String> basesToPaths = new MultiMap<>();
        for (var entry : value.entrySet()) {
            String base = entry.getValue() == null ? "" : entry.getValue().toString();

            String path = base.isEmpty() ? entry.getKey().toString()
                    : SpecsIo.removeCommonPath(entry.getKey(), entry.getValue()).toString();
            // var baseFile = entry.getValue();
            // var pathFile = entry.getKey();

            basesToPaths.add(base, path);
        }

        // Special case: only one empty base folder
        if (basesToPaths.size() == 1 && basesToPaths.containsKey("")) {
            return basesToPaths.get("").stream().collect(Collectors.joining());
        }

        return basesToPaths.entrySet().stream()
                .map(entry -> "$" + entry.getKey() + "$"
                        + entry.getValue().stream().collect(Collectors.joining(FILES_WITH_BASE_FOLDER_SEPARATOR)))
                .collect(Collectors.joining());

        // value.entrySet().stream()
        // .map(entry -> entry.getValue() == null ? "$$" : "$" + entry.getValue() + "$" + entry.getKey())
        // .collect(Collectors.joining(FILES_WITH_BASE_FOLDER_SEPARATOR));
        //
        // return value.entrySet().stream()
        // .map(entry -> entry.getValue() == null ? "$$" : "$" + entry.getValue() + "$" + entry.getKey())
        // .collect(Collectors.joining(FILES_WITH_BASE_FOLDER_SEPARATOR));
    }
}
