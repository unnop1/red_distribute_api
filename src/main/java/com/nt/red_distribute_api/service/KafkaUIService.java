package com.nt.red_distribute_api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.client.KafkaUIClient;
import com.nt.red_distribute_api.entity.ConsumerEntity;

@Service
public class KafkaUIService {
    @Value("${kafka.kafka-ui.host}")
    private String kafkaUiHost;

    @Value("${kafka.kafka-ui.username}")
    private String username;

    @Value("${kafka.kafka-ui.password}")
    private String password;

    @Autowired
    private ConsumerService consumerService;


    public List<HashMap<String, Object>> GetConsumerGroup() throws Exception{
        List<HashMap<String, Object>> dataConsumers = new ArrayList<HashMap<String, Object>>();

        KafkaUIClient kafkaUIClient = new KafkaUIClient(kafkaUiHost, username, password);
        kafkaUIClient.loginWithCookieManager();

        JSONObject consumerResp = kafkaUIClient.GetConsumerGroups();

        JSONArray consumerGroups = new JSONArray(consumerResp.getJSONArray("consumerGroups"));

        List<ConsumerEntity> consumerAll = consumerService.getAllConsumer();
        HashMap<String, Long> consumerMapIDs = new HashMap<String, Long>();
        for(ConsumerEntity consumerEntity : consumerAll){
            consumerMapIDs.put(consumerEntity.getConsumer_group(), consumerEntity.getID());
        }

        for(int i = 0; i < consumerGroups.length(); i++){
            HashMap<String, Object> dataConsumer = new HashMap<String, Object>();
            JSONObject consumerGroup = consumerGroups.getJSONObject(i);
            String groupID = consumerGroup.getString("groupId"); 
            if(consumerMapIDs.get(groupID) != null){
                HashMap<String, Object> coordinatorData = new HashMap<String, Object>();

                JSONObject coordinator = consumerGroup.getJSONObject("coordinator");
                coordinatorData.put("id", coordinator.getInt("id"));
                coordinatorData.put("host", coordinator.getString("host"));
                coordinatorData.put("port", coordinator.getInt("port"));

                dataConsumer.put("topics", consumerGroup.getInt("topics"));
                dataConsumer.put("inherit", consumerGroup.getString("inherit"));
                dataConsumer.put("partitionAssignor", consumerGroup.getString("partitionAssignor"));
                dataConsumer.put("simple", consumerGroup.getBoolean("simple"));
                dataConsumer.put("state", consumerGroup.getString("state"));
                dataConsumer.put("messagesBehind", consumerGroup.getInt("messagesBehind"));
                dataConsumer.put("members", consumerGroup.getInt("members"));
                dataConsumer.put("groupId", consumerGroup.getString("groupId"));
                dataConsumer.put("coordinator", coordinatorData);
                dataConsumer.put("id", consumerMapIDs.get(groupID));

                dataConsumers.add(dataConsumer);
            }
            

        }

        return dataConsumers;


    }

    public JSONObject GetConsumerGroupByConsumerGroupId(String consumerGroupID) throws Exception{
        KafkaUIClient kafkaUIClient = new KafkaUIClient(kafkaUiHost, username, password);
        kafkaUIClient.loginWithCookieManager();
        return kafkaUIClient.GetConsumerGroupByGroupId(consumerGroupID);


    }
}
