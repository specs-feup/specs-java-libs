/**
 * Copyright 2015 SPeCS Research Group.
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

package pt.up.fe.specs.util.classmap;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Set of class T.
 * 
 * <p>
 * Use this class if you want to:<br>
 * 1) Use classes as elements of a set and want the set to respect the hierarchy
 * (e.g., contains will return true for an Integer instance if the class Number
 * is in the set)<br>
 * 
 * @author JoaoBispo
 *
 */
public class ClassSet<E> {

    @SafeVarargs
    public static <E> ClassSet<E> newInstance(Class<? extends E>... classes) {
        return newInstance(Arrays.asList(classes));
    }

    public static <E> ClassSet<E> newInstance(List<Class<? extends E>> classes) {
        ClassSet<E> classSet = new ClassSet<>();

        classes.stream().forEach(aClass -> classSet.add(aClass));

        return classSet;
    }

    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();

    private final ClassMap<E, Object> classMap;

    public ClassSet() {
        this.classMap = new ClassMap<>();
    }

    /**
     * 
     * @param classes
     * @return
     */
    @SuppressWarnings("unchecked")
    public void addAll(Class<? extends E>... classes) {
        addAll(Arrays.asList(classes));
    }

    public void addAll(Collection<Class<? extends E>> classes) {
        for (Class<? extends E> aClass : classes) {
            add(aClass);
        }
    }

    public boolean add(Class<? extends E> e) {
        if (e == null) {
            throw new NullPointerException("Class cannot be null");
        }
        return classMap.put(e, ClassSet.PRESENT) == null;
    }

    /**
     * Returns <tt>true</tt> if this set contains the specified element. More
     * formally, returns <tt>true</tt> if and only if this set contains an element
     * <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @param o element whose presence in this set is to be tested
     * @return <tt>true</tt> if this set contains the specified element
     */
    public boolean contains(Class<? extends E> aClass) {
        if (aClass == null) {
            throw new NullPointerException("Class cannot be null");
        }
        return classMap.tryGet(aClass).isPresent();
    }

    @SuppressWarnings("unchecked")
    public <K extends E> boolean contains(K element) {
        return contains((Class<K>) element.getClass());
    }
}
