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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.gprofer.data;

public class GprofLine {

    private final Double percentage;
    private final Double cumulativeSeconds;
    private final Double selfSeconds;
    private final Integer calls;
    private final Double selfMsCall;
    private final Double totalMsCall;
    private final String name;

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

    public Double getPercentage() {
        return percentage;
    }

    public Double getCumulativeSeconds() {
        return cumulativeSeconds;
    }

    public Double getSelfSeconds() {
        return selfSeconds;
    }

    public Integer getCalls() {
        return calls;
    }

    public Double getSelfMsCall() {
        return selfMsCall;
    }

    public Double getTotalMsCall() {
        return totalMsCall;
    }

    public String getName() {
        return name;
    }
}
