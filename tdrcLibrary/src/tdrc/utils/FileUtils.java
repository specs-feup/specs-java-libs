/**
 * Copyright 2014 Tiago Carvalho.
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

package tdrc.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.util.SpecsIo;

/**
 * Utility class for file operations in tdrcLibrary.
 */
public class FileUtils {

    /**
     * Retrieve a list of files if the files match to the given extension
     * (accepts regular expressions).
     * 
     * @param dir       the directory to search on
     * @param extension the regular expression of the accepted extensions
     * @param recursive should the search be recursive on inner folders?
     * @return a list of accepted files (directories not included!)
     * @throws RuntimeException
     *                          if the given file is not a folder
     */
    public static List<File> getFilesFromDir(File dir, String extension, boolean recursive) {
        final List<File> filesList = new ArrayList<>();
        if (dir.isDirectory()) {
            addFilesFromDir(dir, extension, recursive, filesList);
        } else {
            throw new RuntimeException("The given file is not a folder: " + dir);
        }
        return filesList;
    }

    /**
     * Auxiliary method for
     * {@link FileUtils#getFilesFromDir(File, String, boolean)}.
     * 
     * @param dir       the directory to search on
     * @param extension the regular expression of the accepted extensions
     * @param recursive should the search be recursive on inner folders?
     * @param files     the list to store the accepted files
     *                  the list to store the accepted files
     */
    private static void addFilesFromDir(File dir, String extension, boolean recursive, List<File> files) {
        final List<File> folders = new ArrayList<>();
        for (final File f : dir.listFiles()) {
            if (f.isDirectory() && recursive) { // Necessary to give priority to files in current directory
                folders.add(f);
            } else if (!f.isDirectory() && SpecsIo.getExtension(f).matches(extension)) {
                files.add(f);
            }
        }
        for (final File folder : folders) {
            addFilesFromDir(folder, extension, recursive, files);
        }
    }

}
