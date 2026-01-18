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

package org.suikasoft.jOptions.app;

import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * Kernel class for jOptions applications.
 * 
 * @author Joao Bispo
 *
 */
public interface AppKernel {

    /**
     * The main method of the app.
     * Executes the application with the given options.
     * 
     * @param options the configuration options for the application
     * @return an integer representing the result of the execution
     */
    int execute(DataStore options);
}
