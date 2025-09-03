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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.git;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Represents a local clone of a remote Git repository, with support for options such as commit and folder selection.
 * Provides methods for preparing, cloning, and updating the repository.
 */
public class GitRepo {

    private final Map<GitUrlOption, String> options;
    private final String repoUrl;
    private final File repoFolder;
    private final File workFolder;

    /**
     * Creates a new GitRepo instance with the given repository URL and options.
     *
     * @param repoUrl the URL of the remote repository
     * @param options a map of options for the repository, such as commit and folder
     */
    public GitRepo(String repoUrl, Map<GitUrlOption, String> options) {
        this.repoUrl = repoUrl;
        this.options = options;
        this.repoFolder = createRepoFolder();

        try {
            prepareRepo();
        } catch (Exception e) {
            SpecsLogs.info(
                    "Problem while preparing repo folder '" + repoFolder.getAbsolutePath() + "':" + e.getMessage());
            SpecsLogs.info("Deleting folder and trying again");
            prepareRepo();
        }

        this.workFolder = createWorkFolder();
    }

    /**
     * Creates the working folder for the repository based on the options provided.
     *
     * @return the working folder
     */
    private File createWorkFolder() {
        // Check folder option
        var foldername = options.get(GitUrlOption.FOLDER);

        var workFolder = foldername != null ? new File(repoFolder, foldername) : repoFolder;

        if (!workFolder.isDirectory()) {
            throw new RuntimeException(
                    "Could not find folder '" + foldername + "' in repo root folder '" + repoFolder.getAbsolutePath()
                            + "'");
        }

        if (foldername != null) {
            SpecsLogs.msgInfo("Setting work folder as '" + workFolder.getAbsolutePath() + "'");
        }

        return workFolder;
    }

    /**
     * Creates a new GitRepo instance with the given repository URL and no options.
     *
     * @param repoUrl the URL of the remote repository
     */
    public GitRepo(String repoUrl) {
        this(repoUrl, Collections.emptyMap());
    }

    /**
     * Creates a new GitRepo instance from a repository path.
     *
     * @param repositoryPath the path to the repository
     * @return a new GitRepo instance
     */
    public static GitRepo newInstance(String repositoryPath) {
        SpecsLogs.msgInfo("Processing git url '" + repositoryPath + "'");

        // Repository URL
        var cleanRepoUrl = SpecsIo.cleanUrl(repositoryPath);

        // Parse options
        Map<GitUrlOption, String> urlOptions = parseUrl(repositoryPath);

        return new GitRepo(cleanRepoUrl, urlOptions);
    }

    /**
     * @return the options
     */
    public Map<GitUrlOption, String> getOptions() {
        return options;
    }

    /**
     * @return the repoUrl
     */
    public String getRepoUrl() {
        return repoUrl;
    }

    /**
     * @return the local folder that is the root of the repo
     */
    public File getRepoFolder() {
        return repoFolder;
    }

    /**
     * @return if option FOLDER was specified, returns the corresponding folder inside the repository. Otherwise,
     *         returns the repo root folder.
     */
    public File getWorkFolder() {
        return workFolder;
    }

    /**
     * Creates the folder for the repository based on its name and options.
     *
     * @return the repository folder
     */
    private File createRepoFolder() {

        // Get repo name
        String repoName = SpecsGit.getRepoName(repoUrl);

        // If there is a commit, append to name
        var commit = options.get(GitUrlOption.COMMIT);
        if (commit != null) {
            repoName += "_commit_" + commit;
        }

        // Get repo folder
        File eclipseBuildFolder = SpecsGit.getRepositoriesFolder();

        return new File(eclipseBuildFolder, repoName);
    }

    /**
     * Parses the repository URL and extracts options.
     *
     * @param repositoryPath the repository URL
     * @return a map of options extracted from the URL
     */
    private static Map<GitUrlOption, String> parseUrl(String repositoryPath) {
        var urlStringOptions = SpecsIo.parseUrl(repositoryPath)
                .map(url -> SpecsIo.parseUrlQuery(url))
                .orElse(new HashMap<>());

        // Map each option string to an enum
        return SpecsEnums.toEnumMap(GitUrlOption.class, urlStringOptions);
    }

    /**
     * Prepares the repository. If folder already exists but a problem is detected, deletes the folder.<br>
     * If there is no folder, a clone is performed, otherwise open the repo using the existing folder.<br>
     * If a clone was performed and the option COMMIT has a value, checkouts the commit.<br>
     * A pull is performed if no clone was performed and, there is no COMMIT value, or the COMMIT value corresponds to a
     * branch name.
     */
    private void prepareRepo() {

        checkRepoProblems();

        Git repo = null;

        var clonedRepo = false;
        // If folder does not exist, or if it exists and is empty, clone repository
        if (!repoFolder.exists() || SpecsIo.isEmptyFolder(repoFolder)) {
            try {
                SpecsLogs.msgInfo("Cloning repo '" + repoUrl + "' to folder '" + repoFolder + "'");

                repo = Git.cloneRepository()
                        .setURI(repoUrl)
                        .setDirectory(repoFolder)
                        .setCredentialsProvider(SpecsGit.getCredentials(repoUrl))
                        .call();

                // Cloning repo from scratch, no pull needed
                clonedRepo = true;

            } catch (GitAPIException e) {
                throw new RuntimeException("Could not clone repository '" + repoUrl + "'", e);
            }
        } else {
            try {
                SpecsLogs.msgInfo("Opening existing repo in local folder '" + repoFolder + "'");
                repo = Git.open(repoFolder);
            } catch (IOException e) {
                throw new RuntimeException("Could not open repository in folder '" + repoFolder.getAbsolutePath() + "'",
                        e);
            }
        }

        var commit = options.get(GitUrlOption.COMMIT);

        // If no commit specified, just pull if not cloned and return
        if (commit == null) {
            // Pull repo
            if (!clonedRepo) {
                pull(repo, null);
            }

            return;
        }

        // Commit is present, handle it

        // If the repo was just cloned, checkout the commit
        if (clonedRepo) {

            // Only checkout if not in the correct branch
            String currentBranch = getCurrentBranch(repo);

            // Nothing to be done, return
            if (commit.equals(currentBranch)) {
                SpecsLogs.msgInfo("Already in branch '" + commit + "'");
                return;
            }

            // Check if commit it is a branch name
            boolean isBranchName = SpecsGit.isBranchName(repo, commit);

            var branchOrCommit = isBranchName ? "branch" : "commit";
            try {
                // Check out commit
                SpecsLogs.msgInfo("Checking out " + branchOrCommit + " '" + commit + "'");
                var checkoutCmd = repo.checkout()
                        .setCreateBranch(true)
                        .setName(commit);

                if (isBranchName) {
                    checkoutCmd.setUpstreamMode(SetupUpstreamMode.TRACK)
                            .setStartPoint("origin/" + commit);
                }

                checkoutCmd.call();

            } catch (GitAPIException e) {
                throw new RuntimeException(
                        "Could not checkout " + branchOrCommit + " '" + commit + "' in folder '"
                                + repoFolder.getAbsolutePath()
                                + "'",
                        e);
            }

            return;
        }

        // Repo was already present, if specific commit do nothing, if branch, pull
        boolean isBranchName = SpecsGit.isBranchName(repo, commit);
        if (isBranchName) {
            pull(repo, commit);
        }
    }

    /**
     * Gets the current branch of the repository.
     *
     * @param repo the Git repository
     * @return the name of the current branch
     */
    private String getCurrentBranch(Git repo) {
        String currentBranch = null;
        try {
            currentBranch = repo.getRepository().getBranch();
        } catch (IOException e) {
            throw new RuntimeException("Could not get current branch", e);
        }
        return currentBranch;
    }

    /**
     * Checks for problems in the repository folder and cleans it if necessary.
     */
    private void checkRepoProblems() {

        // If folder only contains a single .git folder, something might have gone wrong, delete folder
        if (repoFolder.isDirectory()) {
            var files = repoFolder.listFiles();
            if (files.length == 1 && files[0].getName().equals(".git")) {
                SpecsLogs.msgInfo("Cleaning folder '" + repoFolder.getAbsolutePath() + "'");
                SpecsIo.deleteFolder(repoFolder);
            }
        }
    }

    /**
     * Pulls the latest changes from the remote repository.
     *
     * @param repo the Git repository
     * @param branch the branch to pull, or null for the default branch
     */
    private void pull(Git repo, String branch) {
        try {
            SpecsLogs.msgInfo("Pulling repo in folder '" + repo.getRepository().getWorkTree() + "'");

            PullCommand pullCmd = repo
                    .pull()
                    .setCredentialsProvider(SpecsGit.getCredentials(repoUrl));

            if (branch != null) {
                pullCmd.setRemoteBranchName(branch);
            }

            pullCmd.call();
        } catch (GitAPIException e) {
            throw new RuntimeException("Could not pull repository", e);
        }
    }

}
