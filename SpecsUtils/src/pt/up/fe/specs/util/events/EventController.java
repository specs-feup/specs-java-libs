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

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.collections.AccumulatorMap;

public class EventController implements EventNotifier, EventRegister {

    // Map<Enum<?>, Collection<EventReceiver>> registeredListeners;
    private final Map<EventId, Collection<EventReceiver>> registeredListeners;
    // To count and keep track of listeners
    private final AccumulatorMap<EventReceiver> listenersCount;

    public EventController() {
	this.registeredListeners = SpecsFactory.newHashMap();
	this.listenersCount = new AccumulatorMap<>();
    }

    /**
     * Registers receiver to all its supported events.
     * 
     * @param reciver
     * @param eventIds
     */
    @Override
    public void registerReceiver(EventReceiver reciver) {
	registerListener(reciver, reciver.getSupportedEvents());
    }

    /**
     * Unregisters listener to all its supported events.
     * 
     * @param receiver
     * @param eventIds
     */
    @Override
    public void unregisterReceiver(EventReceiver receiver) {
	unregisterReceiver(receiver, receiver.getSupportedEvents());
    }

    private void unregisterReceiver(EventReceiver receiver, Collection<EventId> supportedEvents) {
	for (EventId event : supportedEvents) {
	    unregisterListener(receiver, event);
	}
    }

    private void unregisterListener(EventReceiver receiver, EventId eventId) {
	// Check if event is already on table
	Collection<EventReceiver> receivers = this.registeredListeners.get(eventId);
	if (receivers == null) {
	    SpecsLogs.msgWarn("No receivers mapped to EventId '" + eventId + "'");
	    return;
	}

	// Check if receiver is not present
	if (!receivers.contains(receiver)) {
	    SpecsLogs.msgInfo("Event '" + eventId + "' was not registered for receiver '" + receiver + "'");
	    return;
	}

	// Remove receiver
	receivers.remove(receiver);
	// Decrease count
	this.listenersCount.remove(receiver);

    }

    /**
     * Helper method.
     * 
     * @param listener
     * @param eventIds
     */
    // public void registerListener(EventReceiver listener, Enum<?>... eventIds) {
    public void registerListener(EventReceiver listener, EventId... eventIds) {
	registerListener(listener, Arrays.asList(eventIds));
    }

    /**
     * Registers a listener to a list of events.
     * 
     * @param listener
     * @param event
     */
    public void registerListener(EventReceiver listener, Collection<EventId> eventIds) {
	if (eventIds == null) {
	    return;
	}

	for (EventId event : eventIds) {
	    registerListener(listener, event);
	}
    }

    /**
     * Registers a listener to a single event.
     * 
     * @param listener
     * @param eventId
     */
    // public void registerListener(EventReceiver listener, Enum<?> eventId) {
    public void registerListener(EventReceiver listener, EventId eventId) {
	// Check if event is already on table
	Collection<EventReceiver> listeners = this.registeredListeners.get(eventId);
	if (listeners == null) {
	    listeners = SpecsFactory.newLinkedHashSet();
	    this.registeredListeners.put(eventId, listeners);
	}

	// Check if listener is already registered
	if (listeners.contains(listener)) {
	    SpecsLogs.msgInfo("Event '" + eventId + "' already registers for listener '" + listener + "'");
	    return;
	}

	// Register listener
	listeners.add(listener);
	// Add count
	this.listenersCount.add(listener);
    }

    @Override
    public void notifyEvent(Event event) {

	Collection<EventReceiver> listeners = this.registeredListeners.get(event.getId());

	if (listeners == null) {
	    // LoggingUtils.msgInfo("No listeners registered to event " + event.getId());
	    return;
	}

	for (EventReceiver listener : listeners) {
	    listener.acceptEvent(event);
	}

    }

    /**
     * @return true if there is at least one listeners registered
     */
    public boolean hasListeners() {
	return !this.listenersCount.getAccMap().keySet().isEmpty();
    }

    /**
     * 
     * @return the listeners currently registered to the controller
     */
    public Collection<EventReceiver> getListeners() {
	return this.listenersCount.getAccMap().keySet();
    }

}
