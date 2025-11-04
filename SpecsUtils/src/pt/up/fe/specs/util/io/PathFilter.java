/**
 * Copyright 2018 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.io;

import java.io.File;
import java.util.Optional;

import pt.up.fe.specs.util.enums.EnumHelper;
import pt.up.fe.specs.util.lazy.Lazy;

public enum PathFilter {

    FILES,
    FOLDERS,
    FILES_AND_FOLDERS;

    private static final Lazy<EnumHelper<PathFilter>> ENUM_HELPER = EnumHelper.newLazyHelper(PathFilter.class);

    public static EnumHelper<PathFilter> getHelper() {
        return ENUM_HELPER.get();
    }

    public Optional<Boolean> isAllowedTry(File file) {
        if (file.isDirectory()) {
            return Optional.of(this == FOLDERS || this == FILES_AND_FOLDERS);
        }

        if (file.isFile()) {
            return Optional.of(this == FILES || this == FILES_AND_FOLDERS);
        }

        return Optional.empty();
    }

    public boolean isAllowed(File file) {
        return isAllowedTry(file).orElseThrow((() -> new RuntimeException("Could not find path '" + file + "'")));
    }
}
