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
package org.specs.generators.java.members;

import org.specs.generators.java.types.JavaType;

/**
 * Argument declaration for a method
 * 
 * @author Tiago
 * 
 */
public class Argument {
	private String name;
	private JavaType classType;

	/**
	 * Create an Argument with type classType
	 * 
	 * @param classType
	 * @param name
	 */
	public Argument(JavaType classType, String name) {
		setName(name);
		setClassType(classType);
	}

	/**
	 * @return the classType
	 */
	public JavaType getClassType() {
		return classType;
	}

	/**
	 * @param classType
	 *            the classType to set
	 */
	public void setClassType(JavaType classType) {
		this.classType = classType;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return classType.getSimpleType() + " " + name;
	}

	@Override
	public Argument clone() {

		return new Argument(classType.clone(), name);
	}
}
