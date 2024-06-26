package com.nt.red_distribute_api.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

public class KafkaUIClient {
    private String host;

    public KafkaUIClient(String host){
        this.host = host;
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
}
