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

package org.suikasoft.jOptions.persistence;

import java.util.Map;

import org.suikasoft.GsonPlus.JsonStringListXstreamConverter;
import org.suikasoft.XStreamPlus.ObjectXml;
import org.suikasoft.jOptions.DataStore.SimpleDataStore;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.utilities.StringList;

public class DataStoreXml extends ObjectXml<DataStore> {

    private static final Map<String, Class<?>> LIBRARY_CLASSES;

    static {
        LIBRARY_CLASSES = SpecsFactory.newHashMap();

        LIBRARY_CLASSES.put("StringList", StringList.class);
        LIBRARY_CLASSES.put("SimpleDataStore", SimpleDataStore.class);

    }

    private final StoreDefinition storeDefinition;

    public DataStoreXml(StoreDefinition storeDefinition) {
        this.storeDefinition = storeDefinition;
        addMappings(LIBRARY_CLASSES);
        // configureXstream(keys);
        configureXstream(storeDefinition);
    }

    /*
    private <T> void configureXstream(Collection<DataKey<?>> keys) {
        // TODO: This breaks compatibility with previous configuration files
    
        // Collect classes and codecs
        Map<Class<?>, StringCodec<?>> codecs = new HashMap<>();
    
        for (var key : keys) {
            var decoder = key.getDecoder().orElse(null);
            if (decoder == null) {
                SpecsLogs.warn("String encoder/decoder not set for data key of class '" + key.getValueClass() + "'");
            }
    
            codecs.put(key.getValueClass(), decoder);
        }
    
        // Register converters
        for (var entry : codecs.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            // System.out.println("Registering " + entry.getKey());
            registerConverter((Class<Object>) entry.getKey(), (StringCodec<Object>) entry.getValue());
        }
    
        System.out.println("codecs:" + codecs.keySet());
        // var xstream = registerConverter(new StringConverter(supportedClass, codec));
        // registerConverter(JsonStringList.class, JsonStringList.getCodec());
    }
    */

    private void configureXstream(StoreDefinition storeDefinition) {
        // For compatibility with old Clava config files
        getXStreamFile().getXstream().registerConverter(new JsonStringListXstreamConverter());
        /*
        // Collect XStream converters
        Set<Converter> converters = new HashSet<>();
        
        for (var key : storeDefinition.getKeys()) {
            var converter = key.getXstreamConverter();
            if (converter == null) {
                // SpecsLogs.warn("XStream converter not set for data key of class '" + key.getValueClass() + "'");
                continue;
            }
        
            if (!(converter instanceof Converter)) {
                SpecsLogs.warn("Specified a XStream converter that is not of type Converter: " + converter.getClass());
                continue;
            }
        
            converters.add((Converter) converter);
        }
        
        // Register converters
        for (var converter : converters) {
            getXStreamFile().getXstream().registerConverter(converter);
        }
        */
    }

    @Override
    public Class<DataStore> getTargetClass() {
        return DataStore.class;
    }

}
