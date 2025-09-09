/**
 * Copyright 2016 SPeCS.
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

package org.suikasoft.jOptions.gui;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * Provider interface for creating KeyPanel instances for a given DataKey and
 * DataStore.
 *
 * @param <T> the type of value handled by the panel
 */
public interface KeyPanelProvider<T> {
    /**
     * Returns a KeyPanel for the given DataKey and DataStore.
     *
     * @param key  the DataKey
     * @param data the DataStore
     * @return a KeyPanel for the key and data
     */
    KeyPanel<T> getPanel(DataKey<T> key, DataStore data);
}
