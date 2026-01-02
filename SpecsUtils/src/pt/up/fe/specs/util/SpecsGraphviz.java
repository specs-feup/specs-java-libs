/*
 * Copyright 2010 SPeCS Research Group.
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

package pt.up.fe.specs.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.util.lazy.Lazy;

/**
 * Utility methods related to Graphviz graphs.
 *
 * @author Joao Bispo
 */
public class SpecsGraphviz {

    private static final Lazy<Boolean> IS_DOT_AVAILABLE = Lazy.newInstance(SpecsGraphviz::checkDot);

    private static boolean checkDot() {
        var result = SpecsSystem.runProcess(Arrays.asList("dot", "-?"), false, false);
        return result.getReturnValue() == 0;
    }

    public static boolean isDotAvailable() {
        return IS_DOT_AVAILABLE.get();
    }

    public static void renderDot(File dotFile, DotRenderFormat format) {
        var outputFilename = SpecsIo.removeExtension(dotFile) + "." + format.getExtension();
        var outputFile = new File(dotFile.getParentFile(), outputFilename);
        renderDot(dotFile, format, outputFile);
    }

    public static void renderDot(File dotFile, DotRenderFormat format, File outputFile) {
        var command = Arrays.asList("dot", format.getFlag(), dotFile.getAbsolutePath(), "-o",
                outputFile.getAbsolutePath());

        var result = SpecsSystem.runProcess(command, false, false);
        if (result.getReturnValue() == 0) {
            SpecsLogs.debug(() -> "Rendered dot file '" + dotFile.getAbsolutePath() + "' as " + format);
        }
    }

    public static String generateGraph(List<String> declarations, List<String> connections) {
        StringBuilder builder = new StringBuilder();

        builder.append("digraph graphname {\n");
        for (String declaration : declarations) {
            builder.append(declaration);
            builder.append(";\n");
        }
        builder.append("\n");

        for (String connection : connections) {
            builder.append(connection);
            builder.append(";\n");
        }
        builder.append("}");

        return builder.toString();
    }

    /**
     * Shape and Color can be null.
     *
     */
    public static String declaration(String id, String label, String shape,
            String color) {

        // Check Id
        id = checkId(id);

        // Parse label
        label = parseLabel(label);

        StringBuilder builder = new StringBuilder();
        builder.append(id);
        builder.append("[label=\"");
        builder.append(label);
        builder.append("\"");

        if (shape != null) {
            builder.append(", shape=");
            builder.append(shape);
        }

        if (color != null) {
            builder.append(", style=filled fillcolor=\"");
            builder.append(color);
            builder.append("\"");
        }

        builder.append("]");

        return builder.toString();
    }

    /**
     * Label can be null.
     *
     */
    public static String connection(String inputId, String outputId, String label) {

        label = parseLabel(label);

        StringBuilder builder = new StringBuilder();
        builder.append(inputId);
        builder.append(" -> ");
        builder.append(outputId);

        boolean usedOptions = false;
        if (label != null) {
            usedOptions = true;
            builder.append(" [label=\"");
            builder.append(label);
            builder.append("\"");
        }

        if (usedOptions) {
            builder.append("]");
        }

        return builder.toString();
    }

    /**
     * Reads each character, looking for new lines and subtituting them for \n
     *
     */
    public static String parseLabel(String label) {
        return label.replaceAll("\n", "\\\\n");
    }

    /**
     * Removes [ and ] charatect and replaces it by round parenthesis
     *
     */
    public static String formatId(String label) {
        return formatId(label, '0', '0');
    }

    public static String formatId(String label, char leftSquare, char rightSquare) {
        String newLabel = label.replace('[', leftSquare);
        newLabel = newLabel.replace(']', rightSquare);
        return newLabel;
    }

    private static String checkId(String id) {
        if (id.contains("[") || id.contains("]")) {
            System.out.println("Id '" + id + "' has square brackets [], ids cannot have them. Replacing them with 0s");
            return SpecsGraphviz.formatId(id);
        }

        return id;
    }

    public static final String SHAPE_BOX = "box";
    public static final String COLOR_LIGHTBLUE = "lightblue";
    public static final String COLOR_LIGHT_SLATE_BLUE = "lightslateblue";
    public static final String COLOR_GRAY75 = "gray75";
    public static final String COLOR_GREEN = "green";
    public static final String COLOR_GREEN3 = "green3";

}
