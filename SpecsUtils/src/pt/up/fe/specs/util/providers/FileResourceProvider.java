/**
 * Copyright 2016 SPeCS.
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

package pt.up.fe.specs.util.providers;

import java.io.File;
import java.util.prefs.Preferences;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.Preconditions;

/**
 * Provides a resource in the format of a file, that might or not exist yet.
 * 
 * @author JoaoBispo
 *
 */
public interface FileResourceProvider {

    /**
     * Helper class for versioned writing.
     * 
     * @author JoaoBispo
     *
     */
    public static class ResourceWriteData {
        private final File writtenFile;
        private final boolean newFile;

        public ResourceWriteData(File writtenFile, boolean newFile) {
            Preconditions.checkNotNull(writtenFile, "writtenFile should not be null");
            this.writtenFile = writtenFile;
            this.newFile = newFile;
        }

        public File getFile() {
            return writtenFile;
        }

        public boolean isNewFile() {
            return newFile;
        }
    }

    /**
     * Copies this resource to the given folder.
     * 
     * @param folder
     * @return
     */
    File write(File folder);

    /**
     * 
     * @return string representing the version of this resource
     */
    String getVersion();

    /**
     * 
     * @return the name of the file represented by this resource
     */
    String getFilename();

    /**
     * Copies this resource to the destination folder. If the file already exists, uses method getVersion() to determine
     * if the file should be overwritten or not.
     * 
     * <p>
     * If the file already exists but no versioning information is available in the system, the file is overwritten.
     * 
     * <p>
     * The method will use the package of the class indicated in 'context' as the location to store the information
     * about versioning. Keep in mind that calls using the same context will refer to the same local copy of the
     * resource.
     * 
     * @param folder
     * @return
     */
    default ResourceWriteData writeVersioned(File folder, Class<?> context) {
        return writeVersioned(folder, context, true);
    }

    default ResourceWriteData writeVersioned(File folder, Class<?> context, boolean writeIfNoVersionInfo) {
        // Create file
        // String resourceOutput = usePath ? getFilepath() : getFilename();
        String resourceOutput = getFilename();

        File destination = new File(folder, resourceOutput);

        Preferences prefs = Preferences.userNodeForPackage(context);

        // Check version information
        String key = getClass().getSimpleName() + "." + getFilename();

        // If file does not exist, just write file, store version information and return
        if (!destination.exists()) {
            prefs.put(key, getVersion());
            File outputfile = write(folder);
            return new ResourceWriteData(outputfile, true);
        }

        String NOT_FOUND = "<NOT FOUND>";
        String version = prefs.get(key, NOT_FOUND);

        // If current version is the same as the version of the resource just return the existing file
        if (version.equals(getVersion())) {
            return new ResourceWriteData(destination, false);
        }

        // Warn when there is not version information available
        if (version.equals(NOT_FOUND)) {
            // Build message
            String message = "Resource '" + getFilename()
                    + "' already exists, but no versioning information is available.";

            if (writeIfNoVersionInfo) {
                message += "Overwriting file '" + key + "' and storing information.";
            }

            SpecsLogs.msgInfo(message);

            // Return existing file
            if (!writeIfNoVersionInfo) {
                return new ResourceWriteData(destination, false);
            }
        }

        // Copy resource and store version information
        File writtenFile = write(folder);
        prefs.put(key, getVersion());

        assert writtenFile.equals(destination);

        return new ResourceWriteData(writtenFile, true);
    }
}
