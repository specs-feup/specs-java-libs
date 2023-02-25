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
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import javax.script.Bindings;
import javax.script.ScriptContext;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.io.FileSystem;

import com.oracle.truffle.api.TruffleFile;
import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import com.oracle.truffle.polyglot.SpecsPolyglot;

import pt.up.fe.specs.jsengine.AJsEngine;
import pt.up.fe.specs.jsengine.ForOfType;
import pt.up.fe.specs.jsengine.JsEngineResource;
import pt.up.fe.specs.jsengine.JsFileType;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

public class GraalvmJsEngine extends AJsEngine {

    private static final String NEW_ARRAY = "[]"; // Faster
    private static final String NEW_MAP = "a = {}; a";

    //private final GraalJSScriptEngine engine;
    private final Context context;
    private final Set<String> forbiddenClasses;
    private final boolean nashornCompatibility;

    public GraalvmJsEngine(Collection<Class<?>> blacklistedClasses) {
        this(blacklistedClasses, false);
    }

    public GraalvmJsEngine(Collection<Class<?>> blacklistedClasses, boolean nashornCompatibility) {
        this(blacklistedClasses, nashornCompatibility, null);
    }

    public GraalvmJsEngine(Collection<Class<?>> blacklistedClasses, boolean nashornCompatibility,
            Path engineWorkingDirectory) {

        this.forbiddenClasses = blacklistedClasses.stream().map(Class::getName).collect(Collectors.toSet());
        this.nashornCompatibility = nashornCompatibility;

        this.context = createContext(engineWorkingDirectory);

        /* var baseEngine = Engine.newBuilder()
                .option("engine.WarnInterpreterOnly", "false")
                // Expose JavaScript debugging interface
                .option("inspect", "9930")
                .build();

        this.engine = GraalJSScriptEngine.create(baseEngine, contextBuilder);
         */

        // Load Java compatibility layer
        eval(JsEngineResource.JAVA_COMPATIBILITY.read());

        // Add rule to ignore polyglot values
        addToJsRule(Value.class, this::valueToJs);

    }

    private Object valueToJs(Value value) {
        // If a host object, convert (is this necessary?)
        if (value.isHostObject()) {
            SpecsLogs.debug(
                    () -> "GraalvmJsEngie.valueToJs(): Case where we have a Value that is a host object, check if ok. "
                            + value.asHostObject().getClass());
            return toJs(value.asHostObject());
        }

        // Otherwise, already converted
        return value;

    }

    private Context createContext(Path engineWorkingDirectory) {
        Context.Builder contextBuilder = Context.newBuilder("js")
                // .allowHostAccess(hostAccess)
                .allowAllAccess(true)
                .allowHostAccess(HostAccess.ALL)
                // Negate access to some dangerous classes
                .allowHostClassLookup(name -> !forbiddenClasses.contains(name))
                // Set JS version
                .option("js.ecmascript-version", "2022")
                //.option("enable-source-maps", "true");
                .option("engine.WarnInterpreterOnly", "false");
                // Expose JavaScript debugging interface
                //.option("inspect", "9930");
        

        if (engineWorkingDirectory != null) {
            contextBuilder.option("js.commonjs-require", "true");
            contextBuilder.option("js.commonjs-require-cwd", engineWorkingDirectory.toString());
        }


        if (this.nashornCompatibility) {
            contextBuilder.allowExperimentalOptions(true).option("js.nashorn-compat", "true");
        }
        return contextBuilder.build();
    }

    public GraalvmJsEngine() {
        this(Collections.emptyList());
    }

    @Override
    public ForOfType getForOfType() {
        return ForOfType.NATIVE;
    }

    @Override
    public Object eval(String code, JsFileType type, String source) {
        // GraalVM documentation indicates that only .mjs files should be loaded as modules
        // https://www.graalvm.org/reference-manual/js/Modules/

        var graalSource = Source.newBuilder("js", new StringBuilder(code), source);

        switch (type) {
        case NORMAL:
            try {
                return eval(graalSource.build());
            } catch (IOException e) {
                throw new RuntimeException("Could not load JS code as script", e);
            }
        case MODULE:
            try {
                return eval(graalSource
                        .mimeType("application/javascript+module")
                        .build());
            } catch (IOException e) {
                throw new RuntimeException("Could not load JS code as module", e);
            }
        default:
            throw new NotImplementedException(type);
        }

    }

    @Override
    public Value eval(String code) {
        return eval(code, "unnamed_js_code");
    }

    @Override
    public Value eval(String code, String source) {

        // var tempFolder = SpecsIo.getTempFolder("temp_js_code");
        // var tempFile = new File(tempFolder, UUID.randomUUID() + ".js");
        // SpecsIo.deleteOnExit(tempFile);
        // SpecsIo.write(tempFile, code);

        try {
            // return eval(Source.newBuilder("js", new StringBuilder(code), tempFile.getAbsolutePath()).build());
            return eval(Source.newBuilder("js", new StringBuilder(code), source)
                    // .mimeType("application/javascript+module")
                    .build());
        } catch (IOException e) {
            throw new RuntimeException("Could not load JS code", e);
        }
    }

    private Value eval(Source code) {
        try {
            return this.context.eval(code);
        } catch (PolyglotException e) {

            if (e.isHostException()) {
                Throwable hostException = null;
                try {
                    hostException = e.asHostException();
                } catch (Exception unreachableException) {
                    // Will not take this branch since it is a host exception
                    throw new RuntimeException("Should not launch this exception", unreachableException);
                }

                hostException.printStackTrace();
                throw new RuntimeException(e.getMessage(), hostException);
            }

            throw new RuntimeException("Polyglot exception while evaluating JavaScript code", e);
        }

        catch (Exception e) {
            // System.out.println("class: " + e.getClass());
            // e.printStackTrace();
            throw new RuntimeException("Could not evaluate JavaScript code", e);
        }
    }

    @Override
    public Object evalFile(File jsFile) {
        /*
        return this.context.eval("js", "module.exports = require('" + jsFile.getAbsolutePath().toString() + "');");
         */
        try {
            return this.context.eval(Source.newBuilder("js", jsFile).build());
        } catch (IOException e) {
            throw new RuntimeException("Could not load main file.", e);
        } catch (final PolyglotException e) {
            // If host exception, convert to it first
            // Usually has more information about the problem that happened
            Throwable t = e.isHostException() ? e.asHostException() : e;
            throw new RuntimeException("Exception when evaluating javascript", t);
        }
    }

    @Override
    public Object newNativeArray() {
        return eval(NEW_ARRAY);
    }

    @Override
    public Object newNativeMap() {
        return eval(NEW_MAP);
    }

    @Override
    public Value toNativeArray(Object[] values) {
        Value array = eval(NEW_ARRAY);
        for (int i = 0; i < values.length; i++) {
            array.setArrayElement(i, toJs(values[i]));
        }

        return array;
    }

    /**
     * Based on this site: http://programmaticallyspeaking.com/nashorns-jsobject-in-context.html
     *
     * @return
     */
    @Override
    public Object getUndefined() {
        var array = this.context.eval("js", "[undefined]");

        return array.getArrayElement(0);
    }

    @Override
    public String stringify(Object object) {
        return eval("JSON").invokeMember("stringify", object).toString();
    }

    @Override
    public Value getBindings() {
        return this.context.getBindings("js");
    }

    /**
     * Adds the members in the given scope before evaluating the code.
     */
    @Override
    public Object eval(String code, Object scope, JsFileType type, String source) {

        Value scopeValue = asValue(scope);

        Map<String, Object> previousValues = new HashMap<>();

        // Add scope code, save previous values
        for (String key : scopeValue.getMemberKeys()) {

            if (asBoolean(eval("typeof variable === 'undefined'"))) {
                previousValues.put(key, null);
            } else {
                previousValues.put(key, eval(key));
            }

            Value value = scopeValue.getMember(key);

            // If value is undefined, set the key as undefined
            if (value.isNull()) {
                eval(key + " = undefined");
                continue;
            }

            // Otherwise, add the value
            put(key, value);
        }

        // Execute new code
        var result = eval(code, type, source);

        // Restore previous values
        for (var entry : previousValues.entrySet()) {
            var value = entry.getValue();
            if (value == null) {
                // Should remove entry or is undefined enough?
                eval(entry.getKey() + " = undefined");
            } else {
                put(entry.getKey(), value);
            }
        }

        return result;
    }

    public Value asValue(Object object) {
        if (object instanceof GraalvmBindings) {
            return ((GraalvmBindings) object).getValue();
        }

        return this.context.asValue(object);
    }

    /**
     * Convenience method to handle JS maps.
     * 
     * @param value
     * @return
     */
    private Bindings asBindings(Object value) {
        if (value instanceof GraalvmBindings) {
            return (Bindings) value;
        }

        return new GraalvmBindings(this.asValue(value));
    }

    @Override
    public boolean asBoolean(Object value) {
        return asValue(value).asBoolean();
    }

    @Override
    public double asDouble(Object value) {
        return asValue(value).asDouble();
    }

    @Override
    public void nashornWarning(String message) {
        SpecsLogs.warn(message);
    }

    @Override
    public boolean isArray(Object object) {
        return asValue(object).hasArrayElements();
    }

    @Override
    public boolean isNumber(Object object) {
        return asValue(object).isNumber();
    }

    @Override
    public boolean isObject(Object object) {
        return asValue(object).hasMembers();

    }

    @Override
    public boolean isString(Object object) {
        return asValue(object).isString();
    }

    @Override
    public boolean isBoolean(Object object) {
        return asValue(object).isBoolean();

    }

    @Override
    public boolean isUndefined(Object object) {
        return asValue(object).isNull();
    }

    @Override
    public Collection<Object> getValues(Object object) {
        var value = asValue(object);

        if (value.isHostObject()) {
            return Arrays.asList(value.asHostObject());
        }

        // // Collection
        // if (object instanceof Collection<?>) {
        // return (Collection<Object>) object;
        // }

        // Array
        if (value.hasArrayElements()) {
            return LongStream.range(0, value.getArraySize())
                    .mapToObj(index -> asValue(value.getArrayElement(index)))
                    .map(elem -> elem.isHostObject() ? (Object) elem.asHostObject() : (Object) elem)
                    .collect(Collectors.toList());
        }

        // Map
        if (value.hasMembers()) {
            return value.getMemberKeys().stream()
                    .map(key -> asValue(value.getMember(key)))
                    .map(elem -> elem.isHostObject() ? (Object) elem.asHostObject() : (Object) elem)
                    .collect(Collectors.toList());
        }

        throw new RuntimeException("Not supported for class '" + object.getClass() + "'");
    }

    @Override
    public Object toJava(Object jsValue) {

        var value = asValue(jsValue);

        // Java object
        if (value.isHostObject()) {
            return value.asHostObject();
        }

        // String
        if (value.isString()) {
            return value.asString();
        }

        // Number
        if (value.isNumber()) {
            return value.asDouble();
        }

        // Boolean
        if (value.isBoolean()) {
            return value.asBoolean();
        }

        // Array
        if (value.hasArrayElements()) {
            return LongStream.range(0, value.getArraySize())
                    .mapToObj(index -> toJava(value.getArrayElement(index)))
                    .collect(Collectors.toList());
        }

        // Map
        if (value.hasMembers()) {
            Map<String, Object> map = new LinkedHashMap<>();

            for (var key : value.getMemberKeys()) {
                map.put(key, toJava(value.getMember(key)));
            }

            return map;
        }

        // null
        if (value.isNull()) {
            return null;
        }

        // Jenkins could not find the symbol getMetaQualifiedName() for some reason
        // throw new NotImplementedException(value.getMetaQualifiedName());
        throw new NotImplementedException("Not implemented for value " + value);
    }

    @Override
    public <T> T convert(Object object, Class<T> targetClass) {
        return asValue(object).as(targetClass);
    }

    /// Bindings-like operations

    @Override
    public Object put(Object bindings, String key, Object value) {
        return asBindings(bindings).put(key, value);
    }

    @Override
    public Object remove(Object bindings, String key) {
        return asBindings(bindings).remove(key);
    }

    @Override
    public Set<String> keySet(Object bindings) {
        return asBindings(bindings).keySet();

    }

    @Override
    public Object get(Object bindings, String key) {
        return asBindings(bindings).get(key);
    }

    /// Engine related engine-scope operations

    @Override
    public void put(String key, Object value) {
        getBindings().putMember(key, value);
    }

    @Override
    public boolean supportsProperties() {
        // TODO: use nashorn compatibility
        return false;
    }

    @Override
    public Optional<Throwable> getException(Object possibleError) {
        var exception = SpecsPolyglot.getException(possibleError);
        return Optional.ofNullable(exception);
    }

    @Override
    public Object call(Object function, Object... args) {
        var functionValue = asValue(function);

        if (!functionValue.canExecute()) {
            throw new RuntimeException("Cannot execute object '" + function.toString() + "'");
        }

        return functionValue.execute(args);
    }

    @Override
    public boolean isFunction(Object object) {
        var functionValue = asValue(object);

        return functionValue.canExecute();
    }
}
