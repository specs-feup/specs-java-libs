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
 * Main class for JavaScript engine integration and execution.
 * Represents the JavaScript engine used by LARA.
 * 
 * @author JoaoBispo
 *
 */
public interface JsEngine {

    /**
     * Retrieves the type of "for-of" loop supported by the engine.
     * 
     * @return the type of "for-of" loop
     */
    ForOfType getForOfType();

    /**
     * Based on this site: http://programmaticallyspeaking.com/nashorns-jsobject-in-context.html
     *
     * @return the undefined object representation
     */
    Object getUndefined();

    /**
     * Checks if the given object is undefined.
     * 
     * @param object the object to check
     * @return true if the object is undefined, false otherwise
     */
    boolean isUndefined(Object object);

    /**
     * Converts the given object to its string representation.
     * 
     * @param object the object to stringify
     * @return the string representation of the object
     */
    String stringify(Object object);

    /// ENGINE FEATURES

    /**
     * Checks if the engine supports automatic property transformation.
     * 
     * @return true if the engine supports properties, false otherwise
     */
    boolean supportsProperties();

    /// TYPE CONVERSIONS

    /**
     * Converts the given value to a boolean.
     * 
     * @param value the value to convert
     * @return the boolean representation of the value
     */
    boolean asBoolean(Object value);

    /**
     * Converts the given value to a double.
     * 
     * @param value the value to convert
     * @return the double representation of the value
     */
    double asDouble(Object value);

    /**
     * Attempts to convert a JavaScript bindings value to a Java object.
     * 
     * @param value the value to convert
     * @return the Java object representation of the value
     */
    Object toJava(Object value);

    /**
     * Retrieves the bindings of the engine scope.
     * 
     * @return the bindings object
     */
    Object getBindings();

    /**
     * Creates a new JavaScript array.
     * 
     * @return a new JavaScript array
     */
    Object newNativeArray();

    /**
     * Creates a new JavaScript map.
     * 
     * @return a new JavaScript map
     */
    Object newNativeMap();

    /**
     * Converts an array of objects to a JavaScript array.
     * 
     * @param values the array of values
     * @return a JavaScript array containing all the elements in values, with the same indexes
     */
    Object toNativeArray(Object[] values);

    /**
     * Converts a collection of objects to a JavaScript array.
     * 
     * @param values the collection of values
     * @return a JavaScript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(Collection<? extends Object> values) {
        return toNativeArray(values.toArray());
    }

    /**
     * Converts an array of ints to a JavaScript array.
     * 
     * @param values the array of values
     * @return a JavaScript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(int[] values) {
        Object[] newObject = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            newObject[i] = values[i];
        }
        return toNativeArray(newObject);
    }

    /**
     * Converts an array of longs to a JavaScript array.
     * 
     * @param values the array of values
     * @return a JavaScript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(long[] values) {
        Object[] newObject = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            newObject[i] = values[i];
        }
        return toNativeArray(newObject);
    }

    /**
     * Converts an array of floats to a JavaScript array.
     * 
     * @param values the array of values
     * @return a JavaScript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(float[] values) {
        Object[] newObject = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            newObject[i] = values[i];
        }
        return toNativeArray(newObject);
    }

    /**
     * Converts an array of doubles to a JavaScript array.
     * 
     * @param values the array of values
     * @return a JavaScript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(double[] values) {
        Object[] newObject = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            newObject[i] = values[i];
        }
        return toNativeArray(newObject);
    }

    /**
     * Converts an array of booleans to a JavaScript array.
     * 
     * @param values the array of values
     * @return a JavaScript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(boolean[] values) {
        Object[] newObject = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            newObject[i] = values[i];
        }
        return toNativeArray(newObject);
    }

    /**
     * Converts an array of chars to a JavaScript array.
     * 
     * @param values the array of values
     * @return a JavaScript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(char[] values) {
        Object[] newObject = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            newObject[i] = values[i];
        }
        return toNativeArray(newObject);
    }

    /**
     * Converts an array of bytes to a JavaScript array.
     * 
     * @param values the array of values
     * @return a JavaScript array containing all the elements in values, with the same indexes
     */
    default Object toNativeArray(byte[] values) {
        Object[] newObject = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            newObject[i] = values[i];
        }
        return toNativeArray(newObject);
    }

    /**
     * Converts an array of shorts to a JavaScript array.
     * 
     * @param values the array of values
     * @return a JavaScript array containing all the elements in values, with the same indexes
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
     * @param code the JavaScript code to evaluate
     * @return the result of the evaluation
     */
    default Object eval(String code) {
        return eval(code, "unnamed_js_code");
    }

    /**
     * Evaluates the given string of JavaScript code with a specified source description.
     * 
     * @param code the JavaScript code to evaluate
     * @param source a string identifying the source
     * @return the result of the evaluation
     */
    Object eval(String code, String source);

    /**
     * Evaluates the given script with a specified scope, type, and source description.
     * 
     * @param script the JavaScript script to evaluate
     * @param scope the scope in which to evaluate the script
     * @param type the type of the script
     * @param source a string identifying the source
     * @return the result of the evaluation
     */
    Object eval(String script, Object scope, JsFileType type, String source);

    /**
     * Evaluates the given string of JavaScript code with a specified type and source description.
     * 
     * @param code the JavaScript code to evaluate
     * @param type the type of the script
     * @param source a string identifying the source
     * @return the result of the evaluation
     */
    default Object eval(String code, JsFileType type, String source) {
        throw new NotImplementedException(this);
    }

    /**
     * Evaluates the given JavaScript file.
     * 
     * @param jsFile the JavaScript file to evaluate
     * @return the result of the evaluation
     */
    default Object evalFile(File jsFile) {
        return evalFile(jsFile, JsFileType.NORMAL);
    }

    /**
     * Evaluates the given JavaScript file with a specified type.
     * 
     * @param jsFile the JavaScript file to evaluate
     * @param type the type of the script
     * @return the result of the evaluation
     */
    default Object evalFile(File jsFile, JsFileType type) {
        return evalFile(jsFile, type, null);
    }

    /**
     * Evaluates the given JavaScript file with a specified type and content.
     * 
     * @param jsFile the JavaScript file to evaluate
     * @param type the type of the script
     * @param content the content of the file
     * @return the result of the evaluation
     */
    default Object evalFile(File jsFile, JsFileType type, String content) {
        throw new NotImplementedException(this);
    }

    /**
     * Calls the given function with the specified arguments.
     * 
     * @param function the function to call
     * @param args the arguments to pass to the function
     * @return the result of the function call
     */
    Object call(Object function, Object... args);

    /**
     * Checks if the given object is an array.
     * 
     * @param object the object to check
     * @return true if the object is an array, false otherwise
     */
    boolean isArray(Object object);

    /**
     * Checks if the given object is a number.
     * 
     * @param object the object to check
     * @return true if the object is a number, false otherwise
     */
    boolean isNumber(Object object);

    /**
     * Checks if the given object has members.
     * 
     * @param object the object to check
     * @return true if the object has members, false otherwise
     */
    boolean isObject(Object object);

    /**
     * Checks if the given object is a string.
     * 
     * @param object the object to check
     * @return true if the object is a string, false otherwise
     */
    boolean isString(Object object);

    /**
     * Checks if the given object is a boolean.
     * 
     * @param object the object to check
     * @return true if the object is a boolean, false otherwise
     */
    boolean isBoolean(Object object);

    /**
     * Checks if the given object can be called (executed).
     * 
     * @param object the object to check
     * @return true if the object can be called, false otherwise
     */
    boolean isFunction(Object object);

    /**
     * Retrieves the values inside the given object (e.g., map, array).
     * 
     * @param object the object to retrieve values from
     * @return a collection of values inside the object
     */
    Collection<Object> getValues(Object object);

    /**
     * Converts an object to the given Java class.
     * 
     * @param <T> the target class type
     * @param object the object to convert
     * @param targetClass the target class
     * @return the converted object
     */
    <T> T convert(Object object, Class<T> targetClass);

    /// Engine related engine-scope operations

    /**
     * Sets the specified value with the specified key in the ENGINE_SCOPE Bindings of the protected context field.
     *
     * @param key the key to set
     * @param value the value to set
     */
    void put(String key, Object value);

    /// Bindings-like operations

    /**
     * Sets the specified value with the specified key in the given bindings object.
     * 
     * @param bindings the bindings object
     * @param key the key to set
     * @param value the value to set
     * @return the previous value associated with the key, or null if there was no mapping for the key
     */
    Object put(Object bindings, String key, Object value);

    /**
     * Removes the specified key from the given bindings object.
     * 
     * @param bindings the bindings object
     * @param key the key to remove
     * @return the value associated with the key, or null if there was no mapping for the key
     */
    Object remove(Object bindings, String key);

    /**
     * Retrieves the set of keys in the given bindings object.
     * 
     * @param bindings the bindings object
     * @return the set of keys in the bindings object
     */
    Set<String> keySet(Object bindings);

    /**
     * Retrieves the value associated with the specified key in the given bindings object.
     * 
     * @param bindings the bindings object
     * @param key the key to retrieve
     * @return the value associated with the key, or null if there was no mapping for the key
     */
    Object get(Object bindings, String key);

    /**
     * Retrieves the value associated with the specified key in the given bindings object and converts it to the target class.
     * 
     * @param <T> the target class type
     * @param bindings the bindings object
     * @param key the key to retrieve
     * @param targetClass the target class
     * @return the converted value associated with the key
     */
    default <T> T get(Object bindings, String key, Class<T> targetClass) {
        return convert(get(bindings, key), targetClass);
    }

    /**
     * Retrieves the value associated with the specified key in the engine scope bindings.
     * 
     * @param key the key to retrieve
     * @return the value associated with the key, or null if there was no mapping for the key
     */
    default Object get(String key) {
        return get(getBindings(), key);
    }

    /**
     * Retrieves the value associated with the specified key in the engine scope bindings and converts it to the target class.
     * 
     * @param <T> the target class type
     * @param key the key to retrieve
     * @param targetClass the target class
     * @return the converted value associated with the key
     */
    default <T> T get(String key, Class<T> targetClass) {
        return get(getBindings(), key, targetClass);
    }

    /**
     * Adds a JavaScript conversion rule for objects that are instances of a given class.
     * 
     * @param <VS> the base class type
     * @param <KS> the specific class type
     * @param key the class to add the rule for
     * @param rule the conversion rule
     */
    <VS, KS extends VS> void addToJsRule(Class<KS> key, Function<VS, Object> rule);

    /**
     * Maps classes to JavaScript conversion rules.
     * 
     * @return the function class map containing the conversion rules
     */
    FunctionClassMap<Object, Object> getToJsRules();

    /**
     * Converts a given Java object to a more compatible type in JavaScript.
     * 
     * New conversion rules can be added with the method {@link #addToJsRule(Class, Function)}.
     * 
     * Conversions currently supported by default:<br>
     * - null to undefined;<br>
     * - Java array to JS array;<br>
     * - Java List to JS array;<br>
     * - Java Set to JS array;<br>
     * - JsonArray to JS array;<br>
     * 
     * @param javaObject the Java object to convert
     * @return the converted JavaScript object
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
     * Retrieves a Throwable if the given object is an error generated by the engine.
     * 
     * @param possibleError the object to check
     * @return an Optional containing the Throwable if the object is an error, or an empty Optional otherwise
     */
    default Optional<Throwable> getException(Object possibleError) {
        throw new NotImplementedException(this);
    }
}
