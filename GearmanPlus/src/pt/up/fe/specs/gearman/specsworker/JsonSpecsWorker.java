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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.gearman.GearmanFunctionCallback;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * Abstract Gearman worker that handles JSON input/output using Gson.
 *
 * @param <I> the input type
 * @param <O> the output type
 */
public abstract class JsonSpecsWorker<I, O> extends SpecsWorker {

    private final Gson gson;
    private final Class<I> inputClass;

    /**
     * Constructs a JsonSpecsWorker with the given input class and timeout settings.
     *
     * @param inputClass the class of the input type
     * @param timeout the timeout duration
     * @param timeUnit the time unit for the timeout
     */
    public JsonSpecsWorker(Class<I> inputClass, long timeout, TimeUnit timeUnit) {
        super(timeout, timeUnit);
        this.inputClass = inputClass;
        this.gson = new GsonBuilder().create();
    }

    /**
     * Handles the work by deserializing input, invoking the worker, and serializing the result as JSON.
     *
     * @param function the function name
     * @param data the input data as bytes
     * @param callback the Gearman callback
     * @return the result as JSON bytes
     * @throws Exception if processing fails
     */
    @Override
    public byte[] workInternal(String function, byte[] data, GearmanFunctionCallback callback) throws Exception {
        I parsedData = gson.fromJson(new String(data), inputClass);
        O result = workInternal(function, parsedData, callback);
        // Print time-stamp
        var time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        SpecsLogs.info("Finished job '" + this.getClass().getName() + "' at " + time);
        return gson.toJson(result).getBytes();
    }

    /**
     * Performs the actual work using the deserialized input.
     *
     * @param function the function name
     * @param data the input data
     * @param callback the Gearman callback
     * @return the result
     */
    public abstract O workInternal(String function, I data, GearmanFunctionCallback callback);

    /**
     * Returns the error output as JSON bytes.
     *
     * @param message the error message
     * @return the error output as JSON bytes
     */
    @Override
    protected byte[] getErrorOutput(String message) {
        return gson.toJson(getTypedErrorOutput(message)).getBytes();
    }

    /**
     * Returns a typed error output object for the given message.
     *
     * @param message the error message
     * @return the error output object
     */
    protected abstract O getTypedErrorOutput(String message);

}
