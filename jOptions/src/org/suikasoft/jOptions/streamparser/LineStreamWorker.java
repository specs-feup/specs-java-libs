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
 * Worker for parsing a section of a
 * {@link pt.up.fe.specs.util.utilities.LineStream} into a {@link DataClass}.
 *
 * @param <T> the type of DataClass
 */
public interface LineStreamWorker<T extends DataClass<T>> {

    /**
     * Creates a new worker with the given id, initializer, and apply function.
     *
     * @param id    the worker id
     * @param init  the initializer
     * @param apply the apply function
     * @return a new LineStreamWorker
     */
    static <T extends DataClass<T>> LineStreamWorker<T> newInstance(String id, Consumer<T> init,
            BiConsumer<LineStream, T> apply) {
        return new GenericLineStreamWorker<>(id, init, apply);
    }

    /**
     * Creates a new worker with the given id and apply function.
     *
     * @param id    the worker id
     * @param apply the apply function
     * @return a new LineStreamWorker
     */
    static <T extends DataClass<T>> LineStreamWorker<T> newInstance(String id, BiConsumer<LineStream, T> apply) {
        Consumer<T> init = data -> {
        };
        return new GenericLineStreamWorker<>(id, init, apply);
    }

    /**
     * Returns the id of this worker.
     *
     * @return the worker id
     */
    String getId();

    /**
     * Initializes any data the worker might need (e.g., initial values in
     * DataStore).
     *
     * @param data the data to initialize
     */
    void init(T data);

    /**
     * Parses the line stream and updates the data.
     *
     * @param lineStream the line stream
     * @param data       the data to update
     */
    void apply(LineStream lineStream, T data);

    /**
     * Finalizes a worker, after all workers have been executed. By default, does
     * nothing.
     *
     * @param data the data to finalize
     */
    default void close(T data) {
    }
}
