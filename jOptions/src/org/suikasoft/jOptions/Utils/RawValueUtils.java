/**
 * Copyright 2013 SPeCS Research Group.
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

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.StringCodec;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.classmap.ClassMap;

public class RawValueUtils {

    private static final ClassMap<Object, StringCodec<?>> DEFAULT_CONVERTERS;

    static {
	DEFAULT_CONVERTERS = new ClassMap<>();

	RawValueUtils.DEFAULT_CONVERTERS.put(String.class, value -> value);
	RawValueUtils.DEFAULT_CONVERTERS.put(Boolean.class, value -> Boolean.valueOf(value));
    }

    /**
     * Attempts to transform a value in String format to a value in the target object.
     * 
     * <p>
     * - Checks if OptionDefinition implements ValueConverter.<br>
     * - Tries to find a default converter in the table.<br>
     * - Returns null.
     * 
     * @param optionDef
     * @param rawValue
     */
    public static Object getRealValue(DataKey<?> optionDef, String value) {

	// Check if it has a decoder
	if (optionDef.getDecoder().isPresent()) {
	    Object realValue = optionDef.getDecoder().get().decode(value);

	    // Check if value could be converted
	    if (realValue != null) {
		return realValue;
	    }
	}

	// Check default decoders
	StringCodec<?> decoder = RawValueUtils.DEFAULT_CONVERTERS.get(optionDef.getValueClass());

	if (decoder != null) {
	    return decoder.decode(value);
	}

	SpecsLogs.msgWarn("Could not find a valid converter for option " + optionDef);
	return null;

    }

}
