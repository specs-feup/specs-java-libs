/*
 * Copyright 2010 SPeCS Research Group.
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

package pt.up.fe.specs.util.jobs;

import java.io.File;
import java.util.List;

/**
 * Represents the files and root folder of a set of files for compilation.
 * 
 * @author Joao Bispo
 */
public class FileSet {

    /**
     * INSTANCE VARIABLES
     */
    private final List<String> sourceFilenames;
    private final File sourceFolder;
    private String outputName;

    public FileSet(File sourceFolder, List<String> sourceFiles, String outputName) {
        this.sourceFilenames = sourceFiles;
        this.sourceFolder = sourceFolder;
        this.outputName = outputName;
    }

    public List<String> getSourceFilenames() {
        return this.sourceFilenames;
    }

    public File getSourceFolder() {
        return this.sourceFolder;
    }

    public String outputName() {
        return this.outputName;
    }

    public void setOutputName(String outputName) {
        this.outputName = outputName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SOURCEFOLDER:" + this.sourceFolder;
    }

}
