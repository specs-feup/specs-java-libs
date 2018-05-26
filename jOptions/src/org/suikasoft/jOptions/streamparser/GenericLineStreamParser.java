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

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.util.utilities.LineStream;

class GenericLineStreamParser implements LineStreamParser {

    private final DataStore data;
    private final Map<String, LineStreamWorker> workers;

    public GenericLineStreamParser(Map<String, LineStreamWorker> workers) {
        this.data = DataStore.newInstance("Generic LineStream Data");
        this.workers = workers;

        // Initialize data for each worker
        this.workers.values().forEach(worker -> worker.init(data));
    }

    @Override
    public DataStore getData() {
        return data;
    }

    @Override
    public boolean parse(String id, LineStream lineStream) {
        LineStreamWorker worker = workers.get(id);
        if (worker == null) {
            return false;
        }

        worker.apply(lineStream, data);

        return true;
    }

    @Override
    public Collection<String> getIds() {
        return workers.keySet();
    }

}
