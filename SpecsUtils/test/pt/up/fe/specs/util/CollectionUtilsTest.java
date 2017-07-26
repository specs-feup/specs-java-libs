/**
 * Copyright 2014 SPeCS Research Group.
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

package pt.up.fe.specs.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CollectionUtilsTest {

    @Test
    public void getFirstIndex() {
	List<Number> numbers = Arrays.asList(Integer.valueOf(2), Double.valueOf(3.5));

	assertTrue(SpecsCollections.getFirstIndex(numbers, Integer.class) == 0);
	assertTrue(SpecsCollections.getFirstIndex(numbers, Double.class) == 1);
	assertTrue(SpecsCollections.getFirstIndex(numbers, Number.class) == 0);
	assertTrue(SpecsCollections.getFirstIndex(numbers, Float.class) == -1);
    }

}
