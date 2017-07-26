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

package pt.up.fe.specs.util;

import org.junit.Test;

public class LoggingUtilsSnippet {

    @Test
    public void testFileHandler() {
	SpecsSystem.programStandardInit();

	// LoggingUtils.addLogFile(new File("c:\\temp_dir\\log.txt"));
	// SimpleFileHandler handler = SimpleFileHandler.newInstance(new File("c:\\temp_dir\\log.txt"));
	// handler.setFormatter(new ConsoleFormatter());
	/*
	FileHandler fileHandler = null;

	try {
	    fileHandler = new FileHandler("c:\\temp_dir\\log.txt");
	} catch (SecurityException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	 */
	// LoggingUtils.addHandler(handler);

	System.out.println("HELLO!");

	// IntStream.range(1, 25).map(i -> i * i).forEach(i -> System.out.println(i));
    }

}
