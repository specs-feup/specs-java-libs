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
package org.specs.generators.java.junit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.specs.generators.java.members.Field;
import org.specs.generators.java.types.JavaType;
import org.specs.generators.java.types.JavaTypeFactory;
import org.specs.generators.java.types.Primitive;

public class FieldTest {

	@Test
	public void testGenerateCode() {
		final String fieldName = "field";
		final JavaType intType = JavaTypeFactory.getPrimitiveType(Primitive.INT);
		final Field tester = new Field(intType, fieldName);
		final StringBuilder result = new StringBuilder("private int " + fieldName + ";");
		assertEquals("Generated", result.toString(), tester.generateCode(0).toString());

	}

}
