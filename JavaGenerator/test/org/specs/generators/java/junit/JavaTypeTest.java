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
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.specs.generators.java.types.JavaGenericType;
import org.specs.generators.java.types.JavaType;
import org.specs.generators.java.types.JavaTypeFactory;
import org.specs.generators.java.types.Primitive;

public class JavaTypeTest {

	@Test
	public void testGenerateCode() {
		JavaType javaType = JavaTypeFactory.getWildCardType();
		assertEquals("?", javaType.getSimpleType());

		javaType = JavaTypeFactory.getIntType();
		assertEquals("int", javaType.getSimpleType());
		assertEquals("java.lang.int", javaType.getCanonicalType());

		javaType.setArray(true);
		assertEquals("int[]", javaType.getSimpleType());
		javaType.setArrayDimension(3);
		assertEquals("int[][][]", javaType.getSimpleType());

		javaType = JavaTypeFactory.getPrimitiveWrapper(Primitive.INT);
		assertEquals("Integer", javaType.getSimpleType());
		assertEquals("java.lang.Integer", javaType.getCanonicalType());

		javaType = new JavaType(Map.class);
		assertEquals("Map", javaType.getSimpleType());
		assertEquals("java.util.Map", javaType.getCanonicalType());
		final JavaGenericType genericTypeKey = new JavaGenericType(JavaTypeFactory.getWildCardType());
		genericTypeKey.addType(new JavaType("MyClass", "org.specs"));
		final JavaGenericType genericTypeValue = new JavaGenericType(JavaTypeFactory.getStringType());
		javaType.addGeneric(genericTypeKey);
		javaType.addGeneric(genericTypeValue);
		assertEquals("Map<? extends MyClass, String>", javaType.getSimpleType());
		assertEquals("java.util.Map<? extends org.specs.MyClass, java.lang.String>", javaType.getCanonicalType());

		javaType = new JavaType(List.class);
		final JavaGenericType genStrinType = new JavaGenericType(JavaTypeFactory.getStringType());
		javaType.addGeneric(genStrinType);
		assertEquals("List<String>", javaType.getSimpleType());
		assertEquals("java.util.List<java.lang.String>", javaType.getCanonicalType());
		javaType.setArrayDimension(3);
		assertEquals("List<String>[][][]", javaType.getSimpleType());

		javaType = new JavaType("java.lang.int[][]");
		assertEquals("int[][]", javaType.getSimpleType());
		assertEquals("java.lang.int[][]", javaType.getCanonicalType());

		try {
			javaType = new JavaType("int[][");
			fail();
		} catch (final RuntimeException e) {

			assertEquals("Bad format for array definition. Bad characters: [", e.getMessage());
		}

		try {
			javaType = new JavaType("int[]", true);
			fail();
		} catch (final RuntimeException e) {

			String expectedMessage = "Cannot define array dimension both in the name and int the array argument: ";
			expectedMessage += "int[] vs dimension of 1";
			assertEquals(expectedMessage, e.getMessage());
		}

	}
}
