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

package pt.up.fe.specs.util.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * Maps events to EventListeners.
 *
 * @author Joao Bispo
 *
 */
public class ActionsMap {

    private final Map<EventId, EventAction> actionsMap;

    public ActionsMap() {
        this.actionsMap = new HashMap<>();
    }

    public EventAction putAction(EventId eventId, EventAction action) {
        Objects.requireNonNull(action, "EventAction cannot be null");

        EventAction previousAction = this.actionsMap.put(eventId, action);

        if (previousAction != null) {
            SpecsLogs.warn("Event '" + eventId + "' already in table. Replacing action '"
                    + previousAction + "' with action '" + action + "'");
        }

        return previousAction;
    }

    /**
     * Performs the action related to the given event.
     *
     */
    public void performAction(Event event) {
        // Get action
        EventAction action = this.actionsMap.get(event.getId());

        if (action == null) {
            SpecsLogs.warn("Could not find an action for event '" + event.getId() + "'");
            return;
        }

        // Execute action
        action.performAction(event);
    }

    public Set<EventId> getSupportedEvents() {
        return this.actionsMap.keySet();
    }

}
