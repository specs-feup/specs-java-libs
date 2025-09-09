/*
 * Copyright 2010 SPeCS Research Group.
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

package org.suikasoft.jOptions.gui.panels.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.Serial;
import java.util.Map;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.suikasoft.jOptions.JOptionKeys;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.App;
import org.suikasoft.jOptions.gui.AppFrame;
import org.suikasoft.jOptions.gui.KeyPanel;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Panel which loads and can edit the options file.
 *
 * <p>
 * This panel provides controls for loading, editing, and saving application
 * options.
 * 
 * @author Joao Bispo
 */
public class OptionsPanel extends GuiTab {

    @Serial
    private static final long serialVersionUID = 1L;

    private final App app;

    private final BaseSetupPanel setupPanel;
    private final JButton saveButton;
    private final JButton saveAsButton;
    private final JLabel fileInfo;
    private final JFileChooser fileChooser;

    private File outputFile;

    /**
     * Constructs an OptionsPanel for the given application and DataStore.
     *
     * @param app  the application instance
     * @param data the DataStore
     */
    public OptionsPanel(App app, DataStore data) {
        super(data);
        this.app = app;

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        setupPanel = new BaseSetupPanel(app.getDefinition(), getData());

        JScrollPane optionsPanel = new JScrollPane();
        optionsPanel.getVerticalScrollBar().setUnitIncrement(10);

        optionsPanel.setPreferredSize(new Dimension(AppFrame.PREFERRED_WIDTH + 10, AppFrame.PREFERRED_HEIGHT + 10));
        optionsPanel.setViewportView(setupPanel);

        fileInfo = new JLabel();
        updateFileInfoString();

        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveAsButton = new JButton("Save as...");

        saveButton.addActionListener(this::saveButtonActionPerformed);

        saveAsButton.addActionListener(this::saveAsButtonActionPerformed);

        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        savePanel.add(saveButton);
        savePanel.add(saveAsButton);
        savePanel.add(fileInfo);

        setLayout(new BorderLayout(5, 5));
        add(savePanel, BorderLayout.PAGE_START);

        add(optionsPanel, BorderLayout.CENTER);

    }

    /**
     * Retrieves the panels associated with the setup.
     *
     * @return a map of panel names to KeyPanel objects
     */
    public Map<String, KeyPanel<?>> getPanels() {
        return setupPanel.getPanels();
    }

    /**
     * Handles the action performed when the save button is clicked.
     *
     * @param evt the action event
     */
    private void saveButtonActionPerformed(ActionEvent evt) {
        if (outputFile == null) {
            saveAsButtonActionPerformed(evt);
            return;
        }

        app.getPersistence().saveData(outputFile, setupPanel.getData());
    }

    /**
     * Handles the action performed when the save-as button is clicked.
     *
     * @param evt the action event
     */
    private void saveAsButtonActionPerformed(ActionEvent evt) {
        if (outputFile == null) {
            fileChooser.setCurrentDirectory(new File("./"));
        } else if (outputFile.exists()) {
            fileChooser.setCurrentDirectory(outputFile);
        } else {
            fileChooser.setCurrentDirectory(new File("./"));
        }

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            outputFile = file;
            saveButton.setEnabled(true);
            updateFileInfoString();

            getData().set(JOptionKeys.CURRENT_FOLDER_PATH, Optional.of(SpecsIo.getCanonicalFile(file).getParent()));

            app.getPersistence().saveData(outputFile, setupPanel.getData());
        }
    }

    /**
     * Updates the values in the setup panel with the given DataStore.
     *
     * @param map the DataStore containing the new values
     */
    public void updateValues(DataStore map) {
        setupPanel.loadValues(map);
        saveButton.setEnabled(true);
        updateFileInfoString();
    }

    /**
     * Updates the file information label with the current output file.
     */
    private void updateFileInfoString() {
        File file = outputFile;
        String filename;
        if (file == null) {
            filename = "none";
        } else {
            filename = "'" + file.getName() + "'";
        }

        String text = "Selected file: " + filename;
        fileInfo.setText(text);
    }

    /**
     * Retrieves the current output file.
     *
     * @return the output file
     */
    public File getOutputFile() {
        return outputFile;
    }

    /**
     * Sets the current output file.
     *
     * @param outputFile the output file to set
     */
    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    /**
     * Called when entering the tab. Updates the setup panel with the current
     * configuration.
     */
    @Override
    public void enterTab() {
        File configFile = getData().get(AppKeys.CONFIG_FILE);

        DataStore map;
        if (configFile.getPath().isEmpty()) {
            map = app.getDefinition().getDefaultValues();
        } else {
            map = getDataStore();
        }

        updateValues(map);
    }

    /**
     * Retrieves the DataStore based on the current configuration file.
     *
     * @return the DataStore
     */
    private DataStore getDataStore() {
        File outputFile = getData().get(AppKeys.CONFIG_FILE);
        if (outputFile == null) {
            return DataStore.newInstance(app.getDefinition());
        }

        if (outputFile.exists()) {
            setOutputFile(outputFile);
        }

        if (!outputFile.isFile()) {
            return DataStore.newInstance(app.getDefinition());
        }

        String optionsFilename = outputFile.getPath();

        File file = new File(optionsFilename);
        if (!file.isFile()) {
            SpecsLogs.getLogger().warning("Could not open file '" + optionsFilename + "'");
            outputFile = null;
            saveButton.setEnabled(false);
            updateFileInfoString();
            return DataStore.newInstance(app.getDefinition());
        }

        DataStore newMap;

        try {
            newMap = app.getPersistence().loadData(file);
        } catch (Exception e) {
            SpecsLogs.info("Could not parse configuration file");
            newMap = null;
        }

        if (newMap == null) {
            SpecsLogs.info("Given file '" + optionsFilename + "' is not a compatible options file.");
            outputFile = null;
            saveButton.setEnabled(false);
            updateFileInfoString();
            return DataStore.newInstance(app.getDefinition());
        }

        return newMap;
    }

    /**
     * Called when exiting the tab. Updates the configuration file in the DataStore.
     */
    @Override
    public void exitTab() {
        File outputFile = getOutputFile();
        if (outputFile != null) {
            getData().set(AppKeys.CONFIG_FILE, getOutputFile());
        }
    }

    /**
     * Retrieves the name of the tab.
     *
     * @return the tab name
     */
    @Override
    public String getTabName() {
        return "Options";
    }

    /**
     * Retrieves the panel associated with the given DataKey.
     *
     * @param key the DataKey
     * @return the KeyPanel associated with the key
     */
    public KeyPanel<?> getPanel(DataKey<?> key) {
        var panel = getPanels().get(key.getName());

        if (panel == null) {
            throw new RuntimeException(
                    "Could not find key '" + key + "' in OptionsPanel. Available options: " + getPanels().keySet());
        }

        return panel;
    }

}
