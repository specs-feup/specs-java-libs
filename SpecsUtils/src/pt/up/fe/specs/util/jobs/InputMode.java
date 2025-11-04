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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.jobs;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Distinguishes between two situations about the given source folder:
 * 
 * 1) files: Each .c file inside the source folder is a program; 3) folders:
 * Each folder inside the source folder is a program; 1) singleFile: the source
 * folder is interpreted as a single file, which corresponds to a program; 2)
 * singleFolder: The files inside the source folder is a program;
 * 
 * @author Joao Bispo
 */
public enum InputMode {
    files,
    /**
     * The given path represents a folder that contains several folders, and each
     * folder is a project.
     */
    folders,
    singleFile,
    singleFolder;

    public List<FileSet> getPrograms(File sourcePath, Collection<String> extensions, Integer folderLevel) {
        switch (this) {
            case folders:
                if (folderLevel == null) {
                    throw new IllegalArgumentException("FolderLevel cannot be null for folders mode");
                }
                if (extensions == null) {
                    throw new IllegalArgumentException("Extensions collection cannot be null");
                }
                return JobUtils.getSourcesFoldersMode(sourcePath, extensions, folderLevel);
            case files:
                if (extensions == null) {
                    throw new IllegalArgumentException("Extensions collection cannot be null");
                }
                return JobUtils.getSourcesFilesMode(sourcePath, extensions);
            case singleFile:
                // singleFile mode doesn't use extensions parameter, so null is allowed
                return JobUtils.getSourcesSingleFileMode(sourcePath, extensions);
            case singleFolder:
                if (extensions == null) {
                    throw new IllegalArgumentException("Extensions collection cannot be null");
                }
                return JobUtils.getSourcesSingleFolderMode(sourcePath, extensions);
            default:
                throw new RuntimeException("Case not supported:" + this);
        }
    }

    /**
     * Returns true if the path mode represents a folder. False, if it represents a
     * file.
     *
     */
    public boolean isFolder() {
        return (this != singleFile);
    }

}
