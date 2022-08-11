/**
 * Copyright 2022 SPeCS.
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

package pt.up.fe.specs.git;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;

/**
 * Information about a git repository.
 * 
 * @author Joao Bispo
 * 
 * @deprecated
 *
 */
@Deprecated
public class GitRepoOld {

    private final File localFolder;
    private final File rootFolder;

    public GitRepoOld(File workFolder, File rootFolder) {
        this.localFolder = workFolder;
        this.rootFolder = rootFolder;
    }

    public GitRepoOld(File repoFolder) {
        this(repoFolder, repoFolder);
    }

    /**
     * The local folder resultant from parsing a git URL.
     * 
     * <p>
     * Different from the repo root folder when specifying option 'folder' in the URL.
     * 
     * @return the workFolder
     */
    public File getLocalFolder() {
        return localFolder;
    }

    public Git getRepo() {
        try {
            return Git.open(rootFolder);
        } catch (IOException e) {
            throw new RuntimeException("Could not open git repository in folder '" + rootFolder + "'", e);
        }
    }
    // /**
    // * @return the rootFolder
    // */
    // public File getRootFolder() {
    // return rootFolder;
    // }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GitRepo [localFolder=" + localFolder + ", rootFolder=" + rootFolder + "]";
    }

}
