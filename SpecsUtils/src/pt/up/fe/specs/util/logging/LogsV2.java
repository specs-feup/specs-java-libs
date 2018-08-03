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

public interface LogsV2 {

    // Class<?> getLoggerClass();

    default LoggerWrapper getLogger() {
        return SpecsLoggers.getLogger(getClass().getName());
    }

    // default SpecsLoggerV2 getBaseLogger() {
    // return SpecsLoggers.getLogger(getBaseClass().getName().toLowerCase());
    // }

    /*
    default void info(T tag, String message) {
        // LogsHelper.logMessage(getLoggerName(tag), tag, message, (logger, msg) -> logger.info(msg));
    
        String prefix = LogsHelper.getPrefix(tag);
    
        SpecsLoggerV2 logger = SpecsLoggers.getLogger(getLoggerName(tag));
        System.out.println("LEVEL:" + logger.getJavaLogger().getLevel());
        logger.info(prefix + message);
        // System.out.println("ADASD");
        // logging.accept(logger, prefix + message);
    }
    
    default void info(String message) {
        getLogger().info(msg);
        info(null, message);
    }
    
    static <T extends Enum<T>> EnumLogsV2<T> newInstance(Class<T> enumClass) {
        return () -> enumClass;
    }
    */
}
