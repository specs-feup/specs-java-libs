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

package org.suikasoft.jOptions.gui.panels.option;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.suikasoft.jOptions.JOptionKeys;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;

import pt.up.fe.specs.util.SpecsIo;

/**
 *
 * @author Joao Bispo
 */
public class FilePanel extends KeyPanel<File> {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JButton browseButton;
    private final JFileChooser fc;
    private final JTextField textField;

    private FileOpener fileOpener;

    /**
     * Helper constructor for a FilePanel that has a browse button for files and folders.
     * 
     * @param key
     * @param data
     */
    public FilePanel(DataKey<File> key, DataStore data) {
        this(key, data, JFileChooser.FILES_AND_DIRECTORIES, Collections.emptyList());
    }

    /**
     * 
     * @param key
     * @param data
     * @param fileChooserMode
     *            JFileChooser option
     */
    public FilePanel(DataKey<File> key, DataStore data, int fileChooserMode, Collection<String> extensions) {
        super(key, data);
        setOnFileOpened(this::openFile);
        textField = new JTextField();
        browseButton = new JButton();

        // Init file chooser
        fc = new JFileChooser();
        fc.setFileSelectionMode(fileChooserMode);

        // Set extensions
        if (!extensions.isEmpty()) {
            String extensionsString = extensions.stream().collect(Collectors.joining(", *.", "*.", ""));
            FileFilter filter = new FileNameExtensionFilter(key.getLabel() + " files (" + extensionsString + ")",
                    extensions.toArray(new String[0]));

            fc.setFileFilter(filter);
        }

        // Init browse button
        browseButton.setText("Browse...");
        browseButton.addActionListener(this::browseButtonActionPerformed);

        setLayout(new BorderLayout());

        add(textField, BorderLayout.CENTER);
        add(browseButton, BorderLayout.EAST);

    }

    public void setText(String text) {
        // Normalize text
        text = SpecsIo.normalizePath(text);
        textField.setText(text);
    }

    public String getText() {
        return SpecsIo.normalizePath(textField.getText());
    }

    private void browseButtonActionPerformed(ActionEvent evt) {

        File currentFile = getFile(textField.getText(), getKey(), getData());
        if (currentFile.getPath().isEmpty()) {
            Optional<String> currentFolderPath = getData().getTry(JOptionKeys.CURRENT_FOLDER_PATH);
            if (currentFolderPath.isPresent()) {
                currentFile = SpecsIo.getCanonicalFile(new File(currentFolderPath.get()));
            }
        }

        fc.setCurrentDirectory(currentFile);
        int returnVal = fc.showOpenDialog(this);

        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = fc.getSelectedFile();

        // Try to make path relative to current setup file
        Optional<String> currentFolderPath = getData().getTry(JOptionKeys.CURRENT_FOLDER_PATH);
        if (currentFolderPath.isPresent()) {
            String relativePath = SpecsIo.getRelativePath(file, new File(currentFolderPath.get()));

            // Always using relative path if setup file is defined, should be more useful
            textField.setText(relativePath);

        } else {
            textField.setText(file.getAbsolutePath());
        }

        fileOpener.onOpenFile(file);
    }

    private static File getFile(String fieldValue, DataKey<File> key, DataStore data) {

        Optional<String> currentFolderPath = data.getTry(JOptionKeys.CURRENT_FOLDER_PATH);
        if (!currentFolderPath.isPresent()) {
            // LoggingUtils.msgWarn("CHECK THIS CASE, WHEN CONFIG IS NOT DEFINED");
            return new File(fieldValue);
        }

        DataStore tempData = DataStore.newInstance("FilePanelTemp", data);
        // When reading a value from the GUI to the user DataStore, use absolute path

        tempData.set(JOptionKeys.CURRENT_FOLDER_PATH, currentFolderPath.get());
        tempData.set(JOptionKeys.USE_RELATIVE_PATHS, false);
        tempData.setString(key, fieldValue);

        File value = tempData.get(key);

        return value;

    }

    @Override
    public File getValue() {
        return getFile(textField.getText(), getKey(), getData());
    }

    private static String processFile(DataStore data, File file) {
        // If empty path, set empty text field
        if (file.getPath().isEmpty()) {
            return "";
        }

        File currentValue = file;

        // When showing the path in the GUI, make it relative to the current setup file

        Optional<String> currentFolder = data.getTry(JOptionKeys.CURRENT_FOLDER_PATH);
        // System.out.println("GUI SET ENTRY VALUE:" + currentValue);
        // System.out.println("GUI SET CURRENT FOLDER:" + currentFolder);
        if (currentFolder.isPresent()) {
            String relativePath = SpecsIo.getRelativePath(currentValue, new File(currentFolder.get()));
            currentValue = new File(relativePath);
        }

        // System.out.println("CURRENT FOLDER:" + currentFolder);
        // If path is absolute, make it canonical
        if (currentValue.isAbsolute()) {
            return SpecsIo.getCanonicalFile(currentValue).getPath();
        }

        return currentValue.getPath();
    }

    @Override
    public <ET extends File> void setValue(ET value) {
        setText(processFile(getData(), value));
        /*
        // If empty path, set empty text field
        if (value.getPath().isEmpty()) {
        setText("");
        return;
        }
        
        File currentValue = value;
        
        // When showing the path in the GUI, make it relative to the current setup file
        
        Optional<String> currentFolder = getData().getTry(JOptionKeys.CURRENT_FOLDER_PATH);
        // System.out.println("GUI SET ENTRY VALUE:" + currentValue);
        // System.out.println("GUI SET CURRENT FOLDER:" + currentFolder);
        if (currentFolder.isPresent()) {
        String relativePath = IoUtils.getRelativePath(currentValue, new File(currentFolder.get()));
        currentValue = new File(relativePath);
        }
        
        // System.out.println("CURRENT FOLDER:" + currentFolder);
        // If path is absolute, make it canonical
        if (currentValue.isAbsolute()) {
        setText(IoUtils.getCanonicalFile(currentValue).getPath());
        } else {
        setText(currentValue.getPath());
        }
        
        // System.out.println("GUI SET VALUE:" + currentValue);
        */
    }

    /**
     * Event when opening a file
     * 
     * @param f
     */
    public void openFile(File f) {
    }

    public void setOnFileOpened(FileOpener opener) {
        fileOpener = opener;
    }

    public interface FileOpener {
        public void onOpenFile(File f);
    }
}
