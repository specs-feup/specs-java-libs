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

/**
 * Shows information about progress of jobs.
 * 
 * @author Joao Bispo
 */
public class JobProgress {

    /**
     * INSTANCE VARIABLES
     */
    private final List<Job> jobs;
    private final int numJobs;

    private int counter;

    public JobProgress(List<Job> jobs) {
        this.jobs = jobs;
        this.numJobs = jobs.size();
        this.counter = 0;
    }

    public void initialMessage() {
        SpecsLogs.msgInfo("Found " + this.numJobs + " jobs.");
    }

    public void nextMessage() {
        if (this.counter >= this.numJobs) {
            SpecsLogs.warn("Already showed the total number of steps.");
        }

        this.counter++;

        String message = "Job " + this.counter + " of " + this.numJobs;

        String description = this.jobs.get(this.counter - 1).getDescription();
        if (description != null) {
            message = message + " (" + description + ").";
        }

        SpecsLogs.msgInfo(message);
    }
}
