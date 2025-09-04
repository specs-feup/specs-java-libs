/*
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
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.providers.impl.GenericResource;

/**
 * Functional interface for providing resources.
 * <p>
 * Used for supplying resource objects.
 * </p>
 *
 * Represents a class which provides a string to a Java resource.
 *
 * <p>
 * The resource must exist, the ResourceProvider is responsible for guaranteeing
 * that the resource is valid.
 *
 * @author Joao Bispo
 * @see SpecsIo#getResource(ResourceProvider)
 *
 */
@FunctionalInterface
public interface ResourceProvider extends FileResourceProvider {

    /**
     * Creates a new instance of ResourceProvider with the given resource string.
     *
     * @param resource the resource string
     * @return a new ResourceProvider instance
     */
    static ResourceProvider newInstance(String resource) {
        return () -> resource;
    }

    /**
     * Creates a new instance of ResourceProvider with the given resource string and
     * version.
     *
     * @param resource the resource string
     * @param version  the version string
     * @return a new ResourceProvider instance
     */
    static ResourceProvider newInstance(String resource, String version) {
        return new GenericResource(resource, version);
    }

    /**
     * Returns the default version string for resources.
     *
     * @return the default version string
     */
    static String getDefaultVersion() {
        return "1.0";
    }

    /**
     * The string corresponding to the path to a resource.
     *
     * <p>
     * Resources are '/' separated, and must not end with a '/'.
     *
     * @return the resource path string
     */
    String getResource();

    /**
     * Returns a list with all the resources, in case this class is an enum.
     * Otherwise, returns an empty list.
     *
     * @return a list of ResourceProvider instances
     */
    default List<ResourceProvider> getEnumResources() {
        ResourceProvider[] resourcesArray = getClass().getEnumConstants();

        if (resourcesArray == null) {
            SpecsLogs.warn("Class '" + getClass() + "' is not an enum, returning empty list");
            return Collections.emptyList();
        }

        List<ResourceProvider> resources = new ArrayList<>(resourcesArray.length);

        Collections.addAll(resources, resourcesArray);

        return resources;
    }

    /**
     * Retrieves resources from a list of classes that implement ResourceProvider.
     *
     * @param providers a list of classes implementing ResourceProvider
     * @return a list of ResourceProvider instances
     */
    public static <K extends Enum<K> & ResourceProvider> List<ResourceProvider> getResourcesFromEnum(
            List<Class<? extends ResourceProvider>> providers) {

        List<ResourceProvider> resources = new ArrayList<>();

        for (Class<? extends ResourceProvider> provider : providers) {
            resources.addAll(ProvidersSupport.getResourcesFromEnumSingle(provider));
        }

        return resources;
    }

    /**
     * Retrieves resources from an array of classes that implement ResourceProvider.
     *
     * @param enumClasses an array of classes implementing ResourceProvider
     * @return a list of ResourceProvider instances
     */
    @SafeVarargs
    public static List<ResourceProvider> getResourcesFromEnum(Class<? extends ResourceProvider>... enumClasses) {
        return getResourcesFromEnum(Arrays.asList(enumClasses));
    }

    /**
     * Utility method which returns the ResourceProviders in an enumeration that
     * implements ResourceProvider.
     *
     * @param enumClass the class of the enumeration
     * @return a list of ResourceProvider instances
     */
    public static <K extends Enum<K> & ResourceProvider> List<ResourceProvider> getResources(
            Class<? extends K> enumClass) {

        K[] enums = enumClass.getEnumConstants();

        List<ResourceProvider> resources = new ArrayList<>(enums.length);

        Collections.addAll(resources, enums);

        return resources;
    }

    /**
     * Returns the name of the last part of the resource, without the 'path'.
     *
     * @return the resource name
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
     * Returns the location of the resource, i.e., the parent package/folder.
     *
     * @return the resource location
     */
    default String getResourceLocation() {
        String resourcePath = getFileLocation();

        // Remove resource name
        int slashIndex = resourcePath.lastIndexOf('/');
        if (slashIndex == -1) {
            return "";
        }

        return resourcePath.substring(0, slashIndex + 1);
    }

    /**
     * Returns the path that should be used when copying this resource. By default
     * returns the same as getResource().
     *
     * @return the file location path
     */
    default String getFileLocation() {
        return getResource();
    }

    /**
     * Writes the resource to the working directory.
     *
     * @return the written file
     */
    default File write() {
        return write(SpecsIo.getWorkingDir());
    }

    /**
     * Helper method which by default overwrites the file.
     *
     * @param folder the folder where the resource will be written
     * @return the written file
     */
    @Override
    default File write(File folder) {
        return write(folder, true);
    }

    /**
     * Writes the resource to the specified folder, with an option to overwrite.
     *
     * @param folder    the folder where the resource will be written
     * @param overwrite whether to overwrite the file if it exists
     * @return the written file
     */
    default File write(File folder, boolean overwrite) {
        return write(folder, overwrite, resourceName -> resourceName);
    }

    /**
     * Writes the resource to the specified folder, with options to overwrite and
     * map the resource name.
     *
     * @param folder     the folder where the resource will be written
     * @param overwrite  whether to overwrite the file if it exists
     * @param nameMapper a function to map the resource name
     * @return the written file
     */
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

        if (!success) {
            throw new RuntimeException("Could not write file '" + outputFile + "'");
        }

        return outputFile;
    }

    /**
     * Reads the contents of this resource.
     *
     * @return the resource contents
     */
    default String read() {
        return SpecsIo.getResource(this);
    }

    /**
     * Returns the version of this resource.
     *
     * @return the version string
     */
    @Override
    default String version() {
        return getDefaultVersion();
    }

    /**
     * Returns the filename of this resource.
     *
     * @return the filename string
     */
    @Override
    default String getFilename() {
        return getResourceName();
    }

    /**
     * Converts this resource to an InputStream.
     *
     * @return the InputStream of the resource
     */
    default InputStream toStream() {
        return SpecsIo.resourceToStream(this);
    }
}
