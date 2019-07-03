/**
 * Copyright 2019 SPeCS.
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

package pt.up.fe.specs.util.utilities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @deprecated instead, move it as method of SpecsIo
 * @author JoaoBispo
 *
 */
@Deprecated
public class PathList {

    private final static String SEPARATOR = ";";

    /**
     * Parses a list of paths.
     * 
     * <p>
     * Default path separator is ;<br>
     * A sequence of paths may be prefixed with a $PREFIX$, the paths after the second $ will be prefixed with PREFIX,
     * until a new $PREFIX$ appears. PREFIX can be empty.
     * 
     * 
     * @param workspaceExtra
     * @return
     */
    public static List<String> parse(String pathList) {
        return parse(pathList, SEPARATOR);
    }

    public static List<String> parse(String pathList, String separator) {
        // Separate into prefixes
        Map<String, String> prefixPaths = new LinkedHashMap<>();
        List<String> pathsWithoutPrefix = new ArrayList<>();

        String currentString = pathList;

        int dollarIndex = -1;
        while ((dollarIndex = currentString.indexOf('$')) != -1) {

            // Add whats before the dollar
            if (dollarIndex > 0) {
                pathsWithoutPrefix.add(currentString.substring(0, dollarIndex));
            }

            currentString = currentString.substring(dollarIndex + 1);

            dollarIndex = currentString.indexOf('$');
            if (dollarIndex == -1) {
                throw new RuntimeException("Expected an even number of $");
            }

            String prefix = currentString.substring(0, dollarIndex);
            currentString = currentString.substring(dollarIndex + 1);

            dollarIndex = currentString.indexOf('$');
            String paths = dollarIndex == -1 ? currentString : currentString.substring(0, dollarIndex);

            String previousPaths = prefixPaths.get(prefix);
            if (previousPaths == null) {
                prefixPaths.put(prefix, paths);
            } else {
                prefixPaths.put(prefix, previousPaths + separator + paths);
            }

        }

        // Transform into paths
        List<String> allPaths = new ArrayList<>();

        allPaths.addAll(pathsWithoutPrefix);
        for (var keyValue : prefixPaths.entrySet()) {
            String[] paths = keyValue.getValue().split(SEPARATOR);

            String prefix = keyValue.getKey();
            for (String path : paths) {
                allPaths.add(prefix + path);
            }
        }

        return allPaths;
    }

    // getBases()
    // getPaths()
    // getPaths(base)

}
