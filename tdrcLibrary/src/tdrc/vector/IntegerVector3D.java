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

package tdrc.vector;

/**
 * Represents a 3D vector of integers.
 */
public class IntegerVector3D implements Comparable<IntegerVector3D> {

    private int x;
    private int y;
    private int z;

    /**
     * Default constructor that initializes the vector to (0, 0, 0).
     */
    public IntegerVector3D() {
    }

    /**
     * Constructor that initializes the vector to the given x, y, and z values.
     * 
     * @param x the x-coordinate of the vector
     * @param y the y-coordinate of the vector
     * @param z the z-coordinate of the vector
     */
    public IntegerVector3D(int x, int y, int z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    /**
     * Compares this vector to another vector based on magnitude and angle.
     * 
     * @param o the other vector to compare to
     * @return 1 if this vector is greater, -1 if less, and 0 if equal
     */
    @Override
    public int compareTo(IntegerVector3D o) {
        double magv = Math.sqrt(x * x + y * y + z * z);
        double mago = Math.sqrt(o.x * o.x + o.y * o.y + o.z * o.z);
        if (magv > mago) {
            return 1;
        }
        if (magv < mago) {
            return -1;
        }
        double anglev = Math.atan2(y, x);
        double angleo = Math.atan2(o.y, o.x);
        return anglev == angleo ? 0 : anglev > angleo ? 1 : -1;
    }

    /**
     * Calculates the distance between this vector and another vector in 3D space.
     * 
     * @param o the other vector
     * @return the distance between the two vectors
     */
    public double getDistance(IntegerVector3D o) {
        double dx = x - o.x;
        double dy = y - o.y;
        double dz = z - o.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Gets the x-coordinate of the vector.
     * 
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the vector.
     * 
     * @param x the x-coordinate to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of the vector.
     * 
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the vector.
     * 
     * @param y the y-coordinate to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the z-coordinate of the vector.
     * 
     * @return the z-coordinate
     */
    public int getZ() {
        return z;
    }

    /**
     * Sets the z-coordinate of the vector.
     * 
     * @param z the z-coordinate to set
     */
    public void setZ(int z) {
        this.z = z;
    }

}
