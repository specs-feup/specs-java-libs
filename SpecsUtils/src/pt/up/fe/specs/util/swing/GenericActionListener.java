/*
 * Copyright 2016 SPeCS Research Group.
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

import java.awt.event.ActionEvent;
import java.io.Serial;
import java.util.function.Consumer;

import javax.swing.AbstractAction;

/**
 * Generic implementation of ActionListener for Java Swing.
 * <p>
 * Provides default (empty) implementation for the actionPerformed method.
 * </p>
 */
public class GenericActionListener extends AbstractAction {

    /**
     * Serial version UID for serialization.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Consumer to handle the action event.
     */
    private final Consumer<ActionEvent> consumer;

    /**
     * Creates a new instance of GenericActionListener with the given consumer.
     *
     * @param consumer the consumer to handle the action event
     * @return a new instance of GenericActionListener
     */
    public static GenericActionListener newInstance(Consumer<ActionEvent> consumer) {
        return new GenericActionListener(consumer);
    }

    /**
     * Constructor for GenericActionListener.
     *
     * @param consumer the consumer to handle the action event
     */
    public GenericActionListener(Consumer<ActionEvent> consumer) {
        this.consumer = consumer;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        consumer.accept(e);
    }

}
