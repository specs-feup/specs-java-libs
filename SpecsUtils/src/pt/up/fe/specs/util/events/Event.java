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
 * Marker interface to indicate an enumeration represents Events.
 * 
 * @author Joao Bispo
 *
 * @param <K>
 */
public interface Event {
    // public interface Event<K extends Enum<K>, E> {
    // public interface Event<K extends Enum<K>> {

    // <T> Class<T> getDataClass();
    // Enum<?> getId();
    EventId getId();

    Object getData();

}
