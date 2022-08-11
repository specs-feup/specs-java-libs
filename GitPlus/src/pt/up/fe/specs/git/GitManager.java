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

import java.util.HashMap;
import java.util.Map;

/**
 * Manages a session of git repositories.
 * 
 * @author Joao Bispo
 *
 */
public class GitManager {

    private final Map<String, GitRepo> repoCache;

    public GitManager() {
        this.repoCache = new HashMap<>();
    }

    public GitRepo parseRepositoryUrl(String repositoryPath) {

        var cachedResult = repoCache.get(repositoryPath);
        if (cachedResult != null) {
            return cachedResult;
        }

        // Create repo instance
        var repo = GitRepo.newInstance(repositoryPath);

        // Store in cache
        repoCache.put(repositoryPath, repo);

        return repo;
    }

}
