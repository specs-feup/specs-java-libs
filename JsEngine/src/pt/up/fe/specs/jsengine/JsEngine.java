/**
 * Copyright 2019 SPeCS.
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

package pt.up.fe.specs.jsengine;

import java.util.Collection;

import javax.script.Bindings;
import javax.script.ScriptEngine;

/**
 * Represents the JavaScript engine used by LARA.
 * 
 * TODO: Replace 'Bindings' with 'Object'. Only JsEngine should manipulate JS objects
 * 
 * @author JoaoBispo
 *
 */
public interface JsEngine {

    ScriptEngine getEngine();

    ForOfType getForOfType();

    /**
     * TODO: Remove
     * 
     * @deprecated
     * @return true, if you can pass a reference to 'this' from JS and modify it through a Bindings object.
     */
    @Deprecated
    boolean supportsModifyingThis();

    /**
     * Based on this site: http://programmaticallyspeaking.com/nashorns-jsobject-in-context.html
     *
     * @return
     */
    Object getUndefined();

    boolean isUndefined(Object object);

    String stringify(Object object);

    /// TYPE CONVERSIONS

    Bindings asBindings(Object value);

    boolean asBoolean(Object value);

    /**
     * 
     * @return the Bindings of the engine scope
     */
    Object getBindings();

    /**
     * Creates a new JavaScript array.
     *
     * 
     * @return a
     */
    Object newNativeArray();

    /**
     * Converts an array of objects to a JavaScript array
     *
     * @param values
     *            the array of values
     * @return a javascript array containing all the elements in values, with the same indexes
     */
    Object toNativeArray(Object[] values);

    /**
     * Converts a list of objects to a JavaScript array
     *
     * @param values
     *            the array of values
     * @return a javascript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(Collection<? extends Object> values) {
        return toNativeArray(values.toArray());
    }

    /**
     * Converts an array of ints to a JavaScript array
     *
     * @param values
     *            the array of values
     * @return a javascript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(int[] values) {

        Object[] newObject = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            newObject[i] = values[i];
        }
        return toNativeArray(newObject);
    }

    /**
     * Converts an array of floats to a JavaScript array
     *
     * @param values
     *            the array of values
     * @return a javascript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(float[] values) {

        Object[] newObject = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            newObject[i] = values[i];
        }
        return toNativeArray(newObject);
    }

    /**
     * Converts an array of doubles to a JavaScript array
     *
     * @param values
     *            the array of values
     * @return a javascript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(double[] values) {

        Object[] newObject = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            newObject[i] = values[i];
        }
        return toNativeArray(newObject);
    }

    /**
     * Converts an array of booleans to a JavaScript array
     *
     * @param values
     *            the array of values
     * @return a javascript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(boolean[] values) {

        Object[] newObject = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            newObject[i] = values[i];
        }
        return toNativeArray(newObject);
    }

    /**
     * Converts an array of chars to a JavaScript array
     *
     * @param values
     *            the array of values
     * @return a javascript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(char[] values) {

        Object[] newObject = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            newObject[i] = values[i];
        }
        return toNativeArray(newObject);
    }

    /**
     * Converts an array of bytes to a JavaScript array
     *
     * @param values
     *            the array of values
     * @return a javascript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(byte[] values) {

        Object[] newObject = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            newObject[i] = values[i];
        }
        return toNativeArray(newObject);
    }

    /**
     * Converts an array of shorts to a JavaScript array
     *
     * @param values
     *            the array of values
     * @return a javascript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(short[] values) {

        Object[] newObject = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            newObject[i] = values[i];
        }
        return toNativeArray(newObject);
    }

    Object eval(String code);

    Object eval(String script, Bindings scope);

    default Bindings createBindings() {
        return getEngine().createBindings();
    }

    default void nashornWarning(String message) {
        // Do nothing
    }

    /**
     * 
     * @param object
     * @return true if the given object is an array, false otherwise
     */
    boolean isArray(Object object);

    // Object put(Bindings var, String member, Object value);

    // public Object remove(Bindings object, Object key);

    /**
     * 
     * @param object
     * @return the value inside the given object (e.g., map, array)
     */
    Collection<Object> getValues(Object object);

    /**
     * Converts an objecto to the given Java class.
     * 
     * @param <T>
     * @param object
     * @param toConvert
     * @return
     */
    <T> T convert(Object object, Class<T> targetClass);

}
