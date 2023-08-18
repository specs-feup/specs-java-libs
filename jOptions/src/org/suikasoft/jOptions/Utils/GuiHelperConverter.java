/**
 * Copyright 2023 SPeCS.
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

package org.suikasoft.jOptions.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.SetupFieldOptions.DefaultValue;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

/**
 * Converts enums that implement {@link SetupFieldEnum} to StoreDefinition.
 */
public class GuiHelperConverter {

    public static <T extends Enum<?> & SetupFieldEnum> List<StoreDefinition> toStoreDefinition(
            @SuppressWarnings("unchecked") Class<T>... setups) {
        return toStoreDefinition(Arrays.asList(setups));
    }

    public static <T extends Enum<?> & SetupFieldEnum> List<StoreDefinition> toStoreDefinition(List<Class<T>> setups) {
        var converter = new GuiHelperConverter();

        var definitions = new ArrayList<StoreDefinition>();

        for (var setup : setups) {
            definitions.add(converter.convert(setup));
        }

        return definitions;
    }

    public <T extends Enum<?> & SetupFieldEnum> StoreDefinition convert(Class<T> setup) {
        var name = setup.getSimpleName();
        var keys = getDataKeys(setup.getEnumConstants());

        return StoreDefinition.newInstance(name, keys);
    }

    public <T extends Enum<?> & SetupFieldEnum> List<DataKey<?>> getDataKeys(
            @SuppressWarnings("unchecked") T... setupKeys) {
        var keys = new ArrayList<DataKey<?>>();

        for (var setupKey : setupKeys) {
            keys.add(getDataKey(setupKey));
        }
        return keys;
    }

    public <T extends Enum<?> & SetupFieldEnum> DataKey<?> getDataKey(T setupKey) {
        var key = getBaseDataKey(setupKey);

        // Set default value (must be immutable)
        if (setupKey instanceof DefaultValue) {
            var defaultValueProvider = (DefaultValue) setupKey;
            var defaultValue = defaultValueProvider.getDefaultValue();

            if (defaultValue != null) {
                Supplier<? extends Object> defaultSupplier = () -> defaultValue.getRawValue();
                key.setDefaultRaw(defaultSupplier);
            }
        }

        return key;
    }

    private <T extends Enum<?> & SetupFieldEnum> DataKey<? extends Object> getBaseDataKey(T setupKey) {
        switch (setupKey.getType()) {
        case string:
            return KeyFactory.string(setupKey.name());
        default:
            throw new NotImplementedException(setupKey.getType());
        }

    }
}
