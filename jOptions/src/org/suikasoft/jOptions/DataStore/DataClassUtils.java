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

package org.suikasoft.jOptions.DataStore;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.providers.StringProvider;

/**
 * Utility class for DataClass-related operations.
 *
 * <p>This class provides static methods for safely converting DataClass values to strings, handling cycles and common types.
 */
public class DataClassUtils {

    /**
     * Properly converts to string the value of a DataClass.
     *
     * <p>Simply calling toString() on a DataClass value might cause infinite cycles, in case there are circular dependencies.
     *
     * @param dataClassValue the value to convert
     * @return a string representation of the value
     */
    public static String toString(Object dataClassValue) {
        if (dataClassValue == null) {
            return "null";
        }

        if (dataClassValue instanceof StringProvider) {
            return ((StringProvider) dataClassValue).getString();
        }

        if (dataClassValue instanceof DataClass) {
            DataClass<?> dataClass = (DataClass<?>) dataClassValue;

            return "'" + dataClass.getDataClassName() + "'";
        }

        if (dataClassValue instanceof Optional) {
            Optional<?> optional = (Optional<?>) dataClassValue;
            return optional.map(value -> toString(value)).orElse("Optional.empty");
        }

        if (dataClassValue instanceof List) {
            return ((Collection<?>) dataClassValue).stream()
                    .map(value -> value != null ? toString(value) : "null")
                    .collect(Collectors.joining(", ", "[", "]"));
        }

        return dataClassValue.toString();
    }

}
