/**
 * Copyright 2013 SPeCS Research Group.
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

package pt.up.fe.specs.util.jobs;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * @author Joao Bispo
 * 
 */
public class JobUtils {

    /**
     * The given path represents a folder that contains several folders, and each folder is a project.
     * 
     * @param sourceFolder
     * @param extensions
     * @param folderLevel
     * @return
     */
    public static List<FileSet> getSourcesFoldersMode(File sourceFolder,
	    Collection<String> extensions, int folderLevel) {

	// System.out.println("SOURCE FOLDER:"+sourceFolder);

	int currentLevel = folderLevel;
	List<File> currentFolderList = Arrays.asList(sourceFolder);
	while (currentLevel > 0) {
	    currentLevel--;

	    List<File> newFolderList = SpecsFactory.newArrayList();
	    for (File folder : currentFolderList) {
		newFolderList.addAll(SpecsIo.getFolders(folder));
	    }

	    currentFolderList = newFolderList;
	    // System.out.println("LEVEL "+(folderLevel-currentLevel)+":"+currentFolderList);
	}
	// System.out.println("FOLDERS:\n");
	// Create filesets
	List<FileSet> programSources = new ArrayList<>();
	for (File folder : currentFolderList) {
	    // System.out.println(folder);
	    FileSet source = singleFolderProgramSource(folder, extensions);
	    String outputName = createOutputName(folder, folderLevel);
	    // System.out.println("OUTPUT NAME:" + outputName);
	    source.setOutputName(outputName);

	    programSources.add(source);
	}
	/*
	// Get existing

	// Get folders
	File[] contents = sourceFolder.listFiles();
	for (File folder : contents) {
	    if (!folder.isDirectory()) {
		LoggingUtils.msgInfo("Ignoring file '" + folder.getPath()
			+ "' in source folders path.");
		continue;
	    }

	    FileSet source = singleFolderProgramSource(folder, extensions);
	    programSources.add(source);

	}
	 */

	return programSources;
    }

    private static String createOutputName(File folder, int folderLevel) {
	// StringBuilder builder = new StringBuilder();

	String currentName = folder.getName();

	File currentFolder = folder;
	for (int i = 1; i < folderLevel; i++) {
	    File parent = currentFolder.getParentFile();

	    currentName = parent.getName() + "_" + currentName;
	    currentFolder = parent;
	}

	return currentName;
    }

    /**
     * The given path represents a folder that contains several files, each file is a project.
     * 
     * @param jobOptions
     * @param targetOptions
     * @return
     */
    public static List<FileSet> getSourcesFilesMode(File sourceFolder, Collection<String> extensions) {

	// Get extensions
	String sourceFoldername = sourceFolder.getPath();
	// Get sources
	List<File> files = SpecsIo.getFilesRecursive(sourceFolder, new HashSet<>(extensions));

	// Each file is a program
	List<FileSet> programSources = new ArrayList<>();
	for (File file : files) {
	    FileSet newProgramSource = singleFileProgramSource(file, sourceFoldername);
	    programSources.add(newProgramSource);
	}

	return programSources;
    }

    /**
     * The source is a single .c file which is a program.
     * 
     * @param jobOptions
     * @param targetOptions
     * @return
     */
    public static List<FileSet> getSourcesSingleFileMode(File sourceFile,
	    Collection<String> extensions) {

	// The file is a program
	List<FileSet> programSources = SpecsFactory.newArrayList();
	String sourceFoldername = sourceFile.getParent();

	programSources.add(singleFileProgramSource(sourceFile, sourceFoldername));

	return programSources;
    }

    public static List<FileSet> getSourcesSingleFolderMode(File sourceFolder,
	    Collection<String> extensions) {

	List<FileSet> programSources = new ArrayList<>();
	// String parentFoldername = sourceFolder.getParent();

	programSources.add(singleFolderProgramSource(sourceFolder, extensions));

	return programSources;
    }

    /**
     * Runs a job, returns the return value of the job after completing.
     * 
     * @param job
     * @return
     */
    public static int runJob(Job job) {
	int returnValue = job.run();
	if (returnValue != 0) {
	    SpecsLogs.getLogger().warning(
		    "Problems while running job: returned value '" + returnValue + "'.\n" + "Job:"
			    + job.toString() + "\n");
	}

	return returnValue;
    }

    /**
     * Runs a batch of jobs. If any job terminated abruptly (a job has flag 'isInterruped' active), remaning jobs are
     * cancelled.
     * 
     * @param jobs
     * @return true if all jobs completed successfully, false otherwise
     */
    public static boolean runJobs(List<Job> jobs) {
	JobProgress jobProgress = new JobProgress(jobs);
	jobProgress.initialMessage();

	for (Job job : jobs) {
	    jobProgress.nextMessage();

	    runJob(job);

	    // Check if we cancel other jobs.
	    if (job.isInterrupted()) {
		SpecsLogs.getLogger().info("Cancelling remaining jobs.");
		return false;
	    }
	}

	return true;
    }

    /**
     * Creates a ProgramSource from a given folder.
     * 
     * <p>
     * Collects all files in the given folder with the given extension.
     * 
     * @param sourceFolder
     * @param extensions
     * @param sourceFoldername
     * @return
     */
    private static FileSet singleFolderProgramSource(File sourceFolder,
	    Collection<String> extensions) {

	// Get source files for program
	List<File> files = SpecsIo.getFilesRecursive(sourceFolder, extensions);

	List<String> sourceFilenames = new ArrayList<>();
	for (File file : files) {
	    sourceFilenames.add(file.getPath());
	}

	String baseFilename = sourceFolder.getName();

	return new FileSet(sourceFolder, sourceFilenames, baseFilename);
    }

    private static FileSet singleFileProgramSource(File sourceFile, String sourceFoldername) {
	File sourceFolder = sourceFile.getParentFile();

	List<String> sourceFilenames = SpecsFactory.newArrayList();
	sourceFilenames.add(sourceFile.getPath());

	String outputName = SpecsIo.removeExtension(sourceFile.getName());

	return new FileSet(sourceFolder, sourceFilenames, outputName);
    }
}
