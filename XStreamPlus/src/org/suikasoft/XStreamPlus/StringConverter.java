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
 * specific language governing permissions and limitations under the License.
 */

package org.suikasoft.XStreamPlus;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Converter for serializing and deserializing objects using a StringCodec with XStream.
 *
 * @param <T> the type supported by this converter
 */
public class StringConverter<T> extends AbstractSingleValueConverter {

    private final Class<T> supportedClass;
    private final StringCodec<T> codec;

    /**
     * Constructs a StringConverter for the given class and codec.
     *
     * @param supportedClass the class supported by this converter
     * @param codec the codec to use for encoding/decoding
     */
    public StringConverter(Class<T> supportedClass, StringCodec<T> codec) {
        this.supportedClass = supportedClass;
        this.codec = codec;
    }

    /**
     * Checks if the converter can handle the given type.
     *
     * @param type the class to check
     * @return true if the type is supported, false otherwise
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class type) {
        return supportedClass.isAssignableFrom(type);
    }

    /**
     * Converts a string to an object using the codec.
     *
     * @param str the string to decode
     * @return the decoded object
     */
    @Override
    public Object fromString(String str) {
        return codec.decode(str);
    }

    /**
     * Converts an object to a string using the codec.
     *
     * @param obj the object to encode
     * @return the encoded string
     */
    @SuppressWarnings("unchecked")
    @Override
    public String toString(Object obj) {
        return codec.encode((T) obj);
    }

}
