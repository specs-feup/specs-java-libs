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

package pt.up.fe.specs.util;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;

/**
 * INNER CLASS
 * 
 * @deprecated Accepts files with a certain extension.
 */
@Deprecated
class ExtensionFilter implements FilenameFilter {

    private final String extension;
    private final String separator;
    private final boolean followSymlinks;

    /**
     * Note: By default follows symlinks.
     * 
     * @param extension
     */
    public ExtensionFilter(String extension) {
        this(extension, true);
    }

    public ExtensionFilter(String extension, boolean followSymlinks) {
        this.extension = extension;
        // this.separator = SpecsIo.DEFAULT_EXTENSION_SEPARATOR;
        this.separator = ".";
        this.followSymlinks = followSymlinks;
    }

    @Override
    public boolean accept(File dir, String name) {

        String suffix = separator + extension.toLowerCase();

        if (!followSymlinks) {

            File f = new File(dir, name);

            /* Fail if this is a symlink. */
            if (Files.isSymbolicLink(f.toPath())) {
                return false;
            }
        }

        return name.toLowerCase().endsWith(suffix);
    }

}