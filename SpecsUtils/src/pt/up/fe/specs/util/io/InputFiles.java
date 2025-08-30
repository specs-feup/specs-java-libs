/*
 * Copyright 2011 SPeCS Research Group.
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

package pt.up.fe.specs.util.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 *
 * @author Joao Bispo
 */
public class InputFiles {

    public InputFiles(boolean isSingleFile, File inputPath, List<File> inputFiles) {
        this.isSingleFile = isSingleFile;
        this.inputPath = inputPath;
        this.inputFiles = inputFiles;
    }

    /**
     * Collects the file or files of the input path.
     *
     * @param inputPath can be the path to a single file or to a folder
     * @return
     */
    public static InputFiles newInstance(String inputPath) {
        File inputPathFile = new File(inputPath);
        if (!inputPathFile.exists()) {
            SpecsLogs.warn("Input path '" + inputPathFile + "' does not exist.");
            return null;
        }

        // Determine if it is a file or a folder
        boolean isSingleFile = false;
        if (inputPathFile.isFile()) {
            isSingleFile = true;
        }

        List<File> inputFiles = InputFiles.getFiles(inputPath, isSingleFile);

        return new InputFiles(isSingleFile, inputPathFile, inputFiles);
    }

    private static List<File> getFiles(String inputPath, boolean isSingleFile) {
        // Is File mode
        if (isSingleFile) {
            File inputFile = SpecsIo.existingFile(inputPath);
            if (inputFile == null) {
                throw new RuntimeException("Could not open file '" + inputPath + "'");
            }
            List<File> files = new ArrayList<>();
            files.add(inputFile);
            return files;
        }

        // Is Folder mode
        File inputFolder = SpecsIo.mkdir(inputPath);
        if (inputFolder == null) {
            throw new RuntimeException("Could not open folder '" + inputPath + "'");
        }

        return SpecsIo.getFilesRecursive(inputFolder);

    }

    public final boolean isSingleFile;
    public final File inputPath;
    public final List<File> inputFiles;
}
