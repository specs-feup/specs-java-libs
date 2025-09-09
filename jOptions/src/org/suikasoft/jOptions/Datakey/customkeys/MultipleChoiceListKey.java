/**
 * Copyright 2019 SPeCS.
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

package org.suikasoft.jOptions.Datakey.customkeys;

import java.util.ArrayList;
import java.util.List;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.DataKeyExtraData;
import org.suikasoft.jOptions.Datakey.GenericKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

/**
 * Represents a key with several choices from which several can be selected.
 *
 * @param <T> the type of the available choices
 */
public class MultipleChoiceListKey<T> extends GenericKey<List<T>> {

    /**
     * DataKey for the available choices for this key.
     */
    @SuppressWarnings("rawtypes")
    public static final DataKey<List> AVAILABLE_CHOICES = KeyFactory.object("availableChoices", List.class);

    /**
     * Constructs a MultipleChoiceListKey with the given id and available choices.
     *
     * @param id               the key id
     * @param availableChoices the list of available choices
     */
    public MultipleChoiceListKey(String id, List<T> availableChoices) {
        super(id, new ArrayList<>(availableChoices), null, null, null, null, null, null, null, null,
                new DataKeyExtraData());
        getExtraData().get().set(AVAILABLE_CHOICES, availableChoices);
    }
}
