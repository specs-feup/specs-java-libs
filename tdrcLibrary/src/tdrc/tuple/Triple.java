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

public class Triple<X, Y, Z> {
    private X x;
    private Y y;
    private Z z;

    public static <X, Y, Z> Triple<X, Y, Z> newInstance(X x, Y y, Z z) {
        return new Triple<>(x, y, z);
    }

    protected Triple(X x, Y y, Z z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public X getX() {
        return x;
    }

    public void setX(X x) {
        this.x = x;
    }

    public Y getY() {
        return y;
    }

    public void setY(Y y) {
        this.y = y;
    }

    public Z getZ() {
        return z;
    }

    public void setZ(Z z) {
        this.z = z;
    }

    @Override
    public String toString() {

        return "(" + x + "," + y + "," + z + ")";
    }

}
