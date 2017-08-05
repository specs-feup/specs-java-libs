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

package pt.up.fe.specs.guihelper.gui.FieldPanels;

import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.gui.FieldPanel;

/**
 *
 * @author Joao Bispo
 */
public class StringPanel extends FieldPanel {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private JLabel label;
    private JTextField textField;
    private boolean resizeTextfield;

    public StringPanel(String labelName) {
	label = new JLabel(labelName + ":");
	textField = new JTextField(15);

	add(label);
	add(textField);
	// setLayout(new FlowLayout(FlowLayout.LEFT));
	FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
	// flowLayout.setVgap(10);
	setLayout(flowLayout);
	// setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

	resizeTextfield = true;
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

	int borders = 30;

	// Resize text field.
	int newWidth = r.width - label.getBounds().width - borders * 2;

	// Consider 5 pixels per column
	int pixelsPerColumn = 15;
	int newColumns = (int) Math.ceil(newWidth / pixelsPerColumn);

	textField.setColumns(newColumns);

    }
}
