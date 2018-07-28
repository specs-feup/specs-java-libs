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

import org.suikasoft.jOptions.DataStore.DataClass;

import pt.up.fe.specs.util.utilities.LineStream;

class GenericLineStreamWorker<T extends DataClass<T>> implements LineStreamWorker<T> {

    private final String id;
    private final Consumer<T> init;
    private final BiConsumer<LineStream, T> apply;

    public GenericLineStreamWorker(String id, Consumer<T> init, BiConsumer<LineStream, T> apply) {
        this.id = id;
        this.init = init;
        this.apply = apply;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void init(T data) {
        init.accept(data);
    }

    @Override
    public void apply(LineStream lineStream, T data) {
        apply.accept(lineStream, data);
    }

}
