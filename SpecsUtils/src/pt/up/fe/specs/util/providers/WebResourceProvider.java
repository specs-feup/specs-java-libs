/*
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

package pt.up.fe.specs.util.providers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.providers.impl.GenericWebResourceProvider;

/**
 * Functional interface for providing web resources.
 * <p>
 * Used for supplying web-based resources.
 * </p>
 *
 * @author Joao Bispo
 */
public interface WebResourceProvider extends FileResourceProvider {

    /**
     * Creates a new instance of WebResourceProvider with the given root URL and
     * resource URL.
     *
     * @param rootUrl     the root URL of the web resource
     * @param resourceUrl the specific resource URL
     * @return a new WebResourceProvider instance
     */
    static WebResourceProvider newInstance(String rootUrl, String resourceUrl) {
        return new GenericWebResourceProvider(rootUrl, resourceUrl, "1.0");
    }

    /**
     * Creates a new instance of WebResourceProvider with the given root URL,
     * resource URL, and version.
     *
     * @param rootUrl     the root URL of the web resource
     * @param resourceUrl the specific resource URL
     * @param version     the version of the resource
     * @return a new WebResourceProvider instance
     */
    static WebResourceProvider newInstance(String rootUrl, String resourceUrl, String version) {
        return new GenericWebResourceProvider(rootUrl, resourceUrl, version);
    }

    /**
     * Gets the specific resource URL.
     *
     * @return the resource URL
     */
    String resourceUrl();

    /**
     * Gets the root URL of the web resource.
     *
     * @return the root URL
     */
    String rootUrl();

    /**
     * Constructs the full URL string using the root URL.
     *
     * @return the full URL string
     */
    default String getUrlString() {
        return getUrlString(rootUrl());
    }

    /**
     * Constructs the full URL string using the given root URL.
     *
     * @param rootUrl the root URL to use
     * @return the full URL string
     */
    default String getUrlString(String rootUrl) {
        String sanitizedRootUrl = rootUrl.endsWith("/") ? rootUrl : rootUrl + "/";

        return sanitizedRootUrl + resourceUrl();
    }

    /**
     * Converts the URL string to a URL object.
     *
     * @return the URL object
     * @throws RuntimeException if the URL string cannot be converted
     */
    default URL getUrl() {
        try {
            return new URI(getUrlString()).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new RuntimeException("Could not transform url String into URL", e);
        }
    }

    /**
     * Gets the version of the web resource.
     *
     * @return the version string
     */
    default String version() {
        return "v1.0";
    }

    /**
     * Gets the filename of the web resource, which is the last part of the URL
     * without the path.
     *
     * @return the filename
     */
    @Override
    default String getFilename() {
        String resourcePath = getUrlString();

        // Remove path
        int slashIndex = resourcePath.lastIndexOf('/');
        if (slashIndex == -1) {
            return resourcePath;
        }

        return resourcePath.substring(slashIndex + 1);
    }

    /**
     * Downloads the web resource to the specified folder.
     * Ignores usePath, always writes the file to the destination folder.
     *
     * @param folder the destination folder
     * @return the downloaded file
     */
    @Override
    default File write(File folder) {
        File downloadedFile = SpecsIo.download(getUrlString(), folder);

        SpecsCheck.checkNotNull(downloadedFile, () -> "Could not download file from URL '" + getUrlString() + "'");

        return downloadedFile;
    }

    /**
     * Creates a resource for the given version.
     * <p>
     * It changes the resource path by appending an underscore and the given version
     * as a suffix, before any extension.<br>
     * E.g., if the original resource is "path/executable.exe", returns a resource
     * to "path/executable<version>.exe".
     * </p>
     *
     * @param version the version to append to the resource path
     * @return a new WebResourceProvider instance for the given version
     */
    @Override
    default WebResourceProvider createResourceVersion(String version) {
        // Create new resourceUrl
        String resourceUrlNoExt = SpecsIo.removeExtension(resourceUrl());
        String extension = SpecsIo.getExtension(resourceUrl());
        extension = extension.isEmpty() ? extension : "." + extension;

        String newResourceUrl = resourceUrlNoExt + version + extension;

        return newInstance(rootUrl(), newResourceUrl, version);
    }
}
