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

package pt.up.fe.specs.util.system;

import java.util.Optional;

/**
 * Represents the output of a process.
 * 
 * @author JoaoBispo
 *
 * @param <O>
 * @param <E>
 */
public class ProcessOutput<O, E> {

    private final int returnValue;
    private final O stdOut;
    private final E stdErr;
    private final Exception outputException;

    public ProcessOutput(int returnValue, O stdOut, E stdErr) {
        this(returnValue, stdOut, stdErr, null);
    }

    public ProcessOutput(int returnValue, O stdOut, E stdErr, Exception outputException) {
        this.returnValue = returnValue;
        this.stdOut = stdOut;
        this.stdErr = stdErr;
        this.outputException = outputException;
    }

    public Optional<Exception> getOutputException() {
        return Optional.ofNullable(outputException);
    }

    /**
     * Checks if the execution returned an error value.
     * 
     * @return true if there was an error, false otherwise
     */
    public boolean isError() {
        // If the return value was anything other than 0, we can assume there was an execution error
        return this.returnValue != 0;
    }

    /**
     * @return the returnValue
     */
    public int getReturnValue() {
        return this.returnValue;
    }

    /**
     * @return the stdOut
     */
    public O getStdOut() {
        return this.stdOut;
    }

    /**
     * @return the stdErr
     */
    public E getStdErr() {
        return this.stdErr;
    }

    @Override
    public String toString() {
        var output = new StringBuilder();

        output.append("Return value: " + returnValue + "\n");

        output.append("StdOut: " + stdOut + "\n");
        output.append("StdErr: " + stdErr + "\n");

        if (outputException != null) {
            output.append("Exception: " + outputException);
        }

        return output.toString();
    }

}
