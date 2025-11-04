/**
 * Copyright 2013 SPeCS Research Group.
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

package pt.up.fe.specs.util.utilities;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.providers.ResourceProvider;

public class Replacer {

    private String currentString;

    public Replacer(String string) {
        this.currentString = string;
    }

    public Replacer(ResourceProvider resource) {
        this(SpecsIo.getResource(resource));
    }

    public void replaceGeneric(CharSequence target, Object replacement) {
        replace(target, replacement.toString());
    }

    public void replace(String target, int replacement) {
        replace(target, Integer.toString(replacement));
    }

    public Replacer replace(CharSequence target, CharSequence replacement) {
        this.currentString = this.currentString.replace(target, replacement);

        return this;
    }

    public Replacer replaceRegex(String regex, String replacement) {
        this.currentString = this.currentString.replaceAll(regex, replacement);

        return this;
    }

    /**
     * Helper method which accepts any kind of object.
     *
     */
    public Replacer replace(Object target, Object replacement) {
        return replace(target.toString(), replacement.toString());
    }

    @Override
    public String toString() {
        return this.currentString;
    }

}
