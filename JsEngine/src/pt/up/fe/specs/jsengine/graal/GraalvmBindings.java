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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.jsengine.graal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.script.Bindings;

import org.apache.commons.lang3.tuple.Pair;
import org.graalvm.polyglot.Value;

public class GraalvmBindings implements Bindings {

    // private final Bindings bindings;
    private final GraalvmJsEngine engine;
    private final Value bindings;

    public GraalvmBindings(GraalvmJsEngine engine, Bindings bindings) {
        this.engine = engine;
        this.bindings = engine.asValue(bindings);
    }

    public Value getValue() {
        return bindings;
    }

    @Override
    public int size() {
        return bindings.getMemberKeys().size();
    }

    @Override
    public boolean isEmpty() {
        return bindings.getMemberKeys().isEmpty();
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public void clear() {
        bindings.getMemberKeys().stream()
                .forEach(bindings::removeMember);
    }

    @Override
    public Set<String> keySet() {
        return bindings.getMemberKeys();
    }

    @Override
    public Collection<Object> values() {
        return bindings.getMemberKeys().stream()
                .map(bindings::getMember)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        Set<Entry<String, Object>> set = new HashSet<>();
        for (var key : bindings.getMemberKeys()) {
            set.add(Pair.of(key, bindings.getMember(key)));
        }

        return set;
    }

    @Override
    public Object put(String name, Object value) {
        Value valueObject = engine.asValue(bindings);
        Value previousValue = valueObject.getMember(name);
        valueObject.putMember(name, value);
        return previousValue;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> toMerge) {
        toMerge.entrySet().stream()
                .forEach(entry -> bindings.putMember(entry.getKey(), entry.getValue()));
    }

    @Override
    public boolean containsKey(Object key) {
        return bindings.hasMember(key.toString());
    }

    @Override
    public Object get(Object key) {
        return bindings.getMember(key.toString());
    }

    @Override
    public Object remove(Object key) {
        Value previousValue = bindings.getMember(key.toString());
        bindings.removeMember(key.toString());
        return previousValue;
    }

    @Override
    public String toString() {
        // Special case for array
        if (bindings.hasArrayElements()) {
            StringBuilder arrayString = new StringBuilder();
            for (int i = 0; i < bindings.getArraySize(); i++) {
                if (i != 0) {
                    arrayString.append(",");
                }
                arrayString.append(bindings.getArrayElement(i));
            }

            return arrayString.toString();
            // Object[] list = bindings.as(Object[].class);
            // return Arrays.toString(list);
        }

        // Undefined
        // if (bindings.isNull()) {
        // return "";
        // }

        return bindings.toString();
    }
}
