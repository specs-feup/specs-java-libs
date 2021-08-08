/**
 * Copyright 2012 SPeCS Research Group.
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import pt.up.fe.specs.util.Preconditions;
import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.providers.impl.GenericResource;

/**
 * Represents a class which provides a string to a Java resource.
 * 
 * <p>
 * The resource must exist, the ResourceProvider is responsible for guaranteeing that the resource is valid.
 *
 * @author Joao Bispo
 * @see SpecsIo#getResource(ResourceProvider)
 *
 */
@FunctionalInterface
public interface ResourceProvider extends FileResourceProvider {

    static ResourceProvider newInstance(String resource) {
        return () -> resource;
    }

    static ResourceProvider newInstance(String resource, String version) {
        return new GenericResource(resource, version);
    }

    static String getDefaultVersion() {
        return "1.0";
    }

    /**
     * The string corresponding to the path to a resource.
     *
     * <p>
     * Resources are '/' separated, and must not end with a '/'.
     *
     * @return
     */
    String getResource();

    /**
     * Returns a list with all the resources, in case this class is an enum. Otherwise, returns an empty list.
     *
     * @return
     */
    default List<ResourceProvider> getEnumResources() {
        ResourceProvider[] resourcesArray = getClass().getEnumConstants();

        if (resourcesArray == null) {
            SpecsLogs.warn("Class '" + getClass() + "' is not an enum, returning empty list");
            return Collections.emptyList();
        }

        List<ResourceProvider> resources = SpecsFactory.newArrayList(resourcesArray.length);

        for (ResourceProvider provider : resourcesArray) {
            resources.add(provider);
        }

        return resources;
    }

    public static <K extends Enum<K> & ResourceProvider> List<ResourceProvider> getResourcesFromEnum(
            List<Class<? extends ResourceProvider>> providers) {

        List<ResourceProvider> resources = new ArrayList<>();

        for (Class<? extends ResourceProvider> provider : providers) {
            resources.addAll(ProvidersSupport.getResourcesFromEnumSingle(provider));
        }

        return resources;
    }

    @SafeVarargs
    public static List<ResourceProvider> getResourcesFromEnum(Class<? extends ResourceProvider>... enumClasses) {
        return getResourcesFromEnum(Arrays.asList(enumClasses));
    }

    /**
     * Utility method which returns the ResourceProviders in an enumeration that implements ResourceProvider.
     *
     * @param enumClass
     * @return
     */
    public static <K extends Enum<K> & ResourceProvider> List<ResourceProvider> getResources(
            Class<? extends K> enumClass) {

        K[] enums = enumClass.getEnumConstants();

        List<ResourceProvider> resources = SpecsFactory.newArrayList(enums.length);

        for (K anEnum : enums) {
            resources.add(anEnum);
        }

        return resources;
    }

    /**
     *
     * @return the name of the last part of resource, without the 'path'
     */
    default String getResourceName() {
        String resourcePath = getResource();
        // Remove path
        int slashIndex = resourcePath.lastIndexOf('/');
        if (slashIndex == -1) {
            return resourcePath;
        }

        return resourcePath.substring(slashIndex + 1);
    }

    /**
     * Returns the location of the resource, i.e., the parent package/folder
     *
     * @return
     */
    default String getResourceLocation() {
        String resourcePath = getResource();
        // Remove resource name
        int slashIndex = resourcePath.lastIndexOf('/');
        if (slashIndex == -1) {
            return "";
        }

        return resourcePath.substring(0, slashIndex + 1);
    }

    /**
     * Returns the path that should be used when copying this resource. By default returns the same as
     * getResourceLocation().
     * 
     * @return
     */
    default String getFileLocation() {
        return getResourceLocation();
    }

    default File write() {
        return write(SpecsIo.getWorkingDir());
    }

    /**
     * Helper method which by default overwrites the file.
     */
    @Override
    default File write(File folder) {
        return write(folder, true);
    }

    default File write(File folder, boolean overwrite) {
        return write(folder, overwrite, resourceName -> resourceName);
    }

    default File write(File folder, boolean overwrite, Function<String, String> nameMapper) {
        Preconditions.checkArgument(folder.isDirectory(), folder + " does not exist");

        var filename = nameMapper.apply(getResourceName());
        File outputFile = new File(folder, filename);

        // File file already exists and should not overwrite, return
        if (outputFile.exists() & !overwrite) {
            return outputFile;
        }

        // Write file
        boolean success = SpecsIo.resourceCopyWithName(getResource(), filename, folder);
        /*
        boolean success = SpecsIo.write(outputFile, SpecsIo.getResource(this));
        */
        if (!success) {
            throw new RuntimeException("Could not write file '" + outputFile + "'");
        }

        return outputFile;
    }

    /**
     * 
     * @return the contents of this resource
     */
    default String read() {
        return SpecsIo.getResource(this);
    }

    /**
     *
     * @return string representing the version of this resource
     */
    @Override
    default String getVersion() {
        return getDefaultVersion();
    }

    @Override
    default String getFilename() {
        return getResourceName();
    }

    default InputStream toStream() {
        return SpecsIo.resourceToStream(this);
    }
}
