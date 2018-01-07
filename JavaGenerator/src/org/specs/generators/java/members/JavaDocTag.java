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

import org.specs.generators.java.IGenerate;
import org.specs.generators.java.enums.JDocTag;
import org.specs.generators.java.utils.Utils;

/**
 * Association between a javadoc tag and its description
 * 
 * @author Tiago
 * 
 */
public class JavaDocTag implements IGenerate {

	// The tag name and its description
	private JDocTag tag;
	private StringBuilder description;

	/**
	 * Add a new Tag with no comment
	 * 
	 * @param tag
	 *            the javadoc tag
	 */
	public JavaDocTag(JDocTag tag) {
		setTag(tag);
		setDescription(new StringBuilder());
	}

	/**
	 * Add a new Tag with the comment inside the {@link String}
	 * 
	 * @param tag
	 *            the javadoc tag
	 * @param description
	 *            the {@link String} containing the description
	 */
	public JavaDocTag(JDocTag tag, String descriptionStr) {
		setTag(tag);
		setDescription(new StringBuilder(descriptionStr));
	}

	/**
	 * Add a new Tag with the comment inside the {@link String}
	 * 
	 * @param tag
	 *            the javadoc tag
	 * @param description
	 *            the {@link String} containing the description
	 */
	public JavaDocTag(JDocTag tag, StringBuilder description) {
		setTag(tag);
		setDescription(description);
	}

	/**
	 * Append a {@link StringBuilder} to the description
	 * 
	 * @param description
	 *            the {@link StringBuilder} to append
	 */
	public void append(StringBuilder description) {
		this.description.append(description);
	}

	/**
	 * Append a {@link String} to the description
	 * 
	 * @param descriptionStr
	 *            the {@link String} to append
	 */
	public void append(String descriptionStr) {
		description.append(descriptionStr);
	}

	/**
	 * @return the description
	 */
	public StringBuilder getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(StringBuilder description) {
		this.description = description;
	}

	/**
	 * @return the tag
	 */
	public JDocTag getTag() {
		return tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(JDocTag tag) {
		this.tag = tag;
	}

	/**
	 * Generate the javadoc tag
	 * 
	 * @param indentation
	 *            level of indentation
	 * @return the generated javadoc tag
	 */
	@Override
	public StringBuilder generateCode(int indentation) {
		final StringBuilder generated = Utils.indent(indentation);
		generated.append(tag.getTag());
		generated.append(" ");
		generated.append(description);
		return generated;
	}

	@Override
	public String toString() {
		return generateCode(0).toString();
	}
}
