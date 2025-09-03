/**
 * Copyright 2013 SPeCS Research Group.
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

package pt.up.fe.specs.util.jobs;

import java.io.File;
import java.util.List;

/**
 * @author Joao Bispo
 * 
 */
public interface JobBuilder {

    /**
     * Builds Jobs according to the given ProgramSources, returns null if any
     * problem happens.
     * 
     * @param outputFolder
     * @param programs
     * 
     * @return
     */
    List<Job> buildJobs(List<FileSet> programs, File outputFolder);

}
