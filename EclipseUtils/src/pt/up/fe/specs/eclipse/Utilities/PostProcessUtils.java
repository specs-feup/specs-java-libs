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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * Based from here: https://stackoverflow.com/questions/11502260/modifying-a-text-file-in-a-zip-archive-in-java
     * 
     * @param jarFile
     */
    private static void processJarFile(File jarFile) {
        processFile(Paths.get(jarFile.getAbsolutePath()), MANIFEST_PATH);
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

    private static void processZipFileInplace(File zipFile) {
        // Find JAR file
        Path zipFilePath = Paths.get(zipFile.getAbsolutePath());

        try (FileSystem fs = FileSystems.newFileSystem(zipFilePath, (ClassLoader) null)) {

            // for (var rootPath : fs.getRootDirectories()) {
            // System.out.println("OATH: " + rootPath);
            // }
            var jarFiles = new ArrayList<Path>();

            Files.walkFileTree(fs.getRootDirectories().iterator().next(), Collections.emptySet(), 1,
                    new FileVisitor<Path>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                                throws IOException {
                            // System.out.println("preVisitDirectory: " + dir);
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                                throws IOException {
                            // System.out.println("visitFile: " + file);

                            var ext = SpecsIo.getExtension(file.toString()).toLowerCase();
                            if ("jar".equals(ext)) {
                                jarFiles.add(file);
                            }

                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFileFailed(Path file, IOException exc)
                                throws IOException {
                            // System.out.println("visitFileFailed: " + file);
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                                throws IOException {
                            // System.out.println("postVisitDirectory: " + dir);
                            return FileVisitResult.CONTINUE;
                        }
                    });

            System.out.println("JAR FILES: " + jarFiles);

            var jarPath = jarFiles.get(0);

            processFile(jarPath, MANIFEST_PATH);

        } catch (IOException e) {
            throw new RuntimeException("Could not process zip file '" + zipFile.getAbsolutePath() + "'", e);
        }

        // processFile(jarFile, MANIFEST_PATH);
    }

    private static void processFile(Path zipFilePath, String manifestPath) {

        try (FileSystem fs = FileSystems.newFileSystem(zipFilePath)) {
            Path source = fs.getPath(manifestPath);

            SpecsCheck.checkArgument(Files.exists(source),
                    () -> "Could not find path '" + manifestPath + "' inside path '" + zipFilePath + "'");

            var tempName = manifestPath + ".eclipse-utils-temp";
            Path temp = fs.getPath(tempName);
            if (Files.exists(temp)) {
                SpecsLogs.info("Deleting temporary file '" + tempName + "' inside '" + zipFilePath + "'");
                Files.delete(temp);
            }
            Files.move(source, temp);
            modifyManifest(temp, source);
            Files.delete(temp);
        } catch (IOException e) {
            throw new RuntimeException("Could not process JAR file", e);
        }

    }

    /**
     * Based from here: https://stackoverflow.com/questions/11502260/modifying-a-text-file-in-a-zip-archive-in-java
     * 
     * @param src
     * @param dst
     * @throws IOException
     */
    static void modifyManifest(Path src, Path dst) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Files.newInputStream(src)));
                BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(Files.newOutputStream(dst)))) {

            // Simply replace whatever new line is being used with just \n

            String line;
            while ((line = br.readLine()) != null) {
                // line = line.replace("key1=value1", "key1=value2");
                bw.write(line);

                bw.write("\n");
                // bw.newLine();
            }
        }
    }
}
