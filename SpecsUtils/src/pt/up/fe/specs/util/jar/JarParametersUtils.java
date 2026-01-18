/**
 * Copyright 2012 SPeCS Research Group.
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

package pt.up.fe.specs.util.jar;

/**
 * The purpose of this class is to provide to the user some methods helping to
 * manage the parameters while running an application (.jar, .exe...).
 * 
 * @author remi
 *
 */
public class JarParametersUtils {

    /** The values the arguments can have for requiring help. */
    private static final String[] HELP_ARG = { "-help", "-h", ".?", "/?", "?" };

    /**
     * Returns true if the argument represents an help requirement (value "-help",
     * "-h", "/?"...). Returns false
     * otherwise.
     * 
     * @param help The string the user wants to know if it is an help requirement.
     * @return true if the argument represents an help requirement (value "-help",
     *         "-h", "/?"...). Returns false
     *         otherwise.
     */
    public static boolean isHelpRequirement(String help) {
        for (String help_arg : JarParametersUtils.HELP_ARG) {
            if (help.equals(help_arg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the String "for any help > 'className' -help".
     * 
     * @param jarName The name the .jar file the help is required in.
     * @return the String "for any help > 'jarName' -help".
     */
    public static String askForHelp(String jarName) {
        return "for any help > " + jarName + " " + JarParametersUtils.HELP_ARG[0];
    }

}
