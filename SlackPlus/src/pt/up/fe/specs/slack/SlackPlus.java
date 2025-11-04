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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.slack;

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
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.lazy.LazyString;

public class SlackPlus {

    private static boolean debug = SpecsSystem.isDebug();

    private static final String TOKEN_FILE_NAME = "_token";

    // this token is a workspace-level token
    private static final LazyString token = new LazyString(() -> SpecsIo.read(TOKEN_FILE_NAME).trim());

    /**
     * Tries to create a new public channel. On success, {@link Result} has the ID of the new channel. On error, it has
     * the error string.
     * 
     * @param channelName
     * @return
     */
    public static Result<String, String> createPublicChannel(String channelName) {

        return createChannel(channelName, false);
    }

    /**
     * Tries to create a new channel. On success, {@link Result} has the ID of the new channel. On error, it has the
     * error string.
     * 
     * @param channelName
     * @param isPrivate
     * @return
     */
    public static Result<String, String> createChannel(String channelName, boolean isPrivate) {

        Map<String, String> requiredData = new HashMap<>();
        requiredData.put("name", channelName);
        requiredData.put("is_private", String.valueOf(isPrivate));
        String dataString = new GsonBuilder().setPrettyPrinting().create().toJson(requiredData);

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI("https://slack.com/api/conversations.create"))
                    .header("Authorization", "Bearer " + token.toString())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(dataString))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> responseString;
        try {
            responseString = client.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> response = convertResponse(responseString);

        if (responseOk(response)) {

            Map<String, Object> channel = (Map<String, Object>) response.get("channel");
            String channelId = (String) channel.get("id");

            return Result.ok(channelId);
        }

        debugPrint(request, responseString);
        String error = (String) response.get("error");
        return Result.err(error);
    }

    // TODO: return email instead of ID
    /**
     * Tries to add a list of users to a channel. On error, returns a {@link List} of {@link UserError}. On success,
     * {@link Result} returns the channel ID. Note that even if there are errors for individual users, the remaining
     * ones may have been inserted. Only those in the errors list actually failed.
     * 
     * @param userIds
     * @param channelId
     * @return
     */
    public static Result<String, List<UserError>> addUsersToChannel(List<String> userIds, String channelId) {

        Map<String, Object> requiredData = new HashMap<>();
        requiredData.put("channel", channelId);
        requiredData.put("users", userIds);
        String dataString = new GsonBuilder().setPrettyPrinting().create().toJson(requiredData);

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI("https://slack.com/api/conversations.invite"))
                    .header("Authorization", "Bearer " + token.toString())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(dataString))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> responseString;
        try {
            responseString = client.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> response = convertResponse(responseString);

        // if there are different errors for different users
        List<Map<String, Object>> errors = (List<Map<String, Object>>) response.get("errors");
        if (errors != null) {

            List<UserError> userErrors = errors.stream()
                    .map(e -> new UserError(
                            (String) e.get("user"),
                            (String) e.get("error")))
                    .collect(Collectors.toList());

            return Result.err(userErrors);
        }

        // if there is only one error for all users
        if (!responseOk(response)) {

            List<UserError> userErrors = userIds.stream()
                    .map(e -> new UserError(
                            e,
                            (String) response.get("error")))
                    .collect(Collectors.toList());

            return Result.err(userErrors);
        }

        // success
        Map<String, Object> channel = (Map<String, Object>) response.get("channel");
        String insertedChannelId = (String) channel.get("id");
        return Result.ok(insertedChannelId);
    }

    /**
     * Tries to find a channel ID by name. On success, {@link Result} has the ID of the channel. On error, it has the
     * error string.
     * 
     * @param testName
     * @return
     */
    public static Result<String, String> findChannel(String testName) {

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI("https://slack.com/api/conversations.list"))
                    .header("Authorization", "Bearer " + token.toString())
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> responseString;
        try {
            responseString = client.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> response = convertResponse(responseString);

        if (responseOk(response)) {

            List<Map<String, Object>> channels = (List<Map<String, Object>>) response.get("channels");

            for (var channel : channels) {

                String channelName = (String) channel.get("name");

                if (testName.equals(channelName)) {

                    String channelId = (String) channel.get("id");
                    return Result.ok(channelId);
                }
            }

            return Result.err("no_channel_found");
        }

        debugPrint(request, responseString);
        String error = (String) response.get("error");
        return Result.err(error);
    }

    /**
     * Tries to find a student's email based on ID. On success, {@link Result} has the email of the student. On error,
     * it has the error string.
     * 
     * @param id
     * @return
     */
    public static Result<String, String> getEmailFromId(String id) {

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI("https://slack.com/api/users.info?user=" + id))
                    .header("Authorization", "Bearer " + token.toString())
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> responseString;
        try {
            responseString = client.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> response = convertResponse(responseString);

        if (responseOk(response)) {

            Map<String, Object> channel = (Map<String, Object>) response.get("user");
            Map<String, Object> profile = (Map<String, Object>) channel.get("profile");

            String email = (String) profile.get("email");

            return Result.ok(email);
        }

        debugPrint(request, responseString);
        String error = (String) response.get("error");
        return Result.err(error);
    }

    /**
     * Tries to find a student based on email. On success, {@link Result} has the ID of the student. On error, it has
     * the error string.
     * 
     * @param email
     * @return
     */
    public static Result<String, String> getIdFromEmail(String email) {

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI("https://slack.com/api/users.lookupByEmail?email=" + email))
                    .header("Authorization", "Bearer " + token.toString())
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> responseString;
        try {
            responseString = client.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> response = convertResponse(responseString);

        if (responseOk(response)) {

            Map<String, Object> channel = (Map<String, Object>) response.get("user");

            String id = (String) channel.get("id");
            return Result.ok(id);
        }

        debugPrint(request, responseString);
        String error = (String) response.get("error");
        return Result.err(error);
    }

    private static boolean responseOk(Map<String, Object> map) {
        return (boolean) map.get("ok");
    }

    private static void debugPrint(HttpRequest request, HttpResponse<String> response) {

        if (debug) {
            System.out.println(request);
            System.out.println(response);
            System.out.println(response.body());
        }
    }

    private static Map<String, Object> convertResponse(HttpResponse<String> responseString) {
        return new Gson().fromJson(responseString.body(), Map.class);
    }
}
