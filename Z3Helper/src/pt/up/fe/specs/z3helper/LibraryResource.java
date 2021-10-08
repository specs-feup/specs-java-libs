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

package pt.up.fe.specs.z3helper;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum LibraryResource implements ResourceProvider {
    WIN64_LIBZ3("win64/libz3.dll"),
    WIN64_LIBZ3JAVA("win64/libz3java.dll"),
    LINUX64_LIBZ3("linux64/libz3.so"),
    LINUX64_LIBZ3JAVA("linux64/libz3java.so");

    private final String path;

    private LibraryResource(String path) {
        this.path = path;
    }

    @Override
    public String getResource() {
        return this.path;
    }

    public String getFileName() {
        return this.path.substring(this.path.indexOf('/') + 1);
    }
}
