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

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class OptionalConverterV2 implements Converter {

    @Override
    public boolean canConvert(Class type) {
        return type.equals(Optional.class);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        var optional = (Optional<?>) source;

        writer.addAttribute("isPresent", Boolean.toString(optional.isPresent()));

        if (optional.isPresent()) {
            var value = optional.get();
            writer.startNode("value");
            writer.addAttribute("classname", value.getClass().getName());
            context.convertAnother(value);
            writer.endNode();
        }

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {

        var isPresent = Boolean.parseBoolean(reader.getAttribute("isPresent"));

        if (!isPresent) {
            return Optional.empty();
        }

        reader.moveDown();
        var dummyOptional = Optional.of("dummy");
        var classname = reader.getAttribute("classname");
        Class<?> valueClass;
        try {
            valueClass = Class.forName(classname);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not unmarshal optional", e);
        }
        var value = context.convertAnother(dummyOptional, valueClass);
        reader.moveUp();

        return Optional.of(value);
    }

}
