package com.nt.red_distribute_api.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.client.KafkaUIClient;

@Service
public class KafkaUIService {
    @Value("${kafka.kafka-ui.host}")
    private String kafkaUiHost;

    @Value("${kafka.kafka-ui.username}")
    private String username;

    @Value("${kafka.kafka-ui.password}")
    private String password;


    public JSONObject GetConsumerGroup() throws Exception{
        KafkaUIClient kafkaUIClient = new KafkaUIClient(kafkaUiHost, username, password);
        kafkaUIClient.loginWithCookieManager();
        return kafkaUIClient.GetConsumerGroups();


    }
}
