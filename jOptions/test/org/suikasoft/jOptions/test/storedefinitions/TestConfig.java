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
import org.suikasoft.jOptions.test.keys.TestKeys;

public class TestConfig implements StoreDefinitionProvider {

    public static final String SETUP_NAME = "Test Config";

    @Override
    public StoreDefinition getStoreDefinition() {
	StoreDefinitionBuilder builder = new StoreDefinitionBuilder(TestConfig.SETUP_NAME);

	builder.addKey(TestKeys.A_STRING);
	builder.addKey(TestKeys.A_BOOLEAN);
	builder.addKey(TestKeys.A_STRINGLIST);
	builder.addKey(TestKeys.A_FILELIST);
	builder.addKey(TestKeys.A_SETUP);
	builder.addKey(TestKeys.A_SETUP_LIST);
	builder.addKey(TestKeys.A_MULTIPLE_OPTION);

	return builder.build();
    }

    public static String getSetupName() {
	return TestConfig.SETUP_NAME;
    }

}
