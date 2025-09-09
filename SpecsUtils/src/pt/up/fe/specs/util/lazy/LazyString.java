/**
 * Copyright 2017 SPeCS.
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

package pt.up.fe.specs.util.lazy;

import java.util.Objects;
import java.util.function.Supplier;

public class LazyString {

    private final Lazy<String> lazyString;

    public LazyString(Supplier<String> lazyString) {
        this.lazyString = Lazy.newInstance(Objects.requireNonNull(lazyString, () -> "Supplier cannot be null"));
    }

    @Override
    public String toString() {
        String result = lazyString.get();
        return result != null ? result : "null";
    }

}
