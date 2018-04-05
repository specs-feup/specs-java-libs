/**
 * Copyright 2018 SPeCS.
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

import java.util.function.Supplier;

/**
 * Utility class with methods for checkers.
 * 
 * @author JoaoBispo
 *
 */
public class SpecsCheck {

    private SpecsCheck() {
    }

    public static void checkArgument(boolean expression, Supplier<String> supplier) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(supplier.get()));
        }
    }

    public static <T> T checkNotNull(T reference, Supplier<String> supplier) {
        if (reference == null) {
            throw new NullPointerException(supplier.get());
        }

        return reference;
    }
}
