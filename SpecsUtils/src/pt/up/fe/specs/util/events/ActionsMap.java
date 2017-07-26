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

package pt.up.fe.specs.util.events;

import java.util.Map;
import java.util.Set;

import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Maps events to EventListeners.
 * 
 * @author Joao Bispo
 * 
 */
public class ActionsMap {

    // private final Map<Enum<?>, EventAction> actionsMap;
    private final Map<EventId, EventAction> actionsMap;

    public ActionsMap() {
	this.actionsMap = SpecsFactory.newHashMap();
    }

    // public EventAction putAction(Enum<?> eventId, EventAction action) {
    public EventAction putAction(EventId eventId, EventAction action) {

	EventAction previousAction = this.actionsMap.put(eventId, action);

	if (previousAction != null) {
	    SpecsLogs.msgWarn("Event '" + eventId + "' already in table. Replacing action '"
		    + previousAction + "' with action '" + action + "'");
	}

	return previousAction;
    }

    /**
     * Performs the action related to the given event.
     * 
     * @param event
     */
    public void performAction(Event event) {
	// Get action
	EventAction action = this.actionsMap.get(event.getId());

	if (action == null) {
	    SpecsLogs.msgWarn("Could not find an action for event '" + event.getId() + "'");
	    return;
	}

	// Execute action
	action.performAction(event);
    }

    public Set<EventId> getSupportedEvents() {
	return this.actionsMap.keySet();
    }

}
