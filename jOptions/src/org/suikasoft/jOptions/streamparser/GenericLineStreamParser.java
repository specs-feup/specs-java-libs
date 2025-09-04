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

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import org.suikasoft.jOptions.DataStore.DataClass;

import pt.up.fe.specs.util.utilities.LineStream;

/**
 * Default implementation of {@link LineStreamParser}.
 *
 * @param <T> the type of DataClass
 */
class GenericLineStreamParser<T extends DataClass<T>> implements LineStreamParser<T> {

    private final T data;
    private final Map<String, LineStreamWorker<T>> workers;
    private LineStream currentLineStream;
    private Predicate<String> lineIgnore;
    private int numExceptions;

    /**
     * Creates a new parser with the given input data and workers.
     *
     * @param inputData the initial data
     * @param workers the map of worker IDs to workers
     */
    public GenericLineStreamParser(T inputData, Map<String, LineStreamWorker<T>> workers) {
        this.data = inputData;
        this.workers = workers;
        this.workers.values().forEach(worker -> worker.init(data));
        currentLineStream = null;
        lineIgnore = null;
        numExceptions = 0;
    }

    /**
     * Returns the number of exceptions encountered during parsing.
     *
     * @return the number of exceptions
     */
    @Override
    public int getNumExceptions() {
        return numExceptions;
    }

    /**
     * Returns the data associated with this parser.
     *
     * @return the data
     */
    @Override
    public T getData() {
        return data;
    }

    /**
     * Returns the predicate used to ignore lines.
     *
     * @return the line ignore predicate
     */
    @Override
    public Predicate<String> getLineIgnore() {
        if (lineIgnore == null) {
            return string -> false;
        }
        return lineIgnore;
    }

    /**
     * Parses the given line stream using the worker associated with the given ID.
     *
     * @param id the worker ID
     * @param lineStream the line stream to parse
     * @return true if the parsing was successful, false otherwise
     */
    @Override
    public boolean parse(String id, LineStream lineStream) {
        try {
            this.currentLineStream = lineStream;
            LineStreamWorker<T> worker = workers.get(id);
            if (worker == null) {
                return false;
            }
            worker.apply(lineStream, data);
            return true;
        } catch (Exception e) {
            numExceptions++;
            throw e;
        }
    }

    /**
     * Returns the collection of worker IDs.
     *
     * @return the worker IDs
     */
    @Override
    public Collection<String> getIds() {
        return workers.keySet();
    }

    /**
     * Closes all workers associated with this parser.
     *
     */
    @Override
    public void close() {
        for (LineStreamWorker<T> worker : workers.values()) {
            worker.close(data);
        }
    }

    /**
     * Returns the number of lines read by the current line stream.
     *
     * @return the number of read lines
     */
    @Override
    public long getReadLines() {
        return currentLineStream.getReadLines();
    }

    /**
     * Returns the number of characters read by the current line stream.
     *
     * @return the number of read characters
     */
    @Override
    public long getReadChars() {
        return currentLineStream.getReadChars();
    }

    /**
     * Sets the predicate used to ignore lines.
     *
     * @param ignorePredicate the line ignore predicate
     */
    @Override
    public void setLineIgnore(Predicate<String> ignorePredicate) {
        this.lineIgnore = ignorePredicate;
    }

}
