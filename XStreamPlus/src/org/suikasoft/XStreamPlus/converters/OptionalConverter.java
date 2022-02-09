/**
 * Copyright 2022 SPeCS.
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

package org.suikasoft.XStreamPlus.converters;

import java.util.Optional;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class OptionalConverter extends AbstractSingleValueConverter {

    private static final String EMPTY_OPTIONAL = "%XSTREAM_EMPTY_OPTIONAL%";
    // private static final String OPTIONAL_PREFIX = "OPTIONAL";

    private final XStream xstream;

    // private int counter;
    // private Map<String, Optional<?>> optionals;
    // private Set<Long> seenOptionals;

    public OptionalConverter(XStream xstream) {
        this.xstream = xstream;
        // this.counter = 0;
        // this.optionals = new HashMap<>();
        // this.seenOptionals = new HashSet<>();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class type) {
        return type.equals(Optional.class);
    }

    @Override
    public Object fromString(String str) {
        if (str.equals(EMPTY_OPTIONAL)) {
            return Optional.empty();
        }

        // var opt = Optional.of(10);

        return Optional.of(xstream.fromXML(str));
    }

    @Override
    public String toString(Object obj) {

        Optional<?> optional = (Optional<?>) obj;

        if (optional.isEmpty()) {
            return EMPTY_OPTIONAL;
        }

        // If optional is present, associate with an ID
        // var optionalI

        return xstream.toXML(optional.get());
    }

}
