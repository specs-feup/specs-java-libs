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
    private final Execution execution;
    boolean interrupted;

    private Job(Execution execution) {
        this.execution = execution;
        this.interrupted = false;
    }

    /**
     * Launches the compilation job in a separate process.
     *
     */
    public int run() {

        int result = this.execution.run();

        // Check for interruption regardless of return code
        if (this.execution.isInterrupted()) {
            this.interrupted = true;
            return 0;
        }

        if (result != 0) {
            SpecsLogs.msgInfo("Execution returned with error value '" + result + "'");
            return -1;
        }

        return 0;
    }

    public boolean isInterrupted() {
        return this.interrupted;
    }

    public static Job singleProgram(List<String> commandArgs, String workingDir) {
        ProcessExecution exec = new ProcessExecution(commandArgs, workingDir);
        return new Job(exec);
    }

    public static Job singleJavaCall(Runnable runnable) {
        return singleJavaCall(runnable, null);
    }

    public static Job singleJavaCall(Runnable runnable, String description) {
        JavaExecution exec = new JavaExecution(runnable);

        exec.setDescription(description);

        return new Job(exec);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.execution.toString();
    }

    public String getCommandString() {
        if (!(this.execution instanceof ProcessExecution pExecution)) {
            SpecsLogs
                    .msgInfo("First job is not of class 'ProcessExecution', returning empty string");
            return "";
        }

        return pExecution.getCommandString();
    }

    public String getDescription() {
        return this.execution.getDescription();
    }

}
