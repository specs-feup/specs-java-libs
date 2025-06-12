/**
 * Copyright 2016 SPeCS.
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

package pt.up.fe.specs.gearman.specsworker;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.gearman.GearmanFunction;
import org.gearman.GearmanFunctionCallback;

import com.google.gson.GsonBuilder;

/**
 * Generic Gearman worker that delegates work to a provided function and builds error output using a function.
 * <p>
 * This worker allows dynamic delegation of Gearman jobs to a user-supplied {@link GearmanFunction} and custom error output
 * formatting using a {@link Function}.
 */
public class GenericSpecsWorker extends SpecsWorker {

    private final GearmanFunction function;
    private final Function<String, Object> outputBuilder;

    private final String workerName;

    /**
     * Constructs a new GenericSpecsWorker.
     *
     * @param workerName the name of the worker
     * @param function the Gearman function to delegate work to
     * @param outputBuilder a function to build error output objects from error messages
     * @param timeout the timeout value
     * @param timeUnit the time unit for the timeout
     */
    public GenericSpecsWorker(String workerName, GearmanFunction function, Function<String, Object> outputBuilder,
            long timeout,
            TimeUnit timeUnit) {
        super(timeout, timeUnit);
        this.workerName = workerName;
        this.function = function;
        this.outputBuilder = outputBuilder;
    }

    /**
     * Returns the name of this worker.
     *
     * @return the worker name
     */
    @Override
    public String getWorkerName() {
        return workerName;
    }

    /**
     * Setup hook called before work execution. Default implementation does nothing.
     */
    @Override
    public void setUp() {
        // No setup required by default
    }

    /**
     * Teardown hook called after work execution. Default implementation does nothing.
     */
    @Override
    public void tearDown() {
        // No teardown required by default
    }

    /**
     * Delegates the work to the provided Gearman function.
     *
     * @param function the function name
     * @param data the input data
     * @param callback the Gearman callback
     * @return the result of the delegated function
     * @throws Exception if the delegated function throws an exception
     */
    @Override
    public byte[] workInternal(String function, byte[] data, GearmanFunctionCallback callback) throws Exception {
        return this.function.work(function, data, callback);
    }

    /**
     * Builds the error output using the provided outputBuilder function and serializes it as JSON.
     *
     * @param message the error message
     * @return the error output as JSON bytes
     */
    @Override
    protected byte[] getErrorOutput(String message) {
        return new GsonBuilder().create().toJson(outputBuilder.apply(message)).getBytes();
    }

}
