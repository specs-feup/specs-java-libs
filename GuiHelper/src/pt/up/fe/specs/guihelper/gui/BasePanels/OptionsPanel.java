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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.guihelper.gui.BasePanels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pt.up.fe.specs.guihelper.GuiHelperUtils;
import pt.up.fe.specs.guihelper.Base.SetupDefinition;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.guihelper.gui.AppFrame;
import pt.up.fe.specs.guihelper.gui.FieldPanel;
import pt.up.fe.specs.guihelper.gui.FieldPanels.SetupPanel;
import pt.up.fe.specs.guihelper.gui.Interfaces.FileReceiver;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Panel which loads and can edit the options file.
 * 
 * @author Joao Bispo
 */
public class OptionsPanel extends GuiTab {

    private static final long serialVersionUID = 1L;

    private BaseSetupPanel appFilePanel;
    private List<FileReceiver> fileReceivers;
    private SetupData optionFile;
    private JButton saveButton;
    private JButton saveAsButton;
    private JLabel fileInfo;

    private File outputFile;

    private List<SetupPanel> setupPanels;

    public OptionsPanel(SetupDefinition enumKeys) {
        this(enumKeys, Collections.emptyList());
    }

    public OptionsPanel(SetupDefinition enumKeys, List<JPanel> panels) {
        JComponent optionsPanel = initEnumOptions(enumKeys);
        initSetupPanels();

        SetupData newSetup = SetupData.create(enumKeys);
        assignNewOptionFile(newSetup);
        fileInfo = new JLabel();
        updateFileInfoString();

        saveButton = new JButton("Save");
        // By default, the save button is disable, until there is a valid
        // file to save to.
        saveButton.setEnabled(false);
        saveAsButton = new JButton("Save as...");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }

        });

        saveAsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                saveAsButtonActionPerformed(evt);
            }

        });

        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        savePanel.add(saveButton);
        savePanel.add(saveAsButton);
        savePanel.add(fileInfo);

        // If optionFile no file, save button is null;
        // Only "unnulls" after "Save As..." and after successful update.

        setLayout(new BorderLayout(5, 5));
        add(savePanel, BorderLayout.PAGE_START);

        add(optionsPanel, BorderLayout.CENTER);

        /*
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(savePanel);
        add(Box.createRigidArea(new Dimension(0,5)));
        add(optionsPanel);
        add(Box.createVerticalGlue());
         *
         */

        // add(appFilePanel, BorderLayout.CENTER);
        // panels = ((AppFilePanel)appFilePanel).getPanels();
    }

    /**
     * Find setup panels and adds them to the list
     */
    private void initSetupPanels() {
        setupPanels = new ArrayList<>();
        Map<String, FieldPanel> panelMap = appFilePanel.getPanels();
        for (String key : panelMap.keySet()) {
            SetupPanel setupPanel = getSetupPanel(panelMap.get(key));
            if (setupPanel == null) {
                continue;
            }
            setupPanels.add(setupPanel);
        }
    }

    private static SetupPanel getSetupPanel(FieldPanel panel) {
        Class<?>[] interfaces = panel.getClass().getInterfaces();
        Set<Class<?>> classSet = new HashSet<>(Arrays.asList(interfaces));
        if (!classSet.contains(SetupPanel.class)) {
            return null;
        }

        return (SetupPanel) panel;
    }

    public SetupData getOptionFile() {
        return optionFile;
    }

    private void saveButtonActionPerformed(ActionEvent evt) {
        updateInternalMap();
        GuiHelperUtils.saveData(outputFile, optionFile);
    }

    private void saveAsButtonActionPerformed(ActionEvent evt) {
        JFileChooser fc;

        if (outputFile == null) {
            fc = new JFileChooser(new File("./"));
        } else {
            fc = new JFileChooser(outputFile);
        }

        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            updateFile(file);
        }
    }

    public void updateValues(String optionsFilename) {
        // Check if filename is a valid optionsfile
        File file = new File(optionsFilename);
        if (!file.isFile()) {
            SpecsLogs.warn("Could not open file '" + optionsFilename + "'");
            outputFile = null;
            saveButton.setEnabled(false);
            updateFileInfoString();
            return;
        }

        SetupData newMap = GuiHelperUtils.loadData(file);
        if (newMap == null) {
            SpecsLogs.warn("Given file '" + optionsFilename + "' is not a compatible options file.");
            outputFile = null;
            saveButton.setEnabled(false);
            updateFileInfoString();
            return;
        }

        // Load file
        assignNewOptionFile(newMap);

        appFilePanel.loadValues(optionFile);
        saveButton.setEnabled(true);
        updateFileInfoString();

        // Update receivers
        for (FileReceiver fileReceiver : fileReceivers) {
            fileReceiver.updateFile(file);
        }

    }

    private JComponent initEnumOptions(SetupDefinition keys) {
        appFilePanel = new BaseSetupPanel(keys);

        // Build file receivers recursively
        fileReceivers = getFileReceivers(appFilePanel.getPanels().values());

        JScrollPane scrollPane = new JScrollPane();

        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.setPreferredSize(new Dimension(AppFrame.PREFERRED_WIDTH + 10, AppFrame.PREFERRED_HEIGHT + 10));
        scrollPane.setViewportView(appFilePanel);

        return scrollPane;

    }

    private static List<FileReceiver> getFileReceivers(Collection<FieldPanel> panels) {
        return getFileReceivers(panels, new ArrayList<>());
    }

    private static List<FileReceiver> getFileReceivers(Collection<FieldPanel> panels,
            List<FileReceiver> fileReceivers) {
        for (FieldPanel panel : panels) {
            // Add itself if a file receiver
            if (panel instanceof FileReceiver) {
                fileReceivers.add((FileReceiver) panel);
            }

            // If panel contains other panels, add them too
            Collection<FieldPanel> children = panel.getPanels();
            if (!children.isEmpty()) {
                getFileReceivers(children, fileReceivers);
            }
        }

        return fileReceivers;
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
    private void updateFile(File file) {
        updateInternalMap();
        outputFile = file;
        saveButton.setEnabled(true);
        updateFileInfoString();

    }

    private void updateInternalMap() {
        // Get info from panels
        SetupData updatedMap = appFilePanel.getMapWithValues();
        // Update internal optionfile
        optionFile = updatedMap;
    }

    /**
     * Can only be called after setup panels are initallized.
     * 
     * @param newOptionFile
     */
    private void assignNewOptionFile(SetupData newOptionFile) {
        optionFile = newOptionFile;
    }

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
    public void enterTab(TabData data) {
        File outputFile = data.getConfigFile();
        if (outputFile == null) {
            return;
        }

        if (outputFile.exists()) {
            this.setOutputFile(outputFile);
        }
        if (outputFile.isFile()) {
            this.updateValues(outputFile.getPath());
        }
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.guihelper.gui.BasePanels.GuiTab#exitTab(pt.up.fe.specs.guihelper.Gui.BasePanels.TabData)
     */
    @Override
    public void exitTab(TabData data) {
        data.setConfigFile(this.getOutputFile());
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.guihelper.gui.BasePanels.GuiTab#getTabName()
     */
    @Override
    public String getTabName() {
        return "Options";
    }

}
