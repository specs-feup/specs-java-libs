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

public class DataClassUtils {

    /**
     * Properly converts to string the value of a DataClass.
     * 
     * <p>
     * Simply calling toString() on a DataClass value might cause infinite cycles, in case there are circular
     * dependences.
     * 
     * @param dataClassValue
     * @return
     */
    public static String toString(Object dataClassValue) {
        if (dataClassValue instanceof DataClass) {
            DataClass<?> dataClass = (DataClass<?>) dataClassValue;

            return "'" + dataClass.getDataClassName() + "'";
        }

        return dataClassValue.toString();
    }

}
