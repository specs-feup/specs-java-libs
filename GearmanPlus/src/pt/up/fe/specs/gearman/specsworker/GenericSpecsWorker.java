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

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.gearman.GearmanFunction;
import org.gearman.GearmanFunctionCallback;

import com.google.gson.GsonBuilder;

public class GenericSpecsWorker extends SpecsWorker {

    private final GearmanFunction function;
    private final Function<String, Object> outputBuilder;

    private final String workerName;

    public GenericSpecsWorker(String workerName, GearmanFunction function, Function<String, Object> outputBuilder,
            long timeout,
            TimeUnit timeUnit) {
        super(timeout, timeUnit);

        this.workerName = workerName;
        this.function = function;
        this.outputBuilder = outputBuilder;
    }

    @Override
    public String getWorkerName() {
        return workerName;
    }

    @Override
    public void setUp() {

    }

    @Override
    public void tearDown() {

    }

    @Override
    public byte[] workInternal(String function, byte[] data, GearmanFunctionCallback callback) throws Exception {
        return this.function.work(function, data, callback);
    }

    @Override
    protected byte[] getErrorOutput(String message) {
        return new GsonBuilder().create().toJson(outputBuilder.apply(message)).getBytes();
    }

}
