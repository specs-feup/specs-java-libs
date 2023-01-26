package pt.up.fe.specs.GitlabPlus;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
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

    public enum Role {
        DEVELOPER("30"),
        MAINTAINER("40"),
        OWNER("50");

        private final String level;

        private Role(String level) {
            this.level = level;
        }

        @Override
        public String toString() {
            return level;
        }
    }

    private static final String TOKEN_FILE_NAME = "_token";
    private static final String DEBUG_FILE_NAME = "_debug";

    private static final LazyString token = new LazyString(() -> SpecsIo.read(TOKEN_FILE_NAME).trim());
    private static boolean debug = false;
    static {
        File f = new File(DEBUG_FILE_NAME);
        if (f.exists() && !f.isDirectory()) {
            debug = true;
        }
    }

    public static Optional<String> addUserToProject(String projectId, String username, Role role)
            throws URISyntaxException, IOException, InterruptedException {

        return addUserToProjectFromId(projectId, getUserId(username), role);
    }

    public static Optional<String> addUserToProjectFromId(String projectId, String userId, Role role)
            throws URISyntaxException, IOException, InterruptedException {

        Map<String, String> requiredData = new HashMap<>();
        requiredData.put("user_id", userId);
        requiredData.put("access_level", role.toString());
        String dataString = new GsonBuilder().setPrettyPrinting().create().toJson(requiredData);

        var request = HttpRequest.newBuilder()
                .uri(new URI("https://git.fe.up.pt/api/v4/projects/" + projectId + "/members"))
                .header("PRIVATE-TOKEN", token.toString())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(dataString))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        var response = client.send(request, BodyHandlers.ofString());

        if (debug) {
            System.out.println(request);
            System.out.println(response);
            System.out.println(response.body());
        }

        if (response.statusCode() != 201) {

            return Optional.empty();
        }

        return Optional.of(getIdFromObject(response));
    }

    public static Optional<String> createRepository(Map<String, String> data)
            throws URISyntaxException, IOException, InterruptedException {

        String dataString = new GsonBuilder().setPrettyPrinting().create().toJson(data);

        var request = HttpRequest.newBuilder()
                .uri(new URI("https://git.fe.up.pt/api/v4/projects/"))
                .header("PRIVATE-TOKEN", token.toString())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(dataString))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        var response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 201) {

            return Optional.empty();
        }

        return Optional.of(getIdFromObject(response));
    }

    /**
     * Gets the user ID from the username. For instance, username pmsp returns ID 1387.
     * 
     * @param username
     *            the username used to access git.fe.up.pt, e.g., pmsp or up123456789
     * @return a string containing the ID
     * 
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public static String getUserId(String username) throws URISyntaxException, IOException, InterruptedException {

        var request = HttpRequest.newBuilder()
                .uri(new URI("https://git.fe.up.pt/api/v4/users?username=" + username))
                .header("PRIVATE-TOKEN", token.toString())
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        var response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {

            throw new RuntimeException("Failed to get user ID from username");
        }

        return getIdFromArray(response, 0, true);
    }

    /**
     * Returns the string representation an ID coming from a response object.
     * 
     * @param response
     * @return
     */
    private static String getIdFromObject(HttpResponse<String> response) {

        Map<String, Object> map = new Gson().fromJson(response.body(), Map.class);

        return getId(map);
    }

    /**
     * Returns the string representation an ID coming from a response array.
     * 
     * @param response
     * @param position
     * @param throwOnMultiple
     * @return
     */
    private static String getIdFromArray(HttpResponse<String> response, int position, boolean throwOnMultiple) {

        List<Object> list = new Gson().fromJson(response.body(), List.class);

        if (throwOnMultiple) {

            if (list.size() > 1) {

                throw new RuntimeException("Query returned multiple results when only one was expected");
            }
        }

        Map map = (Map) list.get(position);

        return getId(map);
    }

    /**
     * Gets the string representation of the ID attribute of an unknown response object.
     * 
     * @param data
     * @return
     */
    private static String getId(Map data) {

        return String.valueOf((double) data.get("id")).replace(".0", "");
    }
}
