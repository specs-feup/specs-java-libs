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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.jacobi;

public class DwAccess {
    private final String x;
    private final String y;
    private final String constant;
    private final String signal;

    public DwAccess(String x, String y, String constant, String signal) {
        this.x = x;
        this.y = y;
        this.constant = constant;
        this.signal = signal;
    }

    @Override
    public String toString() {
        return "DwAccess [x=" + x + ", y=" + y + ", constant=" + constant + ", signal=" + signal + "]";
    }

    /**
     * @return the x
     */
    public String getX() {
        return x;
    }

    /**
     * @return the y
     */
    public String getY() {
        return y;
    }

    /**
     * @return the constant
     */
    public String getConstant() {
        return constant;
    }

    /**
     * @return the signal
     */
    public String getSignal() {
        return signal;
    }

}
