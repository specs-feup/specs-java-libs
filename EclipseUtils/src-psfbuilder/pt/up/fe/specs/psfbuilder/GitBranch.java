/**
 * Copyright 2015 SPeCS.
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

package pt.up.fe.specs.psfbuilder;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

public class GitBranch {

    // private static final String GIT_FOLDER = ".git";
    private static final String DEFAULT_REMOTE = "origin";
    private static final String DEFAULT_BRANCH = "master";

    private final File workingTree;
    private final String remote;
    private final String branch;

    public GitBranch(File workingTree, String remote, String branch) {
	this.workingTree = workingTree;
	this.remote = remote;
	this.branch = branch;
    }

    public String getBranch() {
	return branch;
    }

    public String getRemote() {
	return remote;
    }

    public File getWorkingTree() {
	return workingTree;
    }

    public static GitBranch newInstance(File location) {
	FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder()
		.findGitDir(location.getAbsoluteFile());

	if (repoBuilder.getGitDir() == null) {
	    throw new RuntimeException("Could not find a git repository for folder '"
		    + SpecsIo.getWorkingDir().getAbsolutePath() + "'");
	}

	// Open an existing repository
	try (Repository repo = repoBuilder.build()) {
	    StoredConfig config = repo.getConfig();
	    Set<String> remotes = config.getSubsections("remote");

	    if (remotes.isEmpty()) {
		throw new RuntimeException("Could not find a remote in '" + repo.getWorkTree() + "'");
	    }

	    // Get a remote. Try origin first, if not found, get the first on the list
	    String remoteName = getRemoteName(remotes);
	    String remote = config.getString("remote", remoteName, "url");

	    Set<String> branches = config.getSubsections("branch");
	    if (branches.isEmpty()) {
		throw new RuntimeException("Could not find a branch in '" + repo.getWorkTree() + "'");
	    }

	    String branch = getBranchName(branches);

	    return new GitBranch(repo.getWorkTree(), remote, branch);
	} catch (IOException e) {
	    throw new RuntimeException(e.getMessage(), e);
	}

    }

    private static String getBranchName(Set<String> branches) {
	if (branches.contains(DEFAULT_BRANCH)) {
	    return DEFAULT_BRANCH;
	}

	String firstBranch = branches.stream().findFirst().get();

	SpecsLogs.msgInfo("Could not find branch '" + DEFAULT_BRANCH + "', returning the first branch found, '"
		+ firstBranch + "'");

	return firstBranch;
    }

    private static String getRemoteName(Set<String> remotes) {
	if (remotes.contains(DEFAULT_REMOTE)) {
	    return DEFAULT_REMOTE;
	}

	String firstRemote = remotes.stream().findFirst().get();

	SpecsLogs.msgInfo("Could not find remote '" + DEFAULT_REMOTE + "', returning the first remote found, '"
		+ firstRemote + "'");

	return firstRemote;
    }

    @Override
    public String toString() {
	return getRemote() + ";" + getBranch();
    }
}
