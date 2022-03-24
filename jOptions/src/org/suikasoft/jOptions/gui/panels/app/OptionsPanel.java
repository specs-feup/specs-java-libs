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
 * @author Joao Bispo
 */
public class OptionsPanel extends GuiTab {

    private static final long serialVersionUID = 1L;

    private final App app;

    private final BaseSetupPanel setupPanel;
    // private DataStore optionsData;
    private final JButton saveButton;
    private final JButton saveAsButton;
    private final JLabel fileInfo;
    private final JFileChooser fileChooser;

    private File outputFile;

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

        // JComponent optionsPanel = initEnumOptions(definition);

        // optionsData = DataStore.newInstance(app.getDefinition());
        fileInfo = new JLabel();
        updateFileInfoString();

        saveButton = new JButton("Save");
        // By default, the save button is disable, until there is a valid
        // file to save to.
        saveButton.setEnabled(false);
        saveAsButton = new JButton("Save as...");

        saveButton.addActionListener(evt -> saveButtonActionPerformed(evt));

        saveAsButton.addActionListener(evt -> saveAsButtonActionPerformed(evt));

        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        savePanel.add(saveButton);
        savePanel.add(saveAsButton);
        savePanel.add(fileInfo);

        // If optionFile no file, save button is null;
        // Only "unnulls" after "Save As..." and after successful update.

        setLayout(new BorderLayout(5, 5));
        add(savePanel, BorderLayout.PAGE_START);

        add(optionsPanel, BorderLayout.CENTER);

    }

    public Map<String, KeyPanel<? extends Object>> getPanels() {
        return setupPanel.getPanels();
    }

    // public DataStore getOptionFile() {
    // return optionsData;
    // }

    private void saveButtonActionPerformed(ActionEvent evt) {
        // updateInternalMap();
        // optionsData = setupPanel.getData();
        if (outputFile == null) {
            saveAsButtonActionPerformed(evt);
            return;
        }

        app.getPersistence().saveData(outputFile, setupPanel.getData());
    }

    private void saveAsButtonActionPerformed(ActionEvent evt) {
        // JFileChooser fc;

        // If no output file, choose current folder
        if (outputFile == null) {
            fileChooser.setCurrentDirectory(new File("./"));
        }
        // If output file exists, choose as folder
        else if (outputFile.exists()) {
            fileChooser.setCurrentDirectory(outputFile);
        }
        // Otherwise, use current folder as default
        else {
            fileChooser.setCurrentDirectory(new File("./"));
        }

        // app.getPersistence().saveData(outputFile, setupPanel.getData());

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // Files returned from this chooser cannot be folders
            outputFile = file;
            saveButton.setEnabled(true);
            updateFileInfoString();

            // Update current folder path
            getData().set(JOptionKeys.CURRENT_FOLDER_PATH, Optional.of(SpecsIo.getCanonicalFile(file).getParent()));
            // getData().set(JOptionKeys.CURRENT_FOLDER_PATH, SpecsIo.getCanonicalFile(file).getParent());
            // updateFile(file);

            // Automatically save data
            app.getPersistence().saveData(outputFile, setupPanel.getData());
        }
    }

    // public void updateValues(String optionsFilename) {
    public void updateValues(DataStore map) {

        // Load file
        // optionsData = map;

        setupPanel.loadValues(map);
        saveButton.setEnabled(true);
        updateFileInfoString();

        // Update receivers
        // for (FileReceiver fileReceiver : fileReceivers) {
        // fileReceiver.updateFile(file);
        // }

    }

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

    /*
     * Sets the current option file to the given file.
     */
    // private void updateFile(File file) {
    // // updateInternalMap();
    // // optionsData = setupPanel.getData();
    // outputFile = file;
    // saveButton.setEnabled(true);
    // updateFileInfoString();
    //
    // }

    // private void updateInternalMap() {
    // // Get info from panels
    // optionsData = appFilePanel.getData();
    //
    // // Update internal optionfile
    // // optionsData = updatedMap;
    // }

    /**
     * Can only be called after setup panels are initallized.
     * 
     * @param newOptionFile
     */
    // private void assignNewOptionFile(DataStore newOptionFile) {
    // optionFile = newOptionFile;
    // }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.guihelper.gui.BasePanels.GuiTab#enterTab(pt.up.fe.specs.guihelper.Gui.BasePanels.TabData)
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
        // File outputFile = getData().get(AppKeys.CONFIG_FILE);
        // if (outputFile == null) {
        // return;
        // }
        //
        // if (outputFile.exists()) {
        // setOutputFile(outputFile);
        // }
        //
        // if (outputFile.isFile()) {
        // updateValues(outputFile.getPath());
        // }
    }

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

        // Check if filename is a valid optionsfile
        File file = new File(optionsFilename);
        if (!file.isFile()) {
            SpecsLogs.getLogger().warning("Could not open file '" + optionsFilename + "'");
            outputFile = null;
            saveButton.setEnabled(false);
            updateFileInfoString();
            return DataStore.newInstance(app.getDefinition());
        }

        DataStore newMap = app.getPersistence().loadData(file);

        // SetupData newMap = GuiHelperUtils.loadData(file);
        if (newMap == null) {
            SpecsLogs.warn("Given file '" + optionsFilename + "' is not a compatible options file.");
            outputFile = null;
            saveButton.setEnabled(false);
            updateFileInfoString();
            return DataStore.newInstance(app.getDefinition());
        }

        return newMap;
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.guihelper.gui.BasePanels.GuiTab#exitTab(pt.up.fe.specs.guihelper.Gui.BasePanels.TabData)
     */
    @Override
    public void exitTab() {
        File outputFile = getOutputFile();
        if (outputFile != null) {
            getData().set(AppKeys.CONFIG_FILE, getOutputFile());
        }
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.guihelper.gui.BasePanels.GuiTab#getTabName()
     */
    @Override
    public String getTabName() {
        return "Options";
    }

    public KeyPanel<? extends Object> getPanel(DataKey<?> key) {
        var panel = getPanels().get(key.getName());

        if (panel == null) {
            throw new RuntimeException(
                    "Could not find key '" + key + "' in OptionsPanel. Available options: " + getPanels().keySet());
        }

        return panel;
    }

}
