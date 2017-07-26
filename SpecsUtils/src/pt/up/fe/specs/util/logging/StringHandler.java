/**
 * Copyright 2013 SPeCS Research Group.
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

import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class StringHandler extends StreamHandler {

    private final StringBuilder buffer;

    /**
     * Create a <tt>ConsoleHandler</tt> for <tt>System.err</tt>.
     * <p>
     * The <tt>ConsoleHandler</tt> is configured based on <tt>LogManager</tt> properties (or their default values).
     * 
     */
    public StringHandler() {
	// setOutputStream(printStream);

	this.buffer = new StringBuilder();
    }

    public String getString() {
	return this.buffer.toString();
    }

    /*
    public static StringHandler newInstance() {
    /*
    FileOutputStream outputStream = null;

    try {
        outputStream = new FileOutputStream(logFile);
    } catch (FileNotFoundException e) {
        return null;
    }
     */
    /*
    	return new StringHandler(new PrintStream(outputStream));
    }
     */
    /**
     * Publish a <tt>LogRecord</tt>.
     * <p>
     * The logging request was made initially to a <tt>Logger</tt> object, which initialized the <tt>LogRecord</tt> and
     * forwarded it here.
     * <p>
     * 
     * @param record
     *            description of the log event. A null record is silently ignored and is not published
     */
    @Override
    public synchronized void publish(LogRecord record) {
	// super.publish(record);
	this.buffer.append(record.getMessage());
	flush();
    }

    /**
     * Override <tt>StreamHandler.close</tt> to do a flush but not to close the output stream. That is, we do <b>not</b>
     * close <tt>System.err</tt>.
     */
    @Override
    public synchronized void close() {
	flush();
    }
}
