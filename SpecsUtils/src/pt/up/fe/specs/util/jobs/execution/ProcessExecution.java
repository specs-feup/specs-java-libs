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

package pt.up.fe.specs.util.jobs.execution;

import java.io.File;
import java.util.List;

import pt.up.fe.specs.util.SpecsSystem;

/**
 * 
 * @author Joao Bispo
 */
public class ProcessExecution implements Execution {

    /**
     * INSTANCE VARIABLES
     */
    private final List<String> commandArgs;
    String workingFoldername;

    boolean interrupted;

    public ProcessExecution(List<String> commandArgs, String workingFoldername) {
        this.commandArgs = commandArgs;
        this.workingFoldername = workingFoldername;

        this.interrupted = false;
    }

    /**
     * Launches the compilation job in a separate process.
     * 
     * @return
     */
    @Override
    public int run() {
        return SpecsSystem.run(this.commandArgs, new File(this.workingFoldername));
    }

    @Override
    public boolean isInterrupted() {
        return this.interrupted;
    }

    public String getCommandString() {
        if (this.commandArgs.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(this.commandArgs.get(0));
        for (int i = 1; i < this.commandArgs.size(); i++) {
            builder.append(" ");
            builder.append(this.commandArgs.get(i));
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return getCommandString();
    }

    @Override
    public String getDescription() {
        return "Run '" + this.commandArgs.get(0) + "'";
    }

}
