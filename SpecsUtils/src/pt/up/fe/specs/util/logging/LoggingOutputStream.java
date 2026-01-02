/*
 * Copyright 2009 SPeCS Research Group.
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An OutputStream that writes contents to a Logger upon each call to flush().
 *
 * <p>
 * This class is used by LoggingUtils methods to redirect the System.out and
 * System.err streams.
 *
 * @author Joao Bispo
 * @author <a href="http://blogs.sun.com/nickstephen/entry/java_redirecting_system_out_and">...</a>
 */
public class LoggingOutputStream extends ByteArrayOutputStream {

    //
    // INSTANCE VARIABLES
    //
    final private Logger logger;
    final private Level level;

    /**
     * Constructor
     * 
     * @param logger Logger to write to
     * @param level  Level at which to write the log message
     */
    public LoggingOutputStream(Logger logger, Level level) {
        super();
        this.logger = logger;
        this.level = level;
    }

    /**
     * Upon flush() write the existing contents of the OutputStream to the logger as
     * a log record.
     * 
     * @throws java.io.IOException in case of error
     */
    @Override
    public void flush() throws IOException {

        String record;
        synchronized (this) {
            super.flush();
            record = this.toString();
            super.reset();

            if (record.isEmpty()) {
                // avoid empty records
                return;
            }

            this.logger.logp(this.level, "", "", record);
        }
    }

}
