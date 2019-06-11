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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.google.common.base.Preconditions;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 *
 * @author JoaoBispo
 *
 */
public class SpecsGit {

    private static final String SPECS_GIT_REPOS_FOLDER = "specs_git_repos";

    public static File parseRepositoryUrl(String repositoryPath) {
        String repoName = getRepoName(repositoryPath);

        // Get repo folder
        File eclipseBuildFolder = getRepositoriesFolder();
        File repoFolder = new File(eclipseBuildFolder, repoName);

        // If folder does not exist, or if it exists and is empty, clone repository
        if (!repoFolder.exists() || SpecsIo.isEmptyFolder(repoFolder)) {
            try {
                SpecsLogs.msgInfo("Cloning repo '" + repositoryPath + "' to folder '" + repoFolder + "'");
                Git.cloneRepository()
                        .setURI(repositoryPath)
                        .setDirectory(repoFolder)
                        .call();

                return repoFolder;
            } catch (GitAPIException e) {
                throw new RuntimeException("Could not clone repository '" + repositoryPath + "'", e);
            }
        }

        // Repository already exists, pull

        try {
            SpecsLogs.msgInfo("Pulling repo '" + repositoryPath + "' in folder '" + repoFolder + "'");
            Git gitRepo = Git.open(repoFolder);
            PullCommand pullCmd = gitRepo.pull();
            pullCmd.call();
        } catch (GitAPIException | IOException e) {
            throw new RuntimeException("Could not pull repository '" + repositoryPath + "'", e);
        }

        return repoFolder;
    }

    public static String getRepoName(String repositoryPath) {
        try {
            String repoPath = new URI(repositoryPath).getPath();

            Preconditions.checkArgument(!repoPath.endsWith("/"),
                    "Did not expect path to end with '/', take care of this case");

            if (repoPath.toLowerCase().endsWith(".git")) {
                repoPath = repoPath.substring(0, repoPath.length() - ".git".length());
            }

            int slashIndex = repoPath.lastIndexOf('/');
            if (slashIndex != -1) {
                repoPath = repoPath.substring(slashIndex + 1);
            }

            return repoPath;

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    public static File getRepositoriesFolder() {
        return new File(SpecsIo.getTempFolder(), SPECS_GIT_REPOS_FOLDER);
    }

}
