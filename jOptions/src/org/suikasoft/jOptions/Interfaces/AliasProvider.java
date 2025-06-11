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

package org.suikasoft.jOptions.Interfaces;

import java.util.Map;

/**
 * Interface for providing alias mappings for names.
 * <p>
 * This interface defines a contract for classes that need to provide mappings between alias names and their
 * corresponding original names. Implementing classes should ensure that the mappings are accurate and up-to-date.
 * </p>
 */
public interface AliasProvider {

    /**
     * Maps alias names to the corresponding original name.
     * <p>
     * This method returns a map where the keys are alias names and the values are the original names they represent.
     * The map should not contain null keys or values.
     * </p>
     *
     * @return a map from alias to original name
     */
    Map<String, String> getAlias();
}
