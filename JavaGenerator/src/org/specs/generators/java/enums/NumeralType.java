/*
 * Copyright 2013 SPeCS.
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
package org.specs.generators.java.enums;

/**
 * Types requiring numerical return
 * 
 * @author Tiago @
 */
public enum NumeralType {
	INT(int.class.getName()), DOUBLE(double.class.getName()), FLOAT(float.class.getName()), LONG(
			long.class.getName()), SHORT(short.class.getName()), BYTE(byte.class.getName());

	NumeralType(String type) {
		this.type = type;
	}

	private String type;

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return type;
	}

	public static boolean contains(String type) {
		for (final NumeralType nt : values()) {
			if (nt.type.equals(type)) {
				return true;
			}
		}
		return false;
	}
}