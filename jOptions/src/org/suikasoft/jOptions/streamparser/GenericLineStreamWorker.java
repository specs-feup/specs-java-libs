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

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.util.utilities.LineStream;

class GenericLineStreamWorker implements LineStreamWorker {

    private final String id;
    private final Consumer<DataStore> init;
    private final BiConsumer<LineStream, DataStore> apply;

    public GenericLineStreamWorker(String id, Consumer<DataStore> init, BiConsumer<LineStream, DataStore> apply) {
        this.id = id;
        this.init = init;
        this.apply = apply;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void init(DataStore data) {
        init.accept(data);
    }

    @Override
    public void apply(LineStream lineStream, DataStore data) {
        apply.accept(lineStream, data);
    }

}
