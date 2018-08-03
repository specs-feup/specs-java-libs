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

public enum ClavaLogsTest {

    Metrics;

    public final static EnumLogger<ClavaLogsTest> CLAVA_LOGS = EnumLogger.newInstance(ClavaLogsTest.class);

    // public void info(String message) {
    // CLAVA_LOGS.info(this, message);
    // }

    public static void info(String message) {
        CLAVA_LOGS.info(message);
    }

    public static void metrics(String message) {
        CLAVA_LOGS.info(ClavaLogsTest.Metrics, message);
    }
    // protected static SpecsLoggerV2 getLogger(String tag) {
    // String loggerName = SpecsLoggers.getLoggerName(ClavaLogsTest.class, tag);
    // return SpecsLoggers.getLogger(loggerName, () -> new SpecsLoggerV2(loggerName));
    // }
}
