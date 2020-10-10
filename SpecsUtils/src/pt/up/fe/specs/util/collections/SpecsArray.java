/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.util.collections;

public class SpecsArray {

    /**
     * 
     * @param object
     * @return the length of the object if it is an array, or -1 if the object is not an array
     */
    public static int getLength(Object object) {
        var objectClass = object.getClass();
        if (!objectClass.getClass().isArray()) {
            return -1;
        }

        var componentClass = objectClass.getComponentType();
        if (!componentClass.isPrimitive()) {
            return ((Object[]) object).length;
        }

        if (componentClass.equals(int.class)) {
            return ((int[]) object).length;
        }

        if (componentClass.equals(long.class)) {
            return ((long[]) object).length;
        }

        if (componentClass.equals(double.class)) {
            return ((double[]) object).length;
        }

        if (componentClass.equals(float.class)) {
            return ((float[]) object).length;
        }

        if (componentClass.equals(boolean.class)) {
            return ((boolean[]) object).length;
        }

        if (componentClass.equals(char.class)) {
            return ((char[]) object).length;
        }

        if (componentClass.equals(byte.class)) {
            return ((byte[]) object).length;
        }

        if (componentClass.equals(short.class)) {
            return ((short[]) object).length;
        }

        throw new RuntimeException("Not implemented for array class " + componentClass);
    }
}
