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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.git;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for managing multiple Git repositories within the JVM.
 * Provides methods to retrieve and cache repository instances by path.
 *
 * TODO: Candidate for using GitManager.
 *
 * @author Joao Bispo
 */
public class GitRepos {

    /**
     * Maps repo names to the folder where they are in the system. One instance per
     * JVM.
     */
    private static final Map<String, GitRepo> REPOS = new ConcurrentHashMap<>();

    /**
     * Returns a {@link GitRepo} instance for the given repository path. If not
     * cached, creates and caches a new instance.
     *
     * @param repositoryPath the path to the repository
     * @return the GitRepo instance
     */
    public static GitRepo getRepo(String repositoryPath) {
        var repo = REPOS.get(repositoryPath);

        if (repo == null) {
            repo = GitRepo.newInstance(repositoryPath);
            REPOS.put(repositoryPath, repo);
        }

        return repo;
    }

    /**
     * Returns the working folder for the given repository path.
     *
     * @param repositoryPath the path to the repository
     * @return the working folder as a File
     */
    public File getFolder(String repositoryPath) {
        return getRepo(repositoryPath).getWorkFolder();
    }
}
