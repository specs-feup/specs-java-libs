/**
 * Copyright 2017 SPeCS.
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

package tdrc.tuple;

/**
 * Represents a triple of values.
 *
 * @param <X> the type of the first value
 * @param <Y> the type of the second value
 * @param <Z> the type of the third value
 */
public class Triple<X, Y, Z> {
    private X x;
    private Y y;
    private Z z;

    /**
     * Creates a new instance of Triple with the given values.
     *
     * @param x   the first value
     * @param y   the second value
     * @param z   the third value
     * @param <X> the type of the first value
     * @param <Y> the type of the second value
     * @param <Z> the type of the third value
     * @return a new Triple instance
     */
    public static <X, Y, Z> Triple<X, Y, Z> newInstance(X x, Y y, Z z) {
        return new Triple<>(x, y, z);
    }

    /**
     * Constructs a Triple with the given values.
     *
     * @param x the first value
     * @param y the second value
     * @param z the third value
     */
    protected Triple(X x, Y y, Z z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    /**
     * Gets the first value of the Triple.
     *
     * @return the first value
     */
    public X getX() {
        return x;
    }

    /**
     * Sets the first value of the Triple.
     *
     * @param x the first value to set
     */
    public void setX(X x) {
        this.x = x;
    }

    /**
     * Gets the second value of the Triple.
     *
     * @return the second value
     */
    public Y getY() {
        return y;
    }

    /**
     * Sets the second value of the Triple.
     *
     * @param y the second value to set
     */
    public void setY(Y y) {
        this.y = y;
    }

    /**
     * Gets the third value of the Triple.
     *
     * @return the third value
     */
    public Z getZ() {
        return z;
    }

    /**
     * Sets the third value of the Triple.
     *
     * @param z the third value to set
     */
    public void setZ(Z z) {
        this.z = z;
    }

    /**
     * Returns a string representation of the Triple.
     *
     * @return a string in the format "(x,y,z)"
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }

    /**
     * Checks if this Triple is equal to another object.
     *
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) obj;
        return java.util.Objects.equals(x, triple.x) &&
                java.util.Objects.equals(y, triple.y) &&
                java.util.Objects.equals(z, triple.z);
    }

    /**
     * Returns the hash code for this Triple.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y, z);
    }

}
