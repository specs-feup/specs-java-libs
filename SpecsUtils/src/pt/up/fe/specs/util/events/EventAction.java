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

/**
 * Represents a response to a single event.
 * 
 * @author Joao Bispo
 * 
 */
// public abstract class EventAction {
public interface EventAction {
    /*
     * keleton class that performs an action related to a single event.
     * 
     * <p>
     * Useful for creating anonymous classes that respond to single events.
     * 
        protected final Enum<?> eventId;

        public EventAction(Event event) {
    	this.event = event;
        }

        public Event getEvent() {
    	return event;
        }
        
        public abstract void performAction(Event event);
        */

    void performAction(Event event);
}
