/**
 * Copyright 2018 SPeCS.
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

package pt.up.fe.specs.gprofer.data;

/**
 * Represents a single line of profiling data from gprof output.
 */
public class GprofLine {

    private final Double percentage;
    private final Double cumulativeSeconds;
    private final Double selfSeconds;
    private final Integer calls;
    private final Double selfMsCall;
    private final Double totalMsCall;
    private final String name;

    /**
     * Constructs a GprofLine with the given profiling values.
     *
     * @param percentage the percentage of time spent in this function
     * @param cumulativeSeconds the cumulative seconds up to this function
     * @param selfSeconds the self seconds spent in this function
     * @param calls the number of calls to this function
     * @param selfMsCall the self milliseconds per call
     * @param totalMsCall the total milliseconds per call
     * @param name the function name
     */
    public GprofLine(Double percentage, Double cumulativeSeconds, Double selfSeconds, Integer calls, Double selfMsCall,
            Double totalMsCall, String name) {
        this.percentage = percentage;
        this.cumulativeSeconds = cumulativeSeconds;
        this.selfSeconds = selfSeconds;
        this.calls = calls;
        this.selfMsCall = selfMsCall;
        this.totalMsCall = totalMsCall;
        this.name = name;
    }

    /**
     * @return the percentage of time spent in this function
     */
    public Double getPercentage() {
        return percentage;
    }

    /**
     * @return the cumulative seconds up to this function
     */
    public Double getCumulativeSeconds() {
        return cumulativeSeconds;
    }

    /**
     * @return the self seconds spent in this function
     */
    public Double getSelfSeconds() {
        return selfSeconds;
    }

    /**
     * @return the number of calls to this function
     */
    public Integer getCalls() {
        return calls;
    }

    /**
     * @return the self milliseconds per call
     */
    public Double getSelfMsCall() {
        return selfMsCall;
    }

    /**
     * @return the total milliseconds per call
     */
    public Double getTotalMsCall() {
        return totalMsCall;
    }

    /**
     * @return the function name
     */
    public String getName() {
        return name;
    }
}
