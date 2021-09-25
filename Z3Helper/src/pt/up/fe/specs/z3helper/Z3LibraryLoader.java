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

package pt.up.fe.specs.z3helper;

import java.io.File;
import java.io.IOException;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.lang.SpecsPlatforms;
import pt.up.fe.specs.util.SpecsSystem;

/**
 *
 * @author Luís Reis
 *
 */
public class Z3LibraryLoader {
    private static boolean loaded = false;

    private Z3LibraryLoader() {
    }

    /**
     * Loads the native libraries necessary for Z3 to work.
     * 
     * @return True if the libraries were loaded, false if they had already previously been loaded.
     * @throws IOException
     *             If there was a problem copying the native libraries.
     * @throws UnsupportedPlatformException
     *             If there was a problem loading the libraries, namely if the current platform is not supported.
     */
    public static synchronized boolean loadNativeLibraries() throws IOException, UnsupportedPlatformException {
        if (Z3LibraryLoader.loaded) {
            return false;
        }

        if (SpecsSystem.is64Bit()) {
            if (SpecsPlatforms.isWindows()) {
                return loadWin64Libraries();
            }
            if (SpecsPlatforms.isLinux()) {
                return loadLinux64Libraries();
            }
        }

        throw new UnsupportedPlatformException();
    }

    private static boolean loadWin64Libraries() throws IOException {
        prepareResourcesForLoading(LibraryResource.WIN64_LIBZ3, LibraryResource.WIN64_LIBZ3JAVA);

        try {
            System.loadLibrary("libz3");
            Z3LibraryLoader.loaded = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();

            throw new RuntimeException(
                    "Failed to load z3java with java lib path " + System.getProperty("java.library.path"), e);
        }
    }

    private static boolean loadLinux64Libraries() throws IOException {

        prepareResourcesForLoading(LibraryResource.LINUX64_LIBZ3JAVA);

        try {
            System.loadLibrary("z3java");
            Z3LibraryLoader.loaded = true;

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            throw new RuntimeException(
                    "Failed to load z3java with java lib path " + System.getProperty("java.library.path"), e);
        }
    }

    private static void prepareResourcesForLoading(LibraryResource... resources) throws IOException {
        File directory = SpecsIo.getJarPath(Z3LibraryLoader.class).get();
        String path = directory.getAbsoluteFile().toString();
        SpecsSystem.addJavaLibraryPath(path);

        for (LibraryResource resource : resources) {
            copyResource(directory, resource);
        }
    }

    private static File copyResource(File directory, LibraryResource resource) {
        File destination = new File(directory, resource.getFileName());

        SpecsIo.copy(SpecsIo.resourceToStream(resource), destination);

        return destination;
    }
}
