/**
 * Copyright 2021 SPeCS.
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

package pt.up.fe.specs.jsengine.libs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;

import pt.up.fe.specs.jsengine.JsEngine;
import pt.up.fe.specs.jsengine.JsEngineType;
import pt.up.fe.specs.jsengine.JsEngineWebResources;
import pt.up.fe.specs.util.SpecsIo;

public class JsEsprima {

    private static final ThreadLocal<JsEngine> ESPRIMA_ENGINE = ThreadLocal
            .withInitial(JsEsprima::newBabelEngine);

    private static final Set<String> IGNORE_KEYS = new HashSet<>(Arrays.asList("type", "loc", "comments"));

    private static JsEngine newBabelEngine() {
        // Create JsEngine
        var engine = JsEngineType.GRAALVM.newEngine();

        // Get Babel source code
        var babelSource = JsEngineWebResources.ESPRIMA.writeVersioned(SpecsIo.getTempFolder("specs_js-engine"),
                JsEsprima.class);

        // Load babel
        engine.eval(SpecsIo.read(babelSource.getFile()));

        return engine;
    }

    private static JsEngine getEngine() {
        return ESPRIMA_ENGINE.get();
    }

    public static Map<String, Object> parse(String jsCode) {
        return parse(jsCode, "<unknown source>");
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> parse(String jsCode, String path) {
        // Escape back-ticks
        var normalizedCode = jsCode.replace("`", "\\`");
        var engine = getEngine();

        var result = engine
                .eval("code = `" + normalizedCode + "`; "
                        + "var ast = esprima.parse(code, {loc:true,comment:true}); "
                        + "var string = JSON.stringify(ast);"
                        + "string;")
                .toString();

        // SpecsIo.write(new File("js.json"), result);

        return new Gson().fromJson(result, Map.class);
        // @SuppressWarnings("unchecked")
        // var astRoot = (Map<String, Object>) engine.toJava(result);

        // return astRoot;
        // GenericDataNode astRoot = convert(result, engine);

        // System.out.println("AST: " + astRoot);
        // var ast = engine.convert(result, String.class);
        //
        // // String stringAst = (String) javascriptEngine.get("string");
        // JsonParser jsonParser = new JsonParser();
        //
        // JsonElement jsonTree = jsonParser.parse(ast);
        // JsonObject program = jsonTree.getAsJsonObject();
        // program.addProperty("path", path);
        //
        // System.out.println("RESUT: " + program);

    }

    public static List<Map<String, Object>> getChildren(Map<String, Object> node) {
        var children = new ArrayList<Map<String, Object>>();

        for (var entry : node.entrySet()) {

            // Ignore certain keys
            if (IGNORE_KEYS.contains(entry.getKey())) {
                continue;
            }

            var value = entry.getValue();

            // If a Map, consider a child
            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                var child = (Map<String, Object>) value;
                children.add(child);
                continue;
            }

            // If not a list, ignore
            if (!(value instanceof List)) {
                continue;
            }

            @SuppressWarnings("unchecked")
            var partialChildren = (List<Map<String, Object>>) value;

            // If it has a child, check if Map
            if (!partialChildren.isEmpty() && !(partialChildren.get(0) instanceof Map)) {
                continue;
            }

            // Assume they are children
            children.addAll(partialChildren);
        }

        return children;
    }

    public static List<Map<String, Object>> getDescendants(Map<String, Object> node) {
        return getDescendantsStream(node).collect(Collectors.toList());
    }

    public static Stream<Map<String, Object>> getChildrenStream(Map<String, Object> node) {
        return JsEsprima.getChildren(node).stream();
    }

    public static Stream<Map<String, Object>> getDescendantsStream(Map<String, Object> node) {
        return JsEsprima.getChildrenStream(node)
                .flatMap(c -> JsEsprima.getDescendantsAndSelfStream(c));
    }

    public static Stream<Map<String, Object>> getDescendantsAndSelfStream(Map<String, Object> node) {
        return Stream.concat(Stream.of(node), getDescendantsStream(node));
    }

    // private static GenericDataNode convert(Object result, JsEngine engine) {
    // System.out.println("Keys: " + engine.keySet(result));
    //
    // // Create node
    // var node = new GenericDataNode();
    // var children = new ArrayList<GenericDataNode>();
    //
    // for (var key : engine.keySet(result)) {
    // var value = engine.get(result, key);
    //
    // // Boolean
    //
    // System.out.println("KEY: " + key + "; Value: " + value);
    // }
    //
    // // TODO Auto-generated method stub
    // return null;
    // }
}
