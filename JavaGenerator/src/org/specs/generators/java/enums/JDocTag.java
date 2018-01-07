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
 * Tags used in javadoc
 * 
 * @author Tiago
 */
public enum JDocTag {
	AUTHOR("@author"), CATEGORY("@category"), DEPRECATED("@deprecated"), SEE("@see"), VERSION("@version"), PARAM(
			"@param"), RETURN("@return"),;
	private String tag;

	JDocTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}
}