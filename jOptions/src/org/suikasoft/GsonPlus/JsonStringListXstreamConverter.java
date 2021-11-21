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

package org.suikasoft.GsonPlus;

import java.util.ArrayList;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import pt.up.fe.specs.util.utilities.StringList;

/**
 * For compatibility with legacy Clava configuration files
 * 
 * @author JBispo
 *
 */
public class JsonStringListXstreamConverter implements Converter {

    @Override
    public boolean canConvert(Class type) {
        return type.equals(JsonStringList.class);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        throw new RuntimeException("This should not be used");

    }

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
