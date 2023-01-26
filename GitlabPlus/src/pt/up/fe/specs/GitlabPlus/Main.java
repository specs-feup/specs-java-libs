/**
 * Copyright 2023 SPeCS.
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

package pt.up.fe.specs.GitlabPlus;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import pt.up.fe.specs.GitlabPlus.GitlabPlus.Role;

public class Main {

    // group ID for Compilers2023
    // public static final int GROUP_ID = 5527;

    // group ID for Compilers2023Test
    public static final int GROUP_ID = 5548;

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {

        String projectName = "comp2023-1a";
        String projectDescription = "Project for group A of class 1";

        Map<String, String> data = new HashMap<>();
        data.put("name", projectName);
        data.put("description", projectDescription);
        data.put("namespace_id", String.valueOf(GROUP_ID));
        data.put("initialize_with_readme", "true");

        String projectId = GitlabPlus.createRepository(data).orElseThrow();
        System.out.println("ID of newly created project: " + projectId);

        String addedMemberId = GitlabPlus.addUserToProject(projectId, "jbispo", Role.DEVELOPER)
                .orElseThrow();
        System.out.println("ID of added member: " + addedMemberId);
    }
}
