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

package org.suikasoft.jOptions.GenericImplementations;

import java.io.File;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Dummy implementation of persistence, just to test code.
 * 
 * <p>
 * Setup is maintained in memory, instead of being saved to a file.
 * 
 * @author Joao Bispo
 * 
 */
public class DummyPersistence implements AppPersistence {

    private DataStore setup;

    public DummyPersistence(DataStore setup) {
	this.setup = setup;

    }

    public DummyPersistence(StoreDefinition setupDefinition) {
	this(DataStore.newInstance(setupDefinition));
    }

    /* (non-Javadoc)
     * @see org.suikasoft.jOptions.Interfaces.AppPersistence#loadData(java.io.File)
     */
    @Override
    public DataStore loadData(File file) {
	return setup;
    }

    /* (non-Javadoc)
     * @see org.suikasoft.jOptions.Interfaces.AppPersistence#saveData(java.io.File, org.suikasoft.jOptions.Interfaces.Setup, boolean)
     */
    @Override
    public boolean saveData(File file, DataStore setup, boolean keepSetupFile) {
	this.setup = setup;
	return true;
    }

}
