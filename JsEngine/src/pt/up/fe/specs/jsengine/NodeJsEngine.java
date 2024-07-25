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

public class NodeJsEngine implements JsEngine {

    public NodeJsEngine() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public <VS, KS extends VS> void addToJsRule(Class<KS> key, Function<VS, Object> rule) {
        throw new NotImplementedException(this);
    }

    @Override
    public boolean asBoolean(Object value) {
        return (boolean) value;
    }

    @Override
    public double asDouble(Object value) {
        return (double) value;
    }

    @Override
    public Object call(Object function, Object... args) {
        throw new NotImplementedException(this);
    }

    @Override
    public <T> T convert(Object object, Class<T> targetClass) {
        // TODO: Implement
        throw new NotImplementedException(this);
    }

    @Override
    public Object eval(String code) {
        // TODO: Implement
        throw new NotImplementedException(this);
    }

    @Override
    public Object eval(String code, JsFileType type, String source) {
        throw new NotImplementedException(this);
    }

    @Override
    public Object eval(String script, Object scope, JsFileType type, String source) {
        throw new NotImplementedException(this);
    }

    @Override
    public Object eval(String code, String source) {
        throw new NotImplementedException(this);
    }

    @Override
    public Object evalFile(File jsFile) {
        throw new NotImplementedException(this);
    }

    @Override
    public Object get(Object bindings, String key) {
        throw new NotImplementedException(this);
    }

    @Override
    public <T> T get(Object bindings, String key, Class<T> targetClass) {
        throw new NotImplementedException(this);
    }

    @Override
    public Object get(String key) {
        throw new NotImplementedException(this);
    }

    @Override
    public <T> T get(String key, Class<T> targetClass) {
        throw new NotImplementedException(this);
    }

    @Override
    public Object getBindings() {
        throw new NotImplementedException(this);
    }

    @Override
    public Optional<Throwable> getException(Object possibleError) {
        throw new NotImplementedException(this);
    }

    @Override
    public ForOfType getForOfType() {
        throw new NotImplementedException(this);
    }

    @Override
    public FunctionClassMap<Object, Object> getToJsRules() {
        throw new NotImplementedException(this);
    }

    @Override
    public Object getUndefined() {
        return UndefinedValue.getUndefined();
    }

    @Override
    public Collection<Object> getValues(Object object) {
        // TODO: Implement
        throw new NotImplementedException(this);
    }

    @Override
    public boolean isArray(Object object) {
        throw new NotImplementedException(this);
    }

    @Override
    public boolean isBoolean(Object object) {
        throw new NotImplementedException(this);
    }

    @Override
    public boolean isFunction(Object object) {
        throw new NotImplementedException(this);
    }

    @Override
    public boolean isNumber(Object object) {
        throw new NotImplementedException(this);
    }

    @Override
    public boolean isObject(Object object) {
        throw new NotImplementedException(this);
    }

    @Override
    public boolean isString(Object object) {
        throw new NotImplementedException(this);
    }

    @Override
    public boolean isUndefined(Object object) {
        throw new NotImplementedException(this);
    }

    @Override
    public Set<String> keySet(Object bindings) {
        throw new NotImplementedException(this);
    }

    @Override
    public void nashornWarning(String message) {
        throw new NotImplementedException(this);
    }

    @Override
    public Object newNativeArray() {
        throw new NotImplementedException(this);
    }

    @Override
    public Object newNativeMap() {
        throw new NotImplementedException(this);
    }

    @Override
    public Object put(Object bindings, String key, Object value) {
        throw new NotImplementedException(this);
    }

    @Override
    public void put(String key, Object value) {
        throw new NotImplementedException(this);
    }

    @Override
    public Object remove(Object bindings, String key) {
        throw new NotImplementedException(this);
    }

    @Override
    public String stringify(Object object) {
        throw new NotImplementedException(this);
    }

    @Override
    public boolean supportsProperties() {
        throw new NotImplementedException(this);
    }

    @Override
    public Object toJava(Object value) {
        throw new NotImplementedException(this);
    }

    @Override
    public Object toJs(Object javaObject) {
        // TODO: Implement
        throw new NotImplementedException(this);
    }

    @Override
    public Object toNativeArray(boolean[] values) {
        return values;
    }

    @Override
    public Object toNativeArray(byte[] values) {
        return values;
    }

    @Override
    public Object toNativeArray(char[] values) {
        return values;
    }

    @Override
    public Object toNativeArray(Collection<? extends Object> values) {
        return values;
    }

    @Override
    public Object toNativeArray(double[] values) {
        return values;
    }

    @Override
    public Object toNativeArray(float[] values) {
        return values;
    }

    @Override
    public Object toNativeArray(int[] values) {
        return values;
    }

    @Override
    public Object toNativeArray(long[] values) {
        return values;
    }

    @Override
    public Object toNativeArray(Object[] values) {
        return values;
    }

    @Override
    public Object toNativeArray(short[] values) {
        return values;
    }

}
