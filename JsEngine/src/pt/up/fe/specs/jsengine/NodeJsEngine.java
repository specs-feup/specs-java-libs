/**
 * Copyright 2023 SPeCS.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * under the License.
 */

package pt.up.fe.specs.jsengine;

import pt.up.fe.specs.jsengine.node.UndefinedValue;
import pt.up.fe.specs.util.classmap.FunctionClassMap;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Implementation of a JavaScript engine using Node.js.
 * Provides methods for executing JavaScript code in a Node.js process.
 */
public class NodeJsEngine implements JsEngine {

    /**
     * Constructor for NodeJsEngine.
     */
    public NodeJsEngine() {
    }

    /**
     * Adds a JavaScript rule to the engine.
     *
     * @param key  the class key
     * @param rule the function rule
     * @param <VS> the value type
     * @param <KS> the key type
     */
    @Override
    public <VS, KS extends VS> void addToJsRule(Class<KS> key, Function<VS, Object> rule) {
        throw new NotImplementedException(this);
    }

    /**
     * Converts the given value to a boolean.
     *
     * @param value the value to convert
     * @return the boolean representation of the value
     */
    @Override
    public boolean asBoolean(Object value) {
        return (boolean) value;
    }

    /**
     * Converts the given value to a double.
     *
     * @param value the value to convert
     * @return the double representation of the value
     */
    @Override
    public double asDouble(Object value) {
        return (double) value;
    }

    /**
     * Calls a JavaScript function with the given arguments.
     *
     * @param function the function to call
     * @param args     the arguments to pass to the function
     * @return the result of the function call
     */
    @Override
    public Object call(Object function, Object... args) {
        throw new NotImplementedException(this);
    }

    /**
     * Converts an object to the specified target class.
     *
     * @param object      the object to convert
     * @param targetClass the target class
     * @param <T>         the type of the target class
     * @return the converted object
     */
    @Override
    public <T> T convert(Object object, Class<T> targetClass) {
        throw new NotImplementedException(this);
    }

    /**
     * Evaluates JavaScript code.
     *
     * @param code the JavaScript code to evaluate
     * @return the result of the evaluation
     */
    @Override
    public Object eval(String code) {
        throw new NotImplementedException(this);
    }

    /**
     * Evaluates JavaScript code with the specified type and source.
     *
     * @param code   the JavaScript code to evaluate
     * @param type   the type of the JavaScript file
     * @param source the source of the JavaScript code
     * @return the result of the evaluation
     */
    @Override
    public Object eval(String code, JsFileType type, String source) {
        throw new NotImplementedException(this);
    }

    /**
     * Evaluates JavaScript code with the specified scope, type, and source.
     *
     * @param script the JavaScript code to evaluate
     * @param scope  the scope for the evaluation
     * @param type   the type of the JavaScript file
     * @param source the source of the JavaScript code
     * @return the result of the evaluation
     */
    @Override
    public Object eval(String script, Object scope, JsFileType type, String source) {
        throw new NotImplementedException(this);
    }

    /**
     * Evaluates JavaScript code with the specified source.
     *
     * @param code   the JavaScript code to evaluate
     * @param source the source of the JavaScript code
     * @return the result of the evaluation
     */
    @Override
    public Object eval(String code, String source) {
        throw new NotImplementedException(this);
    }

    /**
     * Evaluates a JavaScript file.
     *
     * @param jsFile the JavaScript file to evaluate
     * @return the result of the evaluation
     */
    @Override
    public Object evalFile(File jsFile) {
        throw new NotImplementedException(this);
    }

    /**
     * Gets a value from the specified bindings using the given key.
     *
     * @param bindings the bindings to retrieve the value from
     * @param key      the key to use for retrieval
     * @return the retrieved value
     */
    @Override
    public Object get(Object bindings, String key) {
        throw new NotImplementedException(this);
    }

    /**
     * Gets a value from the specified bindings using the given key and converts it to the target class.
     *
     * @param bindings    the bindings to retrieve the value from
     * @param key         the key to use for retrieval
     * @param targetClass the target class to convert the value to
     * @param <T>         the type of the target class
     * @return the retrieved and converted value
     */
    @Override
    public <T> T get(Object bindings, String key, Class<T> targetClass) {
        throw new NotImplementedException(this);
    }

    /**
     * Gets a value using the given key.
     *
     * @param key the key to use for retrieval
     * @return the retrieved value
     */
    @Override
    public Object get(String key) {
        throw new NotImplementedException(this);
    }

    /**
     * Gets a value using the given key and converts it to the target class.
     *
     * @param key         the key to use for retrieval
     * @param targetClass the target class to convert the value to
     * @param <T>         the type of the target class
     * @return the retrieved and converted value
     */
    @Override
    public <T> T get(String key, Class<T> targetClass) {
        throw new NotImplementedException(this);
    }

    /**
     * Gets the bindings of the engine.
     *
     * @return the bindings
     */
    @Override
    public Object getBindings() {
        throw new NotImplementedException(this);
    }

    /**
     * Gets the exception from a possible error object.
     *
     * @param possibleError the possible error object
     * @return an optional containing the exception if present
     */
    @Override
    public Optional<Throwable> getException(Object possibleError) {
        throw new NotImplementedException(this);
    }

    /**
     * Gets the type of "for-of" iteration supported by the engine.
     *
     * @return the "for-of" type
     */
    @Override
    public ForOfType getForOfType() {
        throw new NotImplementedException(this);
    }

    /**
     * Gets the rules for converting objects to JavaScript.
     *
     * @return the rules for conversion
     */
    @Override
    public FunctionClassMap<Object, Object> getToJsRules() {
        throw new NotImplementedException(this);
    }

    /**
     * Gets the undefined value.
     *
     * @return the undefined value
     */
    @Override
    public Object getUndefined() {
        return UndefinedValue.getUndefined();
    }

    /**
     * Gets the values of the specified object.
     *
     * @param object the object to retrieve values from
     * @return the collection of values
     */
    @Override
    public Collection<Object> getValues(Object object) {
        throw new NotImplementedException(this);
    }

    /**
     * Checks if the specified object is an array.
     *
     * @param object the object to check
     * @return true if the object is an array, false otherwise
     */
    @Override
    public boolean isArray(Object object) {
        throw new NotImplementedException(this);
    }

    /**
     * Checks if the specified object is a boolean.
     *
     * @param object the object to check
     * @return true if the object is a boolean, false otherwise
     */
    @Override
    public boolean isBoolean(Object object) {
        throw new NotImplementedException(this);
    }

    /**
     * Checks if the specified object is a function.
     *
     * @param object the object to check
     * @return true if the object is a function, false otherwise
     */
    @Override
    public boolean isFunction(Object object) {
        throw new NotImplementedException(this);
    }

    /**
     * Checks if the specified object is a number.
     *
     * @param object the object to check
     * @return true if the object is a number, false otherwise
     */
    @Override
    public boolean isNumber(Object object) {
        throw new NotImplementedException(this);
    }

    /**
     * Checks if the specified object is an object.
     *
     * @param object the object to check
     * @return true if the object is an object, false otherwise
     */
    @Override
    public boolean isObject(Object object) {
        throw new NotImplementedException(this);
    }

    /**
     * Checks if the specified object is a string.
     *
     * @param object the object to check
     * @return true if the object is a string, false otherwise
     */
    @Override
    public boolean isString(Object object) {
        throw new NotImplementedException(this);
    }

    /**
     * Checks if the specified object is undefined.
     *
     * @param object the object to check
     * @return true if the object is undefined, false otherwise
     */
    @Override
    public boolean isUndefined(Object object) {
        throw new NotImplementedException(this);
    }

    /**
     * Gets the set of keys from the specified bindings.
     *
     * @param bindings the bindings to retrieve keys from
     * @return the set of keys
     */
    @Override
    public Set<String> keySet(Object bindings) {
        throw new NotImplementedException(this);
    }

    /**
     * Creates a new native array.
     *
     * @return the new native array
     */
    @Override
    public Object newNativeArray() {
        throw new NotImplementedException(this);
    }

    /**
     * Creates a new native map.
     *
     * @return the new native map
     */
    @Override
    public Object newNativeMap() {
        throw new NotImplementedException(this);
    }

    /**
     * Puts a value into the specified bindings using the given key.
     *
     * @param bindings the bindings to put the value into
     * @param key      the key to use for the value
     * @param value    the value to put
     * @return the previous value associated with the key, or null if there was no mapping
     */
    @Override
    public Object put(Object bindings, String key, Object value) {
        throw new NotImplementedException(this);
    }

    /**
     * Puts a value using the given key.
     *
     * @param key   the key to use for the value
     * @param value the value to put
     */
    @Override
    public void put(String key, Object value) {
        throw new NotImplementedException(this);
    }

    /**
     * Removes a value from the specified bindings using the given key.
     *
     * @param bindings the bindings to remove the value from
     * @param key      the key to use for removal
     * @return the removed value
     */
    @Override
    public Object remove(Object bindings, String key) {
        throw new NotImplementedException(this);
    }

    /**
     * Converts an object to its string representation.
     *
     * @param object the object to stringify
     * @return the string representation of the object
     */
    @Override
    public String stringify(Object object) {
        throw new NotImplementedException(this);
    }

    /**
     * Checks if the engine supports properties.
     *
     * @return true if the engine supports properties, false otherwise
     */
    @Override
    public boolean supportsProperties() {
        throw new NotImplementedException(this);
    }

    /**
     * Converts a JavaScript value to a Java object.
     *
     * @param value the JavaScript value to convert
     * @return the converted Java object
     */
    @Override
    public Object toJava(Object value) {
        throw new NotImplementedException(this);
    }

    /**
     * Converts a Java object to a JavaScript value.
     *
     * @param javaObject the Java object to convert
     * @return the converted JavaScript value
     */
    @Override
    public Object toJs(Object javaObject) {
        return javaObject;
    }

    /**
     * Converts a boolean array to a native array.
     *
     * @param values the boolean array to convert
     * @return the native array
     */
    @Override
    public Object toNativeArray(boolean[] values) {
        return values;
    }

    /**
     * Converts a byte array to a native array.
     *
     * @param values the byte array to convert
     * @return the native array
     */
    @Override
    public Object toNativeArray(byte[] values) {
        return values;
    }

    /**
     * Converts a char array to a native array.
     *
     * @param values the char array to convert
     * @return the native array
     */
    @Override
    public Object toNativeArray(char[] values) {
        return values;
    }

    /**
     * Converts a collection to a native array.
     *
     * @param values the collection to convert
     * @return the native array
     */
    @Override
    public Object toNativeArray(Collection<? extends Object> values) {
        return values;
    }

    /**
     * Converts a double array to a native array.
     *
     * @param values the double array to convert
     * @return the native array
     */
    @Override
    public Object toNativeArray(double[] values) {
        return values;
    }

    /**
     * Converts a float array to a native array.
     *
     * @param values the float array to convert
     * @return the native array
     */
    @Override
    public Object toNativeArray(float[] values) {
        return values;
    }

    /**
     * Converts an int array to a native array.
     *
     * @param values the int array to convert
     * @return the native array
     */
    @Override
    public Object toNativeArray(int[] values) {
        return values;
    }

    /**
     * Converts a long array to a native array.
     *
     * @param values the long array to convert
     * @return the native array
     */
    @Override
    public Object toNativeArray(long[] values) {
        return values;
    }

    /**
     * Converts an object array to a native array.
     *
     * @param values the object array to convert
     * @return the native array
     */
    @Override
    public Object toNativeArray(Object[] values) {
        return values;
    }

    /**
     * Converts a short array to a native array.
     *
     * @param values the short array to convert
     * @return the native array
     */
    @Override
    public Object toNativeArray(short[] values) {
        return values;
    }

}
