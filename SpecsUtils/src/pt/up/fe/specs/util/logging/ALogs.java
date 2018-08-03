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

import java.util.function.BiConsumer;

public abstract class ALogs<T extends Enum<T>> implements Logs<T> {

    // private final T defaultValue;
    private final String baseName;

    // protected ALogs(T defaultValue) {
    protected ALogs() {
        // this.defaultValue = defaultValue;
        this.baseName = getClass().getName();
    }

    protected String getLoggerName(Enum<T> tag) {
        String loggerName = baseName + ".";

        loggerName += tag != null ? tag.name() : "$root";

        return loggerName;
    }

    protected void logMessage(T tag, String message, BiConsumer<LoggerWrapper, String> logging) {
        String prefix = getPrefix(tag);

        String loggerName = getLoggerName(tag);
        logging.accept(SpecsLoggers.getLogger(loggerName), prefix + message);
    }

    private String getPrefix(T tag) {
        if (tag == null) {
            return "";
        }

        return "[" + tag.name() + "] ";
    }

    @Override
    public void info(T tag, String message) {
        // String prefix = getPrefix(tag);
        //
        // String loggerName = getLoggerName(tag);
        // SpecsLoggers.getLogger(loggerName).info(prefix + message);
        logMessage(tag, message, (logger, msg) -> logger.info(msg));
    }

    @Override
    public void warn(T tag, String message) {
        // TODO Auto-generated method stub

    }

}
