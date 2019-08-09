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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.script.Bindings;

import org.apache.commons.lang3.tuple.Pair;
import org.graalvm.polyglot.Value;

public class GraalvmBindings implements Bindings {

    // private final Bindings bindings;
    // private final GraalvmJsEngine engine;
    private final Value bindings;

    // public GraalvmBindings(GraalvmJsEngine engine, Object bindings) {
    // this(engine, engine.asValue(bindings));
    // // this.engine = engine;
    // // this.bindings = engine.asValue(bindings);
    // }

    // public GraalvmBindings(GraalvmJsEngine engine, Value bindings) {
    public GraalvmBindings(Value bindings) {
        // this.engine = engine;
        this.bindings = bindings;
        // System.out.println("CONSTRUCTOR VALUE: " + bindings);
        // System.out.println("HAS ARRAY ELEM: " + bindings.hasArrayElements());
    }

    public Value getValue() {
        return bindings;
    }

    @Override
    public int size() {

        if (bindings.hasArrayElements()) {
            return (int) bindings.getArraySize();
        }

        if (bindings.hasMembers()) {
            return bindings.getMemberKeys().size();
        }

        return 0;

    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
        // return bindings.getMemberKeys().isEmpty();
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public void clear() {
        int arraySize = (int) bindings.getArraySize();
        for (int i = arraySize - 1; i >= 0; i++) {
            bindings.removeArrayElement(i);
        }

        bindings.getMemberKeys().stream()
                .forEach(bindings::removeMember);

    }

    @Override
    public Set<String> keySet() {
        if (bindings.hasArrayElements()) {
            Set<String> indexes = new HashSet<>();

            for (int i = 0; i < bindings.getArraySize(); i++) {
                indexes.add(Integer.toString(i));
            }

            return indexes;
        }

        if (bindings.hasMembers()) {
            return bindings.getMemberKeys();
        }

        return Collections.emptySet();
    }

    @Override
    public Collection<Object> values() {
        if (bindings.hasArrayElements()) {
            List<Object> elements = new ArrayList<>((int) bindings.getArraySize());

            for (int i = 0; i < bindings.getArraySize(); i++) {
                elements.add(bindings.getArrayElement(i));
            }

            return elements;
        }

        if (bindings.hasMembers()) {
            return bindings.getMemberKeys().stream()
                    .map(bindings::getMember)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {

        if (bindings.hasArrayElements()) {
            Set<Entry<String, Object>> set = new HashSet<>();
            for (int i = 0; i < bindings.getArraySize(); i++) {
                set.add(Pair.of(Integer.toString(i), bindings.getArrayElement(i)));
            }

            return set;
        }

        if (bindings.hasMembers()) {
            Set<Entry<String, Object>> set = new HashSet<>();
            for (var key : bindings.getMemberKeys()) {
                set.add(Pair.of(key, bindings.getMember(key)));
            }

            return set;
        }

        return Collections.emptySet();
    }

    @Override
    public Object put(String name, Object value) {
        if (bindings.hasArrayElements()) {
            long index = Long.valueOf(name);

            Value previousValue = index < bindings.getArraySize() ? bindings.getArrayElement(index) : null;
            bindings.setArrayElement(index, value);

            return previousValue;
        }

        // Value valueObject = engine.asValue(bindings);
        // Value previousValue = valueObject.getMember(name);
        // valueObject.putMember(name, value);

        // Assume object map
        Value previousValue = bindings.getMember(name);
        bindings.putMember(name, value);
        return previousValue;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> toMerge) {
        if (bindings.hasArrayElements()) {
            toMerge.entrySet().stream()
                    .forEach(entry -> bindings.setArrayElement(Integer.valueOf(entry.getKey()), entry.getValue()));
            return;
        }

        toMerge.entrySet().stream()
                .forEach(entry -> bindings.putMember(entry.getKey(), entry.getValue()));
    }

    @Override
    public boolean containsKey(Object key) {
        if (bindings.hasArrayElements()) {
            return Long.valueOf(key.toString()) < bindings.getArraySize();
        }

        return bindings.hasMember(key.toString());
    }

    @Override
    public Object get(Object key) {
        if (bindings.hasArrayElements()) {
            // System.out.println("ARRAY GET: " + bindings.getArrayElement(Long.valueOf(key.toString())));
            return bindings.getArrayElement(Long.valueOf(key.toString()));
        }

        return bindings.getMember(key.toString());
    }

    @Override
    public Object remove(Object key) {
        if (bindings.hasArrayElements()) {
            long index = Long.parseLong(key.toString());
            Value previousValue = index < bindings.getArraySize() ? bindings.getArrayElement(index) : null;
            bindings.removeArrayElement(index);
            return previousValue;
        }

        Value previousValue = bindings.getMember(key.toString());
        bindings.removeMember(key.toString());
        return previousValue;
    }

    @Override
    public String toString() {
        // System.out.println("TOSTRING");
        // Special case for array
        /*
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
        */

        // Undefined
        // if (bindings.isNull()) {
        // return "";
        // }

        return bindings.toString();
    }
}
