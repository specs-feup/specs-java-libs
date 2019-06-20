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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import javax.script.Bindings;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;

import pt.up.fe.specs.jsengine.ForOfType;
import pt.up.fe.specs.jsengine.JsEngine;

public class GraalvmJsEngine implements JsEngine {

    private static final String NEW_ARRAY = "[]"; // Faster

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

        this.engine = GraalJSScriptEngine.create(null, contextBuilder);

    }

    private Context.Builder createBuilder() {
        Context.Builder contextBuilder = Context.newBuilder("js")
                // .allowAllAccess(true)
                .allowHostAccess(HostAccess.ALL)
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

    @Override
    public GraalJSScriptEngine getEngine() {
        return engine;
    }

    @Override
    public ForOfType getForOfType() {
        return ForOfType.NATIVE;
    }

    @Override
    public boolean supportsModifyingThis() {
        return true;
        // if (nashornCompatibility) {
        // return true;
        // }
        //
        // return false;
    }

    public Value eval(String code) {
        return engine.getPolyglotContext().eval("js", code);
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

    public Bindings newNativeArray() {
        return asBindings(eval(NEW_ARRAY));
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
    public Bindings toNativeArray(Object[] values) {
        Value array = eval(NEW_ARRAY);
        for (int i = 0; i < values.length; i++) {
            array.putMember("" + i, values[i]);
        }
        return asBindings(array);
    }

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
     * This implementation is slow, since Graal does not support sharing Contexts between engines.
     * 
     * <p>
     * A new engine will be created, and the contents of the given scope will be converted to code and loaded into the
     * new engine, before executing the code.
     */
    @Override
    public Object eval(String code, Bindings scope) {

        // var newEngine = GraalJSScriptEngine.create(engine.getPolyglotEngine(), contextBuilder);
        // Context context = Context.create("js");
        // Context context = Context.newBuilder("js").build();
        Context context = createBuilder().build();
        // Context context = contextBuilder.build();

        // Value b = newEngine.getPolyglotContext().getBindings("js");
        // Value b = asValue(newEngine.getBindings(ScriptContext.ENGINE_SCOPE));
        // Value b = asValue(newEngine.getContext().getBindings(ScriptContext.ENGINE_SCOPE));
        // try {
        // System.out.println("typeof a: " + newEngine.eval("typeof a"));
        // b.putMember("a", 10);
        // System.out.println("typeof a 2: " + newEngine.eval("typeof a"));
        // } catch (ScriptException e) {
        // throw new RuntimeException(e);
        // }

        // Value b = context.getBindings("js");
        //
        // System.out.println("typeof a: " + context.eval("js", "typeof foo"));
        // b.putMember("foo", 10);
        // System.out.println("typeof a 2: " + context.eval("js", "foo"));

        // Context context = newEngine.getPolyglotContext();
        Value jsBindings = context.getBindings("js");

        // GraalvmJsEngine newEngine = new GraalvmJsEngine();
        // Value newBindings = newEngine.engine.getPolyglotContext().getBindings("js");
        // Value newBindings = asValue(newEngine.engine.getBindings(ScriptContext.ENGINE_SCOPE));

        Value scopeValue = asValue(scope);

        // Add scope code
        // System.out.println("SCOPE BEFORE: " + jsBindings.getMemberKeys());
        for (String key : scopeValue.getMemberKeys()) {
            // System.out.println("KEY: " + key);
            Value value = scopeValue.getMember(key);

            // If value is undefined, set the key as undefined
            if (value.isNull()) {
                context.eval("js", key + " = undefined");
                continue;
            }
            // System.out.println("VALUE: " + value);

            // Otherwise, add the value
            jsBindings.putMember(key, value);
            // System.out.printlsn("COULD PUT");
        }
        // System.out.println("SCOPE AFTER: " + jsBindings.getMemberKeys());
        // System.out.println("CODE: " + code);
        // Execute new code
        Value result = context.eval("js", code);
        // System.out.println("RESULT: " + result);
        return result;

        // GraalvmJsEngine newEngine = new GraalvmJsEngine();
        // Value scopeValue = asValue(scope);
        //
        // // Add scope code
        // for (String key : scopeValue.getMemberKeys()) {
        // newEngine.eval(key + " = " + stringify(scopeValue.getMember(key)));
        // }
        //
        // // Execute new code
        // return newEngine.eval(code);

        // newEngine.getPolyglotContext().
        // var newScriptContext = new SimpleScriptContext();
        // newScriptContext.setBindings(scope, ScriptContext.ENGINE_SCOPE);
        // try {
        // return newEngine.eval(code, newScriptContext);
        // } catch (ScriptException e) {
        // throw new RuntimeException(e);
        // }
        // newEngine.getEngine().setBindings(scope, ScriptContext.ENGINE_SCOPE);
        // return newEngine.eval(code);
        // Bindings previousBindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        // engine.setBindings(scope, ScriptContext.ENGINE_SCOPE);
        // Value result = eval(code);
        // engine.setBindings(previousBindings, ScriptContext.ENGINE_SCOPE);
        // return result;
    }

    public Value asValue(Object object) {
        if (object instanceof GraalvmBindings) {
            return ((GraalvmBindings) object).getValue();
        }

        return engine.getPolyglotContext().asValue(object);
    }

    @Override
    public Bindings asBindings(Object value) {
        if (value instanceof GraalvmBindings) {
            return (Bindings) value;
        }

        return new GraalvmBindings(this, asValue(value).as(Bindings.class));
    }

    @Override
    public boolean asBoolean(Object value) {
        return asValue(value).asBoolean();
    }

}
