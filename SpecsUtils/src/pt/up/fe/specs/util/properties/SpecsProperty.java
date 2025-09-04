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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.properties;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSwing;
import pt.up.fe.specs.util.utilities.heapwindow.HeapWindow;

/**
 * Global properties which can be applied to a program.
 * 
 * @author Joao Bispo
 */
public enum SpecsProperty {

    /**
     * Sets the logger level. Receives the name of the logging level.
     */
    LoggingLevel,
    /**
     * The name of the file to write the logging messages above WARNING level.
     */
    WriteErroLog,
    /**
     * Shows the stack trace when warning messages are called. Receives a boolean
     * value.
     */
    ShowStackTrace,
    /**
     * Opens a Swing window (if available) showing information about memory usage of
     * the application. Receives a boolean
     * value.
     */
    ShowMemoryHeap,
    /**
     * Sets a custom Look&Feel, can use name or classname.
     */
    LookAndFeel;

    public static final String PROPERTIES_FILENAME = "suika.properties";

    private static final List<String> SPECS_PROPERTIES = Arrays.asList("specs.properties", PROPERTIES_FILENAME);

    public static void applyProperties(Properties suikaProps) {

        for (SpecsProperty key : SpecsProperty.values()) {

            // Get value corresponding to the key
            String value = suikaProps.getProperty(key.name());

            // If no value found, ignore key
            if (value == null) {
                continue;
            }

            // Apply property
            key.applyProperty(value);
        }

    }

    /**
     * Looks for the file 'suika.properties' on the running folder and applies its
     * options.
     */
    public static void applyProperties() {
        // Look for compatible files representing specs properties, return the first

        File specsPropertiesFile = null;
        for (String filename : SPECS_PROPERTIES) {
            File currentFile = new File(filename);

            if (currentFile.isFile()) {
                specsPropertiesFile = currentFile;
                break;
            }
        }

        // If no compatible properties file found, just return
        if (specsPropertiesFile == null) {
            return;
        }

        SpecsLogs.debug("Applying the SPeCS properties file '" + specsPropertiesFile.getAbsolutePath() + "'");
        Properties specsProperties = SpecsProperties.newInstance(specsPropertiesFile).getProperties();
        SpecsProperty.applyProperties(specsProperties);
    }

    /**
     * A default suika.properties file.
     * 
     * @return a Collection for Strings representing resources needed for this
     *         package to run.
     */
    public static Collection<String> getResources() {
        List<String> resources = new ArrayList<>();
        resources.add(SpecsProperty.PROPERTIES_FILENAME);
        return resources;
    }

    public void applyProperty(String value) {
        if (value == null) {
            return;
        }

        // Set logging level
        if (this == LoggingLevel) {
            Level newLevel = SpecsLogs.parseLevel(value);
            if (newLevel == null) {
                return;
            }

            SpecsLogs.setLevel(newLevel);
            return;
        }

        // Show stack trace
        if (this == ShowStackTrace) {

            Boolean bool = SpecsStrings.parseBoolean(value);
            if (bool == null) {
                return;
            }

            SpecsLogs.setPrintStackTrace(bool);
            return;
        }

        // Show memory heap - SWING option
        if (this == ShowMemoryHeap) {
            Boolean bool = SpecsStrings.parseBoolean(value);

            if (bool == null) {
                return;
            }

            boolean apply = bool && SpecsSwing.isSwingAvailable();
            if (!apply) {
                return;
            }

            (new HeapWindow()).run();
            return;
        }

        if (this == WriteErroLog) {
            if (value.isEmpty()) {
                return;
            }

            // Get hadlers
            Handler[] oldHandlers = SpecsLogs.getRootLogger().getHandlers();
            Handler[] newHandlers = new Handler[oldHandlers.length + 1];

            System.arraycopy(oldHandlers, 0, newHandlers, 0, oldHandlers.length);

            SpecsLogs.msgInfo("Setting error log to file '" + value + "'");
            newHandlers[oldHandlers.length] = SpecsLogs.buildErrorLogHandler(value);
            SpecsLogs.setRootHandlers(newHandlers);
            return;
        }

        if (this == LookAndFeel) {

            // Build look and feels map
            Map<String, String> lookAndFeels = new LinkedHashMap<>();
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                lookAndFeels.put(info.getName(), info.getClassName());
            }
            SpecsLogs.debug("Available look and feels: " + lookAndFeels);

            // First search as name, then as classname
            String lookAndFeel = lookAndFeels.get(value);

            if (lookAndFeel == null && lookAndFeels.containsValue(value)) {
                lookAndFeel = value;
            }

            // If still null, return
            if (lookAndFeel == null) {
                return;
            }

            // Set Custom L&F
            SpecsSwing.setCustomLookAndFeel(lookAndFeel);

            return;
        }

        throw new RuntimeException("Not implemented for option '" + this + "'");
    }
}
