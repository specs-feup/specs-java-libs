/*
 * Copyright 2010 SPeCS Research Group.
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

import java.util.List;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.jobs.execution.Execution;
import pt.up.fe.specs.util.jobs.execution.JavaExecution;
import pt.up.fe.specs.util.jobs.execution.ProcessExecution;

/**
 * 
 * @author Joao Bispo
 */
public class Job {

    /**
     * INSTANCE VARIABLES
     */
    // private final List<ProcessExecution> executions;
    // private final List<Execution> executions;
    private final Execution execution;
    // private final List<String> commandArgs;
    // String workingFoldername;

    boolean interrupted;

    // private Job(List<Execution> executions) {
    private Job(Execution execution) {
	this.execution = execution;
	// this.executions = executions;
	// this.commandArgs = commandArgs;
	// this.workingFoldername = workingFoldername;

	this.interrupted = false;
    }

    /**
     * Launches the compilation job in a separate process.
     * 
     * @return
     */
    public int run() {

	// for (ProcessExecution execution : executions) {
	// for (Execution execution : executions) {
	int result = this.execution.run();

	if (result != 0) {
	    SpecsLogs.msgInfo("Execution returned with error value '" + result + "'");
	    return -1;
	}

	if (this.execution.isInterrupted()) {
	    this.interrupted = true;
	    return 0;
	}
	// }

	return 0;
    }

    public boolean isInterrupted() {
	return this.interrupted;
    }

    /**
     * @param commandArgs
     * @param workingDir
     * @return
     */
    public static Job singleProgram(List<String> commandArgs, String workingDir) {
	ProcessExecution exec = new ProcessExecution(commandArgs, workingDir);
	// List<ProcessExecution> executions = Arrays.asList(exec);
	// List<Execution> executions = FactoryUtils.newArrayList();
	// executions.add(exec);

	// return new Job(executions);
	return new Job(exec);
    }

    public static Job singleJavaCall(Runnable runnable) {
	return singleJavaCall(runnable, null);
    }

    /**
     * @param commandArgs
     * @param workingDir
     * @return
     */
    public static Job singleJavaCall(Runnable runnable, String description) {
	JavaExecution exec = new JavaExecution(runnable);

	exec.setDescription(description);

	// List<Execution> executions = FactoryUtils.newArrayList();
	// executions.add(exec);

	// return new Job(executions);
	return new Job(exec);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return this.execution.toString();
	/*
	if (executions.size() == 1) {
	    return executions.get(0).toString();
	}

	StringBuilder builder = new StringBuilder();
	for (int i = 0; i < executions.size(); i++) {
	    builder.append("Execution " + (i + 1) + ": ");
	    builder.append(executions.get(i));
	    builder.append("\n");
	}

	// return "Executions:" + executions;
	return builder.toString();
	 */
    }

    public String getCommandString() {
	/*
	if (executions.isEmpty()) {
	    return "";
	}

	if (executions.size() > 1) {
	    LoggingUtils
		    .msgInfo("Job has more than one execution, returning the command of just the first execution.\n"
			    + toString());
	}

	// ProcessExecution execution = executions.get(0);
	Execution execution = executions.get(0);
	*/
	if (!(this.execution instanceof ProcessExecution)) {
	    SpecsLogs
	    .msgInfo("First job is not of class 'ProcessExecution', returning empty string");
	    return "";
	}

	ProcessExecution pExecution = (ProcessExecution) this.execution;
	// return execution.getCommandString();
	return pExecution.getCommandString();
    }

    public String getDescription() {
	/*
	if (executions.size() > 1) {
	    LoggingUtils
		    .msgInfo("Job has more than one execution, returning the command of just the first execution.\n"
			    + toString());
	}


	return executions.get(0).getDescription();
	 */
	return this.execution.getDescription();
    }

}
