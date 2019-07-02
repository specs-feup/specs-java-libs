/**
 * Copyright 2018 SPeCS.
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

package org.suikasoft.jOptions.streamparser;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import org.suikasoft.jOptions.DataStore.DataClass;

import pt.up.fe.specs.util.utilities.LineStream;

class GenericLineStreamParser<T extends DataClass<T>> implements LineStreamParser<T> {

    private final T data;
    private final Map<String, LineStreamWorker<T>> workers;

    private LineStream currentLineStream;
    private Predicate<String> lineIgnore;
    private int numExceptions;

    public GenericLineStreamParser(T inputData, Map<String, LineStreamWorker<T>> workers) {
        // this.data = DataStore.newInstance("Generic LineStream Data").addAll(inputData);
        this.data = inputData;
        this.workers = workers;

        // Initialize data for each worker
        this.workers.values().forEach(worker -> worker.init(data));

        currentLineStream = null;
        lineIgnore = null;
        numExceptions = 0;
    }

    @Override
    public int getNumExceptions() {
        return numExceptions;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public Predicate<String> getLineIgnore() {
        if (lineIgnore == null) {
            return string -> false;
        }

        return lineIgnore;
    }

    @Override
    public boolean parse(String id, LineStream lineStream) {
        try {
            this.currentLineStream = lineStream;

            LineStreamWorker<T> worker = workers.get(id);
            if (worker == null) {
                // Check if id should be ignored
                // return getLineIgnore().test(id);

                return false;
            }

            worker.apply(lineStream, data);

            return true;
        } catch (Exception e) {
            numExceptions++;
            throw e;
        }

    }

    @Override
    public Collection<String> getIds() {
        return workers.keySet();
    }

    @Override
    public void close() throws Exception {
        for (LineStreamWorker<T> worker : workers.values()) {
            worker.close(data);
        }
    }

    @Override
    public long getReadLines() {
        return currentLineStream.getReadLines();
    }

    @Override
    public long getReadChars() {
        return currentLineStream.getReadChars();
    }

    @Override
    public void setLineIgnore(Predicate<String> ignorePredicate) {
        this.lineIgnore = ignorePredicate;
    }

}
