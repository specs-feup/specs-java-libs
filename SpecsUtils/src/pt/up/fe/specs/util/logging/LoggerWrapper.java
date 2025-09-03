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

import java.util.logging.Logger;

/**
 * Wrapper around java.util.logging.Logger, which extends class with some logging methods.
 * 
 * @author JoaoBispo
 *
 */
public class LoggerWrapper {

    private final static String NEWLINE = System.getProperty("line.separator");

    // Keeping a reference to a Logger so that it does not get garbage collected.
    private final Logger logger;

    public LoggerWrapper(String name) {
        // Handle null logger names
        if (name == null) {
            throw new NullPointerException("Logger name cannot be null");
        }
        
        this.logger = Logger.getLogger(name);
    }

    public String getName() {
        return logger.getName();
    }

    public Logger getJavaLogger() {
        return logger;
    }

    /**
     * Info-level message.
     * 
     * <p>
     * Use this level to show messages to the user of a program.
     * 
     * 
     * @param logger
     * @param msg
     */
    public void info(String msg) {
        msg = parseMessage(msg);

        logger.info(msg);
    }

    /**
     * Adds a newline to the end of the message, if it does not have one.
     *
     * @param msg
     * @return
     */
    private String parseMessage(String msg) {
        // Handle null messages
        if (msg == null) {
            return null;
        }
        
        if (msg.isEmpty()) {
            return msg;
        }

        return msg + NEWLINE;
    }

}
