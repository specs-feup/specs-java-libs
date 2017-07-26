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

/**
 * Set of class T.
 * 
 * <p>
 * Use this class if you want to:<br>
 * 1) Use classes as elements of a set and want the set to respect the hierarchy (e.g., contains will return true for an
 * Integer instance if the class Number is in the set)<br>
 * 
 * @author JoaoBispo
 *
 */
public class ClassSet<E> {

    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();

    private final ClassMap<E, Object> classMap;

    public ClassSet() {
        this.classMap = new ClassMap<>();
    }

    public boolean add(Class<? extends E> e) {
        return classMap.put(e, ClassSet.PRESENT) == null;
    }

    /**
     * Returns <tt>true</tt> if this set contains the specified element. More formally, returns <tt>true</tt> if and
     * only if this set contains an element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @param o
     *            element whose presence in this set is to be tested
     * @return <tt>true</tt> if this set contains the specified element
     */
    public boolean contains(Class<? extends E> aClass) {
        return classMap.tryGet(aClass).isPresent();
    }

    @SuppressWarnings("unchecked")
    public <K extends E> boolean contains(K element) {
        return contains((Class<K>) element.getClass());
    }
}
