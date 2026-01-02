/**
 * Copyright 2012 SPeCS Research Group.
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

package pt.up.fe.specs.util.system;

public class ProcessOutputAsString extends ProcessOutput<String, String> {

    public ProcessOutputAsString(int returnValue, String stdOut, String stdErr) {
        super(returnValue, stdOut, stdErr);
    }

    /**
     * Returns the contents of the standard output, followed by the contents of the
     * standard error.
     *
     */
    public String getOutput() {
        String out = getStdOut();
        String err = getStdErr();
        
        // Convert null values to "null" string for display
        String outStr = (out == null) ? "null" : out;
        String errStr = (err == null) ? "null" : err;
        
        StringBuilder builder = new StringBuilder();
        builder.append(outStr);
        
        // Add separator newline between stdout and stderr
        // Always add one newline if stdout doesn't end with newline
        if (!outStr.isEmpty() && !outStr.endsWith("\n")) {
            builder.append("\n");
        }
        
        builder.append(errStr);
        
        return builder.toString();
    }

}
