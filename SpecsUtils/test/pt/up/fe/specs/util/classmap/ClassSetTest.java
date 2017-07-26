/**
 * Copyright 2015 SPeCS Research Group.
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

package pt.up.fe.specs.util.classmap;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClassSetTest {

    @Test
    public void test() {
	ClassSet<Object> set = new ClassSet<>();
	set.add(Integer.class);

	assertTrue(set.contains(Integer.MAX_VALUE));
	assertFalse(set.contains(Double.MAX_VALUE));

	set.add(Number.class);
	assertTrue(set.contains(Double.MAX_VALUE));

	// fail("Not yet implemented");
    }

}
