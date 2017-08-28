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
 * specific language governing permissions and limitations under the License. under the License.
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
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.providers.ResourceProvider;

/**
 * @author Joao Bispo
 *
 */
public class GenericApp implements App {

    private final String name;
    private final StoreDefinition definition;
    private final AppPersistence persistence;
    private final AppKernel kernel;
    private final Collection<TabProvider> otherTabs;
    private final Class<?> nodeClass;
    private final ResourceProvider icon;

    public GenericApp(String name, StoreDefinition definition,
            AppPersistence persistence, AppKernel kernel) {

        this(name, definition, persistence, kernel, Collections.emptyList(), null, null);
    }

    private GenericApp(String name, StoreDefinition definition,
            AppPersistence persistence, AppKernel kernel, Collection<TabProvider> otherTabs, Class<?> nodeClass,
            ResourceProvider icon) {

        this.name = name;
        this.definition = definition;
        this.persistence = persistence;
        this.kernel = kernel;
        this.otherTabs = otherTabs;
        this.nodeClass = nodeClass;
        this.icon = icon;
    }

    /* (non-Javadoc)
     * @see org.suikasoft.jOptions.CommandLine.Interfaces.App#getDefinition()
     */
    @Override
    public StoreDefinition getDefinition() {
        return definition;
    }

    /* (non-Javadoc)
     * @see org.suikasoft.jOptions.CommandLine.Interfaces.App#getPersistence()
     */
    @Override
    public AppPersistence getPersistence() {
        return persistence;
    }

    /* (non-Javadoc)
     * @see org.suikasoft.jOptions.CommandLine.Interfaces.App#getKernel()
     */
    @Override
    public AppKernel getKernel() {
        return kernel;
    }

    /* (non-Javadoc)
     * @see org.suikasoft.jOptions.CommandLine.Interfaces.App#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<TabProvider> getOtherTabs() {
        return otherTabs;
    }

    @Override
    public Optional<ResourceProvider> getIcon() {
        return Optional.ofNullable(icon);
    }

    public GenericApp setOtherTabs(Collection<TabProvider> otherTabs) {
        return new GenericApp(name, definition, persistence, kernel, otherTabs, nodeClass, icon);
    }

    public GenericApp setOtherTabs(TabProvider... otherTabs) {
        return setOtherTabs(Arrays.asList(otherTabs));
    }

    public GenericApp setNodeClass(Class<?> nodeClass) {
        return new GenericApp(name, definition, persistence, kernel, otherTabs, nodeClass, icon);
    }

    public GenericApp setIcon(ResourceProvider icon) {
        return new GenericApp(name, definition, persistence, kernel, otherTabs, nodeClass, icon);
    }

    @Override
    public Class<?> getNodeClass() {
        if (nodeClass == null) {
            return this.getClass();
        }

        return nodeClass;
    }

}
