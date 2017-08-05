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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Handler;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

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
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class ProgramPanel extends GuiTab {

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

    // private static final String OPTION_LAST_USED_FILE = "lastUsedFile";
    private static final String OPTION_LAST_USED_ITEMS = "lastUsedItems";
    private static final int LAST_USED_ITEMS_CAPACITY = 5;
    // Items will be filenames, and they should not have the character '?'
    private static final String ITEMS_SEPARATOR_REGEX = "\\?";
    private static final String ITEMS_SEPARATOR = "?";

    private static final String BLANK_OPTION_FILE = "";

    /** Creates new form ProgramPanel */
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
            // filenameTextField.setSelectedItem(head.get());
            filenameTextField.getEditor().setItem(head.get());
            // filenameTextField.getEditor().setItem(firstItem);
            // filenameTextField.setSelectedItem(firstItem);
            // System.out.println("NOT SETTING? -> " + head.get());
            // System.out.println("NOT SETTING 2? -> " + firstItem);
            // System.out.println("EQUAL? " + (firstItem == head.get()));
        } else {
            filenameTextField.getEditor().setItem(buildDefaultOptionFilename());
        }

        // String firstItem = items.isEmpty() ? null : items.get(0);
        // System.out.println("FIRST ITEM:" + firstItem);
        // filenameTextField.setSelectedItem(null);
        // filenameTextField.setSelectedItem(firstItem);

        repaint();
        revalidate();

        // Dimension d = filenameTextField.getPreferredSize();
        // Dimension newD = new Dimension(100, (int) d.getHeight());
        // filenameTextField.setPreferredSize(newD);

        // String defaultOptionFile = buildDefaultOptionFilename();
        // filenameTextField.getEditor().setItem(defaultOptionFile);

        // showStackTrace = true;
        customInit();

    }

    private static List<String> parseLastFiles(String lastFilesString) {
        if (lastFilesString.isEmpty()) {
            return Collections.emptyList();
        }

        String[] files = lastFilesString.split(ProgramPanel.ITEMS_SEPARATOR_REGEX);
        return Arrays.asList(files);
    }

    /**
     * @return the showStackTrace
     */
    /*
    public boolean isShowStackTrace() {
    return showStackTrace;
    }
     */
    /**
     * @param showStackTrace
     *            the showStackTrace to set
     */
    /*
    public void setShowStackTrace(boolean showStackTrace) {
    this.showStackTrace = showStackTrace;
    }
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
        browseButton.addActionListener(evt -> browseButtonActionPerformed(evt));

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(evt -> cancelButtonActionPerformed(evt));

        startButton.setText("Start");
        startButton.addActionListener(evt -> startButtonActionPerformed(evt));

        // outputArea.setColumns(20);
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

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_browseButtonActionPerformed
        // File optionsFile = new File(filenameTextField.getText());
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
    }// GEN-LAST:event_browseButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelButtonActionPerformed

        worker.shutdown();

    }// GEN-LAST:event_cancelButtonActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_startButtonActionPerformed
        execute();

    }// GEN-LAST:event_startButtonActionPerformed

    /**
     *
     */
    public void execute() {
        // Clear text area
        outputArea.setText("");

        // Check if file is valid
        // String filename = filenameTextField.getText();
        // String filename = filenameTextField.getSelectedItem().toString();
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
            // OPT - It might be enough to remove just the last one, if the jcombobox is already full
            filenameTextField.removeAllItems();
            for (String item : lastUsedItems.getItems()) {
                filenameTextField.addItem(item);
            }
        }

        DataStore setup = application.getPersistence().loadData(file);
        worker.execute(setup);
    }

    private static String encodeList(List<String> lastUsedItems) {
        return lastUsedItems.stream().collect(Collectors.joining(ProgramPanel.ITEMS_SEPARATOR));
    }

    public final void setButtonsEnable(boolean enable) {
        browseButton.setEnabled(enable);
        startButton.setEnabled(enable);
        cancelButton.setEnabled(!enable);
    }

    public App getApplication() {
        return application;
    }

    public JComboBox<String> getFilenameTextField() {
        return filenameTextField;
    }

    private String buildDefaultOptionFilename() {
        // Check if App implements AppDefaultConfig
        if (!AppDefaultConfig.class.isInstance(application)) {
            return ProgramPanel.BLANK_OPTION_FILE;
        }

        return ((AppDefaultConfig) application).defaultConfigFile();
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.guihelper.gui.BasePanels.GuiTab#enterTab(pt.up.fe.specs.guihelper.Gui.BasePanels.TabData)
     */
    @Override
    public void enterTab() {
        File file = getData().get(AppKeys.CONFIG_FILE);
        if (file == null || file.getPath().isEmpty()) {
            return;
        }

        getFilenameTextField().getEditor().setItem(file.getPath());
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.guihelper.gui.BasePanels.GuiTab#exitTab(pt.up.fe.specs.guihelper.Gui.BasePanels.TabData)
     */
    @Override
    public void exitTab() {
        // Config file
        String path = getFilenameTextField().getEditor().getItem().toString();
        if (path.trim().isEmpty()) {
            getData().remove(AppKeys.CONFIG_FILE);
            getData().remove(JOptionKeys.CURRENT_FOLDER_PATH);
            return;
        }

        File configFile = new File(path);
        configFile = configFile.getAbsoluteFile();

        // For the case when there is no config file defined
        File workingFolder = configFile;
        if (!configFile.isDirectory()) {
            workingFolder = configFile.getParentFile();
        }
        // String workingFolderPath = configFile.getAbsoluteFile().getParent();

        getData().set(AppKeys.CONFIG_FILE, new File(getFilenameTextField().getEditor().getItem().toString()));
        getData().set(JOptionKeys.CURRENT_FOLDER_PATH, workingFolder.getPath());
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.guihelper.gui.BasePanels.GuiTab#getTabName()
     */
    @Override
    public String getTabName() {
        return "Program";
    }

}
