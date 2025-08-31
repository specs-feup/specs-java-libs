/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.util.utilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Maps classes to other assignable classes that have been added to this
 * instance, respecting the hierarchy and the order by which classes where
 * added.
 * 
 * @author jbispo
 *
 */
public class ClassMapper {

    private final Set<Class<?>> currentClasses;
    private Map<Class<?>, Class<?>> cacheFound;
    private Set<Class<?>> cacheMissing;

    public ClassMapper() {
        currentClasses = new LinkedHashSet<>();
        cacheFound = new HashMap<>();
        cacheMissing = new HashSet<>();
    }

    public ClassMapper(ClassMapper classMapper) {
        currentClasses = new LinkedHashSet<>(classMapper.currentClasses);
        cacheFound = new HashMap<>(classMapper.cacheFound);
        cacheMissing = new HashSet<>(classMapper.cacheMissing);
    }

    private void emptyCache() {
        if (!cacheFound.isEmpty()) {
            cacheFound = new HashMap<>();
        }

        if (!cacheMissing.isEmpty()) {
            cacheMissing = new HashSet<>();
        }
    }

    public boolean add(Class<?> aClass) {
        if (aClass == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }

        // Everytime a class is added, invalidate cache
        emptyCache();

        return currentClasses.add(aClass);
    }

    public Optional<Class<?>> map(Class<?> aClass) {
        if (aClass == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }

        // Check if correct class has been calculated
        var mapping = cacheFound.get(aClass);
        if (mapping != null) {
            return Optional.of(mapping);
        }

        // Check if class does not have a mapping
        if (cacheMissing.contains(aClass)) {
            return Optional.empty();
        }

        // Calculate mapping of current class
        mapping = calculateMapping(aClass);

        if (mapping == null) {
            cacheMissing.add(aClass);
            return Optional.empty();
        }

        cacheFound.put(aClass, mapping);
        return Optional.of(mapping);

    }

    private Class<?> calculateMapping(Class<?> aClass) {
        /// Check if class is exactly one of the classes already present
        if (currentClasses.contains(aClass)) {
            return aClass;
        }

        Class<?> currentClass = aClass;

        while (currentClass != null) {
            // Test current class
            if (this.currentClasses.contains(currentClass)) {
                return currentClass;
            }

            // Test interfaces recursively
            Class<?> interfaceMapping = findInterfaceMapping(currentClass);
            if (interfaceMapping != null) {
                return interfaceMapping;
            }

            // Go to the next super class
            currentClass = currentClass.getSuperclass();
        }

        return null;
    }

    private Class<?> findInterfaceMapping(Class<?> aClass) {
        // Check interfaces of this class
        for (Class<?> interf : aClass.getInterfaces()) {
            if (this.currentClasses.contains(interf)) {
                return interf;
            }
            // Recursively check interfaces of interfaces
            Class<?> nestedInterfaceMapping = findInterfaceMapping(interf);
            if (nestedInterfaceMapping != null) {
                return nestedInterfaceMapping;
            }
        }
        return null;
    }

}
