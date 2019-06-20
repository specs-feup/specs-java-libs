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

package pt.up.fe.specs.jsengine.nashorn;

import java.util.Collection;
import java.util.Collections;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import pt.up.fe.specs.jsengine.ForOfType;
import pt.up.fe.specs.jsengine.JsEngine;

/**
 * @deprecated uses Nashorn classes, should be replaced with GraalvmJsEngine
 * @author JoaoBispo
 * 
 */
@Deprecated
public class NashornEngine implements JsEngine {

    private static final String NEW_ARRAY = "[]"; // Faster

    private final NashornScriptEngine engine;

    public NashornEngine(Collection<Class<?>> blacklistedClasses) {
        // The presence of a ClassFilter, even if empty, disables Java reflection
        if (blacklistedClasses.isEmpty()) {
            this.engine = (NashornScriptEngine) new NashornScriptEngineFactory().getScriptEngine();
        } else {
            this.engine = (NashornScriptEngine) new NashornScriptEngineFactory()
                    .getScriptEngine(new RestrictModeFilter(blacklistedClasses));
        }

    }

    public NashornEngine() {
        this(Collections.emptyList());
    }

    @Override
    public ScriptEngine getEngine() {
        return engine;
    }

    @Override
    public ForOfType getForOfType() {
        return ForOfType.FOR_EACH;
    }

    @Override
    public boolean supportsModifyingThis() {
        return true;
    }

    @Override
    public Object getBindings() {
        return getEngine().getBindings(ScriptContext.ENGINE_SCOPE);
    }

    @Override
    public Bindings newNativeArray() {
        try {
            return (Bindings) engine.eval(NEW_ARRAY);
        } catch (ScriptException e) {
            throw new RuntimeException("Could not create new array ", e);
        }
    }

    @Override
    public Bindings toNativeArray(Object[] values) {
        Bindings bindings = newNativeArray();
        for (int i = 0; i < values.length; i++) {
            bindings.put("" + i, values[i]);
        }
        return bindings;
    }

    /**
     * Based on this site: http://programmaticallyspeaking.com/nashorns-jsobject-in-context.html
     *
     * @return
     */
    @Override
    public Object getUndefined() {
        try {
            ScriptObjectMirror arrayMirror = (ScriptObjectMirror) engine.eval("[undefined]");
            return arrayMirror.getSlot(0);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String stringify(Object object) {
        ScriptObjectMirror json = (ScriptObjectMirror) eval("JSON");
        return json.callMember("stringify", object).toString();
    }

    @Override
    public Object eval(String script) {
        try {
            return getEngine().eval(script);
        } catch (ScriptException e) {
            throw new RuntimeException("Exception while evaluation code '" + script + "'", e);
        }
    }
    //
    // @Override
    // public Object put(Bindings var, String member, Object value) {
    // return var.put(member, value);
    // }
    //
    // @Override
    // public Object remove(Bindings object, Object key) {
    // return object.remove(key);
    // }

    @Override
    public Object eval(String script, Bindings scope) {
        try {
            return engine.eval(script, (Bindings) scope);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Bindings asBindings(Object value) {
        return (Bindings) value;
    }

    @Override
    public boolean asBoolean(Object result) {
        return (boolean) result;
    }
}
