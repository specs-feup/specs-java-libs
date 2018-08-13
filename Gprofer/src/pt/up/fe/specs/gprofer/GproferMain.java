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

package pt.up.fe.specs.gprofer;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.gprofer.data.GprofData;

public class GproferMain {

    public static void main(String[] args) {

        File binary = new File(
                "/home/pedro/Documents/repositories/AntarexIT4I-master/Betweenness/Code/build/betweenness");
        List<String> binaryArgs = Arrays.asList("-f",
                "/home/pedro/Documents/repositories/AntarexIT4I-master/Betweenness/Graphs/graph-prt-port.csv");

        int numRuns = 1;

        // File workingDir = new File("/home/pedro/Desktop/gprof_tests/src/");
        // boolean deleteWorkingDir = false;
        // boolean checkReturn = true;

        GprofData data = Gprofer.profile(binary,
                binaryArgs,
                numRuns);

        System.out.println(Gprofer.getJsonData(data));

        // warn("This is a warning message");
    }

    static void warn(String message) {

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stackTrace[2]; // 0 is getStracktrace, 1 is this method

        System.out.printf(buildShortMessage(message, s));
    }

    static String buildShortMessage(String message, StackTraceElement s) {

        StringBuilder builder = new StringBuilder(message);
        builder.append("    ");
        builder.append(s.getClassName());
        builder.append(".");
        builder.append(s.getMethodName());
        builder.append("(");
        builder.append(s.getFileName());
        builder.append(":");
        builder.append(s.getLineNumber());
        builder.append(")");

        return builder.toString();
    }

}
