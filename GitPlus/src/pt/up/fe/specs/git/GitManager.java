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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

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

        // Parse options
        // Map<GitUrlOption, String> urlOptions = parseUrl(repositoryPath);

        // Create destination folder based on options (branch and commit changes folder)
        // var repoFolder = getRepoFolder(repositoryPath, urlOptions);

        // Clone or pull with options
        // var gitRepo = getRepo(repositoryPath, urlOptions, repoFolder);
        // prepareRepo(repo);

        // Store in cache
        // repoCache.put(repositoryPath, gitRepo);
        repoCache.put(repositoryPath, repo);

        return repo;
    }

    // private void prepareRepo(GitRepo repo) {
    // // Make sure repository is in an ok state in the repoFolder
    //
    // prepareRepo(repositoryPath, urlOptions, repoFolder);
    //
    // // Check folder option
    // var foldername = urlOptions.get(GitUrlOption.FOLDER);
    //
    // var workFolder = foldername != null ? new File(repoFolder, foldername) : repoFolder;
    //
    // if (!workFolder.isDirectory()) {
    // throw new RuntimeException(
    // "Could not find folder '" + foldername + "' in folder '" + repoFolder.getAbsolutePath() + "'");
    // }
    //
    // return new GitRepoOld(workFolder, repoFolder);
    // }

    // private GitRepoOld getRepo(String repositoryPath, Map<GitUrlOption, String> urlOptions, File repoFolder) {
    // // Make sure repository is in an ok state in the repoFolder
    // // TODO: return boolean if just cloned, to know if should pull or not
    // // Separate branch from commit again
    // prepareRepo(repositoryPath, urlOptions, repoFolder);
    //
    // // Check folder option
    // var foldername = urlOptions.get(GitUrlOption.FOLDER);
    //
    // var workFolder = foldername != null ? new File(repoFolder, foldername) : repoFolder;
    //
    // if (!workFolder.isDirectory()) {
    // throw new RuntimeException(
    // "Could not find folder '" + foldername + "' in folder '" + repoFolder.getAbsolutePath() + "'");
    // }
    //
    // return new GitRepoOld(workFolder, repoFolder);
    // }

    /**
     * Prepares the repository in the given folder.
     * 
     * @param repositoryPath
     * @param urlOptions
     * @param repoFolder
     */
    private void prepareRepo(String repositoryPath, Map<GitUrlOption, String> urlOptions, File repoFolder) {

        SpecsLogs.msgInfo("Processing git url '" + repositoryPath + "'");
        var cleanRepoUrl = SpecsIo.cleanUrl(repositoryPath);

        // If folder only contains a single .git folder, something might have gone wrong, delete folder
        if (repoFolder.isDirectory()) {
            var files = repoFolder.listFiles();
            if (files.length == 1 && files[0].getName().equals(".git")) {
                SpecsLogs.msgInfo("Cleaning folder '" + repoFolder.getAbsolutePath() + "'");
                SpecsIo.deleteFolder(repoFolder);
            }
        }

        Git repo = null;

        var clonedRepo = false;
        // If folder does not exist, or if it exists and is empty, clone repository
        if (!repoFolder.exists() || SpecsIo.isEmptyFolder(repoFolder)) {
            try {
                SpecsLogs.msgInfo("Cloning repo '" + cleanRepoUrl + "' to folder '" + repoFolder + "'");

                repo = Git.cloneRepository()
                        .setURI(cleanRepoUrl)
                        .setDirectory(repoFolder)
                        .setCredentialsProvider(SpecsGit.getCredentials(repositoryPath))
                        .call();

                // Cloning repo from scratch, no pull needed
                clonedRepo = true;

                // Check if there is a login/pass in the url
                // CredentialsProvider cp = getCredentials(repositoryPath);
                // System.out.println("SETTING NULL");
                // clone.setCredentialsProvider(null);
                // clone.call();
            } catch (GitAPIException e) {
                throw new RuntimeException("Could not clone repository '" + cleanRepoUrl + "'", e);
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

        var commit = urlOptions.get(GitUrlOption.COMMIT);

        // If no commit specified, just pull if not cloned and return
        if (commit == null) {
            // Pull repo

            pull(cleanRepoUrl, repo);

            return;
        }

        boolean isBranchName = SpecsGit.isBranchName(repo, commit);

        System.out.println("IS BRANCH:" + isBranchName);
        /*
        // If branch, checkout branch
        
        var branch = urlOptions.get(GitUrlOption.BRANCH);
        
        String currentBranch = null;
        try {
            currentBranch = repo.getRepository().getBranch();
        } catch (IOException e) {
            throw new RuntimeException("Could not get current branch", e);
        }
        
        if (branch != null) {
            try {
        
                // First, fetch branches
                // SpecsLogs.info("Fetching branches");
                // var fetchResult = repo.fetch()
                // .setRefSpecs(new RefSpec("refs/heads/" + branch))
                // .call();
        
                // System.out.println("FETCH RESULT: " + fetchResult.getTrackingRefUpdates());
        
                // Only checkout if not in the correct branch
                if (!branch.equals(currentBranch)) {
                    SpecsLogs.msgInfo("Checking out branch '" + branch + "'");
                    repo.checkout()
                            .setCreateBranch(true)
                            .setName(branch)
                            // .setStartPoint(commit)
                            .call();
                } else {
                    SpecsLogs.msgInfo("Already in branch '" + branch + "'");
                }
            } catch (GitAPIException e) {
                throw new RuntimeException(
                        "Could not checkout branch '" + branch + "' in folder '" + repoFolder.getAbsolutePath() + "'",
                        e);
            }
        }
        
        var commit = urlOptions.get(GitUrlOption.COMMIT);
        if (commit != null) {
        
            try {
                repo.checkout()
                        .setCreateBranch(true)
                        .setName(commit)
                        // .setStartPoint(commit)
                        .call();
            } catch (GitAPIException e) {
                throw new RuntimeException(
                        "Could not checkout commit '" + commit + "' in folder '" + repoFolder.getAbsolutePath() + "'",
                        e);
            }
            // (new RevWalk(repo, 0).parseCommit(null)
        }
        
        return needsPull;
        */

    }

    /**
     * TODO: cleanRepoUrl here is needed because getRepoFolder receives the Query params, to avoid parsing them again.
     * If there is a class just for a single repo url, this could be managed differently.
     * 
     * @param cleanRepoUrl
     * @param repo
     */
    private void pull(String cleanRepoUrl, Git repo) {
        try {
            SpecsLogs.msgInfo("Pulling repo in folder '" + repo.getRepository().getWorkTree() + "'");

            PullCommand pullCmd = repo
                    .pull()
                    .setCredentialsProvider(SpecsGit.getCredentials(cleanRepoUrl));
            pullCmd.call();
        } catch (GitAPIException e) {
            throw new RuntimeException("Could not pull repository", e);
        }
    }

    // private File getRepoFolder(String repositoryPath, Map<GitUrlOption, String> urlOptions) {
    //
    // // Get repo name
    // String repoName = SpecsGit.getRepoName(repositoryPath);
    //
    // // If there is a branch, append to name
    // // var branch = urlOptions.get(GitUrlOption.BRANCH);
    // // if (branch != null) {
    // // repoName += "_branch_" + branch;
    // // }
    //
    // // If there is a commit, append to name
    // var commit = urlOptions.get(GitUrlOption.COMMIT);
    // if (commit != null) {
    // repoName += "_commit_" + commit;
    // }
    //
    // // Get repo folder
    // File eclipseBuildFolder = SpecsGit.getRepositoriesFolder();
    //
    // return new File(eclipseBuildFolder, repoName);
    // }
    //
    // private Map<GitUrlOption, String> parseUrl(String repositoryPath) {
    // var urlStringOptions = SpecsIo.parseUrl(repositoryPath)
    // .map(url -> SpecsIo.parseUrlQuery(url))
    // .orElse(new HashMap<>());
    //
    // // Map each option string to an enum
    // return SpecsEnums.toEnumMap(GitUrlOption.class, urlStringOptions);
    // }
}
