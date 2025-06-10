/*
 * GenericMouseListener.java
 *
 * Utility class for handling mouse events in Java Swing applications. Provides a generic implementation of MouseListener for use in the SPeCS ecosystem.
 *
 * Copyright 2025 SPeCS Research Group.
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

package pt.up.fe.specs.util.swing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

/**
 * Generic implementation of MouseListener for Java Swing.
 * <p>
 * Provides default (empty) implementations for all MouseListener methods.
 * </p>
 */
public class GenericMouseListener implements MouseListener {

    /**
     * Consumer to handle mouse click events.
     */
    private Consumer<MouseEvent> onClick;

    /**
     * Consumer to handle mouse press events.
     */
    private Consumer<MouseEvent> onPress;

    /**
     * Consumer to handle mouse release events.
     */
    private Consumer<MouseEvent> onRelease;

    /**
     * Consumer to handle mouse entered events.
     */
    private Consumer<MouseEvent> onEntered;

    /**
     * Consumer to handle mouse exited events.
     */
    private Consumer<MouseEvent> onExited;

    /**
     * Default constructor initializing all event handlers to empty implementations.
     */
    public GenericMouseListener() {
        onClick = onPress = onRelease = onEntered = onExited = empty();
    }

    /**
     * Creates a GenericMouseListener with a specific click event handler.
     *
     * @param listener the Consumer to handle mouse click events
     * @return a new GenericMouseListener instance
     */
    public static GenericMouseListener click(Consumer<MouseEvent> listener) {
        return new GenericMouseListener().onClick(listener);
    }

    /**
     * Sets the click event handler.
     *
     * @param listener the Consumer to handle mouse click events
     * @return the current GenericMouseListener instance
     */
    public GenericMouseListener onClick(Consumer<MouseEvent> listener) {
        onClick = listener;
        return this;
    }

    /**
     * Sets the press event handler.
     *
     * @param listener the Consumer to handle mouse press events
     * @return the current GenericMouseListener instance
     */
    public GenericMouseListener onPressed(Consumer<MouseEvent> listener) {
        onPress = listener;
        return this;
    }

    /**
     * Sets the release event handler.
     *
     * @param listener the Consumer to handle mouse release events
     * @return the current GenericMouseListener instance
     */
    public GenericMouseListener onRelease(Consumer<MouseEvent> listener) {
        onRelease = listener;
        return this;
    }

    /**
     * Sets the entered event handler.
     *
     * @param listener the Consumer to handle mouse entered events
     * @return the current GenericMouseListener instance
     */
    public GenericMouseListener onEntered(Consumer<MouseEvent> listener) {
        onEntered = listener;
        return this;
    }

    /**
     * Sets the exited event handler.
     *
     * @param listener the Consumer to handle mouse exited events
     * @return the current GenericMouseListener instance
     */
    public GenericMouseListener onExited(Consumer<MouseEvent> listener) {
        onExited = listener;
        return this;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        onClick.accept(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        onPress.accept(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        onRelease.accept(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        onEntered.accept(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        onExited.accept(e);
    }

    /**
     * Provides an empty implementation for event handlers.
     *
     * @return a Consumer that does nothing
     */
    private static Consumer<MouseEvent> empty() {
        return e -> {
        };
    }

}
