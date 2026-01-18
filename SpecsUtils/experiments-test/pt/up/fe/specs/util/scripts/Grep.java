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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.scripts;

import java.util.List;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;

public class Grep {

    public static void main(String[] args) {

	SpecsSystem.programStandardInit();

	if (args.length < 2) {
	    SpecsLogs.msgInfo("<input file> <pattern> (<capturing group>)");
	    return;
	}

	// First arg is the input text
	String content = SpecsIo.read(SpecsIo.existingFile(args[0]));

	// Second arg is the pattern
	String pattern = args[1];
	SpecsLogs.msgLib("Regex: " + pattern);

	// Third (optional) arg is the capturing group
	Integer capturingGroup = null;
	if (args.length > 2) {
	    capturingGroup = SpecsStrings.parseInteger(args[2]);
	}

	if (capturingGroup == null) {
	    List<String> results = SpecsStrings.getRegex(content, pattern);
	    for (String result : results) {
		SpecsLogs.msgInfo(result);
	    }

	    return;
	}

	List<String> capturedGroup = SpecsStrings.getRegexGroups(content, pattern, capturingGroup);
	for (String result : capturedGroup) {
	    SpecsLogs.msgInfo(result);
	}

	return;
    }
}
