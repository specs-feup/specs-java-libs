/**
 *  Copyright 2013 SPeCS Research Group.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.eclipse;

import java.io.File;

import org.junit.Test;

import pt.up.fe.specs.eclipse.Utilities.DeployUtils;

/**
 * @author Joao Bispo
 *
 */
public class FtpTester {

    @Test
    public void testFtp() {
	
	DeployUtils.runAnt(new File("ftp.xml"));
	
	/*
	// Run script
		// ProcessUtils.run(Arrays.asList("ant", "build.xml"), IoUtils.getWorkingDir().getPath());
		// Launch ant
		Project project = new Project();
		project.init();

		File buildFile = new File("ant_ftp.xml");

		ProjectHelper.configureProject(project, buildFile);

		project.addBuildListener(DeployUtils.newStdoutListener());
		
		project.executeTarget(project.getDefaultTarget());
	*/
	//ProcessUtils.run(Arrays.asList("WinSCP.com"), IoUtils.getWorkingDir().getAbsolutePath());
	
	/*
	FTPClient client = new FTPClient();
	
	try {
	    client.connect("specsuser:SpecS#12345@specs.fe.up.pt");
	    //client.login("specsuser", "SpecS#12345");
	    String dir = client.currentDirectory();
	    client.disconnect(true);
	} catch (IllegalStateException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (FTPIllegalReplyException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (FTPException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	*/
	
	
    }
}
