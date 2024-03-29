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

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import pt.up.fe.specs.util.classmap.FunctionClassMap;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

/**
 * Represents the JavaScript engine used by LARA.
 * 
 * TODO: Replace 'Bindings' with 'Object'. Only JsEngine should manipulate JS objects
 * 
 * @author JoaoBispo
 *
 */
public interface JsEngine {

    // ScriptEngine getEngine();

    ForOfType getForOfType();

    /**
     * Based on this site: http://programmaticallyspeaking.com/nashorns-jsobject-in-context.html
     *
     * @return
     */
    Object getUndefined();

    boolean isUndefined(Object object);

    String stringify(Object object);

    /// ENGINE FEATURES

    /**
     * 
     * @return if true, the engine can automatically transform obj.prop to obj.getProp(). False otherwise
     */
    boolean supportsProperties();

    /// TYPE CONVERSIONS

    // Bindings asBindings(Object value);

    boolean asBoolean(Object value);

    double asDouble(Object value);

    /**
     * Attempts to convert a JS bindings value to a Java object.
     * 
     * @param value
     * @return
     */
    Object toJava(Object value);

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
     * Creates a new JavaScript map.
     *
     * 
     * @return a
     */
    Object newNativeMap();

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
     * Converts an array of longs to a JavaScript array
     *
     * @param values
     *            the array of values
     * @return a javascript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(long[] values) {

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

    /**
     * Evaluates the given string of JavaScript code. It is preferable to use the version that accepts a string with a
     * description of the source.
     * 
     * @param code
     * @return
     */
    default Object eval(String code) {
        return eval(code, "unnamed_js_code");
    }

    /**
     * 
     * @param code
     * @param source
     *            a String identifying the source
     * @return
     */
    Object eval(String code, String source);

    /**
     * 
     * @param script
     * @param scope
     * @param type
     * @param source
     *            a String identifying the source. If the code is loaded as module and this function has been called
     *            before with the same value for source, it might consider the module is already in cache
     * @return
     */
    Object eval(String script, Object scope, JsFileType type, String source);

    default Object eval(String code, JsFileType type, String source) {
        throw new NotImplementedException(this);
    }

    default Object evalFile(File jsFile) {
        return evalFile(jsFile, JsFileType.NORMAL);
    }

    default Object evalFile(File jsFile, JsFileType type) {
        return evalFile(jsFile, type, null);
    }

    /**
     * 
     * @param jsFile
     * @param type
     * @param content
     *            if the contents of the file need to be changed, but you need to load as a file, so that the relative
     *            paths in imports keep working
     * @return
     */
    default Object evalFile(File jsFile, JsFileType type, String content) {
        throw new NotImplementedException(this);
    }

    Object call(Object function, Object... args);

    // default Bindings createBindings() {
    // return getEngine().createBindings();
    // }

    default void nashornWarning(String message) {
        // Do nothing
    }

    /**
     * 
     * @param object
     * @return true if the given object is an array, false otherwise
     */
    boolean isArray(Object object);

    /**
     * 
     * @param object
     * @return true if the given object is a number, false otherwise
     */
    boolean isNumber(Object object);

    /**
     * 
     * @param object
     * @return true if the given object has members, false otherwise
     */
    boolean isObject(Object object);

    /**
     * 
     * @param object
     * @return true if the given object is a string, false otherwise
     */
    boolean isString(Object object);

    /**
     * 
     * @param object
     * @return true if the given object is a boolean, false otherwise
     */
    boolean isBoolean(Object object);

    /**
     * 
     * @param object
     * @return true if the object can be called (executed)
     */
    boolean isFunction(Object object);

    // Object put(Bindings var, String member, Object value);

    // public Object remove(Bindings object, Object key);

    /**
     * 
     * @param object
     * @return the value inside the given object (e.g., map, array)
     */
    Collection<Object> getValues(Object object);

    /**
     * Converts an object to the given Java class.
     * 
     * @param <T>
     * @param object
     * @param toConvert
     * @return
     */
    <T> T convert(Object object, Class<T> targetClass);

    /// Engine related engine-scope operations

    /**
     * Sets the specified value with the specified key in the ENGINE_SCOPE Bindings of the protected context field.
     *
     * @param key
     * @param value
     */
    void put(String key, Object value);

    /// Bindings-like operations

    Object put(Object bindings, String key, Object value);

    Object remove(Object bindings, String key);

    Set<String> keySet(Object bindings);

    Object get(Object bindings, String key);

    default <T> T get(Object bindings, String key, Class<T> targetClass) {
        return convert(get(bindings, key), targetClass);
    }

    default Object get(String key) {
        return get(getBindings(), key);
    }

    default <T> T get(String key, Class<T> targetClass) {
        return get(getBindings(), key, targetClass);
    }

    /**
     * Adds a JS conversion rule for objects that are instances of a given class.
     * 
     * @param key
     * @param rule
     */
    // void addToJsRule(Class<?> key, BiFunction<Object, JsEngine, Object> rule);
    <VS, KS extends VS> void addToJsRule(Class<KS> key, Function<VS, Object> rule);

    /**
     * Maps classes to JS conversion rules.
     * 
     * @return
     */
    FunctionClassMap<Object, Object> getToJsRules();

    /**
     * Converts a given Java object to a more compatible type in JavaScript.
     * 
     * New conversion rules can be added with the method
     * 
     * Conversions currently supported by default:<br>
     * - null to undefined;<br>
     * - Java array to JS array;<br>
     * - Java List to JS array;<br>
     * - Java Set to JS array;<br>
     * - JsonArray to JS array;<br>
     * 
     * @param javaObject
     * @return
     */
    default Object toJs(Object javaObject) {

        // Null
        if (javaObject == null) {
            return getUndefined();
        }

        var objectClass = javaObject.getClass();

        // Array
        if (objectClass.isArray()) {
            var componentClass = objectClass.getComponentType();

            if (!componentClass.isPrimitive()) {
                var objectArray = (Object[]) javaObject;
                var convertedObjectArray = Arrays.stream(objectArray).map(this::toJs).toArray();
                return toNativeArray(convertedObjectArray);
            }

            if (componentClass.equals(int.class)) {
                return toNativeArray((int[]) javaObject);
            }

            if (componentClass.equals(long.class)) {
                return toNativeArray((long[]) javaObject);
            }

            if (componentClass.equals(double.class)) {
                return toNativeArray((double[]) javaObject);
            }

            if (componentClass.equals(float.class)) {
                return toNativeArray((float[]) javaObject);
            }

            if (componentClass.equals(boolean.class)) {
                return toNativeArray((boolean[]) javaObject);
            }

            if (componentClass.equals(char.class)) {
                return toNativeArray((char[]) javaObject);
            }

            if (componentClass.equals(byte.class)) {
                return toNativeArray((byte[]) javaObject);
            }

            if (componentClass.equals(short.class)) {
                return toNativeArray((short[]) javaObject);
            }

            throw new RuntimeException("Not implemented for array class " + componentClass);
        }

        // Check if there is a conversion rule for the class of this object
        var rules = getToJsRules();
        var processedObject = rules.applyTry(javaObject);

        return processedObject.orElse(javaObject);
    }

    /**
     * 
     * @param possibleError
     * @return a Throwable if the given object is an error generated by the engine.
     */
    default Optional<Throwable> getException(Object possibleError) {
        throw new NotImplementedException(this);
    }
}
