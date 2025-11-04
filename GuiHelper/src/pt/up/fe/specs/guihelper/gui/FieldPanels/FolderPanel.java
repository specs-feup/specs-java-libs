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

package pt.up.fe.specs.guihelper.gui.FieldPanels;

import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.gui.FieldPanel;
import pt.up.fe.specs.guihelper.gui.Interfaces.FileReceiver;
import pt.up.fe.specs.util.SpecsIo;

/**
 *
 * @author Joao Bispo
 */
public class FolderPanel extends FieldPanel implements FileReceiver {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JButton browseButton;
    private final JFileChooser fc;
    private final JLabel label;
    private final JTextField textField;
    private final boolean resizeTextfield;

    private File currentFolder;

    public FolderPanel(String labelName) {
	label = new JLabel(labelName + ":");
	textField = new JTextField(15);
	browseButton = new JButton();

	// Init file chooser
	fc = new JFileChooser();
	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	// Init browse button
	browseButton.setText("Browse...");
	browseButton.addActionListener(this::browseButtonActionPerformed);

	add(label);
	add(textField);
	add(browseButton);

	// setLayout(new FlowLayout(FlowLayout.LEFT));
	FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
	// flowLayout.setVgap(10);
	setLayout(flowLayout);
	// setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

	resizeTextfield = true;

	currentFolder = null;
    }

    public void setText(String text) {
	textField.setText(text);
    }

    public String getText() {
	return textField.getText();
    }

    @Override
    public FieldType getType() {
	return FieldType.string;
    }

    @Override
    public FieldValue getOption() {
	return FieldValue.create(getText(), getType());
    }

    @Override
    // public void updatePanel(FieldValue option) {
    public void updatePanel(Object option) {
	// String newValue = BaseUtils.getString(option);
	String newValue = (String) option;
	setText(newValue);
    }

    @Override
    public JLabel getLabel() {
	return label;
    }

    @Override
    protected void componentResizedPrivate(ComponentEvent e) {
	if (!resizeTextfield) {
	    return;
	}

	Rectangle r = e.getComponent().getBounds();

	int borders = 40;

	// Resize text field.
	int newWidth = r.width - label.getBounds().width - borders * 2;

	// Consider 5 pixels per column
	int pixelsPerColumn = 15;
	int newColumns = (int) Math.ceil(newWidth / pixelsPerColumn);

	textField.setColumns(newColumns);

    }

    private void browseButtonActionPerformed(ActionEvent evt) {

	fc.setCurrentDirectory(getOptionFile());
	int returnVal = fc.showOpenDialog(this);

	if (returnVal != JFileChooser.APPROVE_OPTION) {
	    return;
	}

	File file = fc.getSelectedFile();

	// Try to make path relative to current setup file
	if (currentFolder != null) {
	    String relativePath = SpecsIo.getRelativePath(file, currentFolder);

	    // Always using relative path if setup file is defined, should be more useful
	    textField.setText(relativePath);
	    /*
	    	    // If relative path starts with '..', use absolute path instead
	    	    if (relativePath.startsWith("..")) {
	    		textField.setText(file.getAbsolutePath());
	    	    } else {
	    		textField.setText(relativePath);
	    	    }
	    	    */
	} else {
	    textField.setText(file.getAbsolutePath());
	}

    }

    private File getOptionFile() {
	File optionsFile;

	if (currentFolder != null) {
	    // Use parent, so we can see the folder in the browser
	    optionsFile = new File(currentFolder, textField.getText()).getParentFile();
	} else {
	    optionsFile = new File(textField.getText());
	}

	if (!optionsFile.exists()) {
	    optionsFile = new File("./");
	}

	return optionsFile;
    }

    @Override
    public void updateFile(File file) {

	if (file.isDirectory()) {
	    currentFolder = file;
	    return;
	}

	if (file.isFile()) {
	    currentFolder = file.getParentFile();
	    return;
	}
    }

}
