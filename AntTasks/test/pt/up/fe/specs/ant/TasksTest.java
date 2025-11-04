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

import org.junit.Test;

import pt.up.fe.specs.ant.tasks.Sftp;
import pt.up.fe.specs.util.SpecsIo;

public class TasksTest {

    @Test
    public void testSftp() {
        // Create dummy file for transfer
        File dummyFile = new File("dummmy_file.txt");
        SpecsIo.write(dummyFile, "dummy");

        AntTask sftpTask = new Sftp()
                .set(Sftp.LOGIN, "login")
                .set(Sftp.PASS, "pass")
                .set(Sftp.HOST, "host")
                .set(Sftp.PORT, "port")
                .set(Sftp.DESTINATION_FOLDER, "destinationFolder")
                .set(Sftp.NEW_FILENAME, "new_name.txt")
                .set(Sftp.FILE_TO_TRANSFER, dummyFile);

        System.out.println("Output:\n" + sftpTask.getScript());

        SpecsIo.delete(dummyFile);
    }

}
