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

package org.suikasoft.jOptions.Utils;

import java.io.File;

import pt.up.fe.specs.util.SpecsIo;

/**
 * TODO: Rename to ConfigFile
 * 
 * @author JoaoBispo
 *
 */
public class SetupFile {

    private File setupFile;

    public SetupFile() {
	setupFile = null;
    }

    public SetupFile setFile(File setupFile) {
	this.setupFile = setupFile;
	return this;
    }

    public File getFile() {
	return setupFile;
    }

    /**
     * If no setup file is defined, returns the current work folder.
     * 
     * @return
     */
    public File getParentFolder() {
	if (setupFile == null) {
	    return SpecsIo.getWorkingDir();
	}

	File parent = setupFile.getParentFile();

	if (parent == null) {
	    return SpecsIo.getWorkingDir();
	}

	return parent;
    }

    /**
     * 
     */
    public void resetFile() {
	setupFile = null;
    }

}
