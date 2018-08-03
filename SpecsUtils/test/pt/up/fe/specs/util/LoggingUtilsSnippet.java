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

import static pt.up.fe.specs.util.logging.SpecsLogsTest.*;

import java.util.logging.Level;

import org.junit.Test;

import pt.up.fe.specs.util.logging.ClavaLoggerTag;
import pt.up.fe.specs.util.logging.ClavaLoggerUser;
import pt.up.fe.specs.util.logging.ClavaLogsTest;

public class LoggingUtilsSnippet implements ClavaLoggerUser {

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

        // System.out.println("HELLO!");

        // IntStream.range(1, 25).map(i -> i * i).forEach(i -> System.out.println(i));

        SPECS_LOGS.info("SPeCS info message");
        ClavaLogsTest.info("Clava info message");
        ClavaLogsTest.metrics("Clava info metrics message");

        // Disable metrics
        ClavaLogsTest.CLAVA_LOGS.getLogger(ClavaLogsTest.Metrics).getJavaLogger().setLevel(Level.OFF);

        ClavaLogsTest.info("Clava info message 2");
        ClavaLogsTest.metrics("Clava info metrics message 2 - SHOULD NOT APPEAR");

        // Remove custom level
        // If a logger has a level set, it overrides parent settings
        ClavaLogsTest.CLAVA_LOGS.getLogger(ClavaLogsTest.Metrics).getJavaLogger().setLevel(null);

        ClavaLogsTest.info("Clava info message 3");
        ClavaLogsTest.metrics("Clava info metrics message 3");

        // ClavaLogsTest.CLAVA_LOGS.getLogger(null).getJavaLogger().setLevel(Level.OFF);
        // ClavaLogsTest.CLAVA_LOGS.getLogger(ClavaLogsTest.Metrics).getJavaLogger().setLevel(Level.OFF);
        ClavaLogsTest.CLAVA_LOGS.getBaseLogger().getJavaLogger().setLevel(Level.OFF);

        ClavaLogsTest.info("Clava info message 4 - SHOULD NOT APPEAR");
        ClavaLogsTest.metrics("Clava info metrics message 4 - SHOULD NOT APPEAR");

        // SpecsLogsTest.info("SPeCS info message");
        // ClavaLogsTest.info("Clava info message");
        /*
        Logger childLogger = Logger.getLogger("baseLogger.childLogger");
        System.out.println("CHILD LOGGER NAME:" + childLogger.getName());
        
        Logger childLogger2 = Logger.getLogger("baseLogger.childLogger2");
        System.out.println("CHILD LOGGER 2 NAME:" + childLogger2.getName());
        
        Logger baseLogger = Logger.getLogger("baseLogger");
        System.out.println("BASE LOGGER NAME:" + baseLogger.getName());
        
        baseLogger.setLevel(Level.OFF);
        
        childLogger.info("CHILD 1");
        childLogger2.info("CHILD 2");
        
        baseLogger.setLevel(Level.INFO);
        
        childLogger.info("CHILD 1 1");
        childLogger2.info("CHILD 2 2");
        */
        getLogger().info("Hello");
        getLogger().info(ClavaLoggerTag.DEPRECATED, "This is deprecated");

        // getLogger().getBaseLogger().getJavaLogger().setLevel(Level.OFF);

        getLogger().setLevelAll(Level.OFF);
        getLogger().setLevel(null, Level.INFO);
        // getLogger().getLogger(ClavaLoggerTag.DEPRECATED).getJavaLogger().setLevel(Level.OFF);

        getLogger().info("Hello 2");
        getLogger().info(ClavaLoggerTag.DEPRECATED, "This is deprecated 2");
    }

}
