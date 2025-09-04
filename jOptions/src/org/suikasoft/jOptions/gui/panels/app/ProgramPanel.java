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

/*
 * ProgramPanel.java
 *
 * Created on 27/Set/2010, 10:32:12
 */

package org.suikasoft.jOptions.gui.panels.app;

import java.awt.Font;
import java.io.File;
import java.io.Serial;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Handler;
import java.util.prefs.Preferences;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import org.suikasoft.jOptions.JOptionKeys;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.App;
import org.suikasoft.jOptions.app.AppDefaultConfig;
import org.suikasoft.jOptions.gui.ApplicationWorker;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.logging.TextAreaHandler;
import pt.up.fe.specs.util.utilities.LastUsedItems;

/**
 * Panel used to indicate the setup file and which can start and cancel the execution of the program. Also shows the
 * output of the program.
 *
 * <p>This panel provides controls for selecting setup files, starting/cancelling execution, and displaying output.
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class ProgramPanel extends GuiTab {

    @Serial
    private static final long serialVersionUID = 1L;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JButton cancelButton;
    private JComboBox<String> filenameTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea outputArea;
    private javax.swing.JButton startButton;
    // End of variables declaration//GEN-END:variables

    // Other variables
    private JFileChooser fc;
    final private App application;
    private ApplicationWorker worker;
    private final LastUsedItems<String> lastUsedItems;

    private static final String OPTION_LAST_USED_ITEMS = "lastUsedItems";
    private static final int LAST_USED_ITEMS_CAPACITY = 10;
    private static final String ITEMS_SEPARATOR_REGEX = "\\?";
    private static final String ITEMS_SEPARATOR = "?";

    private static final String BLANK_OPTION_FILE = "";

    /**
     * Creates a new ProgramPanel for the given application and DataStore.
     *
     * @param application the application instance
     * @param data the DataStore
     */
    public ProgramPanel(App application, DataStore data) {
        super(data);

        initComponents();

        this.application = application;

        // Get last used files
        Preferences prefs = Preferences.userNodeForPackage(application.getNodeClass());
        String lastFilesString = prefs.get(ProgramPanel.OPTION_LAST_USED_ITEMS, "");

        // Parse string into a list
        List<String> lastFiles = parseLastFiles(lastFilesString);

        // Initialize lastUsedItems
        lastUsedItems = new LastUsedItems<>(ProgramPanel.LAST_USED_ITEMS_CAPACITY, lastFiles);

        // Set items of JComboBox
        List<String> items = lastUsedItems.getItems();
        for (String item : items) {
            filenameTextField.addItem(item);
        }

        // Set head of lastUsedItems as the item that appears in box
        Optional<String> head = lastUsedItems.getHead();
        if (head.isPresent()) {
            filenameTextField.getEditor().setItem(head.get());
        } else {
            filenameTextField.getEditor().setItem(buildDefaultOptionFilename());
        }

        repaint();
        revalidate();

        customInit();
    }

    /**
     * Parses the last used files string into a list of file paths.
     *
     * @param lastFilesString the string containing file paths separated by the separator
     * @return a list of file paths
     */
    private static List<String> parseLastFiles(String lastFilesString) {
        if (lastFilesString.isEmpty()) {
            return Collections.emptyList();
        }

        String[] files = lastFilesString.split(ProgramPanel.ITEMS_SEPARATOR_REGEX);
        return Arrays.asList(files);
    }

    /**
     * Performs custom initialization for the ProgramPanel.
     */
    private void customInit() {

        // Init file chooser
        fc = new JFileChooser();

        // Redirect output to jtextfiled
        Handler[] handlersTemp = SpecsLogs.getRootLogger().getHandlers();
        Handler[] newHandlers = new Handler[handlersTemp.length + 1];
        System.arraycopy(handlersTemp, 0, newHandlers, 0, handlersTemp.length);
        TextAreaHandler jTextAreaHandler = new TextAreaHandler(outputArea);

        newHandlers[handlersTemp.length] = jTextAreaHandler;
        SpecsLogs.setRootHandlers(newHandlers);
        // Init buttons
        setButtonsEnable(true);
        worker = new ApplicationWorker(this);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        filenameTextField = new JComboBox<>();
        filenameTextField.setEditable(true);
        browseButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        startButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        outputArea = new javax.swing.JTextArea();

        setPreferredSize(new java.awt.Dimension(480, 236));

        jLabel1.setText("Options file:");

        browseButton.setText("Browse...");
        browseButton.addActionListener(this::browseButtonActionPerformed);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(this::cancelButtonActionPerformed);

        startButton.setText("Start");
        startButton.addActionListener(this::startButtonActionPerformed);

        outputArea.setEditable(false);
        outputArea.setRows(15);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        jScrollPane1.setViewportView(outputArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(filenameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 311,
                                        Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(browseButton).addContainerGap())
                .addGroup(
                        layout.createSequentialGroup().addContainerGap().addComponent(startButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelButton).addContainerGap(338, Short.MAX_VALUE))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(filenameTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(browseButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(startButton).addComponent(cancelButton))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Handles the action performed when the browse button is clicked.
     *
     * @param evt the action event
     */
    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        File optionsFile = new File(filenameTextField.getEditor().getItem().toString());
        if (!optionsFile.exists()) {
            optionsFile = new File("./");
        }
        fc.setCurrentDirectory(optionsFile);
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            filenameTextField.getEditor().setItem(file.getAbsolutePath());
        }
    }

    /**
     * Handles the action performed when the cancel button is clicked.
     *
     * @param evt the action event
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        worker.shutdown();
    }

    /**
     * Handles the action performed when the start button is clicked.
     *
     * @param evt the action event
     */
    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
        execute();
    }

    /**
     * Executes the application with the selected setup file.
     */
    public void execute() {
        // Clear text area
        outputArea.setText("");

        // Check if file is valid
        String filename = filenameTextField.getEditor().getItem().toString();

        File file = new File(filename);
        if (!file.exists()) {
            SpecsLogs.msgInfo("File '" + filename + "' does not exist.");
            File file2 = new File("./");
            SpecsLogs.msgInfo("Current files in folder '" + file2.getAbsolutePath() + "':");
            SpecsLogs.msgInfo(Arrays.asList(file2.listFiles()).toString());
            return;
        }

        // Mark file as used
        boolean hasChanges = lastUsedItems.used(filename);

        // If changes, save current list of files
        if (hasChanges) {
            String lastUsedFilesString = encodeList(lastUsedItems.getItems());

            // Save accessed file
            Preferences prefs = Preferences.userNodeForPackage(application.getNodeClass());
            prefs.put(ProgramPanel.OPTION_LAST_USED_ITEMS, lastUsedFilesString);

            // Update JComboBox
            filenameTextField.removeAllItems();
            for (String item : lastUsedItems.getItems()) {
                filenameTextField.addItem(item);
            }
        }

        DataStore setup = application.getPersistence().loadData(file);
        worker.execute(setup);
    }

    /**
     * Encodes a list of file paths into a single string separated by the separator.
     *
     * @param lastUsedItems the list of file paths
     * @return the encoded string
     */
    private static String encodeList(List<String> lastUsedItems) {
        return String.join(ProgramPanel.ITEMS_SEPARATOR, lastUsedItems);
    }

    /**
     * Enables or disables the buttons in the panel.
     *
     * @param enable true to enable the buttons, false to disable
     */
    public final void setButtonsEnable(boolean enable) {
        browseButton.setEnabled(enable);
        startButton.setEnabled(enable);
        cancelButton.setEnabled(!enable);
    }

    /**
     * Gets the application instance associated with this panel.
     *
     * @return the application instance
     */
    public App getApplication() {
        return application;
    }

    /**
     * Gets the filename text field component.
     *
     * @return the filename text field
     */
    public JComboBox<String> getFilenameTextField() {
        return filenameTextField;
    }

    /**
     * Builds the default option filename for the application.
     *
     * @return the default option filename
     */
    private String buildDefaultOptionFilename() {
        // Check if App implements AppDefaultConfig
        if (!(application instanceof AppDefaultConfig)) {
            return ProgramPanel.BLANK_OPTION_FILE;
        }

        return ((AppDefaultConfig) application).defaultConfigFile();
    }

    /**
     * Called when entering the tab. Updates the filename text field with the current configuration file path.
     */
    @Override
    public void enterTab() {
        File file = getData().get(AppKeys.CONFIG_FILE);
        if (file == null || file.getPath().isEmpty()) {
            return;
        }

        getFilenameTextField().getEditor().setItem(file.getPath());
    }

    /**
     * Called when exiting the tab. Updates the configuration file and current folder path in the DataStore.
     */
    @Override
    public void exitTab() {
        String path = getFilenameTextField().getEditor().getItem().toString();
        if (path.trim().isEmpty()) {
            getData().remove(AppKeys.CONFIG_FILE);
            getData().remove(JOptionKeys.CURRENT_FOLDER_PATH);
            return;
        }

        File configFile = new File(path);
        configFile = configFile.getAbsoluteFile();

        File workingFolder = configFile;
        if (!configFile.isDirectory()) {
            workingFolder = configFile.getParentFile();
        }

        getData().set(AppKeys.CONFIG_FILE, new File(getFilenameTextField().getEditor().getItem().toString()));
        getData().set(JOptionKeys.CURRENT_FOLDER_PATH, Optional.of(workingFolder.getPath()));
    }

    /**
     * Gets the name of the tab.
     *
     * @return the tab name
     */
    @Override
    public String getTabName() {
        return "Program";
    }

}
