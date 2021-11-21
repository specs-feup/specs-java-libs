/**
 * Copyright 2021 SPeCS.
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

package org.suikasoft.XStreamPlus;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import pt.up.fe.specs.util.parsing.StringCodec;

public class StringConverter<T> extends AbstractSingleValueConverter {

    private final Class<T> supportedClass;
    private final StringCodec<T> codec;

    public StringConverter(Class<T> supportedClass, StringCodec<T> codec) {
        this.supportedClass = supportedClass;
        this.codec = codec;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class type) {
        return supportedClass.isAssignableFrom(type);
    }

    @Override
    public Object fromString(String str) {
        return codec.decode(str);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString(Object obj) {
        return codec.encode((T) obj);
    }

}
