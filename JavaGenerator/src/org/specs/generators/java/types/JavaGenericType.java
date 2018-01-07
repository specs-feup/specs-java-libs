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

import java.util.List;

import org.specs.generators.java.utils.UniqueList;

import tdrc.utils.StringUtils;

public class JavaGenericType {

	private JavaType theType;
	private List<JavaType> extendingTypes;

	public JavaGenericType(JavaType theType) {
		setTheType(theType);
		extendingTypes = new UniqueList<>();
	}

	public boolean addType(JavaType extendedType) {
		return extendingTypes.add(extendedType);
	}

	public JavaType getTheType() {
		return theType;
	}

	public void setTheType(JavaType theType) {
		this.theType = theType;
	}

	public List<JavaType> getExtendingTypes() {
		return extendingTypes;
	}

	public void setExtendingTypes(List<JavaType> extendingTypes) {
		this.extendingTypes = extendingTypes;
	}

	@Override
	public String toString() {
		final String toString = "<" + getCanonicalType() + ">";
		return toString;
	}

	/**
	 * Return a simple representation of this types, i.e., do not include
	 * package. Does not include the '&lt;' and '&gt;' delimiters
	 * 
	 * @return
	 */
	public String getSimpleType() {
		String toString = theType.getSimpleType();
		if (!extendingTypes.isEmpty()) {
			toString += " extends ";
			toString += StringUtils.join(extendingTypes, JavaType::getSimpleType, "& ");
		}
		return toString;
	}

	/**
	 * Return the canonical representation of this types, i.e., include package.
	 * Does not include the '&lt;' and '&gt;' delimiters
	 * 
	 * @return
	 */
	public String getCanonicalType() {
		String toString = theType.getCanonicalName();
		if (!extendingTypes.isEmpty()) {
			toString += " extends ";
			toString += StringUtils.join(extendingTypes, JavaType::getCanonicalName, "& ");
		}
		return toString;
	}

	/**
	 * Return a simple representation of this types, i.e., do not include
	 * package, including the '&lt;' and '&gt;' delimiters
	 * 
	 * @return
	 */
	public String getWrappedSimpleType() {
		String toString = "<" + theType.getSimpleType();

		if (!extendingTypes.isEmpty()) {
			toString += " extends ";
			toString += StringUtils.join(extendingTypes, JavaType::getSimpleType, "& ");
		}
		toString += ">";
		return toString;
	}

	@Override
	public JavaGenericType clone() {

		final JavaGenericType genericType = new JavaGenericType(theType.clone());
		genericType.extendingTypes.forEach(ext -> genericType.addType(ext.clone()));
		return genericType;
	}
}
