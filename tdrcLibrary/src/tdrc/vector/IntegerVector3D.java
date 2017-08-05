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

public class IntegerVector3D implements Comparable<IntegerVector3D> {

    private int x;
    private int y;
    private int z;

    public IntegerVector3D() {
    }

    public IntegerVector3D(int x, int y, int z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

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
        double anglev = Math.atan(y / x);
        double angleo = Math.atan(o.y / o.x);
        return anglev == angleo ? 0 : anglev > angleo ? 1 : -1;
    }

    public double getDistance(IntegerVector3D o) {
        double dx = x - o.x;
        double dy = y - o.y;
        return Math.sqrt(dx * dx + dy * dy);

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

}
