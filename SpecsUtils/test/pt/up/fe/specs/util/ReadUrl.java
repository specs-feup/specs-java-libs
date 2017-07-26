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

package pt.up.fe.specs.util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

import org.junit.Test;

import pt.up.fe.specs.util.utilities.StringLines;

public class ReadUrl {

    @Test
    public void test() {
	// String urlPath = "http://sourceforge.net/directory/language:matlab/?q=matlab";
	String urlPath = "http://sourceforge.net/directory/language%3Amatlab/?q=matlab&page=%INDEX%&_pjax=true";
	String webpage = null;

	File outfile = new File("C:/temp_output/sourceforge_matlab_projects.txt");

	// Set<String> projectNames = FactoryUtils.newHashSet();

	for (int i = 1; i <= 32; i++) {

	    URLConnection con = null;
	    try {
		String parsedUrl = urlPath.replace("%INDEX%", Integer.toString(i));
		System.out.println("TRYING PAGE " + i + "... " + parsedUrl);
		URL url = new URL(parsedUrl);
		con = url.openConnection();
		con.setConnectTimeout(10000);
		con.setReadTimeout(10000);
	    } catch (Exception e) {
		throw new RuntimeException("Could not open URL connection '" + urlPath + "'", e);
	    }

	    try (InputStream in = con.getInputStream()) {
		webpage = SpecsIo.read(in);
		System.out.println("Retrived page, parsing.");
		Set<String> projects = parseProjectNames(webpage);
		// projectNames.addAll(projects);
		System.out.println("Appending '" + projects.size() + "' projects.");
		writeProjectNames(outfile, projects, i);
	    } catch (Exception e) {
		throw new RuntimeException("Could not read webpage in connection '" + con + "'", e);
	    }

	}

	// writeProjectNames(, projectNames);

	// System.out.println("PROJECT NAMES ("+projectNames.size()+"):\n" + projectNames);

    }

    private static void writeProjectNames(File output, Set<String> projectNames, int pageIndex) {
	if (projectNames.isEmpty()) {
	    return;
	}
	StringBuilder builder = new StringBuilder();

	builder.append("Results for page " + pageIndex + "\n");
	for (String name : projectNames) {
	    builder.append(name).append("\n");
	}
	builder.append("\n");

	SpecsIo.append(output, builder.toString());
    }

    public Set<String> parseProjectNames(String sourcefourceWebpage) {
	String regex = "/projects/(.+?)/";
	String regexStop = "(Staff Picks)";

	Set<String> projectNames = SpecsFactory.newHashSet();
	for (String line : StringLines.newInstance(sourcefourceWebpage)) {
	    // Check if stopping condition
	    String stopString = SpecsStrings.getRegexGroup(line, regexStop, 1);
	    if (stopString != null) {
		break;
	    }

	    String project = SpecsStrings.getRegexGroup(line, regex, 1);
	    if (project == null) {
		continue;
	    }

	    projectNames.add(project);
	}

	return projectNames;
	// System.out.println("PROJECTS ("+projectNames.size()+"):"+projectNames);
    }

}
