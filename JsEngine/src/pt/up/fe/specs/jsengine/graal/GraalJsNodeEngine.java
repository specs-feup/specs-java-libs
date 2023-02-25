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

package pt.up.fe.specs.jsengine.graal;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.graalvm.polyglot.Value;

import pt.up.fe.specs.jsengine.AJsEngine;
import pt.up.fe.specs.jsengine.ForOfType;
import pt.up.fe.specs.jsengine.JsFileType;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

public class GraalJsNodeEngine extends AJsEngine {
    

    @Override
    public ForOfType getForOfType() {
        throw new NotImplementedException(this);
    }

    @Override
    public Object evalFile(File jsFile) {
        return NodeJS.runJS(() ->
                    NodeJS.eval("(require(" + jsFile.getAbsolutePath().toString() + "))")
                );
    }

    @Override
    public Value eval(String code) {
        throw new NotImplementedException(this);
    }

    @Override
    public Value eval(String code, String source) {
        throw new NotImplementedException(this);
    }

    @Override
    public Object eval(String code, JsFileType type, String source) {
        throw new NotImplementedException(this);
    }

    @Override
    public Object eval(String code, Object scope, JsFileType type, String source) {
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
    public Value toNativeArray(Object[] values) {
        throw new NotImplementedException(this);
    }

    @Override
    public Object getUndefined() {
        throw new NotImplementedException(this);
    }

    @Override
    public String stringify(Object object) {
        throw new NotImplementedException(this);
    }

    @Override
    public Value getBindings() {
        throw new NotImplementedException(this);
    }

    public Value asValue(Object object) {
        throw new NotImplementedException(this);
    }

    @Override
    public boolean asBoolean(Object value) {
        throw new NotImplementedException(this);
    }

    @Override
    public double asDouble(Object value) {
        throw new NotImplementedException(this);
    }

    @Override
    public void nashornWarning(String message) {
        throw new NotImplementedException(this);
    }

    @Override
    public boolean isArray(Object object) {
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
    public boolean isBoolean(Object object) {
        throw new NotImplementedException(this);

    }

    @Override
    public boolean isUndefined(Object object) {
        throw new NotImplementedException(this);
    }

    @Override
    public Collection<Object> getValues(Object object) {
        throw new NotImplementedException(this);
    }

    @Override
    public Object toJava(Object jsValue) {
        throw new NotImplementedException(this);
    }

    @Override
    public <T> T convert(Object object, Class<T> targetClass) {
        throw new NotImplementedException(this);
    }

    /// Bindings-like operations

    @Override
    public Object put(Object bindings, String key, Object value) {
        throw new NotImplementedException(this);
    }

    @Override
    public Object remove(Object bindings, String key) {
        throw new NotImplementedException(this);
    }

    @Override
    public Set<String> keySet(Object bindings) {
        throw new NotImplementedException(this);
    }

    @Override
    public Object get(Object bindings, String key) {
        throw new NotImplementedException(this);
    }

    /// Engine related engine-scope operations

    @Override
    public void put(String key, Object value) {
        throw new NotImplementedException(this);
    }

    @Override
    public boolean supportsProperties() {
        throw new NotImplementedException(this);
    }

    @Override
    public Optional<Throwable> getException(Object possibleError) {
        throw new NotImplementedException(this);
    }

    @Override
    public Object call(Object function, Object... args) {
        throw new NotImplementedException(this);
    }

    @Override
    public boolean isFunction(Object object) {
        throw new NotImplementedException(this);
    }
}
