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

import pt.up.fe.specs.guihelper.BaseTypes.RawType;

/**
 * List of possible SetupField types.
 *
 * @author Joao Bispo
 */
public enum FieldType {
    /**
     * A string
     */
    string(RawType.String),
    /**
     * A file
     */
    folder(RawType.String),
    /**
     * A boolean
     */
    // bool(RawType.Boolean),
    bool(RawType.String),
    /**
     * An integer
     */
    // integer(RawType.Integer),
    integer(RawType.String),
    /**
     * A double
     */
    // doublefloat(RawType.Double),
    doublefloat(RawType.String),
    /**
     * A list of custom strings
     */
    stringList(RawType.ListOfStrings),
    /**
     * A list of predefined strings, from which we can choose only one.
     *
     * <p>
     * To use this option, EnumKey must implement interface MultipleChoice.
     */
    multipleChoice(RawType.String),
    /**
     * A list of predefined setups, from which we can choose only one.
     *
     * <p>
     * To use this option, EnumKey must implement interface MultipleSetup.
     */
    multipleChoiceSetup(RawType.MapOfSetups),
    /**
     * TODO:
     *
     * A list of predefined setups, from which we can choose multiple setups, but only one instance of each.
     *
     * <p>
     * To use this option, EnumKey must implement interface MultipleSetup.
     */
    // multipleChoiceSetupList(RawType.MapOfSetups),
    /**
     * A list of predefined strings, from which we can choose multiple strings.
     *
     * <p>
     * To use this option, EnumKey must implement interface MultipleChoice.
     */
    multipleChoiceStringList(RawType.ListOfStrings),
    /**
     * One complete setup.
     * <p>
     * To use this option, EnumKey must implement interface SingleSetup.
     */
    setup(RawType.Setup),
    /**
     * One complete setup, integrated seamlessly between the other options.
     * <p>
     * To use this option, EnumKey must implement interface SingleSetup.
     */
    integratedSetup(RawType.Setup),
    /**
     * A list of pre-defined setups, from which we can choose only one.
     *
     * TODO: Future work, if interesting.
     */
    // setupMultipleChoice(RawType.MapOfSetups),
    /**
     * A list of pre-defined setups. There can be several instances of the same type of setup.
     * <p>
     * To use this option, EnumKey must implement interface MultipleSetup.
     */
    setupList(RawType.MapOfSetups);

    private FieldType(RawType internalType) {
	this.internalType = internalType;
    }

    /**
     * 
     * @return
     */
    public RawType getRawType() {
	return internalType;
    }

    final private RawType internalType;

}
