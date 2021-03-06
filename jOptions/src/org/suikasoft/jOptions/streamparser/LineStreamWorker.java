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

public interface LineStreamWorker<T extends DataClass<T>> {

    static <T extends DataClass<T>> LineStreamWorker<T> newInstance(String id, Consumer<T> init,
            BiConsumer<LineStream, T> apply) {
        return new GenericLineStreamWorker<>(id, init, apply);
    }

    static <T extends DataClass<T>> LineStreamWorker<T> newInstance(String id, BiConsumer<LineStream, T> apply) {
        // Do nothing
        Consumer<T> init = data -> {
        };

        return new GenericLineStreamWorker<>(id, init, apply);
    }

    /**
     * Id of this worker, precedes lines to parse in LineStream.
     * 
     * @return
     */
    String getId();

    /**
     * Initializes any data worker might need (e.g. initial values in DataStore)
     */
    void init(T data);

    /**
     * Parses linestream
     * 
     * @param lineStream
     * @param data
     */
    void apply(LineStream lineStream, T data);

    /**
     * Finalizes a worker, after all workers have been executed. By default, does nothing.
     */
    default void close(T data) {

    }
}
