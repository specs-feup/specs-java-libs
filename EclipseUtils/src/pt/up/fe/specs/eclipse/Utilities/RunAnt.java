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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.eclipse.Utilities;

import java.io.File;

import com.google.common.base.Preconditions;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;

public class RunAnt {

    public static void main(String[] args) {
        SpecsSystem.programStandardInit();
        // System.out.println("HELLO");
        // System.out.println("ANT java.home: " + System.getProperty("java.home"));
        // System.out.println("JAVA_HOME ENV:" + System.getenv("JAVA_HOME"));
        Preconditions.checkArgument(args.length > 0, "Expected at least one argument, the ant file");

        File antFile = SpecsIo.existingFile(args[0]);

        String target = null;
        if (args.length > 1) {
            target = args[1];
        }

        DeployUtils.runAnt(antFile, target);
    }

}
