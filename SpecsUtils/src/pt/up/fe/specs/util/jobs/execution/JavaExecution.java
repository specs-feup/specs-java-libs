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

package pt.up.fe.specs.util.jobs.execution;

import pt.up.fe.specs.util.SpecsLogs;

public class JavaExecution implements Execution {

    private static final String DEFAULT_MESSAGE = "Java Execution";

    private final Runnable runnable;
    private boolean interrupted;
    private String description;

    public JavaExecution(Runnable runnable) {
        this.runnable = runnable;
        this.interrupted = false;

        this.description = null;
    }

    @Override
    public int run() {
        try {
            this.runnable.run();
        } catch (Exception e) {
            SpecsLogs.warn(e.getMessage(), e);
            this.interrupted = true;
            return -1;
        }

        return 0;
    }

    @Override
    public boolean isInterrupted() {
        return this.interrupted;
    }

    @Override
    public String getDescription() {
        if (this.description == null) {
            return JavaExecution.DEFAULT_MESSAGE;
        }

        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
