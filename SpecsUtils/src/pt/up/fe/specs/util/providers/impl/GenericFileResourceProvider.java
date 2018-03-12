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

package pt.up.fe.specs.util.providers.impl;

import java.io.File;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.providers.FileResourceProvider;

public class GenericFileResourceProvider implements FileResourceProvider {

    private final File existingFile;
    private final String version;
    private final boolean isVersioned;
    // private final Lazy<File> versionedFile;

    public static GenericFileResourceProvider newInstance(File file) {
        return newInstance(file, null);
    }

    /**
     * Creates a new instance of {@link GenericFileResourceProvider}.
     * 
     * <p>
     * Given file must exist, otherwhise an exception is thrown.
     * 
     * @param existingFile
     * @param version
     * @return
     */
    public static GenericFileResourceProvider newInstance(File existingFile, String version) {
        // File fileWithoutVersion = existingFile;

        // Remove version from file
        // if (version != null) {
        // String strippedFilename = SpecsIo.removeExtension(existingFile);
        // Preconditions.checkArgument(strippedFilename.endsWith(version), "Given filename '" + existingFile
        // + "' does not have the given version '" + version + "' as a suffix");
        // }

        // Create versioned file
        // File versionedFile = getVersionedFile(fileWithoutVersion, version);

        if (!existingFile.isFile()) {
            // if (!versionedFile.isFile()) {
            // System.out.println("FILE:" + versionedFile.getAbsolutePath());
            // throw new RuntimeException("File '" + versionedFile + "' does not exist");
            throw new RuntimeException("File '" + existingFile + "' does not exist");
        }

        return new GenericFileResourceProvider(existingFile, version, false);
        // return new GenericFileResourceProvider(fileWithoutVersion, version);
    }

    // private static File getVersionedFile(File file, String version) {
    // if (version == null) {
    // return file;
    // }
    //
    // // Create new file
    // String filenameNoExt = SpecsIo.removeExtension(file);
    // String extension = SpecsIo.getExtension(file);
    // extension = extension.isEmpty() ? extension : "." + extension;
    //
    // String newFilename = filenameNoExt + version + extension;
    //
    // return new File(SpecsIo.getParent(file), newFilename);
    // }

    private GenericFileResourceProvider(File existingFile, String version, boolean isVersioned) {
        this.existingFile = existingFile;
        this.version = version;
        this.isVersioned = isVersioned;
        // this.versionedFile = Lazy.newInstance(() -> getVersionedFile(fileWithoutVersion, version));
    }

    // public File getFile() {
    // return
    // }

    @Override
    public File write(File folder) {

        // Check if folder is the same where the file
        if (SpecsIo.getParent(existingFile).equals(folder)) {
            return existingFile;
        }

        // Copy file
        File destinationFile = new File(folder, existingFile.getName());
        SpecsIo.copy(existingFile, destinationFile);

        return destinationFile;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getFilename() {
        return getFile().getName();
    }

    public File getFile() {
        return existingFile;
        // return versionedFile.get();
    }

    /**
     * Only implemented for non-versioned resources, always returns itself with updated version.
     */
    @Override
    public FileResourceProvider createResourceVersion(String version) {
        if (isVersioned) {
            throw new NotImplementedException("Not implemented when file is versioned");
        }

        // Create new versioned file
        return newInstance(existingFile, version);

        // File newVersionedFile = getVersionedFile(fileWithoutVersion, version);
        // String filenameNoExt = SpecsIo.removeExtension(fileWithoutVersion);
        // String extension = SpecsIo.getExtension(fileWithoutVersion);
        // extension = extension.isEmpty() ? extension : "." + extension;

        // String newFilename = filenameNoExt + version + extension;

        // File newFile = new File(SpecsIo.getParent(fileWithoutVersion), newFilename);
        // FileResourceProvider provider = newInstance(newVersionedFile, version);
        // return provider;
    }

}
