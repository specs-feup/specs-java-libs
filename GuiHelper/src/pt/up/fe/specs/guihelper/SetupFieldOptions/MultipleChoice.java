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

package pt.up.fe.specs.guihelper.SetupFieldOptions;

import pt.up.fe.specs.util.utilities.StringList;

/**
 * If a SetupFieldEnum wants to use a "multipleChoice[...]" FieldType, the class must implement this interface.
 *
 * <p>
 * <b>Example implementation of the method getChoices:</b>
 * 
 * <pre>
 * {@code
 *    public StringList getChoices() {
 *       if(this == MultipleChoiceOption) {
 *          return new StringList(EnumUtils.buildList(AnEnum.values()));
 *       }
 * 
 *       return null;
 *    }
 * </pre>
 * 
 * @author Joao Bispo
 */
public interface MultipleChoice {

    StringList getChoices();
}
