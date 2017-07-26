/*
 * Copyright 2011 SPeCS Research Group.
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

package pt.up.fe.specs.util.asm.processor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.util.SpecsBits;
import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Contains a snapshot of the names and values of registers.
 *
 * @author Joao Bispo
 */
public class RegisterTable {

    public RegisterTable() {
	this.registerValues = new HashMap<>();
    }

    public Integer put(RegisterId regId, Integer registerValue) {
	if (registerValue == null) {
	    SpecsLogs.getLogger().
		    warning("Null input not accepted.");
	    return null;
	}
	return this.registerValues.put(regId.getName(), registerValue);
    }

    public Integer get(String registerName) {
	// Check if it has key
	if (this.registerValues.containsKey(registerName)) {
	    return this.registerValues.get(registerName);
	}

	// Check if it is just a single bit of the register
	Integer value = getFlagValue(registerName);
	if (value != null) {
	    return value;
	}

	SpecsLogs.getLogger().
		warning("Could not found register '" + registerName + "' in table.");
	return null;
    }

    private Integer getFlagValue(String registerName) {
	Integer bitPosition = RegisterUtils.decodeFlagBit(registerName);
	if (bitPosition == null) {
	    SpecsLogs.getLogger().
		    warning("Could not recognize key: " + registerName);
	    return null;
	}

	String regName = RegisterUtils.decodeFlagName(registerName);
	Integer value = this.registerValues.get(regName);
	if (value == null) {
	    SpecsLogs.getLogger().
		    warning("Register '" + regName + "' not found.");
	    return null;
	}

	return SpecsBits.getBit(bitPosition, value);
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();

	List<String> keys = SpecsFactory.newArrayList(this.registerValues.keySet());
	Collections.sort(keys);
	for (String key : keys) {
	    builder.append(key);
	    builder.append(": ");
	    builder.append(this.registerValues.get(key));
	    builder.append("\n");
	}

	return builder.toString();
    }

    private final Map<String, Integer> registerValues;

    final long serialVersionUID = 1;

}
