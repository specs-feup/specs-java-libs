/**
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

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.providers.impl.CachedStringProvider;

/**
 * Returns a String.
 * 
 * <p>
 * Can be used to pass a lazy-initialized String that is potentially expensive (i.e., the contents of a File) and not
 * always used.
 * 
 * @author JoaoBispo
 *
 */
public interface StringProvider extends KeyProvider<String> {

    /**
     * 
     * 
     * @return a string
     */
    String getString();

    @Override
    default String getKey() {
        return getString();
    }

    /**
     * Creates a new StringProvider backed by the given String.
     * 
     * @param string
     * @return
     */
    static StringProvider newInstance(String string) {
        return () -> string;
    }

    static StringProvider newInstance(File file) {
        return new CachedStringProvider(() -> SpecsIo.read(file));
    }

    static StringProvider newInstance(ResourceProvider resource) {
        return new CachedStringProvider(() -> SpecsIo.getResource(resource));
    }

}
