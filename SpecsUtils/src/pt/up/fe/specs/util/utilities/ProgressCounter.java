/**
 * Copyright 2012 SPeCS Research Group.
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

import pt.up.fe.specs.util.SpecsLogs;

public class ProgressCounter {

    private final int max_count;
    private int currentCount;

    public ProgressCounter(int maxCount) {
        this.max_count = maxCount;
        this.currentCount = 0;
    }

    public String next() {
        int currentCount = nextInt();

        return "(" + currentCount + "/" + this.max_count + ")";
    }

    public int nextInt() {
        if (this.currentCount <= this.max_count) {
            this.currentCount += 1;
        } else {
            SpecsLogs.warn("Already reached the maximum count (" + this.max_count + ")");
        }

        return this.currentCount;
    }

    public int getCurrentCount() {
        return this.currentCount;
    }

    public int getMaxCount() {
        return this.max_count;
    }

    public boolean hasNext() {
        return this.currentCount < this.max_count;
    }
}
