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

package org.suikasoft.jOptions.test.storedefinitions;

import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionBuilder;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionProvider;
import org.suikasoft.jOptions.test.keys.AnotherTestKeys;

public class InnerOptions implements StoreDefinitionProvider {

    public static final String SETUP_NAME = "Inner Options";

    @Override
    public StoreDefinition getStoreDefinition() {
	StoreDefinitionBuilder builder = new StoreDefinitionBuilder(InnerOptions.SETUP_NAME);

	builder.addKey(AnotherTestKeys.ANOTHER_STRING);

	return builder.build();
    }

    public static String getSetupName() {
	return InnerOptions.SETUP_NAME;
    }

}
