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

package pt.up.fe.specs.util.parsing;

import java.io.Serializable;
import java.util.function.Function;

class GenericCodec<T> implements StringCodec<T>, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final Function<T, String> encoder;
    private final Function<String, T> decoder;

    public GenericCodec(Function<T, String> encoder, Function<String, T> decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    @Override
    public String encode(T value) {
        return this.encoder.apply(value);
    }

    @Override
    public T decode(String value) {
        return this.decoder.apply(value);
    }

}
