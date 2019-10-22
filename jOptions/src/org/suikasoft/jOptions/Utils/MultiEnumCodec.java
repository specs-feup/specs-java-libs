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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.parsing.StringCodec;

public class MultiEnumCodec<T extends Enum<T>> implements StringCodec<List<T>> {

    private static final String SEPARATOR = ";";

    private final Class<T> anEnum;
    private final Map<String, T> decodeMap;

    public MultiEnumCodec(Class<T> anEnum) {
        this.anEnum = anEnum;
        this.decodeMap = new HashMap<>();

        for (T enumValue : anEnum.getEnumConstants()) {
            decodeMap.put(enumValue.name(), enumValue);
        }
    }

    @Override
    public List<T> decode(String value) {
        List<T> decodedValues = new ArrayList<>();

        if (value == null) {
            return decodedValues;
        }

        for (var singleValue : value.split(SEPARATOR)) {
            decodedValues.add(decodeSingle(singleValue));
        }

        return decodedValues;
    }

    private T decodeSingle(String value) {

        T enumValue = decodeMap.get(value);

        if (enumValue == null) {
            throw new RuntimeException("Could not find enum '" + value + "' in class '" + anEnum
                    + "'. Available values: " + decodeMap.keySet());
        }

        return enumValue;
    }

    @Override
    public String encode(List<T> value) {
        return value.stream()
                .map(enumValue -> enumValue.name())
                .collect(Collectors.joining(SEPARATOR));
    }

}
