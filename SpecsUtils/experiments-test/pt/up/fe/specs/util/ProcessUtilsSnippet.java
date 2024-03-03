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

package pt.up.fe.specs.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import pt.up.fe.specs.util.system.ProcessOutputAsString;

public class ProcessUtilsSnippet {

    // @Test
    public void testRunProcess() {
        String command = "D:/temp/g_i.exe";
        String workingDir = "D:/temp/";

        ProcessOutputAsString output = SpecsSystem.runProcess(Arrays.asList(command), new File(workingDir), true, false);

        System.out.println("stdout:\n" + output.getStdOut());
        System.out.println("stderr:\n" + output.getStdErr());
    }

    // @Test
    public void testGetNanoTime() {
        int size = 0;
        SpecsSystem.getNanoTime(() -> SpecsSystem.programStandardInit());
        StringBuilder sb = SpecsSystem.getNanoTime(() -> generateOptimized(size));
        System.out.println("OUT:" + sb.toString());
        // StringBuilder sb = generateOptimized(size)
    }

    public StringBuilder generateOptimized(int size) {
        StringBuilder builder = new StringBuilder(size);

        builder.append("I'm a builder\n");

        return builder;
    }

    // @Test
    // @Test
    public void testRunBat() {
        List<String> cmd = Arrays.asList("cmd", "/C", "C:/temp_c/test.bat");
        ProcessOutputAsString output = SpecsSystem.runProcess(cmd, true, false);

        System.out.println("stdout:\n" + output.getStdOut());
        System.out.println("stderr:\n" + output.getStdErr());
    }

    @Test
    public void testPrints() {
        SpecsSystem.programStandardInit();

        System.out.print("1 - A");
        System.out.print("B");
        System.out.println("C");
        System.out.println("2 - Another line");
        SpecsLogs.msgInfo("3 - A third line");
        SpecsLogs.warn("4 - A fourth line");
    }
}
