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

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableModel;

import pt.up.fe.specs.util.swing.MapModel;

/**
 * Utility methods related to Java GUI operation.
 * 
 * @author Joao Bispo
 */
public class SpecsSwing {

    public static final String TEST_CLASSNAME = "javax.swing.JFrame";

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
     * 
     * @return
     */
    public static boolean setSystemLookAndFeel() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            // "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            return true;
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
            SpecsLogs.getLogger().warning(e.getMessage());
        } catch (ClassNotFoundException e) {
            // handle exception
            SpecsLogs.getLogger().warning(e.getMessage());
        } catch (InstantiationException e) {
            // handle exception
            SpecsLogs.getLogger().warning(e.getMessage());
        } catch (IllegalAccessException e) {
            // handle exception
            SpecsLogs.getLogger().warning(e.getMessage());
        }

        return false;
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
        // frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

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
}
