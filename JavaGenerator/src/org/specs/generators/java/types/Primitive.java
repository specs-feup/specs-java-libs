/**
 * Copyright 2015 SPeCS.
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

package org.specs.generators.java.types;

import tdrc.utils.StringUtils;

/**
 * Enumeration of the existing primitives in Java
 * 
 * @author tiago
 *
 */
public enum Primitive {

    VOID("void"),
    BYTE("byte"),
    SHORT("short"),
    INT("int"),
    LONG("long"),
    FLOAT("float"),
    DOUBLE("double"),
    BOOLEAN("boolean"),
    CHAR("char"),;

    private String type;

    Primitive(String type) {
	this.type = type;
    }

    public String getType() {
	return type;
    }

    public static Primitive getPrimitive(String name) {

	for (final Primitive primitive : values()) {

	    if (primitive.type.equals(name)) {
		return primitive;
	    }
	}
	throw new RuntimeException("The type '" + name + "' is not a primitive.");
    }

    public String getPrimitiveWrapper() {

	if (equals(Primitive.INT)) {
	    return "Integer";
	}
	return StringUtils.firstCharToUpper(type);

    }

    public static boolean contains(String name) {

	for (final Primitive primitive : values()) {

	    if (primitive.type.equals(name)) {
		return true;
	    }
	}
	return false;
    }
}
