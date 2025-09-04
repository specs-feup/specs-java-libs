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

package org.suikasoft.jOptions.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Codec for handling multiple choice lists in jOptions.
 *
 * @author JoaoBispo
 *
 * @param <T> the type of elements in the list
 */
public class MultipleChoiceListCodec<T> implements StringCodec<List<T>> {

    private static final String SEPARATOR = "$$$";

    private final StringCodec<T> elementCodec;

    /**
     * Constructs a MultipleChoiceListCodec with the given element codec.
     *
     * @param elementCodec the codec for individual elements
     */
    public MultipleChoiceListCodec(StringCodec<T> elementCodec) {
        this.elementCodec = elementCodec;
    }

    /**
     * Decodes a string into a list of elements.
     *
     * @param value the string to decode
     * @return a list of decoded elements
     */
    @Override
    public List<T> decode(String value) {
        List<T> decodedValues = new ArrayList<>();

        if (value == null) {
            return decodedValues;
        }

        // Use Pattern.quote to escape regex metacharacters and preserve trailing empty elements with limit -1
        String escapedSeparator = Pattern.quote(SEPARATOR);
        for (var singleValue : value.split(escapedSeparator, -1)) {
            decodedValues.add(decodeSingle(singleValue));
        }

        return decodedValues;
    }

    private T decodeSingle(String value) {
        return elementCodec.decode(value);
    }

    /**
     * Encodes a list of elements into a string.
     *
     * @param value the list of elements to encode
     * @return the encoded string
     */
    @Override
    public String encode(List<T> value) {
        return value.stream()
                .map(elementCodec::encode)
                .collect(Collectors.joining(SEPARATOR));
    }

}
