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

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.google.common.base.Preconditions;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Utility class for Git operations using JGit.
 * Provides methods for cloning, checking out, and managing Git repositories.
 *
 * @author JoaoBispo
 */
public class SpecsGit {

    private static final String SPECS_GIT_REPOS_FOLDER = "specs_git_repos";

    /**
     * Retrieves the credentials provider for the given repository URL.
     *
     * @param repositoryPath the URL of the repository
     * @return the credentials provider, or null if no credentials are required
     */
    public static CredentialsProvider getCredentials(String repositoryPath) {

        var currentString = repositoryPath;

        // Remove prefixes
        if (currentString.startsWith("http://")) {
            currentString = currentString.substring("http://".length());
        }

        if (currentString.startsWith("https://")) {
            currentString = currentString.substring("https://".length());
        }

        var firstSlashIndex = currentString.indexOf('/');
        if (firstSlashIndex != -1) {
            currentString = currentString.substring(0, firstSlashIndex);
        }

        // Check if there is a login/pass in the url
        var atIndex = currentString.lastIndexOf('@');

        // No login
        if (atIndex == -1) {
            return null;
        }

        var loginPass = currentString.substring(0, atIndex);

        // Split login pass
        var colonIndex = loginPass.indexOf(':');

        // Only login, ask for password
        if (colonIndex == -1) {

            var login = loginPass;

            Console console = System.console();
            if (console == null) {
                SpecsLogs.info(
                        "Specified git url with login information but no password (expected ':'), and could not use the console");
                return null;
            }

            char[] passwordArray = console.readPassword("Enter password for user '" + login + "': ");

            return new UsernamePasswordCredentialsProvider(login, new String(passwordArray));
        }

        var login = loginPass.substring(0, colonIndex);
        var pass = loginPass.substring(colonIndex + 1, loginPass.length());
        return new UsernamePasswordCredentialsProvider(login, pass);
    }

    /**
     * Pulls the latest changes from the remote repository into the local
     * repository.
     *
     * @param repoFolder the folder of the local repository
     * @return the result of the pull operation
     */
    public static PullResult pull(File repoFolder) {
        return pull(repoFolder, null);
    }

    /**
     * Pulls the latest changes from the remote repository into the local
     * repository.
     *
     * @param repoFolder the folder of the local repository
     * @param cp         the credentials provider
     * @return the result of the pull operation
     */
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

    /**
     * Computes the differences between the working directory and the index of the
     * repository.
     *
     * @param repoFolder the folder of the local repository
     * @return a list of differences
     */
    public static List<DiffEntry> diff(File repoFolder) {
        try {
            SpecsLogs.msgInfo("Diff repo in folder '" + repoFolder + "'");
            Git gitRepo = Git.open(repoFolder);
            return gitRepo.diff().call();
        } catch (GitAPIException | IOException e) {
            SpecsLogs.info("Could not pull repository '" + repoFolder + "': " + e);
            return null;
        }
    }

    /**
     * Clones a repository into the specified folder.
     *
     * @param repositoryPath the URL of the repository
     * @param outputFolder   the folder where the repository will be cloned
     * @param cp             the credentials provider
     * @return the Git object representing the cloned repository
     */
    public static Git clone(String repositoryPath, File outputFolder, CredentialsProvider cp) {
        return clone(repositoryPath, outputFolder, cp, null);
    }

    /**
     * Clones a repository into the specified folder and checks out the specified
     * branch.
     *
     * @param repositoryPath the URL of the repository
     * @param outputFolder   the folder where the repository will be cloned
     * @param cp             the credentials provider
     * @param branch         the branch to check out
     * @return the Git object representing the cloned repository
     */
    public static Git clone(String repositoryPath, File outputFolder, CredentialsProvider cp, String branch) {
        String repoName = getRepoName(repositoryPath);

        // Get repo folder
        File repoFolder = new File(outputFolder, repoName);

        try {
            SpecsLogs.msgInfo("Cloning repo '" + repositoryPath + "' to folder '" + repoFolder + "'");

            var git = Git.cloneRepository()
                    .setURI(repositoryPath)
                    .setDirectory(repoFolder);

            if (cp != null) {
                git.setCredentialsProvider(cp);
            }

            if (branch != null) {
                System.out.println("Setting branch to " + branch);
                git.setBranch(normalizeTag(branch));
            }

            Git repo = git.call();

            repo.close();

            return repo;
        } catch (GitAPIException e) {
            throw new RuntimeException("Could not clone repository '" + repositoryPath + "'", e);
        }
    }

    /**
     * Normalizes a tag name by adding the "refs/tags/" prefix if it is not already
     * present.
     *
     * @param tag the tag name
     * @return the normalized tag name
     */
    public static String normalizeTag(String tag) {
        var prefix = "refs/tags/";
        return tag.startsWith(prefix) ? tag : prefix + tag;
    }

    /**
     * Checks if the specified tag exists in the remote repository.
     *
     * @param repositoryPath the URL of the repository
     * @param tag            the tag name
     * @return true if the tag exists, false otherwise
     */
    public static boolean hasTag(String repositoryPath, String tag) {
        return hasTag(repositoryPath, tag, null);
    }

    /**
     * Checks if the specified tag exists in the remote repository.
     *
     * @param repositoryPath      the URL of the repository
     * @param tag                 the tag name
     * @param credentialsProvider the credentials provider
     * @return true if the tag exists, false otherwise
     */
    public static boolean hasTag(String repositoryPath, String tag, CredentialsProvider credentialsProvider) {
        LsRemoteCommand ls = Git.lsRemoteRepository();
        try {
            ls
                    .setRemote(repositoryPath);

            if (credentialsProvider != null) {
                ls.setCredentialsProvider(credentialsProvider);
            }

            var remoteRefs = ls.setHeads(false) // true by default, set to false if not interested in refs/heads/*
                    .setTags(true) // include tags in result
                    .callAsMap();

            return remoteRefs.keySet().contains(normalizeTag(tag));
        } catch (Exception e) {
            throw new RuntimeException("Could not check tags of repository '" + repositoryPath + "'", e);
        }
    }

    /**
     * Extracts the name of the repository from its URL.
     *
     * @param repositoryPath the URL of the repository
     * @return the name of the repository
     */
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

    /**
     * Returns the folder where repositories are stored.
     *
     * @return the folder where repositories are stored
     */
    public static File getRepositoriesFolder() {
        return new File(SpecsIo.getTempFolder(), SPECS_GIT_REPOS_FOLDER);
    }

    /**
     * Clones or pulls a repository into the specified folder.
     *
     * @param repositoryPath the URL of the repository
     * @param outputFolder   the folder where the repository will be cloned or
     *                       pulled
     * @param user           the username for authentication
     * @param password       the password for authentication
     * @return the folder where the repository is located
     */
    public static File cloneOrPull(String repositoryPath, File outputFolder, String user, String password) {
        String repoName = getRepoName(repositoryPath);

        // Get repo folder
        File repoFolder = new File(outputFolder, repoName);

        // If folder does not exist, or if it exists and is empty, clone repository
        if (!repoFolder.exists() || SpecsIo.isEmptyFolder(repoFolder)) {
            try {
                SpecsLogs.msgInfo("Cloning repo '" + repositoryPath + "' to folder '" + repoFolder + "'");

                var git = Git.cloneRepository()
                        .setURI(repositoryPath)
                        .setDirectory(repoFolder);

                if (user != null & password != null) {
                    CredentialsProvider cp = new UsernamePasswordCredentialsProvider(user, password);
                    git.setCredentialsProvider(cp);
                }

                Git repo = git.call();

                repo.close();
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

    /**
     * Adds the specified files to the index of the repository.
     *
     * @param repoFolder the folder of the local repository
     * @param filesToAdd the files to add
     * @return the result of the add operation
     */
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
            SpecsLogs.warn("Error message:\n", e);
            return null;
        }
    }

    /**
     * Commits and pushes the changes in the repository.
     *
     * @param repoFolder the folder of the local repository
     * @param cp         the credentials provider
     */
    public static void commitAndPush(File repoFolder, CredentialsProvider cp) {
        System.out.println("Commiting and pushing " + repoFolder);

        try (var gitRepo = Git.open(repoFolder)) {
            gitRepo.commit().setMessage("Added JMM tests and JUnit test case").call();
            gitRepo.push().setCredentialsProvider(cp).call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Checks if the specified commit value is the name of a branch in the
     * repository.
     *
     * @param repo   the Git repository
     * @param commit the commit value
     * @return true if the commit value is the name of a branch, false otherwise
     */
    public static boolean isBranchName(Git repo, String commit) {
        try {
            var commitPattern = "refs/remotes/origin/" + commit;

            return repo.branchList()
                    .setListMode(ListMode.REMOTE)
                    .call()
                    .stream()
                    .map(Ref::getName)
                    .filter(name -> name.equals(commitPattern))
                    .findFirst()
                    .isPresent();

        } catch (GitAPIException e) {
            throw new RuntimeException("Could not get list of repository branches", e);
        }

    }
}
