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
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.suikasoft.jOptions.test.storedefinitions;

import org.suikasoft.jOptions.DataStore.EnumDataKeyProvider;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.test.keys.AnotherTestKeys;

/**
 * @author Joao Bispo
 * 
 */
public enum EnumInnerOptions2 implements EnumDataKeyProvider<EnumInnerOptions2> {

    ANOTHER_BOOLEAN(AnotherTestKeys.ANOTHER_BOOLEAN);

    private final DataKey<?> key;

    private EnumInnerOptions2(DataKey<?> key) {
	this.key = key;
    }

    @Override
    public DataKey<?> getDataKey() {
	return key;
    }

    @Override
    public Class<EnumInnerOptions2> getEnumClass() {
	return EnumInnerOptions2.class;
    }

}
