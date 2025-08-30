/**
 * Copyright 2014 SPeCS Research Group.
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

public class SimpleEvent implements Event {

    private final EventId eventId;
    private final Object data;

    public SimpleEvent(EventId eventId, Object data) {
        this.eventId = eventId;
        this.data = data;
    }

    @Override
    public EventId getId() {
        return this.eventId;
    }

    @Override
    public Object getData() {
        return this.data;
    }

}
