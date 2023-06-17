/**
 * Copyright 2023 SPeCS.
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

package pt.up.fe.specs.util.io;

import java.util.Collection;

import pt.up.fe.specs.util.providers.ResourceProvider;

public class ResourceCollection {

    private final String id;
    private final boolean isIdUnique;
    private final Collection<ResourceProvider> resources;

    /**
     * 
     * @param id
     *            identifier for this collection of resources
     * @param isIdUnique
     *            true if this collection of resources have a unique mapping to this id, false if the resources can
     *            change over time for this id
     * @param resources
     *            a collection of resources
     */
    public ResourceCollection(String id, boolean isIdUnique, Collection<ResourceProvider> resources) {
        this.id = id;
        this.isIdUnique = isIdUnique;
        this.resources = resources;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the isIdUnique
     */
    public boolean isIdUnique() {
        return isIdUnique;
    }

    /**
     * @return the resources
     */
    public Collection<ResourceProvider> getResources() {
        return resources;
    }

}
