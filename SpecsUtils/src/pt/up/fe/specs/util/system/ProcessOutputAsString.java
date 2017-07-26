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

    /**
     * @param returnValue
     * @param stdOut
     * @param stdErr
     */
    public ProcessOutputAsString(int returnValue, String stdOut, String stdErr) {
        super(returnValue, stdOut, stdErr);
    }

    /**
     * Returns the contents of the standard output, followed by the contents of the standard error.
     * 
     * @return
     */
    public String getOutput() {
        StringBuilder builder = new StringBuilder();

        String out = getStdOut();

        String err = getStdErr();
        if (err.isEmpty()) {
            return out;
        }

        // Add new line if standard out does not end with a newline, and if both standard output and standard error is
        // not empty.
        builder.append(out);
        if (!out.isEmpty() && !out.endsWith("\n")) {
            builder.append("\n");
        }

        builder.append(err);

        return builder.toString();
    }

}
