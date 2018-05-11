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
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.suikasoft.jOptions.DataStore;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;

public abstract class DataClass<T extends DataClass<T>> {

    private final DataStore data;

    public DataClass() {
        this.data = DataStore.newInstance(getClass());
    }

    public <K> K get(DataKey<K> key) {
        return data.get(key);
    }

    @SuppressWarnings("unchecked")
    public <K, E extends K> T set(DataKey<K> key, E value) {
        data.set(key, value);
        return (T) this;
    }
}
