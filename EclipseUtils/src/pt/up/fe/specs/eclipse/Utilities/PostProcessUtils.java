/**
 * Copyright 2023 SPeCS.
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

package pt.up.fe.specs.eclipse.Utilities;

import java.io.File;
import java.util.UUID;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

public class PostProcessUtils {

    private static final String MANIFEST_PATH = "META-INF/MANIFEST.MF";

    /**
     * Some incompatibilities may occur in generated JAR files. Around middle of 2023 Linux version of openjdk appears
     * to no longer support manifest files with Windows carriage return (even if it is part of the manifest standard).
     * 
     * This method attempts to remove \r inside the manifest file of the JAR.
     * 
     * Supports both JAR files and a ZIP file with a single JAR in the root.
     * 
     * @param builtFile
     */
    public static void processBuiltFile(File builtFile) {
        SpecsCheck.checkArgument(builtFile.isFile(), () -> "File " + builtFile.getAbsolutePath() + " does not exist");

        var extension = SpecsIo.getExtension(builtFile).toLowerCase();
        switch (extension) {
        case "jar":
            processJarFileExtract(builtFile);
            return;
        case "zip":
            processZipFileExtract(builtFile);
            return;
        default:
            throw new RuntimeException("Extension not supported: " + extension);
        }
    }

    private static void processJarFileExtract(File jarFile) {
        var tempFolder = SpecsIo.getTempFolder("extracted_" + jarFile.getName() + "_" + UUID.randomUUID().toString());
        SpecsIo.extractZip(jarFile, tempFolder);

        var manifestFile = new File(tempFolder, MANIFEST_PATH);

        SpecsCheck.checkArgument(manifestFile.isFile(),
                () -> "Could not find manifest file in path '" + manifestFile.getAbsolutePath() + "'");

        SpecsIo.write(manifestFile, SpecsIo.read(manifestFile).replace("\r", ""));

        // Re-zip and replace
        SpecsIo.zip(SpecsIo.getFilesRecursive(tempFolder), tempFolder, jarFile);

        // SpecsIo.zip(null, zipFile, zipFile);
        SpecsIo.deleteFolder(tempFolder);
    }

    private static void processZipFileExtract(File zipFile) {

        var tempFolder = SpecsIo.getTempFolder("extracted_" + zipFile.getName() + "_" + UUID.randomUUID().toString());
        SpecsIo.extractZip(zipFile, tempFolder);

        var jarFiles = SpecsIo.getFiles(tempFolder).stream()
                .filter(file -> file.getName().toLowerCase().endsWith(".jar"))
                .collect(Collectors.toList());

        SpecsCheck.checkArgument(!jarFiles.isEmpty(),
                () -> "Could not find JAR files in root of zip '" + zipFile.getAbsolutePath() + "'");

        if (jarFiles.size() > 1) {
            SpecsLogs.info("Found more that one JAR file in root of zip '" + zipFile.getAbsolutePath()
                    + "', using first occurence: " + jarFiles);
        }

        var jarFile = jarFiles.get(0);

        processBuiltFile(jarFile);

        // Re-zip and replace
        SpecsIo.zip(SpecsIo.getFilesRecursive(tempFolder), tempFolder, zipFile);

        // SpecsIo.zip(null, zipFile, zipFile);
        SpecsIo.deleteFolder(tempFolder);
    }
}
