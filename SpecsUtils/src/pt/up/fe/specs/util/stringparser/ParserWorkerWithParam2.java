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

package pt.up.fe.specs.util.stringparser;

import pt.up.fe.specs.util.utilities.StringSlice;

/**
 * ParserWorker function that accepts two arguments.
 * 
 * @author JoaoBispo
 *
 * @param <T>
 * @param <U>
 * @param <V>
 */
public interface ParserWorkerWithParam2<T, U, V> {

    /**
     * Applies this function to the given arguments.
     *
     * @param s the first function argument
     * @param u the second function argument
     * @return the function result
     */
    ParserResult<T> apply(StringSlice s, U u, V v);
}
