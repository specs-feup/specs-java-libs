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

package org.suikasoft.jOptions.gui.panels.option;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.Serial;
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
import org.suikasoft.jOptions.gui.panels.app.AppKeys;

import pt.up.fe.specs.util.SpecsIo;

/**
 * Panel for selecting and displaying file or directory paths using a text field
 * and browse button.
 *
 * <p>
 * This panel provides a file chooser dialog and text field for DataKey values
 * of type File.
 */
public class FilePanel extends KeyPanel<File> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JButton browseButton;
    private final JFileChooser fc;
    private final JTextField textField;

    private FileOpener fileOpener;

    /**
     * Helper constructor for a FilePanel that has a browse button for files and
     * folders.
     *
     * @param key  the DataKey
     * @param data the DataStore
     */
    public FilePanel(DataKey<File> key, DataStore data) {
        this(key, data, JFileChooser.FILES_AND_DIRECTORIES, Collections.emptyList());
    }

    /**
     * Constructs a FilePanel with a specific file chooser mode and file extensions.
     *
     * @param key             the DataKey
     * @param data            the DataStore
     * @param fileChooserMode JFileChooser option
     * @param extensions      the allowed file extensions
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

    /**
     * Sets the text in the text field.
     *
     * @param text the text to set
     */
    public void setText(String text) {
        textField.setText(text);
    }

    /**
     * Gets the text from the text field.
     *
     * @return the text in the text field
     */
    public String getText() {
        return textField.getText();
    }

    private void browseButtonActionPerformed(ActionEvent evt) {

        File currentFile = getFile(textField.getText(), getKey(), getData());
        if (currentFile.getPath().isEmpty()) {
            Optional<String> currentFolderPath = getData().get(JOptionKeys.CURRENT_FOLDER_PATH);
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
        Optional<String> currentFolderPath = getData().get(JOptionKeys.CURRENT_FOLDER_PATH);
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

        Optional<String> currentFolderPath = data.get(JOptionKeys.CURRENT_FOLDER_PATH);
        if (currentFolderPath.isEmpty()) {
            return new File(fieldValue);
        }

        DataStore tempData = DataStore.newInstance("FilePanelTemp", data);
        // When reading a value from the GUI to the user DataStore, use absolute path

        tempData.set(JOptionKeys.CURRENT_FOLDER_PATH, currentFolderPath);
        tempData.set(JOptionKeys.USE_RELATIVE_PATHS, false);
        data.getTry(AppKeys.CONFIG_FILE).ifPresent(file -> tempData.set(AppKeys.CONFIG_FILE, file));

        tempData.setString(key, fieldValue);

        return tempData.get(key);

    }

    /**
     * Gets the value of the file from the text field.
     *
     * @return the file value
     */
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

        Optional<String> currentFolder = data.get(JOptionKeys.CURRENT_FOLDER_PATH);
        if (currentFolder.isPresent()) {
            String relativePath = SpecsIo.getRelativePath(currentValue, new File(currentFolder.get()));
            currentValue = new File(relativePath);
        }

        // If path is absolute, make it canonical
        if (currentValue.isAbsolute()) {
            return SpecsIo.getCanonicalFile(currentValue).getPath();
        }

        return currentValue.getPath();
    }

    /**
     * Sets the value of the file in the text field.
     *
     * @param value the file value to set
     */
    @Override
    public <ET extends File> void setValue(ET value) {
        setText(processFile(getData(), value));
    }

    /**
     * Event when opening a file.
     *
     * @param f the file being opened
     */
    public void openFile(File f) {
    }

    /**
     * Sets the action to be performed when a file is opened.
     *
     * @param opener the FileOpener instance
     */
    public void setOnFileOpened(FileOpener opener) {
        fileOpener = opener;
    }

    /**
     * Interface for handling file opening events.
     */
    public interface FileOpener {
        /**
         * Called when a file is opened.
         *
         * @param f the file that was opened
         */
        public void onOpenFile(File f);
    }
}
