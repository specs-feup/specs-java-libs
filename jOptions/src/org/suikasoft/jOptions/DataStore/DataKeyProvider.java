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
 * specific language governing permissions and limitations under the License.
 */

package org.suikasoft.jOptions.DataStore;

import org.suikasoft.jOptions.Datakey.DataKey;

/**
 * Interface for classes that provide data keys.
 *
 * <p>This interface defines a contract for providing a DataKey instance.
 */
public interface DataKeyProvider {
    /**
     * Returns the data key associated with this provider.
     *
     * @return the data key
     */
    DataKey<?> getDataKey();
}
