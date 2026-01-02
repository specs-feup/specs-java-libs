/**
 * Copyright 2018 SPeCS.
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

package org.suikasoft.jOptions.DataStore;

import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * Generic implementation of a DataClass backed by a DataStore.
 *
 * <p>
 * This class provides a generic DataClass implementation that delegates to a
 * DataStore.
 *
 * @param <T> the type of the DataClass
 */
public class GenericDataClass<T extends DataClass<T>> extends ADataClass<T> {
    /**
     * Constructs a GenericDataClass with the given DataStore.
     *
     * @param data the DataStore backing this DataClass
     */
    public GenericDataClass(DataStore data) {
        super(data);
    }
}
