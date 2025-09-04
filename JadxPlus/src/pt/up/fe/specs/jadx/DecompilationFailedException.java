/**
 * Copyright 2022 SPeCS.
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

package pt.up.fe.specs.jadx;

/**
 * Exception thrown when decompilation with Jadx fails.
 */
public class DecompilationFailedException extends Exception {

    private static final long serialVersionUID = 5280339014409302798L;

    /**
     * Constructs a new DecompilationFailedException with the specified message and cause.
     *
     * @param msg the detail message
     * @param err the cause of the exception
     */
    public DecompilationFailedException(String msg, Throwable err) {
        super(msg, err);
    }
}
