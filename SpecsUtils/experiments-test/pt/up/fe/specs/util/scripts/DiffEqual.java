/**
 * Copyright 2014 SPeCS Research Group.
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

import java.io.File;

import org.junit.Test;

import pt.up.fe.specs.util.SpecsIo;

public class DiffEqual {

    @Test
    public void test() {
	String parent = "C:\\Users\\JoaoBispo\\Dropbox\\Research\\Work\\2014-10-24 Add BLAS support\\blas_outputs";
	File file1 = new File(parent, "C_naive.txt");
	File file2 = new File(parent, "C_blas.txt");

	String string1 = SpecsIo.read(file1);
	String string2 = SpecsIo.read(file2);

	System.out.println("ARE EQUAL? " + string1.equals(string2));
    }

}
