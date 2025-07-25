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

/**
 * Utility class for working with the Esprima JavaScript parser.
 */
public class JsEsprima {

    private static final ThreadLocal<JsEngine> ESPRIMA_ENGINE = ThreadLocal
            .withInitial(JsEsprima::newEsprimaEngine);

    /**
     * Creates a new instance of the Esprima JavaScript engine.
     * 
     * @return a new JsEngine instance configured with Esprima
     */
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

    /**
     * Retrieves the current thread-local instance of the Esprima engine.
     * 
     * @return the current JsEngine instance
     */
    private static JsEngine getEngine() {
        return ESPRIMA_ENGINE.get();
    }

    /**
     * Parses the given JavaScript code and returns the corresponding AST.
     * 
     * @param jsCode the JavaScript code to parse
     * @return the root node of the parsed AST
     */
    public static EsprimaNode parse(String jsCode) {
        return parse(jsCode, "<unknown source>");
    }

    /**
     * Parses the given JavaScript code and returns the corresponding AST, associating it with the provided source path.
     * 
     * @param jsCode the JavaScript code to parse
     * @param path the source path associated with the code
     * @return the root node of the parsed AST
     */
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

    /**
     * Associates comments with the corresponding nodes in the AST.
     * 
     * @param program the root node of the AST
     */
    private static void associateComments(EsprimaNode program) {
        var comments = program.getComments();

        if (comments.isEmpty()) {
            return;
        }

        var nodes = program.getDescendants();

        var commentsIterator = comments.iterator();

        var currentComment = commentsIterator.next();

        NODES: for (var node : nodes) {
            var nodeLoc = node.getLoc();

            // If node start line is the same or greater than the comment, associate node with comment
            while (nodeLoc.getStartLine() >= currentComment.getLoc().getStartLine()) {
                node.setComment(currentComment);

                if (!commentsIterator.hasNext()) {
                    break NODES;
                }

                currentComment = commentsIterator.next();
            }
        }
    }

}
