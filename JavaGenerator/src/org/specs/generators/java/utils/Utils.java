/*
 * Copyright 2013 SPeCS.
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
package org.specs.generators.java.utils;

import org.specs.generators.java.IGenerate;
import org.specs.generators.java.classtypes.ClassType;
import pt.up.fe.specs.util.SpecsIo;

import java.io.File;

public class Utils {

    private static final String INDENTER = "    ";

    // private static final String INDENTER = "\t";

    /**
     * Returns a {@link StringBuilder} containing the desired indentation
     *
     * @param indentation the level of indentation
     * @return {@link StringBuilder} with indentation
     */
    public static StringBuilder indent(int indentation) {
        final StringBuilder indentationBuffer = new StringBuilder();
        for (int i = 0; i < indentation; i++) {
            indentationBuffer.append(Utils.INDENTER);
        }
        return indentationBuffer;
    }

    /**
     * Generates the java class/enum/interface into the requested folder, according to the class' package
     *
     * @param outputDir
     * @param java      the class to generate and write in the output folder
     * @param replace   replace existing file?
     */

    public static boolean generateToFile(File outputDir, ClassType java, boolean replace) {
        final String pack = java.getClassPackage();
        final String name = java.getName();
        final File outputClass = getFilePath(outputDir, pack, name);
        return writeToFile(outputClass, java, replace);
    }

    /**
     * Create the file path according to the package of the class/interface
     *
     * @param outputDir the output directory
     * @param pack      the class/interface package
     * @param name      the class/interface name
     * @return {@link File} containing the new file path
     */
    private static File getFilePath(File outputDir, String pack, String name) {
        final String fileSeparator = System.getProperty("file.separator");
        String filePath = outputDir.getAbsolutePath() + fileSeparator;
        if (!pack.isEmpty()) {
            final String pathPack = pack.replace(".", fileSeparator);
            filePath += pathPack + fileSeparator;
        }
        makeDirs(new File(filePath));
        filePath += name + ".java";
        final File outputClass = new File(filePath);
        return outputClass;
    }

    /**
     * Write the java code in an output file
     *
     * @param outputFile the file destiny of the code
     * @param java       the code to generate and write;
     * @param replace    replace existing file?
     */
    private static boolean writeToFile(File outputFile, IGenerate java, boolean replace) {
        final StringBuilder generatedJava = java.generateCode(0);
        if (replace || !outputFile.exists()) {
            SpecsIo.write(outputFile, generatedJava.toString());
            return true;
        }
        return false;
    }

    public static void makeDirs(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static String firstCharToUpper(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String ln() {
        return "\n";
        //return SpecsIo.getNewline();
    }

}
