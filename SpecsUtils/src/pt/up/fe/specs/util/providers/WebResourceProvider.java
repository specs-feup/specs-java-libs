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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.providers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.Preconditions;
import pt.up.fe.specs.util.providers.impl.GenericWebResourceProvider;

/**
 * Provides an URL to a web resource.
 * 
 * @author JoaoBispo
 *
 */
public interface WebResourceProvider extends FileResourceProvider {

    static WebResourceProvider newInstance(String rootUrl, String resourceUrl) {
        return new GenericWebResourceProvider(rootUrl, resourceUrl, "1.0");
    }

    static WebResourceProvider newInstance(String rootUrl, String resourceUrl, String version) {
        return new GenericWebResourceProvider(rootUrl, resourceUrl, version);
    }

    String getResourceUrl();

    String getRootUrl();

    /**
     * 
     * @return The string corresponding to an url
     */
    default String getUrlString() {
        return getUrlString(getRootUrl());
    }

    default String getUrlString(String rootUrl) {
        String sanitizedRootUrl = rootUrl.endsWith("/") ? rootUrl : rootUrl + "/";

        return sanitizedRootUrl + getResourceUrl();
    }

    default URL getUrl() {
        try {
            return new URL(getUrlString());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not transform url String into URL", e);
        }
    }

    /**
     * 
     * @return string representing the version of this resource
     */
    @Override
    default String getVersion() {
        return "v1.0";
    }

    /**
     * 
     * @return the name of the last part of the URL, without the 'path'
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
     * Ignores usePath, always writes the file to the destination folder.
     */
    @Override
    default File write(File folder) {
        File downloadedFile = SpecsIo.download(getUrlString(), folder);

        Preconditions.checkNotNull(downloadedFile, "Could not download file from URL '" + getUrlString() + "'");

        return downloadedFile;
    }

    /**
     * Creates a resource for the given version.
     * 
     * <p>
     * It changes the resource path by appending an underscore and the given version as a suffix, before any
     * extension.<br>
     * E.g., if the original resource is "path/executable.exe", returns a resource to "path/executable<version>.exe".
     * 
     * @param version
     * @return
     */
    default WebResourceProvider createResourceVersion(String version) {
        // Create new resourceUrl
        String resourceUrlNoExt = SpecsIo.removeExtension(getResourceUrl());
        String extension = SpecsIo.getExtension(getResourceUrl());
        extension = extension.isEmpty() ? extension : "." + extension;

        String newResourceUrl = resourceUrlNoExt + version + extension;

        return newInstance(getRootUrl(), newResourceUrl, version);
    }
}
