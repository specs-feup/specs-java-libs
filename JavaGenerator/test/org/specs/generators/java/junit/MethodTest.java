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
import org.specs.generators.java.enums.Modifier;
import org.specs.generators.java.members.Method;
import org.specs.generators.java.types.JavaType;
import org.specs.generators.java.types.JavaTypeFactory;
import org.specs.generators.java.types.Primitive;
import org.specs.generators.java.utils.Utils;

public class MethodTest {

	@Test
	public void testGenerateCode() {
		final JavaType intType = JavaTypeFactory.getPrimitiveType(Primitive.INT);
		final Method m = new Method(intType, "max");
		m.addArgument(intType, "a");
		m.addArgument(intType, "b");

		final String returnStr = "/**\n" + " * \n" + " */\n" + "public int max(int a, int b) {\n" + Utils.indent(1)
				+ "// TODO Auto-generated method stub\n" + Utils.indent(1) + "return 0;\n" + "}";

		assertEquals(returnStr, m.generateCode(0).toString());
		m.add(Modifier.ABSTRACT);

		assertEquals("/**\n" + " * \n" + " */\n" + "public abstract int max(int a, int b);",
				m.generateCode(0).toString());

	}

}
