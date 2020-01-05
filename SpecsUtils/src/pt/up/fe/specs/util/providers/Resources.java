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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class to manage resources.
 * 
 * @author JoaoBispo
 *
 */
public class Resources {

    private final String baseFolder;
    private final List<String> resources;

    public Resources(String baseFolder, String... resources) {
        this(baseFolder, Arrays.asList(resources));
    }

    public Resources(String baseFolder, List<String> resources) {
        this.baseFolder = baseFolder.endsWith("/") ? baseFolder : baseFolder + "/";
        this.resources = resources;
    }

    public List<ResourceProvider> getResources() {
        return resources.stream()
                .map(resource -> baseFolder + resource)
                .map(resource -> ResourceProvider.newInstance(resource))
                .collect(Collectors.toList());
    }

}
