/*
 * Copyright 2010 SPeCS Research Group.
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

package pt.up.fe.specs.guihelper;

import java.io.File;

import pt.up.fe.specs.guihelper.Base.SetupDefinition;

/**
 * Represents the entry point of an application.
 *
 * The contract for any class implementing App is that the program defines an enum class implementing SetupField with
 * the options it wants to use, which may be access in the map that is passed to the application.
 *
 * For new applications, should use org.suikasoft.jOptions.app.App instead.
 * 
 * 
 * @author Joao Bispo
 */
public interface App {

    /**
     * Executes the application with the options in the given map.
     *
     * Method throws InterruptedException to support canceling the task. In the application code, insert:
     *
     * if (Thread.currentThread().isInterrupted()) { throw new InterruptedException("Task Cancellation"); }
     *
     * On the places the task can be cancelled.
     *
     * @param options
     * @return
     */
    // int execute(Map<String, OptionValue> options) throws InterruptedException ;
    // int execute(Setup options) throws InterruptedException ;
    int execute(File setupFile) throws InterruptedException;
    // int execute(Setup options) throws InterruptedException ;

    /**
     *
     * @return the enum class implementing the EnumKey interface associated with this App.
     */
    // Class getEnumKeyClass();
    /**
     * @return a Collection containing the EnumKeys which will build the setup of this application.
     */
    // Collection<EnumKey> getEnumKeys();
    SetupDefinition getEnumKeys();
}
