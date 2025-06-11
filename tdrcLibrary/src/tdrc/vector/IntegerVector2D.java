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
 * specific language governing permissions and limitations under the License.
 */

package tdrc.vector;

/**
 * Represents a 2D vector of integers.
 */
public class IntegerVector2D implements Comparable<IntegerVector2D> {

    private int x;
    private int y;

    /**
     * Default constructor that initializes the vector to (0, 0).
     */
    public IntegerVector2D() {
    }

    /**
     * Constructor that initializes the vector to the given x and y values.
     * 
     * @param x the x-coordinate of the vector
     * @param y the y-coordinate of the vector
     */
    public IntegerVector2D(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    /**
     * Compares this vector to another vector based on magnitude and angle.
     * 
     * @param o the other vector to compare to
     * @return 1 if this vector is greater, -1 if less, 0 if equal
     */
    @Override
    public int compareTo(IntegerVector2D o) {
        double magv = Math.sqrt(x * x + y * y);
        double mago = Math.sqrt(o.x * o.x + o.y * o.y);
        if (magv > mago) {
            return 1;
        }
        if (magv < mago) {
            return -1;
        }
        double anglev = Math.atan(y / x);
        double angleo = Math.atan(o.y / o.x);
        return anglev == angleo ? 0 : anglev > angleo ? 1 : -1;
    }

    /**
     * Calculates the distance between this vector and another vector.
     * 
     * @param o the other vector
     * @return the distance between the two vectors
     */
    public double getDistance(IntegerVector2D o) {
        double dx = x - o.x;
        double dy = y - o.y;
        return Math.sqrt(dx * dx + dy * dy);
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
     * @param x the new x-coordinate
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
     * @param y the new y-coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

}
