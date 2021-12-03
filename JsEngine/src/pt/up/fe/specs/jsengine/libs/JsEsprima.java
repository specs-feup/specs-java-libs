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

import java.io.File;
import java.util.Map;

import com.google.gson.Gson;

import pt.up.fe.specs.jsengine.JsEngine;
import pt.up.fe.specs.jsengine.JsEngineType;
import pt.up.fe.specs.jsengine.JsEngineWebResources;
import pt.up.fe.specs.util.SpecsIo;

public class JsEsprima {

    private static final ThreadLocal<JsEngine> ESPRIMA_ENGINE = ThreadLocal
            .withInitial(JsEsprima::newBabelEngine);

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

    public static EsprimaNode parse(String jsCode) {
        return parse(jsCode, "<unknown source>");
    }

    @SuppressWarnings("unchecked")
    public static EsprimaNode parse(String jsCode, String path) {
        // Escape back-ticks
        var normalizedCode = jsCode.replace("`", "\\`");
        var engine = getEngine();

        var result = engine
                .eval("code = `" + normalizedCode + "`; "
                        + "var ast = esprima.parse(code, {loc:true,comment:true}); "
                        + "var string = JSON.stringify(ast);"
                        + "string;")
                .toString();

        SpecsIo.write(new File("js.json"), result);

        var program = new EsprimaNode(new Gson().fromJson(result, Map.class));

        associateComments(program);

        return program;
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

    private static void associateComments(EsprimaNode program) {
        var comments = program.getComments();

        if (comments.isEmpty()) {
            return;
        }

        var nodes = program.getDescendants();

        var commentsIterator = comments.iterator();

        var currentComment = commentsIterator.next();
        // System.out.println("COMMENT LOC: " + currentComment.getLoc());

        NODES: for (var node : nodes) {
            var nodeLoc = node.getLoc();
            // System.out.println("NODE LOC: " + nodeLoc);

            // If node start line is the same or greater than the comment, associate node with comment
            while (nodeLoc.getStartLine() >= currentComment.getLoc().getStartLine()) {
                // System.out.println("FOUND ASSOCIATION: " + node.getType() + " @ " + node.getLoc());
                node.setComment(currentComment);

                if (!commentsIterator.hasNext()) {
                    break NODES;
                }

                currentComment = commentsIterator.next();
                // System.out.println("COMMENT LOC: " + currentComment.getLoc());
            }
        }
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
