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
 * Maps classes to other assignable classes that have been added to this instance, respecting the hierarchy and the
 * order by which classes where added.
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
        // Everytime a class is added, invalidate cache
        emptyCache();

        return currentClasses.add(aClass);
    }

    public Optional<Class<?>> map(Class<?> aClass) {
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

        /// Check if class is exactly one of the classes already present
        if (currentClasses.contains(aClass)) {
            cacheFound.put(aClass, aClass);
            return Optional.of(aClass);
        }

        /// Iterate over all current classes, looking for the first one that is assignable from the parameter class
        for (var currentClass : currentClasses) {
            System.out.println(
                    currentClass + " is assignable from " + aClass + ": " + currentClass.isAssignableFrom(aClass));
            if (currentClass.isAssignableFrom(aClass)) {
                cacheFound.put(aClass, currentClass);
                return Optional.of(currentClass);
            }
        }

        /// Could not find compatible class, mark as missing class
        cacheMissing.add(aClass);
        return Optional.empty();
    }
}
