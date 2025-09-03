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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.providers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for managing and accessing resources.
 * <p>
 * Provides methods for loading, caching, and retrieving resources.
 * </p>
 *
 * @author Joao Bispo
 */
public class Resources {

    /**
     * The base folder where resources are located.
     */
    private final String baseFolder;

    /**
     * A list of resource names.
     */
    private final List<String> resources;

    /**
     * Constructs a Resources object with a base folder and an array of resource
     * names.
     *
     * @param baseFolder the base folder where resources are located
     * @param resources  an array of resource names
     */
    public Resources(String baseFolder, String... resources) {
        this(baseFolder, Arrays.asList(resources));
    }

    /**
     * Constructs a Resources object with a base folder and a list of resource
     * names.
     *
     * @param baseFolder the base folder where resources are located
     * @param resources  a list of resource names
     */
    public Resources(String baseFolder, List<String> resources) {
        this.baseFolder = baseFolder.endsWith("/") ? baseFolder : baseFolder + "/";
        this.resources = resources;
    }

    /**
     * Retrieves a list of ResourceProvider objects corresponding to the resources.
     *
     * @return a list of ResourceProvider objects
     */
    public List<ResourceProvider> getResources() {
        return resources.stream()
                .map(resource -> baseFolder + resource)
                .map(resource -> ResourceProvider.newInstance(resource))
                .collect(Collectors.toList());
    }

}
