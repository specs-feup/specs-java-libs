/**
 * Copyright 2013 SPeCS Research Group.
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

package org.suikasoft.jOptions.cli;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.suikasoft.jOptions.app.App;
import org.suikasoft.jOptions.app.AppKernel;
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.gui.panels.app.TabProvider;
import org.suikasoft.jOptions.persistence.XmlPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.providers.ResourceProvider;

/**
 * Generic implementation of the {@link App} interface for jOptions-based
 * applications.
 */
public class GenericApp implements App {

    private final String name;
    private final StoreDefinition definition;
    private final AppPersistence persistence;
    private final AppKernel kernel;
    private final Collection<TabProvider> otherTabs;
    private final Class<?> nodeClass;
    private final ResourceProvider icon;

    /**
     * Constructs a GenericApp instance with the specified parameters.
     *
     * @param name        the name of the application
     * @param definition  the store definition for the application
     * @param persistence the persistence mechanism for the application
     * @param kernel      the kernel of the application
     * @param otherTabs   additional tabs to be displayed in the application
     * @param nodeClass   the class representing the node structure
     * @param icon        the icon resource for the application
     */
    private GenericApp(String name, StoreDefinition definition,
            AppPersistence persistence, AppKernel kernel, Collection<TabProvider> otherTabs, Class<?> nodeClass,
            ResourceProvider icon) {

        if (name == null) {
            throw new IllegalArgumentException("Application name cannot be null");
        }
        if (definition == null) {
            throw new IllegalArgumentException("Store definition cannot be null");
        }
        if (persistence == null) {
            throw new IllegalArgumentException("Persistence mechanism cannot be null");
        }
        if (kernel == null) {
            throw new IllegalArgumentException("Application kernel cannot be null");
        }

        this.name = name;
        this.definition = definition;
        this.persistence = persistence;
        this.kernel = kernel;
        this.otherTabs = otherTabs;
        this.nodeClass = nodeClass;
        this.icon = icon;
    }

    /**
     * Constructs a GenericApp instance with the specified parameters, using default
     * values for otherTabs, nodeClass, and icon.
     *
     * @param name        the name of the application
     * @param definition  the store definition for the application
     * @param persistence the persistence mechanism for the application
     * @param kernel      the kernel of the application
     */
    public GenericApp(String name, StoreDefinition definition,
            AppPersistence persistence, AppKernel kernel) {

        this(name, definition, persistence, kernel, Collections.emptyList(), null, null);
    }

    /**
     * Constructs a GenericApp instance with the specified parameters, using
     * XmlPersistence as the default persistence mechanism.
     *
     * @param name       the name of the application
     * @param definition the store definition for the application
     * @param kernel     the kernel of the application
     */
    public GenericApp(String name, StoreDefinition definition, AppKernel kernel) {

        this(name, definition, new XmlPersistence(definition), kernel, Collections.emptyList(), null, null);
    }

    /**
     * Gets the store definition of the application.
     *
     * @return the store definition
     */
    @Override
    public StoreDefinition getDefinition() {
        return definition;
    }

    /**
     * Gets the persistence mechanism of the application.
     *
     * @return the persistence mechanism
     */
    @Override
    public AppPersistence getPersistence() {
        return persistence;
    }

    /**
     * Gets the kernel of the application.
     *
     * @return the kernel
     */
    @Override
    public AppKernel getKernel() {
        return kernel;
    }

    /**
     * Gets the name of the application.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the additional tabs to be displayed in the application.
     *
     * @return a collection of tab providers
     */
    @Override
    public Collection<TabProvider> getOtherTabs() {
        return otherTabs;
    }

    /**
     * Gets the icon resource for the application.
     *
     * @return an optional containing the icon resource, or empty if not set
     */
    @Override
    public Optional<ResourceProvider> getIcon() {
        return Optional.ofNullable(icon);
    }

    /**
     * Sets additional tabs for the application.
     *
     * @param otherTabs a collection of tab providers
     * @return a new GenericApp instance with the updated tabs
     */
    public GenericApp setOtherTabs(Collection<TabProvider> otherTabs) {
        if (otherTabs == null) {
            throw new IllegalArgumentException("Other tabs collection cannot be null");
        }
        return new GenericApp(name, definition, persistence, kernel, otherTabs, nodeClass, icon);
    }

    /**
     * Sets additional tabs for the application.
     *
     * @param otherTabs an array of tab providers
     * @return a new GenericApp instance with the updated tabs
     */
    public GenericApp setOtherTabs(TabProvider... otherTabs) {
        return setOtherTabs(Arrays.asList(otherTabs));
    }

    /**
     * Sets the node class for the application.
     *
     * @param nodeClass the class representing the node structure
     * @return a new GenericApp instance with the updated node class
     */
    public GenericApp setNodeClass(Class<?> nodeClass) {
        return new GenericApp(name, definition, persistence, kernel, otherTabs, nodeClass, icon);
    }

    /**
     * Sets the icon resource for the application.
     *
     * @param icon the icon resource
     * @return a new GenericApp instance with the updated icon
     */
    public GenericApp setIcon(ResourceProvider icon) {
        return new GenericApp(name, definition, persistence, kernel, otherTabs, nodeClass, icon);
    }

    /**
     * Gets the node class for the application.
     *
     * @return the node class, or the class of this instance if not set
     */
    @Override
    public Class<?> getNodeClass() {
        if (nodeClass == null) {
            return this.getClass();
        }

        return nodeClass;
    }

}
