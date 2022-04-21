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

package pt.up.fe.specs.util;

import java.awt.AWTError;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.TableModel;

import pt.up.fe.specs.util.swing.MapModel;

/**
 * Utility methods related to Java GUI operation.
 * 
 * @author Joao Bispo
 */
public class SpecsSwing {

    public static final String TEST_CLASSNAME = "javax.swing.JFrame";

    private static String CUSTOM_LOOK_AND_FEEL = null;

    synchronized public static void setCustomLookAndFeel(String value) {
        CUSTOM_LOOK_AND_FEEL = value;
    }

    synchronized public static String getCustomLookAndFeel() {
        return CUSTOM_LOOK_AND_FEEL;
    }

    // public static boolean hasCustomLookAndFeel() {
    // // Since it is a boolean, it should not have problems regarding reading concurrently, according to the Java
    // // memory model
    // return HAS_CUSTOM_LOOK_AND_FEEL;
    // }

    /**
     * Returns true if the Java package Swing is available.
     * 
     * @return
     */
    public static boolean isSwingAvailable() {
        return SpecsSystem.isAvailable(SpecsSwing.TEST_CLASSNAME);
    }

    public static void runOnSwing(Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }

    /**
     * Sets the system Look&Feel for Swing components.
     * 
     * @return true if no problem occurred, false otherwise
     */
    public static boolean setSystemLookAndFeel() {

        // Only set if there is a display available
        if (SpecsSwing.isHeadless()) {
            return true;
        }

        try {
            String lookAndFeel = getSystemLookAndFeel();

            SpecsLogs.debug("Using '" + lookAndFeel + "' as system look and feel");

            // Set System L&F
            UIManager.setLookAndFeel(lookAndFeel);

            // "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            // "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            return true;
        } catch (Exception e) {
            SpecsLogs.warn("Could not set system Look&Feel", e);
        }

        return false;
    }

    public static String getSystemLookAndFeel() {

        // Temporarily disable custom system look and feel
        // if (true) {
        // return UIManager.getSystemLookAndFeelClassName();
        // }

        // Get custom L&F
        String customLookAndFeel = getCustomLookAndFeel();
        if (customLookAndFeel != null) {
            return customLookAndFeel;
        }

        // Get System L&F
        String systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();

        // Build look and feels map
        Map<String, String> lookAndFeels = new LinkedHashMap<>();

        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            lookAndFeels.put(info.getName(), info.getClassName());
        }
        SpecsLogs.debug("Available look and feels: " + lookAndFeels);

        // Avoid Metal & GTK+
        if (!systemLookAndFeel.endsWith(".MetalLookAndFeel") && !systemLookAndFeel.endsWith(".GTKLookAndFeel")) {
            return systemLookAndFeel;
        }

        // // ... unless it is the only one available
        // if (UIManager.getInstalledLookAndFeels().length == 1) {
        // return systemLookAndFeel;
        // }

        // SpecsLogs.debug("Default system look and feel is Metal, trying to use another one");
        // Map<String, String> lookAndFeels = Arrays.stream(UIManager.getInstalledLookAndFeels())
        // .collect(Collectors.toMap(info -> info.getName(), info -> info.getClassName()));

        // // Build look and feels map
        // Map<String, String> lookAndFeels = new LinkedHashMap<>();
        //
        // for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        // lookAndFeels.put(info.getName(), info.getClassName());
        // }
        // SpecsLogs.debug("Available look and feels: " + lookAndFeels);

        // Check if GTK+ is available
        // String gtkLookAndFeel = lookAndFeels.get("GTK+");
        // if (gtkLookAndFeel != null) {
        // return gtkLookAndFeel;
        // }

        String alternativeLookAndFeel = lookAndFeels.values().stream()
                // Return first that is not Metal
                .filter(lookAndFeel -> !lookAndFeel.endsWith(".MetalLookAndFeel"))
                // Recently, GTK+ on Linux is really buggy, avoid it too
                .filter(lookAndFeel -> !lookAndFeel.endsWith(".GTKLookAndFeel"))
                .findFirst().orElse(systemLookAndFeel);

        if (!alternativeLookAndFeel.equals(systemLookAndFeel)) {
            SpecsLogs.debug("Setting 'Look & Feel' to " + alternativeLookAndFeel);
        }

        return alternativeLookAndFeel;
    }

    /**
     * Builds TableModels from Maps.
     * 
     * @param map
     * @param maxElementsPerTable
     * @param rowWise
     * @return
     */
    public static <K extends Comparable<? super K>, V> List<TableModel> getTables(Map<K, V> map,
            int maxElementsPerTable, boolean rowWise, Class<V> valueClass) {
        List<TableModel> tableModels = new ArrayList<>();

        // K and V will be rows
        // int numMaps = (int) Math.ceil((double)map.size() / (double)maxCols);
        // int rowCount = 2;
        // int mapCols = 0;

        // int currentCols = map.size();
        List<K> keys = new ArrayList<>();
        keys.addAll(map.keySet());
        Collections.sort(keys);

        List<K> currentKeys = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            currentKeys.add(keys.get(i));

            if (currentKeys.size() < maxElementsPerTable) {
                continue;
            }

            // Build map
            Map<K, V> newMap = new HashMap<>();
            for (K key : currentKeys) {
                newMap.put(key, map.get(key));
            }
            tableModels.add(new MapModel<>(newMap, rowWise, valueClass));

            currentKeys = new ArrayList<>();
        }

        if (!currentKeys.isEmpty()) {
            // Build map
            Map<K, V> newMap = new HashMap<>();
            for (K key : currentKeys) {
                newMap.put(key, map.get(key));
            }
            tableModels.add(new MapModel<>(newMap, rowWise, valueClass));
        }

        return tableModels;
    }

    /**
     * Builds TableModels from Maps.
     * 
     * @param map
     * @param maxElementsPerTable
     * @param rowWise
     * @return
     */
    public static <K extends Comparable<? super K>, V> TableModel getTable(Map<K, V> map,
            boolean rowWise, Class<V> valueClass) {

        // K and V will be rows

        List<K> keys = new ArrayList<>();
        keys.addAll(map.keySet());
        Collections.sort(keys);

        List<K> currentKeys = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            currentKeys.add(keys.get(i));
        }

        // Build map
        // Map<K, V> newMap = new HashMap<K, V>();
        Map<K, V> newMap = SpecsFactory.newLinkedHashMap();
        for (K key : currentKeys) {
            newMap.put(key, map.get(key));
        }

        return new MapModel<>(newMap, rowWise, valueClass);

    }

    public static JFrame showPanel(JPanel panel, String title) {
        return showPanel(panel, title, 0, 0);
    }

    /**
     * Launches the given panel in a JFrame.
     * 
     * @param panel
     * @param title
     * @param x
     * @param y
     * @return
     */
    public static JFrame newWindow(JPanel panel, String title, int x, int y) {
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setResizable(true);

        frame.setLocation(x, y);

        // Add content to the window.
        frame.add(panel, BorderLayout.CENTER);
        frame.setTitle(title);

        return frame;
    }

    public static JFrame showPanel(JPanel panel, String title, int x, int y) {
        final JFrame frame = newWindow(panel, title, x, y);
        /*
        	JFrame frame = new JFrame();
        
        	// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        	frame.setResizable(true);
        
        	frame.setLocation(x, y);
        
        	// Add content to the window.
        	frame.add(panel, BorderLayout.CENTER);
        	frame.setTitle(title);
         */
        SpecsSwing.runOnSwing(() -> {
            frame.pack();
            frame.setVisible(true);
        });

        return frame;
    }

    /**
     * Taken from here: https://stackoverflow.com/a/16611566
     * 
     * @return true if no screen is available for displaying Swing components, false otherwise
     */
    public static boolean isHeadless() {
        if (GraphicsEnvironment.isHeadless()) {
            return true;
        }

        try {
            GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
            return screenDevices == null || screenDevices.length == 0;
        } catch (HeadlessException | AWTError e) {
            return true;
        }
    }

    /**
     * Opens a folder containing the file and selects it in a default system file manager.
     */
    public static boolean browseFileDirectory(File file) {
        if (!file.exists()) {
            SpecsLogs.debug(() -> "SpecsSwing.browseFileDirectory(): file '" + file + "' does not exist");
        }

        // Tested on Java 15, Desktop.browseFileDirectory() was not working for Windows
        if (SpecsSystem.isWindows()) {

            var command = "explorer.exe /select, " + file.getAbsoluteFile();
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                SpecsLogs.info("Problem while trying to open folder for file '" + file + "': " + e.getMessage());
                return false;
            }
            return true;
            // return;
        }

        if (SpecsSystem.isLinux()) {
            try {
                Runtime.getRuntime().exec(Arrays.asList("gio", "open", file.getAbsolutePath()).toArray(new String[0]));
            } catch (IOException e) {
                SpecsLogs.info("Problem while trying to open folder for file '" + file + "': " + e.getMessage());
                return false;
            }
            return true;
            // return;
        }

        Desktop.getDesktop().browseFileDirectory(file);
        return true;
        // return;
    }
}
