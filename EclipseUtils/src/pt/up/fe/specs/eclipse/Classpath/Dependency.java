/**
 * Copyright 2019 SPeCS.
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

package pt.up.fe.specs.eclipse.Classpath;

import org.suikasoft.jOptions.DataStore.ADataClass;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

/**
 * Represents a dependency.
 *
 * @author JoaoBispo
 *
 */
public class Dependency extends ADataClass<Dependency> {

    public static final DataKey<String> GROUP = KeyFactory.string("group");
    public static final DataKey<String> NAME = KeyFactory.string("name");
    public static final DataKey<String> VERSION = KeyFactory.string("version");

    public String toMaven() {
        return "<dependency>\n" +
                "    <groupId>" + get(GROUP) + "</groupId>\n" +
                "    <artifactId>" + get(NAME) + "</artifactId>\n" +
                "    <version>" + get(VERSION) + "</version>\n" +
                "</dependency>";
    }
}
