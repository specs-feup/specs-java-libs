/*
 * SpecsSwing.java
 *
 * Utility class for Swing-related operations, such as dialogs, UI helpers, and event handling. Provides static helper methods for simplifying Java Swing development in the SPeCS ecosystem.
 *
 * Copyright 2025 SPeCS Research Group.
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
 * Utility methods for Java Swing operations.
 * <p>
 * Provides static helper methods for dialogs, UI helpers, and event handling in Java Swing applications.
 * </p>
 *
 * @author Joao Bispo
 */
public class SpecsSwing {

    /**
     * The class name used to test if Swing is available in the current environment.
     */
    public static final String TEST_CLASSNAME = "javax.swing.JFrame";

    /**
     * Custom Look and Feel class name, if set.
     */
    private static String CUSTOM_LOOK_AND_FEEL = null;

    /**
     * Sets a custom Look and Feel class name.
     *
     * @param value the class name of the custom Look and Feel
     */
    synchronized public static void setCustomLookAndFeel(String value) {
        CUSTOM_LOOK_AND_FEEL = value;
    }

    /**
     * Gets the custom Look and Feel class name, if set.
     *
     * @return the class name of the custom Look and Feel, or null if not set
     */
    synchronized public static String getCustomLookAndFeel() {
        return CUSTOM_LOOK_AND_FEEL;
    }

    /**
     * Returns true if the Java package Swing is available.
     *
     * @return true if Swing is available, false otherwise
     */
    public static boolean isSwingAvailable() {
        return SpecsSystem.isAvailable(SpecsSwing.TEST_CLASSNAME);
    }

    /**
     * Runs the given Runnable on the Swing Event Dispatch Thread.
     *
     * @param r the Runnable to execute
     */
    public static void runOnSwing(Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }

    /**
     * Sets the system Look and Feel for Swing components.
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

            return true;
        } catch (Exception e) {
            SpecsLogs.warn("Could not set system Look&Feel", e);
        }

        return false;
    }

    /**
     * Gets the system Look and Feel class name, avoiding problematic defaults like Metal and GTK+.
     *
     * @return the class name of the system Look and Feel
     */
    public static String getSystemLookAndFeel() {

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

        String alternativeLookAndFeel = lookAndFeels.values().stream()
                .filter(lookAndFeel -> !lookAndFeel.endsWith(".MetalLookAndFeel"))
                .filter(lookAndFeel -> !lookAndFeel.endsWith(".GTKLookAndFeel"))
                .findFirst().orElse(systemLookAndFeel);

        if (!alternativeLookAndFeel.equals(systemLookAndFeel)) {
            SpecsLogs.debug("Setting 'Look & Feel' to " + alternativeLookAndFeel);
        }

        return alternativeLookAndFeel;
    }

    /**
     * Builds TableModels from Maps, splitting into multiple tables if necessary.
     *
     * @param map the map to convert into TableModels
     * @param maxElementsPerTable the maximum number of elements per table
     * @param rowWise whether the table should be row-wise
     * @param valueClass the class of the values in the map
     * @return a list of TableModels
     */
    public static <K extends Comparable<? super K>, V> List<TableModel> getTables(Map<K, V> map,
            int maxElementsPerTable, boolean rowWise, Class<V> valueClass) {
        List<TableModel> tableModels = new ArrayList<>();

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
     * Builds a single TableModel from a Map.
     *
     * @param map the map to convert into a TableModel
     * @param rowWise whether the table should be row-wise
     * @param valueClass the class of the values in the map
     * @return a TableModel
     */
    public static <K extends Comparable<? super K>, V> TableModel getTable(Map<K, V> map,
            boolean rowWise, Class<V> valueClass) {

        List<K> keys = new ArrayList<>();
        keys.addAll(map.keySet());
        Collections.sort(keys);

        List<K> currentKeys = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            currentKeys.add(keys.get(i));
        }

        // Build map
        Map<K, V> newMap = SpecsFactory.newLinkedHashMap();
        for (K key : currentKeys) {
            newMap.put(key, map.get(key));
        }

        return new MapModel<>(newMap, rowWise, valueClass);
    }

    /**
     * Displays a JPanel in a JFrame with the given title.
     *
     * @param panel the JPanel to display
     * @param title the title of the JFrame
     * @return the JFrame containing the panel
     */
    public static JFrame showPanel(JPanel panel, String title) {
        return showPanel(panel, title, 0, 0);
    }

    /**
     * Creates a new JFrame containing the given JPanel.
     *
     * @param panel the JPanel to display
     * @param title the title of the JFrame
     * @param x the x-coordinate of the JFrame
     * @param y the y-coordinate of the JFrame
     * @return the JFrame containing the panel
     */
    public static JFrame newWindow(JPanel panel, String title, int x, int y) {
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocation(x, y);

        // Add content to the window.
        frame.add(panel, BorderLayout.CENTER);
        frame.setTitle(title);

        return frame;
    }

    /**
     * Displays a JPanel in a JFrame with the given title and location.
     *
     * @param panel the JPanel to display
     * @param title the title of the JFrame
     * @param x the x-coordinate of the JFrame
     * @param y the y-coordinate of the JFrame
     * @return the JFrame containing the panel
     */
    public static JFrame showPanel(JPanel panel, String title, int x, int y) {
        final JFrame frame = newWindow(panel, title, x, y);

        SpecsSwing.runOnSwing(() -> {
            frame.pack();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        });

        return frame;
    }

    /**
     * Checks if the current environment is headless, i.e., no screen is available for displaying Swing components.
     *
     * @return true if the environment is headless, false otherwise
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
     *
     * @param file the file to select
     * @return true if the operation was successful, false otherwise
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
        }

        if (SpecsSystem.isLinux()) {
            try {
                var folderToOpen = file.isFile() ? file.getParentFile() : file;
                Runtime.getRuntime()
                        .exec(Arrays.asList("gio", "open", folderToOpen.getAbsolutePath()).toArray(new String[0]));
            } catch (IOException e) {
                SpecsLogs.info("Problem while trying to open folder for file '" + file + "': " + e.getMessage());
                return false;
            }
            return true;
        }

        Desktop.getDesktop().browseFileDirectory(file);
        return true;
    }
}
