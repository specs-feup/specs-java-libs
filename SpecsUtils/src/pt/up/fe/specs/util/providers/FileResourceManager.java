/*
 * Copyright 2018 SPeCS Research Group.
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

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.properties.SpecsProperties;

/**
 * Utility class for managing file resources.
 * <p>
 * Provides methods for loading, caching, and accessing files.
 * </p>
 */
public class FileResourceManager {

    /**
     * Map of available resources, keyed by their names.
     */
    private final Map<String, FileResourceProvider> availableResources;

    /**
     * Map of local resources, keyed by their names.
     */
    private final Map<String, File> localResources;

    /**
     * Creates a FileResourceManager instance from an enum class.
     *
     * @param <E>       the type of the enum
     * @param enumClass the class of the enum
     * @return a new FileResourceManager instance
     */
    public static <E extends Enum<E> & Supplier<FileResourceProvider>> FileResourceManager fromEnum(
            Class<E> enumClass) {

        Map<String, FileResourceProvider> availableResources = new LinkedHashMap<>();
        for (E anEnum : SpecsEnums.extractValues(enumClass)) {
            availableResources.put(anEnum.name(), anEnum.get());
        }

        return new FileResourceManager(availableResources);
    }

    /**
     * Constructs a FileResourceManager with the given available resources.
     *
     * @param availableResources a map of available resources
     */
    public FileResourceManager(Map<String, FileResourceProvider> availableResources) {
        this.availableResources = availableResources;

        // Initialize local resources
        this.localResources = new HashMap<>();
    }

    /**
     * Adds local resources from a specified file.
     *
     * @param localResourcesFilename the filename of the local resources file
     */
    public void addLocalResources(String localResourcesFilename) {
        Map<String, File> resources = buildLocalResources(localResourcesFilename);
        this.localResources.putAll(resources);
    }

    /**
     * Builds a map of local resources from a specified file.
     *
     * @param localResourcesFilename the filename of the local resources file
     * @return a map of local resources
     */
    private Map<String, File> buildLocalResources(String localResourcesFilename) {
        // Check if there is a local resources file
        Optional<File> localResourcesTry = SpecsIo.getLocalFile(localResourcesFilename, getClass());

        if (localResourcesTry.isEmpty()) {
            return new HashMap<>();
        }

        // Read file
        SpecsProperties localResources = SpecsProperties.newInstance(localResourcesTry.get());
        Map<String, File> localResourcesMap = new HashMap<>();
        // Check that for each property, there is a resource
        for (Object key : localResources.getProperties().keySet()) {
            if (!availableResources.containsKey(key.toString())) {
                SpecsLogs.msgInfo(
                        "Resource '" + key + "' in file '" + localResourcesTry.get().getAbsolutePath()
                                + "' not valid. Valid resources:" + availableResources.keySet());
                continue;
            }

            // Check if empty filename
            String filename = localResources.get(key::toString);
            if (filename.trim().isEmpty()) {
                continue;
            }

            // Get file of local resources
            Optional<File> localFile = localResources.getExistingFile(key::toString);

            if (localFile.isEmpty()) {
                SpecsLogs.msgInfo(
                        "Resource '" + key + "' in file '" + localResourcesTry.get().getAbsolutePath()
                                + "' points to non-existing file, ignoring resource.");
                continue;
            }

            localResourcesMap.put(key.toString(), localFile.get());
        }

        return localResourcesMap;
    }

    /**
     * Retrieves a file resource provider for the given enum value.
     *
     * @param resourceEnum the enum value representing the resource
     * @return the file resource provider
     */
    public FileResourceProvider get(Enum<?> resourceEnum) {
        return get(resourceEnum.name());
    }

    /**
     * Retrieves a file resource provider for the given resource name.
     *
     * @param resourceName the name of the resource
     * @return the file resource provider
     */
    public FileResourceProvider get(String resourceName) {
        // 1. Check if there is a local resource for this resource
        File localResource = localResources.get(resourceName);
        if (localResource != null) {
            SpecsLogs.debug(() -> "Using local resource '" + localResource.getAbsolutePath() + "'");
            String version = availableResources.get(resourceName).version();
            return FileResourceProvider.newInstance(localResource, version);
        }

        // 2. Check from available resources
        FileResourceProvider availableResource = availableResources.get(resourceName);
        if (availableResource != null) {
            return availableResource;
        }

        throw new RuntimeException(
                "Resource '" + resourceName + "' not available. Available resources: " + availableResources.keySet());
    }

}
