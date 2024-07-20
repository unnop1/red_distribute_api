package com.nt.red_distribute_api.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaUIClient {
    private String host;
    private String username;
    private String password;
    private String session;

    public KafkaUIClient(String host, String username, String password){
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public static String extractSessionId(String response) {
        if (response != null && response.contains("=")) {
            return response.substring(response.indexOf('=') + 1);
        }
        return null;
    }
    
    public String loginWithCookieManager() throws Exception {
        // Set up the CookieManager
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        HttpClient client = HttpClient.newBuilder()
                                      .cookieHandler(cookieManager)
                                      .version(Version.HTTP_1_1)
                                      .build();

        // Prepare the POST data
        String postData = "username=" + URLEncoder.encode(username, StandardCharsets.UTF_8) + 
                          "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(new URI(host + "/auth"))
                                         .header("Content-Type", "application/x-www-form-urlencoded")
                                         .POST(BodyPublishers.ofString(postData))
                                         .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response Code: " + response.statusCode());

        // Get cookies from the cookie manager
        Map<String, List<String>> cookieMap = cookieManager.get(URI.create(host), response.headers().map());
        String sessionCookie = "";
        for (Map.Entry<String, List<String>> entry : cookieMap.entrySet()) {
            System.out.println("key: " + entry.getKey());
            if ("Cookie".equalsIgnoreCase(entry.getKey())) {
                System.out.println("value: " + entry.getValue());
                for (String cookie : entry.getValue()) {
                    if (cookie.toLowerCase().contains("session")) {
                        sessionCookie = cookie.split(";")[0]; // Extract the session cookie
                        break;
                    }
                }
            }
        }
        this.session = sessionCookie;

        // Return the session cookie
        return sessionCookie;
    } 

    public KafkaListTopicsResp GetKafkaListTopics(){
        KafkaListTopicsResp respData = new KafkaListTopicsResp();
        try {
            URL url = new URL(String.format(
                    "http://%s/api/clusters/kafka-cluster/topics?page=1&perPage=0",
                    host
                )
            );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response into MetricsResp object using ObjectMapper
                ObjectMapper objectMapper = new ObjectMapper();
                KafkaListTopics data = objectMapper.readValue(response.toString(), KafkaListTopics.class);
                respData.setTopics(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String urltest = String.format(
                "http://%s/api/clusters/kafka-cluster/topics?page=1&perPage=0",
                host
            );
            respData.setError(e.getMessage()+" url:" +urltest);
        }
        return respData;
    }

    public KafkaListTopicsResp GetKafkaListTopics(String topic){
        KafkaListTopicsResp respData = new KafkaListTopicsResp();
        try {
            URL url = new URL(String.format(
                    "http://%s/api/clusters/kafka-cluster/topics/%s",
                    host,
                    topic.toUpperCase()
                )
            );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response into MetricsResp object using ObjectMapper
                ObjectMapper objectMapper = new ObjectMapper();
                KafkaListTopics data = objectMapper.readValue(response.toString(), KafkaListTopics.class);
                respData.setTopics(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String urltest = String.format(
                "http://%s/api/clusters/kafka-cluster/topics/%s",
                host
            );
            respData.setError(e.getMessage()+" url:" +urltest);
        }
        return respData;
    }

    public JSONObject GetConsumerGroups(){
        try {
            // Set up the URL and connection
            URL url = new URL(String.format("%s/api/clusters/kafka-cluster/consumer-groups/paged?page=1&perPage=0&search=", host));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Set headers
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Cookie", session);

            // Send the request and get the response code
            int responseCode = connection.getResponseCode();
            // System.out.println("Response Code: " + responseCode);

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Print the response body
            System.out.println("Response code: " + responseCode);

            // Parse the response into a JSONObject
            if (responseCode == 200) {
            JSONObject jsonResponse = new JSONObject(response.toString());

            // Extract the "consumerGroups" array
            // JSONArray consumerGroups = jsonResponse.getJSONArray("consumerGroups");

            // Iterate through the array if needed
            // for (int i = 0; i < consumerGroups.length(); i++) {
            //     JSONObject consumerGroup = consumerGroups.getJSONObject(i);
            //     // Access individual fields in each consumer group object
            //     String groupId = consumerGroup.getString("groupId");
            //     int members = consumerGroup.getInt("members");
            //     int topics = consumerGroup.getInt("topics");
            //     // Print or process these values as needed
            //     System.out.println("Consumer Group ID: " + groupId);
            //     System.out.println("Members: " + members);
            //     System.out.println("Topics: " + topics);
            //     // Access more fields as necessary
            // }
                return jsonResponse;
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject GetConsumerGroupByGroupId(String consumerGroupId){
        try {
            // Set up the URL and connection
            URL url = new URL(String.format("%s/api/clusters/kafka-cluster/consumer-groups/%s", host, consumerGroupId));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Set headers
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Cookie", session);

            // Send the request and get the response code
            int responseCode = connection.getResponseCode();
            // System.out.println("Response Code: " + responseCode);

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Print the response body
            System.out.println("Response code: " + responseCode);

            // Parse the response into a JSONObject
            if (responseCode == 200) {
            JSONObject jsonResponse = new JSONObject(response.toString());

            // Extract the "consumerGroups" array
            // JSONArray consumerGroups = jsonResponse.getJSONArray("consumerGroups");

            // Iterate through the array if needed
            // for (int i = 0; i < consumerGroups.length(); i++) {
            //     JSONObject consumerGroup = consumerGroups.getJSONObject(i);
            //     // Access individual fields in each consumer group object
            //     String groupId = consumerGroup.getString("groupId");
            //     int members = consumerGroup.getInt("members");
            //     int topics = consumerGroup.getInt("topics");
            //     // Print or process these values as needed
            //     System.out.println("Consumer Group ID: " + groupId);
            //     System.out.println("Members: " + members);
            //     System.out.println("Topics: " + topics);
            //     // Access more fields as necessary
            // }
                return jsonResponse;
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
