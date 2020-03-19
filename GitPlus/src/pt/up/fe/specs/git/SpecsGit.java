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
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

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

    public static PullResult pull(File repoFolder) {
        return pull(repoFolder, null);
    }

    public static PullResult pull(File repoFolder, CredentialsProvider cp) {
        try {
            SpecsLogs.msgInfo("Pulling repo in folder '" + repoFolder + "'");
            Git gitRepo = Git.open(repoFolder);
            PullCommand pullCmd = gitRepo.pull();

            if (cp != null) {
                pullCmd.setCredentialsProvider(cp);
            }

            return pullCmd.call();
        } catch (GitAPIException | IOException e) {
            SpecsLogs.info("Could not pull repository '" + repoFolder + "': " + e);
            return null;
        }
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

    public static File cloneOrPull(String repositoryPath, File outputFolder, String user, String password) {
        String repoName = getRepoName(repositoryPath);

        // Get repo folder
        File repoFolder = new File(outputFolder, repoName);

        // If folder does not exist, or if it exists and is empty, clone repository
        if (!repoFolder.exists() || SpecsIo.isEmptyFolder(repoFolder)) {
            try {
                SpecsLogs.msgInfo("Cloning repo '" + repositoryPath + "' to folder '" + repoFolder + "'");

                // Delete folder to avoid errors
                // if (SpecsIo.isEmptyFolder(repoFolder)) {
                // SpecsIo.deleteFolder(repoFolder);
                // }
                var git = Git.cloneRepository()
                        .setURI(repositoryPath)
                        .setDirectory(repoFolder);

                if (user != null & password != null) {
                    CredentialsProvider cp = new UsernamePasswordCredentialsProvider(user, password);
                    git.setCredentialsProvider(cp);// .call();
                }

                // System.out.println("OUTPUT FOLDER:" + outputFolder);
                Git repo = git.call();

                // File workTree = git.getRepository().getWorkTree();
                repo.close();
                // System.out.println("REPO: " + workTree);
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

            if (user != null & password != null) {
                CredentialsProvider cp = new UsernamePasswordCredentialsProvider(user, password);
                pullCmd.setCredentialsProvider(cp);
            }

            pullCmd.call();
        } catch (GitAPIException | IOException e) {
            throw new RuntimeException("Could not pull repository '" + repositoryPath + "'", e);
        }

        return repoFolder;
    }

    public static DirCache add(File repoFolder, List<File> filesToAdd) {
        System.out.println("Git add to " + repoFolder);
        try {
            var gitRepo = Git.open(repoFolder);

            var gitAdd = gitRepo.add();

            for (var repoFile : filesToAdd) {
                var relativePath = SpecsIo.getRelativePath(repoFile, repoFolder);
                System.out.println("Adding " + relativePath);
                gitAdd.addFilepattern(relativePath);
            }

            return gitAdd.call();

        } catch (Exception e) {
            SpecsLogs.msgWarn("Error message:\n", e);
            return null;
        }
    }

    public static void commitAndPush(File repoFolder, CredentialsProvider cp) {
        System.out.println("Commiting and pushing " + repoFolder);

        try (var gitRepo = Git.open(repoFolder)) {
            gitRepo.commit().setMessage("Added JMM tests and JUnit test case").call();
            gitRepo.push().setCredentialsProvider(cp).call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
