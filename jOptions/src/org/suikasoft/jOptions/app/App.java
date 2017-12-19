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

package org.suikasoft.jOptions.app;

import java.util.Collection;
import java.util.Optional;

import org.suikasoft.jOptions.cli.GenericApp;
import org.suikasoft.jOptions.gui.panels.app.TabProvider;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.providers.ResourceProvider;

/**
 * @author Joao Bispo
 *
 */
public interface App {

    /**
     *
     * @return name of the app
     */
    String getName();

    /**
     * The options available for this app.
     *
     * @return
     */
    StoreDefinition getDefinition();

    /**
     * The interface for loading and storing configurations.
     *
     * @return
     */
    AppPersistence getPersistence();

    AppKernel getKernel();

    Collection<TabProvider> getOtherTabs();

    Class<?> getNodeClass();

    Optional<ResourceProvider> getIcon();

    /**
     * Creates a new App.
     *
     * @param name
     * @param definition
     * @param persistence
     * @param kernel
     * @return
     */
    static GenericApp newInstance(String name, StoreDefinition definition,
            AppPersistence persistence, AppKernel kernel) {

        return new GenericApp(name, definition, persistence, kernel);
    }

    static GenericApp newInstance(StoreDefinition definition,
            AppPersistence persistence, AppKernel kernel) {

        return newInstance(definition.getName(), definition, persistence, kernel);
    }

}
