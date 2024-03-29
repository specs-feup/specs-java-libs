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

import java.util.Map;

import com.google.gson.Gson;

import pt.up.fe.specs.jsengine.JsEngine;
import pt.up.fe.specs.jsengine.JsEngineType;
import pt.up.fe.specs.jsengine.JsEngineWebResources;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

public class JsEsprima {

    private static final ThreadLocal<JsEngine> ESPRIMA_ENGINE = ThreadLocal
            .withInitial(JsEsprima::newEsprimaEngine);

    private static JsEngine newEsprimaEngine() {
        // Create JsEngine
        var engine = JsEngineType.GRAALVM.newEngine();

        // Get Esprima source code
        var esprimaSource = JsEngineWebResources.ESPRIMA.writeVersioned(SpecsIo.getTempFolder("specs_js-engine"),
                JsEsprima.class);

        // Load Esprima
        engine.eval(SpecsIo.read(esprimaSource.getFile()));

        // Load parse function
        engine.eval(
                "function parse(code) {var ast = esprima.parse(code, {loc:true,comment:true}); return JSON.stringify(ast);}");

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
        var engine = getEngine();

        // Obtain JSON string representing the AST
        var parseFunction = engine.get("parse");

        var result = "";

        try {
            result = engine.call(parseFunction, jsCode).toString();
        } catch (Exception e) {
            // If there is a problem during parsing, try transpiling to an older version of EcmaScript
            SpecsLogs.info("Parsing with Esprima failed, transpiling with Babel and trying again");
            var es5Code = JsBabel.toES6(jsCode);

            result = engine.call(parseFunction, es5Code).toString();
        }

        var program = new EsprimaNode(new Gson().fromJson(result, Map.class));

        associateComments(program);

        return program;
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

}
