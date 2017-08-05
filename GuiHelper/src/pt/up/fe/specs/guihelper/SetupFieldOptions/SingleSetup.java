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

import pt.up.fe.specs.guihelper.Base.SetupDefinition;

/**
 * If a SetupFieldEnum wants to use a "setup[...]" FieldType, the class must implement this interface.
 * 
 * <p>
 * <b>Example implementation of the method getSetupOptions:</b>
 * 
 * <pre>
 * {@code
 *    public SetupDefinition getSetupOptions() {
 *       if(this == aSetupOption) {
 *          return SetupDefinition.create(aSetupFieldEnum.class);
 *       }
 * 
 *       return null;
 *    }
 * </pre>
 * 
 * @author Joao Bispo
 */
// public interface SingleSetup extends SetupFieldEnum {
public interface SingleSetup {

    /**
     * For each value which can have a setup with multiple values, define an EnumKey file.
     * 
     * @return
     */
    SetupDefinition getSetupOptions();
}
