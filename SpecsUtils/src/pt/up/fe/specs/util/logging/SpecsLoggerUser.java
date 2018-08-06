/**
 * Copyright 2018 SPeCS.
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

package pt.up.fe.specs.util.logging;

import java.util.function.Supplier;

public interface SpecsLoggerUser extends TagLoggerUser<SpecsLoggerTag> {

    static final EnumLogger<SpecsLoggerTag> SPECS_LOGGER = EnumLogger.newInstance(SpecsLoggerTag.class);

    public static EnumLogger<SpecsLoggerTag> getLogger() {
        return SPECS_LOGGER;
    }

    @Override
    default EnumLogger<SpecsLoggerTag> logger() {
        return SPECS_LOGGER;
    }

    public static void deprecated(String message) {
        SPECS_LOGGER.info(SpecsLoggerTag.DEPRECATED, message);
    }

    public static void info(SpecsLoggerTag tag, String message) {
        SPECS_LOGGER.info(tag, message);
    }

    public static void info(String message) {
        SPECS_LOGGER.info(message);
    }

    public static void info(Supplier<String> message) {
        info(message.get());
    }

}
