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
 * ParserWorker function that accepts three arguments.
 * 
 * @author JoaoBispo
 *
 * @param <T>
 * @param <U>
 * @param <V>
 * @param <W>
 */
public interface ParserWorkerWithParam4<T, U, V, W, Y> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t
     *            the first function argument
     * @param u
     *            the second function argument
     * @param w
     *            the third function argument
     * @param y
     *            the fourth function argument
     * @return the function result
     */
    ParserResult<T> apply(StringSlice s, U u, V v, W w, Y y);
}
