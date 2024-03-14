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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.gearman.specsworker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.gearman.GearmanFunctionCallback;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pt.up.fe.specs.util.SpecsLogs;

public abstract class JsonSpecsWorker<I, O> extends SpecsWorker {

    private final Gson gson;
    private final Class<I> inputClass;
    // private final Class<O> outputClass;

    // public TypedSpecsWorker(Class<I> inputClass, Class<O> outputClass, long timeout, TimeUnit timeUnit) {
    public JsonSpecsWorker(Class<I> inputClass, long timeout, TimeUnit timeUnit) {
        super(timeout, timeUnit);

        this.inputClass = inputClass;
        // this.outputClass = outputClass;
        this.gson = new GsonBuilder().create();
    }

    @Override
    public byte[] workInternal(String function, byte[] data, GearmanFunctionCallback callback) throws Exception {

        // Type typeOfT = new TypeToken<I>() {
        // }.getType();

        // I parsedData = gson.fromJson(new String(data), typeOfT);
        I parsedData = gson.fromJson(new String(data), inputClass);
        O result = workInternal(function, parsedData, callback);

        // Print time-stamp
        var time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        SpecsLogs.info("Finished job '" + this.getClass().getName() + "' at " + time);

        return gson.toJson(result).getBytes();
    }

    public abstract O workInternal(String function, I data, GearmanFunctionCallback callback);

    @Override
    protected byte[] getErrorOutput(String message) {
        return gson.toJson(getTypedErrorOutput(message)).getBytes();
    }

    protected abstract O getTypedErrorOutput(String message);

}
