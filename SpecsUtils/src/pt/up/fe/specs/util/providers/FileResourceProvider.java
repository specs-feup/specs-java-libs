/*
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
import java.util.Arrays;
import java.util.Objects;
import java.util.prefs.Preferences;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.providers.impl.GenericFileResourceProvider;

/**
 * Utility class for providing file resources.
 * <p>
 * Used for loading and managing file-based resources.
 * </p>
 *
 * @author Joao Bispo
 */
public interface FileResourceProvider {

    /**
     * Creates a new instance of FileResourceProvider for an existing file.
     *
     * @param existingFile the file to be wrapped by the provider
     * @return a new instance of FileResourceProvider
     */
    static FileResourceProvider newInstance(File existingFile) {
        return GenericFileResourceProvider.newInstance(existingFile);
    }

    /**
     * Creates a new instance of FileResourceProvider for an existing file with a
     * version suffix.
     *
     * @param existingFile  the file to be wrapped by the provider
     * @param versionSuffix the version suffix to be appended
     * @return a new instance of FileResourceProvider
     */
    static FileResourceProvider newInstance(File existingFile, String versionSuffix) {
        return GenericFileResourceProvider.newInstance(existingFile, versionSuffix);
    }

    /**
     * Helper class for versioned writing.
     * <p>
     * Contains information about the written file and whether it is a new file.
     * </p>
     *
     * @author Joao Bispo
     */
    public static class ResourceWriteData {
        private final File writtenFile;
        private final boolean newFile;

        /**
         * Constructs a ResourceWriteData object.
         *
         * @param writtenFile the file that was written
         * @param newFile     whether the file is new
         */
        public ResourceWriteData(File writtenFile, boolean newFile) {
            Objects.requireNonNull(writtenFile, () -> "writtenFile should not be null");
            this.writtenFile = writtenFile;
            this.newFile = newFile;
        }

        /**
         * Gets the written file.
         *
         * @return the written file
         */
        public File getFile() {
            return writtenFile;
        }

        /**
         * Checks if the file is new.
         *
         * @return true if the file is new, false otherwise
         */
        public boolean isNewFile() {
            return newFile;
        }

        /**
         * Makes the file executable if it is new and the operating system is Linux.
         *
         * @param isLinux true if the operating system is Linux, false otherwise
         */
        public void makeExecutable(boolean isLinux) {
            // If file is new and we are in a flavor of Linux, make file executable
            if (isNewFile() && isLinux) {
                SpecsSystem.runProcess(Arrays.asList("chmod", "+x", getFile().getAbsolutePath()), false, true);
            }

            // If on Linux, make folders and files accessible to all users
            if (isLinux) {
                SpecsSystem.runProcess(Arrays.asList("chmod", "-R", "777", getFile().getParentFile().getAbsolutePath()),
                        false, true);
            }

        }
    }

    /**
     * Copies this resource to the given folder.
     *
     * @param folder the destination folder
     * @return the file that was written
     */
    File write(File folder);

    /**
     * Gets the version of this resource.
     *
     * @return a string representing the version of this resource
     */
    String version();

    /**
     * Gets the name of the file represented by this resource.
     *
     * @return the name of the file
     */
    String getFilename();

    /**
     * Copies this resource to the destination folder. If the file already exists,
     * uses method getVersion() to determine if the file should be overwritten or
     * not.
     * <p>
     * If the file already exists but no versioning information is available in the
     * system, the file is overwritten.
     * <p>
     * The method will use the package of the class indicated in 'context' as the
     * location to store the information about versioning. Keep in mind that calls
     * using the same context will refer to the same local copy of the resource.
     *
     * @param folder  the destination folder
     * @param context the class used to store versioning information
     * @return a ResourceWriteData object containing information about the written
     *         file
     */
    default ResourceWriteData writeVersioned(File folder, Class<?> context) {
        return writeVersioned(folder, context, true);
    }

    /**
     * Copies this resource to the destination folder with versioning information.
     *
     * @param folder               the destination folder
     * @param context              the class used to store versioning information
     * @param writeIfNoVersionInfo whether to write the file if no versioning
     *                             information is available
     * @return a ResourceWriteData object containing information about the written
     *         file
     */
    default ResourceWriteData writeVersioned(File folder, Class<?> context, boolean writeIfNoVersionInfo) {
        // Create file
        String resourceOutput = getFilename();
        File destination = new File(folder, resourceOutput);

        Preferences prefs = Preferences.userNodeForPackage(context);
        final String key = getClass().getSimpleName() + "." + getFilename();

        final String NO_VERSION = "<NO VERSION>";
        final String versionToDownload = version() != null ? version() : NO_VERSION;
        final String storedVersion = prefs.get(key, null);

        if (destination.exists() && versionToDownload.equals(storedVersion)) {
            // File exists and version is the same, return existing file
            return new ResourceWriteData(destination, false);
        } else if (destination.exists() && (storedVersion == null || NO_VERSION.equals(storedVersion))) {
            // File exists, no version info available
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

        // Write file and store version info
        File writtenFile = write(folder);

        prefs.put(key, versionToDownload);

        assert writtenFile.equals(destination);

        return new ResourceWriteData(writtenFile, true);
    }

    /**
     * Creates a resource for the given version.
     * <p>
     * It changes the resource path by appending an underscore and the given version
     * as a suffix, before any extension.<br>
     * E.g., if the original resource is "path/executable.exe", returns a resource
     * to "path/executable<version>.exe".
     * </p>
     *
     * @param version the version suffix to be appended
     * @return a new FileResourceProvider for the given version
     */
    default FileResourceProvider createResourceVersion(String version) {
        throw new NotImplementedException(getClass());
    }
}
