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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.guihelper.SetupFieldOptions;

import pt.up.fe.specs.guihelper.Base.ListOfSetupDefinitions;

/**
 * If a SetupFieldEnum wants to use a "setupList[...]" FieldType, the class must implement this interface.
 *
 * <p>
 * <b>Example implementation of the method getSetups:</b>
 * 
 * <pre>
 * {@code 
 *  public ListOfSetupDefinitions getSetups() {
 *     if(this == FieldWhichUseSetupList) {
 *        return new ListOfSetupDefinitions(Arrays.asList(
 *                 SetupDefinition.create(FirstSetup.class),
 *                 SetupDefinition.create(SecondSetup.class)
 *                ));
 *     }
 *
 *     return null;
 *  }
 * 
 * </pre>
 * 
 * @author Joao Bispo
 */
// public interface MultipleSetup extends SetupFieldEnum {
public interface MultipleSetup {

    ListOfSetupDefinitions getSetups();
}
