/*
 * Copyright 2011 SPeCS Research Group.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.asm.processor;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;

/**
 * Utility methods related to Registers.
 *
 * @author Joao Bispo
 */
public class RegisterUtils {

    public static String buildRegisterBit(RegisterId regId, int bitPosition) {
        return regId.getName() + RegisterUtils.REGISTER_BIT_START + bitPosition;
    }

    /**
     *
     * <p>
     * Example: if given the string MSR[29], returns 29.
     * 
     * @return the bit position as an Integer, or null if the input is invalid or
     *         null
     */
    public static Integer decodeFlagBit(String registerFlagName) {
        // Handle null input gracefully
        if (registerFlagName == null) {
            SpecsLogs.warn("Cannot decode flag bit from null input");
            return null;
        }

        int beginIndex = registerFlagName.lastIndexOf(RegisterUtils.REGISTER_BIT_START);

        if (beginIndex == -1) {
            SpecsLogs.warn("Flag '" + registerFlagName + "' does not represent "
                    + "a valid flag.");
            return null;
        }

        String bitNumber = registerFlagName.substring(beginIndex + 1);
        return SpecsStrings.parseInteger(bitNumber);
    }

    /**
     * Example: if given the string MSR_29, returns MSR.
     * 
     * Note: For register names containing underscores (e.g.,
     * "COMPLEX_REG_NAME_15"), this method returns everything before the LAST
     * underscore ("COMPLEX_REG_NAME"), which is consistent with decodeFlagBit()
     * that extracts from the last underscore.
     * This allows round-trip operations to work correctly.
     * 
     * @param registerFlagName the flag notation string (e.g., "MSR_29")
     * @return the register name portion, or null if the input is invalid or null
     */
    public static String decodeFlagName(String registerFlagName) {
        if (registerFlagName == null) {
            SpecsLogs.warn("Cannot decode flag name from null input");
            return null;
        }

        int beginIndex = registerFlagName.lastIndexOf(RegisterUtils.REGISTER_BIT_START);
        if (beginIndex == -1) {
            SpecsLogs.warn("Flag '" + registerFlagName + "' does not represent "
                    + "a valid flag.");
            return null;
        }

        // Validate that the bit portion is numeric
        String bitPortion = registerFlagName.substring(beginIndex + 1);
        Integer bitValue = SpecsStrings.parseInteger(bitPortion);
        if (bitValue == null) {
            SpecsLogs.warn("Flag '" + registerFlagName + "' has invalid bit portion: '"
                    + bitPortion + "' is not a valid integer.");
            return null;
        }

        return registerFlagName.substring(0, beginIndex);
    }

    private static final String REGISTER_BIT_START = "_";
}
