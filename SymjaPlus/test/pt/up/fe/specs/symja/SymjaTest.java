/**
 * Copyright 2013 SPeCS Research Group.
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

package pt.up.fe.specs.symja;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author Joao Bispo
 *
 */
public class SymjaTest {

    @Test
    public void test() {
	String expression = "N*M*i - (N*M*(i-1)+1) + 1";
	Map<String, String> constants = new HashMap<>();
	Integer N = 8, M = 16;
	Integer result = N * M;

	constants.put("N", N.toString());
	constants.put("M", M.toString());

	String output = SymjaPlusUtils.simplify(expression, constants);
	assertEquals(result.toString(), output);

	output = SymjaPlusUtils.simplify("a + halfSize - (a - halfSize) + 1", new HashMap<String, String>());
	System.out.println("Second output:" + output);

    }

}
