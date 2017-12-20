/**
 * Copyright 2017 SPeCS.
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GitRepos {

    // Maps repo names to the folder where they are in the system
    // One instance per JVM
    private static final Map<String, File> REPOS = new ConcurrentHashMap<>();

    // Maps repo names to the folder where they are in the system
    // private final Map<String, File> repos;

    public GitRepos() {
        // this.repos = new HashMap<>();
    }

    public File getFolder(String repositoryPath) {
        // Get repo name
        String repoName = SpecsGit.getRepoName(repositoryPath);

        // Check if it is already in the map
        // File repoFolder = repos.get(repoName);
        File repoFolder = REPOS.get(repoName);
        if (repoFolder == null) {
            repoFolder = SpecsGit.parseRepositoryUrl(repositoryPath);
            // repos.put(repoName, repoFolder);
            REPOS.put(repoName, repoFolder);
        }

        return repoFolder;
    }
}
