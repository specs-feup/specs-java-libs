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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import javax.script.Bindings;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import com.oracle.truffle.polyglot.SpecsPolyglot;

import pt.up.fe.specs.jsengine.ForOfType;
import pt.up.fe.specs.jsengine.JsEngine;
import pt.up.fe.specs.jsengine.JsEngineResource;
import pt.up.fe.specs.jsengine.JsFileType;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

public class GraalvmJsEngine implements JsEngine {

    private static final String NEW_ARRAY = "[]"; // Faster
    private static final String NEW_MAP = "a = {}; a";

    private final GraalJSScriptEngine engine;
    // private final Context.Builder contextBuilder;
    private final Set<String> forbiddenClasses;
    private final boolean nashornCompatibility;

    public GraalvmJsEngine(Collection<Class<?>> blacklistedClasses) {
        this(blacklistedClasses, false);
    }

    public GraalvmJsEngine(Collection<Class<?>> blacklistedClasses, boolean nashornCompatibility) {

        this.forbiddenClasses = blacklistedClasses.stream().map(Class::getName).collect(Collectors.toSet());
        this.nashornCompatibility = nashornCompatibility;

        Context.Builder contextBuilder = createBuilder();
        // System.out.println("CLASS LOADER: " + GraalvmJsEngine.class.getClassLoader());
        // Thread.currentThread().setContextClassLoader(classLoader);
        this.engine = GraalJSScriptEngine.create(null, contextBuilder);

        // Load Java compatibility layer
        eval(JsEngineResource.JAVA_COMPATIBILITY.read());

        // List<ScriptEngineFactory> engines = (new ScriptEngineManager()).getEngineFactories();
        // System.out.println("Available Engines");
        // for (ScriptEngineFactory f : engines) {
        // System.out.println(f.getLanguageName() + " " + f.getEngineName() + " " + f.getNames().toString());
        // }
    }

    private Context.Builder createBuilder() {

        Context.Builder contextBuilder = Context.newBuilder("js")
                .allowAllAccess(true)
                .allowHostAccess(HostAccess.ALL)
                // .option("js.load-from-url", "true")
                // .allowIO(true)
                // .allowCreateThread(true)
                // .allowNativeAccess(true)
                // .allowPolyglotAccess(PolyglotAccess.ALL)
                .allowHostClassLookup(name -> !forbiddenClasses.contains(name));

        if (this.nashornCompatibility) {
            contextBuilder.allowExperimentalOptions(true).option("js.nashorn-compat", "true");
        }
        return contextBuilder;
    }

    public GraalvmJsEngine() {
        this(Collections.emptyList());
    }

    // @Override
    // public GraalJSScriptEngine getEngine() {
    // return engine;
    // }

    @Override
    public ForOfType getForOfType() {
        return ForOfType.NATIVE;
    }

    @Override
    public Object eval(String code, JsFileType type) {
        switch (type) {
        case NORMAL:
            return eval(code);
        case MODULE:
            try {
                return eval(Source.newBuilder("js", new StringBuilder(code), "loaded_js_resource")
                        .mimeType("application/javascript+module").build());
            } catch (IOException e) {
                throw new RuntimeException("Could not load JS code", e);
            }
        default:
            throw new NotImplementedException(type);
        }

    }

    @Override
    public Value eval(String code) {
        try {
            return eval(Source.newBuilder("js", new StringBuilder(code), "loaded_js_resource").build());
        } catch (IOException e) {
            throw new RuntimeException("Could not load JS code", e);
        }
    }

    private Value eval(Source code) {
        try {
            // Value value = asValue(engine.eval(code));
            // Value value = engine.getPolyglotContext().eval("js", code);
            Value value = engine.getPolyglotContext().eval(code);

            // if (value.hasMembers() || value.hasArrayElements()) {
            // return asBindings(value);
            // }

            return value;

        } catch (PolyglotException e) {
            // System.out.println("CUASE: " + e.getCause());
            // System.out.println("Is host ex? " + ((PolyglotException) e).isHostException());
            // System.out.println("Is guest ex? " + ((PolyglotException) e).isGuestException());
            if (e.isHostException()) {
                Throwable hostException = null;
                try {
                    hostException = e.asHostException();
                } catch (Exception unreachableException) {
                    // Will not take this branch since it is a host exception
                    throw new RuntimeException("Should not launch this exception", unreachableException);
                }

                // System.out.println("PE:" + pe.getClass());
                // System.out.println("PE CAUSE: " + pe.getCause());
                // e.getPolyglotStackTrace();
                // pe.printStackTrace();
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

    // @SuppressWarnings("unchecked")
    // public Map<String, Object> evalOld(String code) {
    //
    // try {
    // return (Map<String, Object>) engine.eval(code);
    // } catch (ScriptException e) {
    // throw new RuntimeException("Could not execute code: '" + code + "'", e);
    // }
    // }

    @Override
    public Object newNativeArray() {
        return eval(NEW_ARRAY);
        // return new GenericBindings(evalOld(NEW_ARRAY));
        // try {
        // Map<String, Object> array = (Map<String, Object>) engine.eval(NEW_ARRAY);
        //
        // return new GenericBindings(array);
        // } catch (ScriptException e) {
        // throw new DefaultLARAException("Could not create new array ", e);
        // }
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

        // System.out.println("ARRAY: " + array);
        // System.out.println("AS BINDINGS: " + asBindings(array));
        // var bindings = asBindings(array);
        // System.out.println("ARRAY: " + array);
        // System.out.println("BINDINGS: " + bindings);

        // return bindings;
    }

    // public Object toNativeArrayV2(Object[] values) {
    // Value array = eval(NEW_ARRAY);
    // for (int i = 0; i < values.length; i++) {
    // array.setArrayElement(i, values[i]);
    // }
    //
    // return array;
    // }

    /**
     * Based on this site: http://programmaticallyspeaking.com/nashorns-jsobject-in-context.html
     *
     * @return
     */
    @Override
    public Object getUndefined() {
        var array = engine.getPolyglotContext().eval("js", "[undefined]");

        return array.getArrayElement(0);
    }

    @Override
    public String stringify(Object object) {
        Value json = eval("JSON");

        return json.invokeMember("stringify", object).toString();
        // return json.invokeMember("stringify", asValue(object).asProxyObject()).toString();
    }

    @Override
    public Value getBindings() {
        // return asValue(engine.getBindings(ScriptContext.ENGINE_SCOPE));
        return engine.getPolyglotContext().getBindings("js");
        // return engine.getPolyglotContext().getPolyglotBindings();
    }

    // @Override
    // public Object put(Bindings object, String member, Object value) {
    // Value valueObject = asValue(object);
    // Value previousValue = valueObject.getMember(member);
    // valueObject.putMember(member, value);
    // return previousValue;
    // }
    //
    // @Override
    // public Object remove(Bindings object, Object key) {
    // Value valueObject = asValue(object);
    // Value previousValue = valueObject.getMember(key.toString());
    // valueObject.removeMember(key.toString());
    // return previousValue;
    // }

    /**
     * Adds the members in the given scope before evaluating the code.
     */
    @Override
    public Object eval(String code, Object scope) {

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
        Value result = eval(code);

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

    // /**
    // * This implementation is slow, since Graal does not support sharing Contexts between engines.
    // *
    // * <p>
    // * A new engine will be created, and the contents of the given scope will be converted to code and loaded into the
    // * new engine, before executing the code.
    // */
    // public Object evalOld(String code, Object scope) {
    // // try {
    // // engine.getPolyglotContext().eval("js", code);
    // //
    // //
    // // System.out.println("ENGINE CONTEXT: " + engine.getPolyglotContext());
    // // return engine.eval(code, asBindings(scope));
    // // } catch (ScriptException e) {
    // // throw new RuntimeException("Could not evaluate script with custom scope:\n" + code, e);
    // // }
    //
    // // var newEngine = GraalJSScriptEngine.create(engine.getPolyglotEngine(), contextBuilder);
    // // Context context = Context.create("js");
    // // Context context = Context.newBuilder("js").build();
    // Context context = createBuilder().build();
    // // Context context = engine.getPolyglotContext();
    //
    // // Context context = contextBuilder.build();
    //
    // // Value b = newEngine.getPolyglotContext().getBindings("js");
    // // Value b = asValue(newEngine.getBindings(ScriptContext.ENGINE_SCOPE));
    // // Value b = asValue(newEngine.getContext().getBindings(ScriptContext.ENGINE_SCOPE));
    // // try {
    // // System.out.println("typeof a: " + newEngine.eval("typeof a"));
    // // b.putMember("a", 10);
    // // System.out.println("typeof a 2: " + newEngine.eval("typeof a"));
    // // } catch (ScriptException e) {
    // // throw new RuntimeException(e);
    // // }
    //
    // // Value b = context.getBindings("js");
    // //
    // // System.out.println("typeof a: " + context.eval("js", "typeof foo"));
    // // b.putMember("foo", 10);
    // // System.out.println("typeof a 2: " + context.eval("js", "foo"));
    //
    // // Context context = newEngine.getPolyglotContext();
    // Value jsBindings = context.getBindings("js");
    //
    // // GraalvmJsEngine newEngine = new GraalvmJsEngine();
    // // Value newBindings = newEngine.engine.getPolyglotContext().getBindings("js");
    // // Value newBindings = asValue(newEngine.engine.getBindings(ScriptContext.ENGINE_SCOPE));
    //
    // Value scopeValue = asValue(scope);
    //
    // // Add scope code
    // // System.out.println("SCOPE BEFORE: " + jsBindings.getMemberKeys());
    // for (String key : scopeValue.getMemberKeys()) {
    // // System.out.println("KEY: " + key);
    // Value value = scopeValue.getMember(key);
    //
    // // If value is undefined, set the key as undefined
    // if (value.isNull()) {
    // context.eval("js", key + " = undefined");
    // continue;
    // }
    // // System.out.println("VALUE: " + value);
    //
    // // Otherwise, add the value
    // jsBindings.putMember(key, value);
    // // System.out.printlsn("COULD PUT");
    // }
    // // System.out.println("SCOPE AFTER: " + jsBindings.getMemberKeys());
    // // System.out.println("CODE: " + code);
    // // Execute new code
    // Value result = context.eval("js", code);
    // // System.out.println("RESULT: " + result);
    // return result;
    //
    // // GraalvmJsEngine newEngine = new GraalvmJsEngine();
    // // Value scopeValue = asValue(scope);
    // //
    // // // Add scope code
    // // for (String key : scopeValue.getMemberKeys()) {
    // // newEngine.eval(key + " = " + stringify(scopeValue.getMember(key)));
    // // }
    // //
    // // // Execute new code
    // // return newEngine.eval(code);
    //
    // // newEngine.getPolyglotContext().
    // // var newScriptContext = new SimpleScriptContext();
    // // newScriptContext.setBindings(scope, ScriptContext.ENGINE_SCOPE);
    // // try {
    // // return newEngine.eval(code, newScriptContext);
    // // } catch (ScriptException e) {
    // // throw new RuntimeException(e);
    // // }
    // // newEngine.getEngine().setBindings(scope, ScriptContext.ENGINE_SCOPE);
    // // return newEngine.eval(code);
    // // Bindings previousBindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
    // // engine.setBindings(scope, ScriptContext.ENGINE_SCOPE);
    // // Value result = eval(code);
    // // engine.setBindings(previousBindings, ScriptContext.ENGINE_SCOPE);
    // // return result;
    //
    // }

    public Value asValue(Object object) {
        if (object instanceof GraalvmBindings) {
            return ((GraalvmBindings) object).getValue();
        }

        return engine.getPolyglotContext().asValue(object);
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

        // return new GraalvmBindings(this, asValue(value).as(Bindings.class));
        return new GraalvmBindings(this.asValue(value));
    }

    @Override
    public boolean asBoolean(Object value) {
        return asValue(value).asBoolean();
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
    public <T> T convert(Object object, Class<T> targetClass) {
        return asValue(object).as(targetClass);
    }

    /// Bindings-like operations

    @Override
    public Object put(Object bindings, String key, Object value) {
        // Value bindingsValue = asValue(bindings);
        //
        // Object previousValue = bindingsValue.getMember(memberName);
        // bindingsValue.putMember(memberName, value);
        // return previousValue;
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
        engine.put(key, value);
    }

    @Override
    public boolean supportsProperties() {
        // TODO: use nashorn compatibility
        return false;
    }

    @Override
    public Optional<Throwable> getException(Object possibleError) {
        var exception = SpecsPolyglot.getException(possibleError);
        /*
            	if(exception != null) {
            		exception.printJSStackTrace();
            	}
          */
        return Optional.ofNullable(exception);
    }

}
