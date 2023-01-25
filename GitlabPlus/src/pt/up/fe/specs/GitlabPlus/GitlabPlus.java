package pt.up.fe.specs.GitlabPlus;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.GsonBuilder;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.lazy.LazyString;

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

public class GitlabPlus {

    private static final String TOKEN_FILE = "_token";

    private static final LazyString token = new LazyString(() -> SpecsIo.read(TOKEN_FILE));

    public static void createRepository(int groupId, String projectName, String projectDescription)
            throws URISyntaxException {

        Map<String, String> data = new HashMap<>();
        data.put("name", projectName);
        data.put("description", projectDescription);
        data.put("namespace_id", String.valueOf(groupId));

        String dataString = new GsonBuilder().setPrettyPrinting().create().toJson(data);

        var request = HttpRequest.newBuilder()
                .uri(new URI("https://git.fe.up.pt/api/v4/projects/"))
                .header("PRIVATE-TOKEN", token.toString())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(dataString))
                .build();
    }
}
