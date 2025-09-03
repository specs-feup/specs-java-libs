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

package org.suikasoft.GsonPlus;

import java.util.ArrayList;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import pt.up.fe.specs.util.utilities.StringList;

/**
 * XStream converter for {@link JsonStringList} for compatibility with legacy Clava configuration files.
 *
 * @author JBispo
 */
public class JsonStringListXstreamConverter implements Converter {

    /**
     * Checks if the converter can handle the given type.
     *
     * @param type the class type to check
     * @return true if the type is JsonStringList, false otherwise
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class type) {
        return type != null && type.equals(JsonStringList.class);
    }

    /**
     * Not implemented. Throws exception if called.
     *
     * @param source the source object
     * @param writer the writer
     * @param context the context
     * @throws RuntimeException always
     */
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        throw new RuntimeException("This should not be used");
    }

    /**
     * Unmarshals a JsonStringList from XML.
     *
     * @param reader the XML reader
     * @param context the context
     * @return the unmarshalled object
     */
    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        reader.moveDown();

        // Collect list of strings
        var flags = new ArrayList<String>();

        while (reader.hasMoreChildren()) {
            reader.moveDown();
            flags.add(reader.getValue());
            reader.moveUp();
        }

        reader.moveUp();

        return new StringList(flags);
    }
}
