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

package pt.up.fe.specs.util.logging;

// public class SpecsLogsTest extends ALogs<SpecsLogsEnum> {
public enum SpecsLogsTest {

    // public enum SpecsLogsEnum {
    // Metrics;
    // }
    METRICS;

    public final static EnumLogger<SpecsLogsTest> SPECS_LOGS = EnumLogger.newInstance(SpecsLogsTest.class);
    public final static EnumLogger<SpecsLogsTest> SPECS_LOGS_2 = EnumLogger.newInstance(SpecsLogsTest.class);

    public static void metrics(String message) {
        SPECS_LOGS.info(METRICS, message);
    }
    // protected SpecsLogsTest() {
    // }

    // protected static SpecsLoggerV2 getLogger(String tag) {
    // String loggerName = SpecsLoggers.getLoggerName(SpecsLogsTest.class, tag);
    // return SpecsLoggers.getLogger(loggerName, () -> new SpecsLoggerV2(loggerName));
    // }
    //
    // public static void info(String message) {
    // System.out.println("LOGGING INFO TO " + getLogger("undefined").getName());
    // getLogger("undefined").info(message);
    // }

}
