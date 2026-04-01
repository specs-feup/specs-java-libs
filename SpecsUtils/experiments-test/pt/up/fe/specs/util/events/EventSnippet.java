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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import pt.up.fe.specs.util.SpecsFactory;

public class EventSnippet {

    @Test
    public void test() {

	EventController controller = new EventController();

	EventReceiver listener = new EventReceiver() {

	    @Override
	    public void acceptEvent(Event event) {
		EventId id = event.getId();
		if (id == EventSample.EVENT_1) {
		    System.out.println("Sample 1 event:" + event.getData());

		    return;
		}

		if (id == EventSample2.EVENT_1) {
		    System.out.println("Sample 2 event" + event.getData());
		    return;
		}

	    }

	    /* (non-Javadoc)
	     * @see pt.up.fe.specs.util.Events.EventListener#supportedEvents()
	     */
	    @Override
	    public Collection<EventId> getSupportedEvents() {
		Collection<EventId> events = new ArrayList<>();
		events.addAll(Arrays.asList(EventSample.values()));
		events.addAll(Arrays.asList(EventSample2.values()));

		return events;
	    }
	};

	controller.registerListener(listener, EventSample.EVENT_1, EventSample2.EVENT_1);

	controller.notifyEvent(EventSample.newMemoryWrite(23));
	controller.notifyEvent(EventSample.newMemoryWrite(24));
	controller.notifyEvent(EventSample2.newMemoryRead(42));
	// controller.
	// System.out.println("VALUE:"+event1.getData());
	// System.out.println("VALUE:"+event2.getData());
    }

}
