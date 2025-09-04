/**
 * Copyright 2013 SPeCS Research Group.
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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

public class JarPath {

    public static Optional<String> getJarFolder() {
        return new JarPath(JarPath.class, "<NO_PROPERTY>").buildJarPathInternalTry();
    }

    private final Class<?> programClass;
    private final String programName;
    private final String jarPathProperty;
    private final boolean verbose;

    public JarPath(Class<?> programClass, String jarPathProperty) {
        this(programClass, programClass.getSimpleName(), jarPathProperty);
    }

    public JarPath(Class<?> programClass, String programName, String jarPathProperty) {
        this(programClass, programName, jarPathProperty, true);
    }

    public JarPath(Class<?> programClass, String programName, String jarPathProperty, boolean verbose) {
        this.programClass = programClass;
        this.programName = programName;
        this.jarPathProperty = jarPathProperty;
        this.verbose = verbose;
    }

    public String buildJarPath() {
        String path = buildJarPathInternal();

        if (!path.endsWith("/")) {
            path = path + "/";
        }

        return path;
    }

    private String buildJarPathInternal() {
        String jarPath = buildJarPathInternalTry().orElse(null);
        if (jarPath != null) {
            return jarPath;
        }

        jarPath = SpecsIo.getWorkingDir().getAbsolutePath();
        jarPath = jarPath.replace('\\', '/');
        jarPath = jarPath.substring(0, jarPath.lastIndexOf("/") + 1);

        // 3. As last resort, return current directory. Warn user and recommend to set
        // property
        if (verbose) {
            SpecsLogs.debug(() -> "Could not find Jar path (maybe application is being run from "
                    + "another application in a different process)");
            SpecsLogs.msgInfo(
                    "Setting Jar path to current folder (" + jarPath + ")\n. Try passing the " + this.programName
                            + " Jar location with the system property '" + this.jarPathProperty + "'");
            SpecsLogs.msgInfo("Example: java -D" + this.jarPathProperty + "=<JAR_FOLDER> ...");

        }

        return jarPath;
    }

    private Optional<String> buildJarPathInternalTry() {
        String jarPath;

        // 1. Check if property JAR_PATH is set
        jarPath = System.getProperty(this.jarPathProperty);

        if (jarPath != null) {
            try {
                File jarFolder = SpecsIo.existingFolder(null, jarPath);

                if (jarFolder != null) {
                    try {
                        return Optional.of(jarFolder.getCanonicalPath());
                    } catch (IOException e) {
                        return Optional.of(jarFolder.getAbsolutePath());
                    }
                }
            } catch (RuntimeException e) {
                if (verbose) {
                    SpecsLogs.msgInfo("Invalid path '" + jarPath + "' given by system property '" + this.jarPathProperty
                            + "': " + e.getMessage());
                }
            }

            if (verbose) {
                SpecsLogs.msgInfo("Could not find folder '" + jarPath
                        + "', given by system property '" + this.jarPathProperty + "'");
            }

        }

        // 2. Try to find the location of the jar
        jarPath = getJarPathAuto();
        if (jarPath != null) {
            return Optional.of(jarPath);
        }

        return Optional.empty();
    }

    private String getJarPathAuto() {
        String jarfilePath;

        try {
            var codeSource = this.programClass.getProtectionDomain().getCodeSource();
            if (codeSource == null) {
                return null;
            }

            var location = codeSource.getLocation();
            if (location == null) {
                return null;
            }

            jarfilePath = location.toURI().getPath();

        } catch (URISyntaxException e) {
            SpecsLogs.msgInfo("Problems decoding URI of jarpath\n" + e.getMessage());
            return null;
        }

        // If could not obtain path, return null
        if (jarfilePath == null) {
            return null;
        }

        return jarfilePath.substring(0, jarfilePath.lastIndexOf("/") + 1);

    }

}
