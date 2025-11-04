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

package pt.up.fe.specs.guihelper.gui;

import java.awt.event.ComponentEvent;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;

/**
 * Base class for implementing a JPanel for a specific FieldType.
 *
 * @author Joao Bispo
 */
public abstract class FieldPanel extends JPanel {
    // public abstract class FieldPanel<T super Object> extends JPanel {

    private static final long serialVersionUID = 1L;
    private long lastTime;

    public FieldPanel() {
	identationLevel = 0;
	lastTime = -1;
    }

    /**
     * Each FieldPanel implements an FieldType.
     * 
     * @return the FieldType implemented by this panel
     */
    public abstract FieldType getType();

    /**
     * Extracts an FieldValue from an FieldPanel with the most current values.
     *
     * @return
     */
    public abstract FieldValue getOption();

    // Reads the value inside FieldValue and updates the FieldPanel.
    /**
     * Updates the FieldPanel with the given object.
     *
     * @param option
     */
    public abstract void updatePanel(Object option);

    // public abstract <? super FieldValue> void updatePanel(T option);

    /**
     *
     * @return the label of this panel, or null if it has none
     */
    public abstract JLabel getLabel();

    protected final int identationLevel;

    /**
     * Default implementation, does nothing.
     * 
     * @param e
     */
    final public void componentResized(ComponentEvent e) {
	long currentTime = System.nanoTime();
	// Intervals of 0.5 seconds
	long interval = 1000000000l;
	if (lastTime == -1) {
	    lastTime = currentTime - interval;
	}

	if (currentTime - interval < 0) {
	    return;
	}

	lastTime = currentTime;

	componentResizedPrivate(e);
    }

    /**
     * Default implementation, does nothing.
     * 
     * @param e
     */
    protected void componentResizedPrivate(ComponentEvent e) {

    }

    /**
     * By default, returns an empty list.
     * 
     * @return the nested panels this panel has, if any.
     */
    public Collection<FieldPanel> getPanels() {
	return Collections.emptyList();
    }
}
