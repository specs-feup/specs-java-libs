/*
 * Copyright 2011 SPeCS Research Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.suikasoft.XStreamPlus;

/**
 * Indicates that the object can be serializable to XML. The serialization
 * interface has no methods or fields and serves only to identify the semantics
 * of being serializable.
 *
 * @author Joao Bispo
 */
public interface XmlSerializable {

    // Interface could make objects implement a method like getObjectXml. This
    // works when writing an object. However, when reading an object, we do not
    // have access to the object and as a consequence, no access to the method.
    // Method is being implemented as a static method.
}
