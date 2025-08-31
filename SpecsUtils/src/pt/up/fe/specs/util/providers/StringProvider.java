/*
 * Copyright 2015 SPeCS Research Group.
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
import java.util.Objects;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.providers.impl.CachedStringProvider;

/**
 * Functional interface for providing strings.
 * <p>
 * Used for supplying string resources or values.
 * </p>
 *
 * @author Joao Bispo
 */
public interface StringProvider extends KeyProvider<String> {

    /**
     * Returns the string provided by this StringProvider.
     * 
     * @return a string
     */
    String getString();

    /**
     * Returns the key associated with this StringProvider, which is the string
     * itself.
     * 
     * @return the key
     */
    @Override
    default String getKey() {
        return getString();
    }

    /**
     * Creates a new StringProvider backed by the given String.
     * 
     * @param string the string to be provided
     * @return a new StringProvider instance
     */
    static StringProvider newInstance(String string) {
        return () -> string;
    }

    /**
     * Creates a new StringProvider backed by the contents of the given File.
     * 
     * @param file the file whose contents will be provided
     * @return a new StringProvider instance
     */
    static StringProvider newInstance(File file) {
        Objects.requireNonNull(file, "File cannot be null");
        return new CachedStringProvider(() -> SpecsIo.read(file));
    }

    /**
     * Creates a new StringProvider backed by the contents of the given
     * ResourceProvider.
     * 
     * @param resource the resource whose contents will be provided
     * @return a new StringProvider instance
     */
    static StringProvider newInstance(ResourceProvider resource) {
        Objects.requireNonNull(resource, "Resource cannot be null");
        return new CachedStringProvider(() -> SpecsIo.getResource(resource));
    }

}
