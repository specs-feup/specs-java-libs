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
import java.util.Arrays;
import java.util.Set;

/**
 * @author Joao Bispo
 *
 */
public class UsbTester {

    /**
     * @param args
     */
    public static void main(String[] args) {
	Set<File> previousRoots = SpecsFactory.newHashSet(Arrays.asList(File.listRoots()));

	while (true) {

	    Set<File> currentRoots = SpecsFactory.newHashSet(Arrays.asList(File.listRoots()));

	    Set<File> newRoots = SpecsFactory.newHashSet(currentRoots);
	    newRoots.removeAll(previousRoots);

	    if (!newRoots.isEmpty()) {
		System.out.println("NEW DEVICES:" + newRoots);
	    }

	    Set<File> missingRoots = SpecsFactory.newHashSet(previousRoots);
	    missingRoots.removeAll(currentRoots);

	    if (!missingRoots.isEmpty()) {
		System.out.println("EJECTED DEVICES:" + missingRoots);
	    }

	    previousRoots = currentRoots;

	    /*
	    File[] roots = File.listRoots();
	    for(File root : roots) {
	    if(previousRoots.contains(root)) {
	        continue;
	    }
	    
	    System.out.println("FOUND SOMETHING DIFFERENT:"+root);
	    previousRoots.add(root);
	    }
	     */

	    System.out.println("Sleeping for 1sec");
	    SpecsSystem.sleep(1000);

	}

    }

}
