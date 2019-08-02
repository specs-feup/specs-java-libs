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

import static org.junit.Assert.*;

import java.util.Arrays;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptException;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotAccess;
import org.graalvm.polyglot.Value;
import org.junit.Test;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;

import pt.up.fe.specs.util.SpecsLogs;

public class JsEngineTest {

    // private static final Lazy<JsEngine> GRAAL_JS = Lazy.newInstance(() -> JsEngineType.GRAALVM.newEngine());
    // private static final Lazy<JsEngine> NASHORN = Lazy.newInstance(() -> JsEngineType.NASHORN.newEngine());

    private JsEngine getEngine() {
        return JsEngineType.GRAALVM.newEngine();
        // return JsEngineType.NASHORN.newEngine();
        // return GRAAL_JS.get();
    }

    public static void test(Bindings bindings) {
        System.out.println("INSIDE JAVA: " + bindings);
    }

    public static void testValue(Value value) {

        System.out.println("INSIDE JAVA (GRAAL VALUE): " + value);
        value.putMember("aString", "Hello");
        System.out.println("After adding: " + value);
    }

    // @Test
    public void testModifyThis() {
        // Context jsContext = Context.create("js");
        // var value = jsContext.eval("js", "console.log('Hello from the project');"
        // + "Java.type('org.lara.interpreter.test.JsEngineTest');");

        Context.Builder contextBuilder = Context.newBuilder("js")
                .allowAllAccess(true)
                .allowHostAccess(HostAccess.ALL)
                .allowIO(true)
                .allowCreateThread(true)
                .allowNativeAccess(true)
                .allowPolyglotAccess(PolyglotAccess.ALL);
        var engine = GraalJSScriptEngine.create(null, contextBuilder);

        String script;

        // script = "console.log('Hello from the project');"
        // + "Java.type('org.lara.interpreter.test.JsEngineTest');";

        script = "var Accumulator = function() {\n" +
                "    this.value = 0;\n" +
                "};"
                + "Accumulator.prototype.add = function() {\n"
                + "this.value++;"
                + "console.log(this.value);\n"
                + "Java.type('org.lara.interpreter.test.JsEngineTest').testValue(this);"
                + "return this.value;"
                + "}\n"

                + "var acc = new Accumulator();"
                + "acc.add();"
                + "let user = {\r\n" +
                "  get name() {\r\n" +
                "    return this._name;\r\n" +
                "  },\r\n" +
                "\r\n" +
                "  set name(value) {\r\n" +
                "    if (value.length < 4) {\r\n" +
                "      console.log(\"Name is too short, need at least 4 characters\");\r\n" +
                "      return;\r\n" +
                "    }\r\n" +
                "    this._name = value;\r\n" +
                "  }\r\n" +
                "};\r\n" +
                "\r\n" +
                "user.name = \"Pete\";\r\n" +
                "console.log(user.name); // Pete\r\n" +
                "\r\n" +
                "user.name = \"\"; // Name is too short...";

        try {

            var value = engine.eval(script);
            System.out.println("VALUE: " + value);
            Bindings b = engine.getBindings(ScriptContext.ENGINE_SCOPE);
            b.put("acc2", 10);
            System.out.println("BINDINGS: " + b);

        } catch (ScriptException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
        }
    }

    @Test
    public void testNewArray() {
        assertEquals("2,3", getEngine().toNativeArray(Arrays.asList(2, 3)).toString());
        assertEquals("", getEngine().toNativeArray(Arrays.asList()).toString());

        // Bindings array = getEngine().toNativeArray(Arrays.asList("1, 2, 3"));

    }

    @Test
    public void testToString() {
        assertEquals("undefined", getEngine().eval("undefined").toString());
    }

    @Test
    public void testStringify() {
        JsEngine engine = getEngine();
        // System.out.println("RESULT: " + engine.stringify(engine.eval("var test = {'a': 'aString', 'b':10};")));
        assertEquals("{\"a\":\"aString\",\"b\":10}",
                engine.stringify(engine.eval("var test = {a: 'aString', b: 10}; test;")));
    }

    @Test
    public void testEvalWithBindings() {
        JsEngine engine1 = getEngine();

        Object scope = engine1.eval("var a = {aString: 'Hello', aNumber: 10}; a;");

        // assertEquals("Hello", engine1.eval("var b = this.aString; b;", engine1.asBindings(scope)).toString());
        assertEquals("Hello", engine1.eval("var b = aString; b;", engine1.asBindings(scope)).toString());
        // assertEquals("undefined", engine1.eval("var b = this.aString; b;").toString());
        assertEquals("true", engine1.eval("typeof aString === 'undefined'").toString());
    }

    @Test
    public void testUndefined() {
        assertEquals("undefined", getEngine().getUndefined().toString());
    }

    @Test
    public void testIsArray() {
        JsEngine engine = getEngine();

        assertEquals(true, engine.isArray(engine.eval("var a = [0, 1, 2]; a;")));
        assertEquals(false, engine.isArray(engine.eval("var a = {'aa' : 0}; a;")));
        // System.out.println("ARRAY: " + array);
        // assertEquals("undefined", getEngine().getUndefined().toString());
    }

    // @Test
    // public void testEval() {
    // var engine = JsEngineType.GRAALVM.newEngine();
    // // var value = engine.eval("var test = [10]; test;");
    // var value = engine.eval("var test = {a: 10}; test;");
    // System.out.println("VALUE: " + value);
    // System.out.println("VALUE CLASS: " + value.getClass());
    // }

}
