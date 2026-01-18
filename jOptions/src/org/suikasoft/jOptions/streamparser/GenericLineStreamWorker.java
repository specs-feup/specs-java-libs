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
 * specific language governing permissions and limitations under the License.
 */

package org.suikasoft.jOptions.streamparser;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.suikasoft.jOptions.DataStore.DataClass;

import pt.up.fe.specs.util.utilities.LineStream;

/**
 * Default implementation of {@link LineStreamWorker}.
 *
 * @param <T> the type of DataClass
 */
class GenericLineStreamWorker<T extends DataClass<T>> implements LineStreamWorker<T> {

    private final String id;
    private final Consumer<T> init;
    private final BiConsumer<LineStream, T> apply;

    /**
     * Creates a new worker with the given id, initializer, and apply function.
     *
     * @param id    the worker id
     * @param init  the initializer
     * @param apply the apply function
     */
    public GenericLineStreamWorker(String id, Consumer<T> init, BiConsumer<LineStream, T> apply) {
        this.id = id;
        this.init = init;
        this.apply = apply;
    }

    /**
     * Gets the id of the worker.
     *
     * @return the worker id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Initializes the worker with the given data.
     *
     * @param data the data to initialize
     */
    @Override
    public void init(T data) {
        init.accept(data);
    }

    /**
     * Applies the worker logic to the given line stream and data.
     *
     * @param lineStream the line stream
     * @param data       the data
     */
    @Override
    public void apply(LineStream lineStream, T data) {
        apply.accept(lineStream, data);
    }

}
