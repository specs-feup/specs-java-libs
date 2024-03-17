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
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
 *
 * @author JoaoBispo
 *
 */
public class SpecsGit {

    private static final String SPECS_GIT_REPOS_FOLDER = "specs_git_repos";

    private static final String URL_PARAM_BRANCH = "branch";

    public static File parseRepositoryUrl(String repositoryPath) {
        var repoFolder = parseRepositoryUrl(repositoryPath, true);

        // Check if the git repo is working
        Git git = null;
        try {
            git = Git.open(repoFolder);
        } catch (IOException e) {
            throw new RuntimeException("Could not open git repository in folder '" + repoFolder + "'", e);
        }

        // Get url options
        var urlOptions = SpecsIo.parseUrl(repositoryPath)
                .map(url -> SpecsIo.parseUrlQuery(url))
                .orElse(new HashMap<>());

        // Check if correct branch
        var branch = urlOptions.get(URL_PARAM_BRANCH);
        if (branch != null) {
            checkout(git, branch);
        }

        return repoFolder;
    }

    public static void checkout(Git git, String branchName) {
        try {
            // System.out.println("SETTING TO BRANCH " + branch);

            var currentBranch = git.getRepository().getBranch();

            // System.out.println("CURRENT BRANCH: " + currentBranch);

            // Checkout branch
            if (!branchName.equals(currentBranch)) {

                // Taken from here: https://stackoverflow.com/a/57365145

                var createBranch = !git.branchList()
                        .call()
                        .stream()
                        .map(Ref::getName)
                        .collect(Collectors.toList())
                        .contains("refs/heads/" + branchName);

                git.checkout().setCreateBranch(createBranch)
                        .setName(branchName)
                        .call();

                // var currentFullBranch = git.getRepository().getFullBranch();
                // var startPoint = currentFullBranch.substring(0, currentFullBranch.length() -
                // currentBranch.length())
                // + branch;
                //
                // git.checkout().setCreateBranch(true)
                // .setName(branch)
                // .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                // // .setStartPoint(startPoint)
                // .call();

            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not checkout repository '" + git + "' to branch '" + branchName + "'", e);
        }
    }

    // public static String getBranchName(Git git) {
    // try {
    // var fullBranch = git.getRepository().getFullBranch();
    // var lastSlash = fullBranch.lastIndexOf('/');
    // return lastSlash != -1 ? fullBranch.substring(lastSlash + 1, fullBranch.length()) : fullBranch;
    // } catch (IOException e) {
    // throw new RuntimeException("Could not get the name of the branch of the git repository " + git, e);
    // }
    //
    // }

    private static File parseRepositoryUrl(String repositoryPath, boolean firstTime) {
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
                        .setCredentialsProvider(getCredentials(repositoryPath))
                        .call();
                // Check if there is a login/pass in the url
                // CredentialsProvider cp = getCredentials(repositoryPath);
                // System.out.println("SETTING NULL");
                // clone.setCredentialsProvider(null);
                // clone.call();

                return repoFolder;
            } catch (GitAPIException e) {
                throw new RuntimeException("Could not clone repository '" + repositoryPath + "'", e);
            }
        }

        // Repository already exists, pull

        try {
            SpecsLogs.msgInfo("Pulling repo '" + repositoryPath + "' in folder '" + repoFolder + "'");
            Git gitRepo = Git.open(repoFolder);
            PullCommand pullCmd = gitRepo
                    .pull()
                    .setCredentialsProvider(getCredentials(repositoryPath));
            pullCmd.call();
        } catch (GitAPIException | IOException e) {
            // Sometimes this is a problem that can be solved by deleting the folder and cloning again, try that
            if (firstTime) {
                SpecsLogs.info("Could not pull to folder '" + repoFolder + "', deleting folder and trying again");
                var success = SpecsIo.deleteFolder(repoFolder);
                if (!success) {
                    throw new RuntimeException("Could not delete existing repo folder " + repoFolder.getAbsolutePath());
                }
                return parseRepositoryUrl(repositoryPath, false);
            }

            throw new RuntimeException("Could not pull repository '" + repositoryPath + "'", e);
        }

        return repoFolder;
    }

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

        // System.out.println("CURRENT INDEX: " + currentString);

        // Check if there is a login/pass in the url
        // Look for last index, in case the login has an '@'
        var atIndex = currentString.lastIndexOf('@');

        // No login
        if (atIndex == -1) {
            return null;
        }

        var loginPass = currentString.substring(0, atIndex);
        // System.out.println("LOGIN PASS: " + loginPass);

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
        // System.out.println("LOGIN: " + login);
        // System.out.println("PASS: " + pass);
        return new UsernamePasswordCredentialsProvider(login, pass);
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

    public static Git clone(String repositoryPath, File outputFolder, CredentialsProvider cp) {
        return clone(repositoryPath, outputFolder, cp, null);
    }

    public static Git clone(String repositoryPath, File outputFolder, CredentialsProvider cp, String branch) {
        // TODO: tag should be branch
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

    public static String normalizeTag(String tag) {
        var prefix = "refs/tags/";
        return tag.startsWith(prefix) ? tag : prefix + tag;
    }

    public static boolean hasTag(String repositoryPath, String tag) {
        return hasTag(repositoryPath, tag, null);
    }

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

            // System.out.println("TAGS: " + remoteRefs.keySet());
            //
            // var prefix = "refs/tags/";
            // var completeTag = tag.startsWith(prefix) ? tag : prefix + tag;

            return remoteRefs.keySet().contains(normalizeTag(tag));
        } catch (Exception e) {
            throw new RuntimeException("Could not check tags of repository '" + repositoryPath + "'", e);
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
            SpecsLogs.warn("Error message:\n", e);
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

    public static boolean isBranchName(Git repo, String commit) {
        // Check if commit value is the name of a branch
        // Taken from here: https://stackoverflow.com/a/57365145

        try {

            // for (var branchRef : repo.branchList().setListMode(ListMode.REMOTE).call()) {
            // System.out.println("BRANCH: " + branchRef.getName());
            // }

            // Pattern to search for
            var commitPattern = "refs/remotes/origin/" + commit;

            return repo.branchList()
                    // So that it returns all remotes
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
