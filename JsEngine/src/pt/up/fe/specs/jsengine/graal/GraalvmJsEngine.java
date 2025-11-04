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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.jsengine.graal;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import javax.script.Bindings;
import javax.script.ScriptException;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Source.Builder;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.io.FileSystem;
import org.graalvm.polyglot.io.IOAccess;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import com.oracle.truffle.polyglot.SpecsPolyglot;

import pt.up.fe.specs.jsengine.AJsEngine;
import pt.up.fe.specs.jsengine.ForOfType;
import pt.up.fe.specs.jsengine.JsFileType;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

/**
 * JavaScript engine implementation using GraalVM.
 * Provides methods for executing JavaScript code in a GraalVM context.
 */
public class GraalvmJsEngine extends AJsEngine {

    private static final String NEW_ARRAY = "[]"; // Faster
    private static final String NEW_MAP = "a = {}; a";

    private final GraalJSScriptEngine engine;
    private final Set<String> forbiddenClasses;
    private final boolean nashornCompatibility;

    /**
     * Constructs a GraalvmJsEngine with the given blacklisted classes.
     *
     * @param blacklistedClasses a collection of classes to blacklist
     */
    public GraalvmJsEngine(Collection<Class<?>> blacklistedClasses) {
        this(blacklistedClasses, false);
    }

    /**
     * Constructs a GraalvmJsEngine with the given blacklisted classes and Nashorn compatibility.
     *
     * @param blacklistedClasses a collection of classes to blacklist
     * @param nashornCompatibility whether Nashorn compatibility is enabled
     */
    public GraalvmJsEngine(Collection<Class<?>> blacklistedClasses, boolean nashornCompatibility) {
        this(blacklistedClasses, nashornCompatibility, null);
    }

    /**
     * Constructs a GraalvmJsEngine with the given blacklisted classes, Nashorn compatibility, and working directory.
     *
     * @param blacklistedClasses a collection of classes to blacklist
     * @param nashornCompatibility whether Nashorn compatibility is enabled
     * @param engineWorkingDirectory the working directory for the engine
     */
    public GraalvmJsEngine(Collection<Class<?>> blacklistedClasses, boolean nashornCompatibility,
            Path engineWorkingDirectory) {
        this(blacklistedClasses, nashornCompatibility, engineWorkingDirectory, null, System.out);
    }

    /**
     * Constructs a GraalvmJsEngine with the given parameters.
     *
     * @param blacklistedClasses a collection of classes to blacklist
     * @param nashornCompatibility whether Nashorn compatibility is enabled
     * @param engineWorkingDirectory the working directory for the engine
     * @param nodeModulesFolder the folder containing node modules
     * @param laraiOutputStream the output stream for the engine
     */
    public GraalvmJsEngine(Collection<Class<?>> blacklistedClasses, boolean nashornCompatibility,
            Path engineWorkingDirectory, File nodeModulesFolder, OutputStream laraiOutputStream) {
        this.forbiddenClasses = blacklistedClasses.stream().map(Class::getName).collect(Collectors.toSet());
        this.nashornCompatibility = nashornCompatibility;

        Context.Builder contextBuilder = createBuilder(engineWorkingDirectory, nodeModulesFolder);

        var baseEngine = Engine.newBuilder()
                .option("engine.WarnInterpreterOnly", "false")
                .build();

        this.engine = GraalJSScriptEngine.create(baseEngine, contextBuilder);

        this.engine.getContext().setWriter(new PrintWriter(laraiOutputStream, true));
        this.engine.getContext().setErrorWriter(new PrintWriter(laraiOutputStream, true));

        try {
            engine.eval("42");
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }

        addToJsRule(Value.class, this::valueToJs);
    }

    /**
     * Converts a GraalVM Value to a JavaScript object.
     *
     * @param value the GraalVM Value
     * @return the JavaScript object
     */
    private Object valueToJs(Value value) {
        if (value.isHostObject()) {
            SpecsLogs.debug(
                    () -> "GraalvmJsEngie.valueToJs(): Case where we have a Value that is a host object, check if ok. "
                            + value.asHostObject().getClass());
            return toJs(value.asHostObject());
        }

        return value;
    }

    /**
     * Creates a Context.Builder for the GraalVM engine.
     *
     * @param engineWorkingDirectory the working directory for the engine
     * @param nodeModulesFolder the folder containing node modules
     * @return the Context.Builder
     */
    private Context.Builder createBuilder(Path engineWorkingDirectory, File nodeModulesFolder) {
        Context.Builder contextBuilder = Context.newBuilder("js")
                .allowAllAccess(true)
                .allowHostAccess(HostAccess.ALL)
                .allowHostClassLookup(name -> !forbiddenClasses.contains(name));

        if (nodeModulesFolder != null) {
            FileSystem fs = FileSystem.newDefaultFileSystem();
            fs.setCurrentWorkingDirectory(nodeModulesFolder.toPath());
            contextBuilder.allowIO(IOAccess.newBuilder().fileSystem(fs).build());
        }

        if (nodeModulesFolder != null) {
            if (!nodeModulesFolder.getName().equals("node_modules")
                    && !(new File(nodeModulesFolder, "node_modules").isDirectory())) {
                throw new RuntimeException(
                        "Given node modules folder is not called node_modules, nor contains a node_modules folder: "
                                + nodeModulesFolder.getAbsolutePath());
            }

            contextBuilder.option("js.commonjs-require", "true");
            contextBuilder.option("js.commonjs-require-cwd", nodeModulesFolder.getAbsolutePath());
        }

        contextBuilder.option("js.ecmascript-version", "2022");

        if (this.nashornCompatibility) {
            contextBuilder.allowExperimentalOptions(true).option("js.nashorn-compat", "true");
        }
        return contextBuilder;
    }

    /**
     * Constructs a GraalvmJsEngine with no blacklisted classes.
     */
    public GraalvmJsEngine() {
        this(Collections.emptyList());
    }

    @Override
    public ForOfType getForOfType() {
        return ForOfType.NATIVE;
    }

    @Override
    public Object eval(String code, JsFileType type, String source) {
        var graalSource = Source.newBuilder("js", new StringBuilder(code), source);
        return eval(graalSource, type);
    }

    /**
     * Evaluates JavaScript code with the specified type.
     *
     * @param graalSource the source builder for the code
     * @param type the type of the JavaScript code
     * @return the result of the evaluation
     */
    private Object eval(Builder graalSource, JsFileType type) {
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
        try {
            return eval(Source.newBuilder("js", new StringBuilder(code), source).build());
        } catch (IOException e) {
            throw new RuntimeException("Could not load JS code", e);
        }
    }

    /**
     * Evaluates JavaScript code from a Source object.
     *
     * @param code the Source object containing the code
     * @return the result of the evaluation
     */
    private Value eval(Source code) {
        try {
            Value value = engine.getPolyglotContext().eval(code);
            return value;
        } catch (PolyglotException e) {
            if (e.isHostException()) {
                Throwable hostException = e.asHostException();
                throw new RuntimeException(e.getMessage(), hostException);
            }
            throw new RuntimeException("Polyglot exception while evaluating JavaScript code", e);
        } catch (Exception e) {
            throw new RuntimeException("Could not evaluate JavaScript code", e);
        }
    }

    @Override
    public Object evalFile(File jsFile, JsFileType type, String content) {
        var builder = Source.newBuilder("js", jsFile);
        if (content != null) {
            builder.content(content);
        }
        return eval(builder, type);
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

    @Override
    public Object getUndefined() {
        var array = engine.getPolyglotContext().eval("js", "[undefined]");
        return array.getArrayElement(0);
    }

    @Override
    public String stringify(Object object) {
        Value json = eval("JSON");
        return json.invokeMember("stringify", object).toString();
    }

    @Override
    public Value getBindings() {
        return engine.getPolyglotContext().getBindings("js");
    }

    @Override
    public Object eval(String code, Object scope, JsFileType type, String source) {
        Value scopeValue = asValue(scope);
        Map<String, Object> previousValues = new HashMap<>();

        for (String key : scopeValue.getMemberKeys()) {
            if (asBoolean(eval("typeof variable === 'undefined'"))) {
                previousValues.put(key, null);
            } else {
                previousValues.put(key, eval(key));
            }

            Value value = scopeValue.getMember(key);
            if (value.isNull()) {
                eval(key + " = undefined");
                continue;
            }
            put(key, value);
        }

        var result = eval(code, type, source);

        for (var entry : previousValues.entrySet()) {
            var value = entry.getValue();
            if (value == null) {
                eval(entry.getKey() + " = undefined");
            } else {
                put(entry.getKey(), value);
            }
        }

        return result;
    }

    /**
     * Converts an object to a GraalVM Value.
     *
     * @param object the object to convert
     * @return the GraalVM Value
     */
    public Value asValue(Object object) {
        if (object instanceof GraalvmBindings) {
            return ((GraalvmBindings) object).getValue();
        }
        return engine.getPolyglotContext().asValue(object);
    }

    /**
     * Converts an object to Bindings.
     *
     * @param value the object to convert
     * @return the Bindings
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

        if (value.hasArrayElements()) {
            return LongStream.range(0, value.getArraySize())
                    .mapToObj(index -> asValue(value.getArrayElement(index)))
                    .map(elem -> elem.isHostObject() ? (Object) elem.asHostObject() : (Object) elem)
                    .collect(Collectors.toList());
        }

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

        if (value.isHostObject()) {
            return value.asHostObject();
        }

        if (value.isString()) {
            return value.asString();
        }

        if (value.isNumber()) {
            return value.asDouble();
        }

        if (value.isBoolean()) {
            return value.asBoolean();
        }

        if (value.hasArrayElements()) {
            return LongStream.range(0, value.getArraySize())
                    .mapToObj(index -> toJava(value.getArrayElement(index)))
                    .collect(Collectors.toList());
        }

        if (value.hasMembers()) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (var key : value.getMemberKeys()) {
                map.put(key, toJava(value.getMember(key)));
            }
            return map;
        }

        if (value.isNull()) {
            return null;
        }

        throw new NotImplementedException("Not implemented for value " + value);
    }

    @Override
    public <T> T convert(Object object, Class<T> targetClass) {
        return asValue(object).as(targetClass);
    }

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

    @Override
    public void put(String key, Object value) {
        engine.put(key, value);
    }

    @Override
    public boolean supportsProperties() {
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
