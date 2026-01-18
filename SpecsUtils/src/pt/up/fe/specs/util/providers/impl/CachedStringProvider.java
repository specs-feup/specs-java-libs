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

package pt.up.fe.specs.util.providers.impl;

import java.util.Optional;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.providers.StringProvider;

/**
 * A StringProvider backed by the given File. The provider will return the
 * contents of the file.
 * 
 * @author JoaoBispo
 *
 */
public class CachedStringProvider implements StringProvider {

    private final StringProvider provider;
    private Optional<String> contents;

    public CachedStringProvider(StringProvider provider) {
        this.provider = provider;
        this.contents = Optional.empty();
    }

    @Override
    public String getString() {
        // Load file, if not loaded yet
        if (this.contents.isEmpty()) {
            String string = this.provider.getString();
            if (string == null) {
                SpecsLogs.warn("Could not get contents from provider");
            }

            this.contents = Optional.ofNullable(string);
        }

        return this.contents.orElse(null);
    }

}
