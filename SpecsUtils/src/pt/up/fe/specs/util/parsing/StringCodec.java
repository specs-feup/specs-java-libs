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

package pt.up.fe.specs.util.parsing;

import java.util.function.Function;

/**
 * Encodes/decodes values to/from Strings.
 * 
 * <p>
 * It is recommended that the decoder supports null strings as inputs, to be
 * able to decode 'empty values'.
 * 
 * @author JoaoBispo
 *
 * @param <T>
 */
public interface StringCodec<T> {

    /**
     * Decodes a value from String to an instance of the value type.
     *
     */
    T decode(String value);

    /**
     * As default, uses the .toString() method of the value.
     *
     */
    default String encode(T value) {
        return value.toString();
    }

    static <T> StringCodec<T> newInstance(Function<T, String> encoder, Function<String, T> decoder) {
        return new GenericCodec<>(encoder, decoder);
    }
}
