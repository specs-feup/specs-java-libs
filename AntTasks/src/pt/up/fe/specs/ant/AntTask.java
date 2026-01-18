/**
 * Copyright 2019 SPeCS.
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

package pt.up.fe.specs.ant;

import java.io.File;
import java.util.UUID;

import pt.up.fe.specs.util.SpecsIo;

public interface AntTask {

    /**
     * 
     * @return the ANT script of this task
     */
    String getScript();

    default void run() {
        String scriptName = getClass().getSimpleName() + "_" + UUID.randomUUID().toString() + ".xml";

        // Write script to a temporary folder
        File antScript = new File(SpecsAnt.getTemporaryFolder(), scriptName);

        // Save script
        SpecsIo.write(antScript, getScript());

        SpecsAnt.runAnt(antScript, null);
    }
}
