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
/**
 * 
 */
package org.specs.generators.java.members;

import java.util.ArrayList;
import java.util.List;

import org.specs.generators.java.IGenerate;
import org.specs.generators.java.utils.Utils;

/**
 * An item for a java Enum
 * 
 * @author Tiago Carvalho
 * 
 */
public class EnumItem implements IGenerate {
	private String name;
	private List<String> parameters;

	public EnumItem(String name) {
		this.name = name;
		parameters = new ArrayList<>();
	}

	/**
	 * Add a parameter to the item
	 * 
	 * @param value
	 *            the value to add
	 */
	public void addParameter(String value) {
		parameters.add(value);
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

	/**
	 * @return the parameters
	 */
	public List<String> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

	/*
	 * @Override public boolean equals(Object arg0) { if (!(arg0 instanceof
	 * EnumItem)) return false; EnumItem clone = (EnumItem) arg0; return
	 * clone.name.equals(this.name); }
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final EnumItem other = (EnumItem) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public StringBuilder generateCode(int indentation) {
		final StringBuilder itemBuilder = Utils.indent(indentation);
		itemBuilder.append(name);
		if (!parameters.isEmpty()) {
			itemBuilder.append("(");
			for (final String param : parameters) {
				itemBuilder.append(param + ", ");
			}
			itemBuilder.replace(itemBuilder.length() - 2, itemBuilder.length(), ")");
		}

		return itemBuilder;
	}

}
