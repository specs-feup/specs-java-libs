/*
 * KeyStringProvider.java
 *
 * Functional interface for providing string keys. Used for supplying string-based key values in the SPeCS ecosystem.
 *
 * Copyright 2025 SPeCS Research Group.
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

package pt.up.fe.specs.util.providers;

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.util.SpecsFactory;

/**
 * Functional interface for providing string keys.
 * <p>
 * Used for supplying string-based key values.
 * </p>
 *
 * @author Joao Bispo
 */
public interface KeyStringProvider extends KeyProvider<String> {

    /**
     * Converts an array of KeyStringProvider instances into a list of strings.
     *
     * @param providers an array of KeyStringProvider instances
     * @return a list of string keys provided by the instances
     */
    public static List<String> toList(KeyStringProvider... providers) {
	return toList(Arrays.asList(providers));
    }

    /**
     * Converts a list of KeyStringProvider instances into a list of strings.
     *
     * @param providers a list of KeyStringProvider instances
     * @return a list of string keys provided by the instances
     */
    public static List<String> toList(List<KeyStringProvider> providers) {
	List<String> strings = SpecsFactory.newArrayList();

	providers.forEach(stringProvider -> strings.add(stringProvider.getKey()));

	return strings;
    }

}
