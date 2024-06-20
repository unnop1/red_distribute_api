package com.nt.red_distribute_api.service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.AlterUserScramCredentialsResult;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.CreateAclsResult;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.DeleteAclsResult;
import org.apache.kafka.clients.admin.DeleteRecordsResult;
import org.apache.kafka.clients.admin.DeletedRecords;
import org.apache.kafka.clients.admin.DescribeAclsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListOffsetsResult;
import org.apache.kafka.clients.admin.ListOffsetsResult.ListOffsetsResultInfo;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.OffsetSpec;
import org.apache.kafka.clients.admin.RecordsToDelete;
import org.apache.kafka.clients.admin.ScramCredentialInfo;
import org.apache.kafka.clients.admin.ScramMechanism;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.admin.UserScramCredentialDeletion;
import org.apache.kafka.clients.admin.UserScramCredentialUpsertion;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.acl.AccessControlEntry;
import org.apache.kafka.common.acl.AccessControlEntryFilter;
import org.apache.kafka.common.acl.AclBinding;
import org.apache.kafka.common.acl.AclBindingFilter;
import org.apache.kafka.common.acl.AclOperation;
import org.apache.kafka.common.acl.AclPermissionType;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.resource.PatternType;
import org.apache.kafka.common.resource.ResourcePattern;
import org.apache.kafka.common.resource.ResourcePatternFilter;
import org.apache.kafka.common.resource.ResourceType;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.nt.red_distribute_api.dto.req.kafka.TopicReq;
import com.nt.red_distribute_api.dto.resp.UserAclsInfo;
import com.nt.red_distribute_api.dto.resp.external.ListConsumeMsg;
import com.nt.red_distribute_api.dto.resp.external.TopicDetailResp;

import jakarta.annotation.PostConstruct;

@Service
public class KafkaClientService {

    @Value("${kafka.bootstrap.server}")
    private String bootstrapServer;

    private AdminClient client = null;

    private final Object lock = new Object();

    private Properties props = new Properties();

    private static final int MAX_POLL_TIMEOUT_MS = 100; // Example: 0.1 seconds

    // Maximum time for the entire consumption loop (in milliseconds)
    private static final long MAX_CONSUME_TIME_MS = 3000; // Example: 3 seconds


    @PostConstruct
    public void init() {
        // Initialize props after the value of bootstrapServer has been injected
        props.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.setProperty("security.protocol", "SASL_PLAINTEXT");
        props.setProperty("sasl.mechanism", "SCRAM-SHA-256");
        props.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"admin\" password=\"admin-secret\";");
        client = AdminClient.create(props);
    }

    // public KafkaClientService() {
    //     // Ideally, you would import these settings from a properties file or the like
    //     // props.setProperty("ssl.endpoint.identification.algorithm", "https");
    //     // props.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "34.142.215.79:9092,34.142.251.254:9092");
    //     props.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
    //     props.setProperty("security.protocol", "SASL_PLAINTEXT");
    //     props.setProperty("sasl.mechanism", "SCRAM-SHA-256");
    //     props.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"admin\" password=\"admin-secret\";");
    //     client = AdminClient.create(props);
    // }
    public void teardown() {
        client.close();
    }

    public void topicListing() throws InterruptedException, ExecutionException {
        ListTopicsResult ltr = client.listTopics();
        KafkaFuture<Set<String>> names = ltr.names();
        System.out.println(names.get());
    }

    public List<UserAclsInfo> ListUserAcls(String username) throws InterruptedException, ExecutionException {
        List<UserAclsInfo> aclHashMaps = new ArrayList<>();
        ResourcePatternFilter patternFilter = new ResourcePatternFilter(ResourceType.ANY, null, PatternType.ANY);
        AccessControlEntryFilter entityFilter = new AccessControlEntryFilter(null, null, AclOperation.ANY, AclPermissionType.ANY);
        if (!username.equals("UnknownUsername")) {
            System.out.println("username:"+username);
            entityFilter = new AccessControlEntryFilter("User:"+username, null, AclOperation.ANY, AclPermissionType.ANY);
        }
        AclBindingFilter userAclsFilter = new AclBindingFilter(
            patternFilter,
            entityFilter
        ); 

        DescribeAclsResult results = client.describeAcls(userAclsFilter);
        
        try {
            // Wait for the future to complete and get the result
            while (!results.values().isDone()){}
            Collection<AclBinding> aclBindings = results.values().get(); 
            System.out.println(results.values());
            if (aclBindings.isEmpty()) {
                System.out.println("No ACLs found matching the filter criteria.");
            } else {
                // Print or process the ACL bindings
                
                for (AclBinding aclBinding : aclBindings) {
                    UserAclsInfo userAclInfo = new UserAclsInfo();
                    userAclInfo.setName(aclBinding.pattern().name());
                    userAclInfo.setResource_type( aclBinding.pattern().resourceType());
                    userAclInfo.setPattern_type( aclBinding.pattern().patternType());
                    userAclInfo.setPrincipal( aclBinding.entry().principal());
                    userAclInfo.setHost( aclBinding.entry().host());
                    userAclInfo.setOperation(aclBinding.entry().operation());
                    userAclInfo.setPermission_type( aclBinding.entry().permissionType());
                    aclHashMaps.add(userAclInfo);
                    System.out.println(aclBinding);
                }
            }
            
            return aclHashMaps;
        } catch (InterruptedException | ExecutionException e) {
            // Handle any exceptions that might occur during the asynchronous operation
            System.err.println("Error retrieving ACLs: " + e.getMessage());
            throw e; // Rethrow the exception to propagate it further if needed
        }
    }

    public ArrayList<Object> createUserAndAcls(String user, String password, List<UserAclsInfo> userTopicAcls, String consumerGroup){
        ArrayList<Object> userAndAcls = new ArrayList<Object>();
        // 1. Create a new user
        userAndAcls.add(createUser(user, password));

        // 2. Create a ACLs 
        userAndAcls.add(createAcls(user, userTopicAcls, consumerGroup));
        return userAndAcls;
    }

    public List<UserAclsInfo> initUserAclsTopicList(String username,List<String> topics){
        List<UserAclsInfo> userAclsTopicList = new ArrayList<UserAclsInfo>();
        for(String topic : topics){
            if (getTopicDescription(topic).getData() == null || getTopicDescription(topic).getError() != null){
                continue;
            }
            UserAclsInfo userAclsInfo = new UserAclsInfo();
            userAclsInfo.setHost("*");
            userAclsInfo.setName(topic);
            userAclsInfo.setOperation(AclOperation.ALL);
            userAclsInfo.setPattern_type(PatternType.LITERAL);
            userAclsInfo.setPermission_type(AclPermissionType.ALLOW);
            userAclsInfo.setPrincipal("User:"+username);
            userAclsInfo.setResource_type(ResourceType.TOPIC);
            userAclsTopicList.add(userAclsInfo);
        }
        return userAclsTopicList;
    }

    public Object createUser(String user, String password){
        ScramMechanism userScramMechanism = ScramMechanism.SCRAM_SHA_256;
        AlterUserScramCredentialsResult results = client.alterUserScramCredentials(Arrays.asList(
            new UserScramCredentialUpsertion(user, new ScramCredentialInfo(userScramMechanism, 8192), password)
        ));
        
        while(!results.all().isDone()){}
        return results.values();
    }

    public Object createAcls(String user, List<UserAclsInfo> userAcls, String consumerGroup){
        try {

            ArrayList<AclBinding> arrayUserAcls = new ArrayList<AclBinding>();

            for (UserAclsInfo aclInfo : userAcls){
                AclBinding userTopicAcl = new AclBinding(
                    new ResourcePattern(aclInfo.getResource_type(), aclInfo.getName(), aclInfo.getPattern_type()),
                    new AccessControlEntry(aclInfo.getPrincipal(), aclInfo.getHost(), aclInfo.getOperation(), aclInfo.getPermission_type())
                ); 
                arrayUserAcls.add(userTopicAcl);
            }

            AclBinding userGroupAcl = new AclBinding(
                new ResourcePattern(ResourceType.GROUP, consumerGroup, PatternType.LITERAL),
                new AccessControlEntry("User:"+user, "*", AclOperation.READ, AclPermissionType.ALLOW)
            );
            arrayUserAcls.add(userGroupAcl);

            System.out.println("arrayUserAcls size:"+arrayUserAcls.size());
        
            CreateAclsResult results = client.createAcls(arrayUserAcls);

            System.out.println("create acls results");
            
            for (KafkaFuture<Void> future : results.values().values())
                future.get();
            return results.all().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteUserAndAcls(String user, List<UserAclsInfo> userAcls){
        // 1. Delete User
        this.deleteUser(user);

        // 2. Delete User ACLs
        this.deleteAcls(user, userAcls);
    }

    public String deleteUser(String user){
        try {
            final String userName = user;
            ScramMechanism userScramMechanism = ScramMechanism.SCRAM_SHA_256;

            AlterUserScramCredentialsResult results = client.alterUserScramCredentials(Arrays.asList(
                new UserScramCredentialDeletion(userName, userScramMechanism)
            ));
            // KafkaFuture<Void> future = results.all();
            for (KafkaFuture<Void> future : results.values().values())
                future.get();
            results.all().get();
            return null;
        }catch (Exception e) {
            return e.getMessage();
        }
        
    }

    public String deleteAcls(String user, List<UserAclsInfo> userAcls){
        
        try {
            /*
            {
                "resourceType": "TOPIC",
                "resourceName": "*",
                "namePatternType": "LITERAL",
                "principal": "User:userapp",
                "host": "*",
                "operation": "ALL",
                "permission": "ALLOW"
            }
            */
            ArrayList<AclBindingFilter> arrayUserAcls = new ArrayList<AclBindingFilter>();
            for (UserAclsInfo aclInfo : userAcls){
                AclBindingFilter userAcl = new AclBindingFilter(
                    new ResourcePatternFilter(aclInfo.getResource_type(), aclInfo.getName(), aclInfo.getPattern_type()),
                    new AccessControlEntryFilter(aclInfo.getPrincipal(), aclInfo.getHost(), aclInfo.getOperation(), aclInfo.getPermission_type())
                );  
                arrayUserAcls.add(userAcl);
            }

        
            DeleteAclsResult results = client.deleteAcls(arrayUserAcls);
            results.all().get();

            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String createTopic(TopicReq topic) {
        try {
            KafkaFuture<Void> future = client.createTopics(
                Collections.singleton(
                    new NewTopic(
                        topic.getTopicName(), 
                        topic.getPartitions(), 
                        topic.getReplicationFactor()
                    ).configs(Collections.singletonMap("retention.ms", topic.getRetentionMs())) // Add retention.ms configuration
                ),
                new CreateTopicsOptions().timeoutMs(10000)).all();
            future.get();
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String updateTopic(TopicReq topic) throws InterruptedException, ExecutionException {
        try{
            // Your Topic Resource
            ConfigResource cr = new ConfigResource(ConfigResource.Type.TOPIC, topic.getTopicName());

            // Create all your configurations
            Collection<ConfigEntry> entries = new ArrayList<>();
            entries.add(new ConfigEntry(TopicConfig.RETENTION_MS_CONFIG, topic.getRetentionMs()));

            // Create the Map
            Config config = new Config(entries);
            Map<ConfigResource, Config> configs = new HashMap<>();
            configs.put(cr, config);

            // Call alterConfigs()
            client.alterConfigs(configs);
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    public String deleteTopic(String topicName) {
        try {
            KafkaFuture<Void> future = client.deleteTopics(Collections.singleton(topicName)).all();
            future.get();
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Map<TopicPartition, Long> calculateConsumerLag(String groupId, Collection<String> topics) throws InterruptedException, ExecutionException {
        Map<TopicPartition, Long> consumerLagMap = new HashMap<>();

        // Get end offsets for the topic partitions
        Map<TopicPartition, OffsetSpec> endOffsetsRequest = new HashMap<>();
        for (String topic : topics) {
            DescribeTopicsResult describeTopicsResult = client.describeTopics(Collections.singletonList(topic));
            Map<String, TopicDescription> topicDescriptionMap = describeTopicsResult.all().get();
            for (Map.Entry<String, TopicDescription> entry : topicDescriptionMap.entrySet()) {
                TopicDescription topicDescription = entry.getValue();
                for (TopicPartitionInfo partitionInfo : topicDescription.partitions()) {
                    TopicPartition topicPartition = new TopicPartition(topic, partitionInfo.partition());
                    endOffsetsRequest.put(topicPartition, OffsetSpec.latest());
                }
            }
        }
        Map<TopicPartition, ListOffsetsResultInfo> endOffsetsResponse = client.listOffsets(endOffsetsRequest).all().get();

        // Get current offsets for the consumer group
        Map<TopicPartition, OffsetAndMetadata> currentOffsetsResponse = client.listConsumerGroupOffsets(groupId).partitionsToOffsetAndMetadata().get();

        // Calculate lag for each partition
        for (Map.Entry<TopicPartition, ListOffsetsResultInfo> entry : endOffsetsResponse.entrySet()) {
            TopicPartition partition = entry.getKey();
            Long endOffset = entry.getValue().offset();
            Long currentOffset = currentOffsetsResponse.containsKey(partition) ? currentOffsetsResponse.get(partition).offset() : 0L;
            Long lag = endOffset - currentOffset;
            consumerLagMap.put(partition, lag);
            System.out.println("endOffset: "+endOffset+", currentOffset: "+currentOffset + ", lag: "+lag);
        }

        return consumerLagMap;
    }

    public String purgeDataInTopic(String topicName) {
        synchronized (lock) {
            try {
                DescribeTopicsResult describeTopicsResult = client.describeTopics(Collections.singletonList(topicName));
                Map<String, TopicDescription> topicDescriptionMap = describeTopicsResult.all().get();
                for (Map.Entry<String, TopicDescription> entry : topicDescriptionMap.entrySet()) {
                    TopicDescription topicDescription = entry.getValue();
                    for (TopicPartitionInfo partitionInfo : topicDescription.partitions()) {
                        TopicPartition topicPartition = new TopicPartition(topicName, partitionInfo.partition());
                        long lastOffset = getLastOffset(topicPartition);
                        RecordsToDelete recordsToDelete = RecordsToDelete.beforeOffset(lastOffset);
                        Map<TopicPartition, RecordsToDelete> topicPartitionRecordToDelete = Collections.singletonMap(topicPartition, recordsToDelete);
                        DeleteRecordsResult deleteRecordsResult = client.deleteRecords(topicPartitionRecordToDelete);
                        Map<TopicPartition, KafkaFuture<DeletedRecords>> lowWatermarks = deleteRecordsResult.lowWatermarks();
                        try {
                            for (Map.Entry<TopicPartition, KafkaFuture<DeletedRecords>> entryDelete : lowWatermarks.entrySet()) {
                                System.out.println(entryDelete.getKey().topic() + " " + entryDelete.getKey().partition() + " " + entryDelete.getValue().get().lowWatermark());
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            } catch (Exception e) {
                return e.getMessage();
            }
        }
    }
    
    private long getLastOffset(TopicPartition topicPartition) throws ExecutionException, InterruptedException {
        try {
            ListOffsetsResult listOffsetsResult = client.listOffsets(Collections.singletonMap(topicPartition, OffsetSpec.latest()));
            Map<TopicPartition, ListOffsetsResultInfo> offsets = listOffsetsResult.all().get();
            return offsets.get(topicPartition).offset();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
    }

    public ListConsumeMsg consumeMessages(String username, String password, String topic, String groupConsumerId, int offset, int limit) {
        ListConsumeMsg resp = new ListConsumeMsg();
        List<String> messageList = Collections.synchronizedList(new ArrayList<>());
        String groupId = groupConsumerId;
        Properties consumeProps = new Properties();
        consumeProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        consumeProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumeProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumeProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumeProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // SASL configuration
        consumeProps.put("security.protocol", "SASL_PLAINTEXT");
        consumeProps.put("sasl.mechanism", "SCRAM-SHA-256");
        consumeProps.put("sasl.jaas.config", String.format(
            "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";",
            username, password
        ));

        int messageLimit = limit; // Limit on the number of messages to consume
        long startOffset = offset;   // Starting offset

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumeProps)) {
            consumer.subscribe(Collections.singletonList(topic));
            consumer.poll(Duration.ofSeconds(1)); // Poll to join the group and get assignment
            consumer.seekToBeginning(consumer.assignment());

            boolean consuming = true;
            while (consuming) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
                for (ConsumerRecord<String, String> record : records) {
                    if (record.offset() >= startOffset && messageList.size() < messageLimit) {
                        messageList.add(record.value());
                    }
                    if (messageList.size() >= messageLimit) {
                        consuming = false;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            resp.setErr(e.getMessage());
        }

        resp.setMessages(messageList);
        return resp;

        
    }

    public String adminPublishMessage(String topic, String message) {
        String errMsg = "";

        Properties publishProps = new Properties();
        publishProps.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        publishProps.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        publishProps.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        publishProps.setProperty("security.protocol", "SASL_PLAINTEXT");
        publishProps.setProperty("sasl.mechanism", "SCRAM-SHA-256");
        publishProps.setProperty("sasl.jaas.config",
                "org.apache.kafka.common.security.scram.ScramLoginModule required " +
                        "username=\"admin\" " +
                        "password=\"admin-secret\";");

        KafkaProducer<String, String> producer = new KafkaProducer<>(publishProps);
        try {
            // Create the Kafka producer
            
            System.out.println("Kafka Producer created successfully.");

            // Prepare the message to send
            String key = ""; // Optional, can be null
            String value = message;

            // Create a ProducerRecord
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);

            // Send the record
            Future<RecordMetadata> future = producer.send(record);
            RecordMetadata metadata = future.get(); // Synchronously wait for the result
            System.out.printf("Sent record(key=%s value=%s) meta(partition=%d, offset=%d)%n",
                    record.key(), record.value(), metadata.partition(), metadata.offset());
        } catch (Exception e) {
            e.printStackTrace();
            errMsg = "error in kafka: " + e.getMessage();
            System.err.println(errMsg);
        } finally {
            // Ensure producer is closed
            if (producer != null) {
                producer.close();
            }
        }
        return errMsg;
    }

    public String consumerPublishMessage(String username, String password, String topic, String message) {
        String errMsg = null;

        Properties publishProps = new Properties();
        publishProps.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        publishProps.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        publishProps.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        publishProps.setProperty("security.protocol", "SASL_PLAINTEXT");
        publishProps.setProperty("sasl.mechanism", "SCRAM-SHA-256");
        publishProps.setProperty("sasl.jaas.config",
                "org.apache.kafka.common.security.scram.ScramLoginModule required " +
                        "username=\"" + username + "\" " +
                        "password=\"" + password + "\";");

        KafkaProducer<String, String> producer = new KafkaProducer<>(publishProps);
        try {
            // Create the Kafka producer
            
            System.out.println("Kafka Producer created successfully.");

            // Prepare the message to send
            String key = ""; // Optional, can be null
            String value = message;

            // Create a ProducerRecord
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);

            // Send the record
            Future<RecordMetadata> future = producer.send(record);
            RecordMetadata metadata = future.get(); // Synchronously wait for the result
            System.out.printf("Sent record(key=%s value=%s) meta(partition=%d, offset=%d)%n",
                    record.key(), record.value(), metadata.partition(), metadata.offset());
        } catch (Exception e) {
            e.printStackTrace();
            errMsg = "error in kafka: " + e.getMessage();
            // System.err.println(errMsg);
        } finally {
            // Ensure producer is closed
            if (producer != null) {
                producer.close();
            }
        }
        return errMsg;
    }


    public TopicDetailResp getTopicDescription(String topicName) {
        TopicDetailResp topicDetail = new TopicDetailResp();
        Collection<TopicListing> listings;
        List<String> selectTopics = new ArrayList<>();
        List<HashMap<String, Object>> dataTopicDetails = new ArrayList<HashMap<String, Object>>();
        HashMap<String, HashMap<String, Object>> mapConfigTopicDetails = new HashMap<String, HashMap<String, Object>>();
        
        try {
            listings = getTopicListing(false);
            List<String> topics = listings.stream().map(TopicListing::name)
            .collect(Collectors.toList());
            
            for(String topic : topics){
                if(topicName.toLowerCase().equals("all")){
                    selectTopics.add(topic);
                    mapConfigTopicDetails.put(topic, new HashMap<String, Object>());
                }else{
                    if(topicName.toUpperCase().equals(topic.toUpperCase())){
                        selectTopics.add(topic);
                        mapConfigTopicDetails.put(topic, new HashMap<String, Object>());
                        break;
                    }
                }
            }
            
            // Describe TOPIC
            DescribeTopicsResult result = client.describeTopics(selectTopics);
            result.values().forEach((key, value) -> {
                try {
                    String detailTopicName = value.get().name();
                    // System.out.println(key + ": " + value.get());
                    HashMap<String, Object> dataTopic = mapConfigTopicDetails.get(detailTopicName);
                    dataTopic.put("topic_name", topicName);
                    dataTopic.put("is_internal", value.get().isInternal());
                    dataTopic.put("partitions", value.get().partitions());
                    // dataTopic.put("acl_operation", value.get().authorizedOperations());
                    mapConfigTopicDetails.put(detailTopicName, dataTopic);
                } catch (InterruptedException e) {
                    topicDetail.setError(e.getMessage());
                } catch (ExecutionException e) {
                    topicDetail.setError(e.getMessage());
                }
            });

            for(String topic : topics){
                dataTopicDetails.add(mapConfigTopicDetails.get(topic));
            }
            topicDetail.setData(dataTopicDetails);
            
        } catch (InterruptedException e) {
            topicDetail.setError(e.getMessage());
        } catch (ExecutionException e) {
            topicDetail.setError(e.getMessage());
        }
        return topicDetail;
    }

    private Collection<TopicListing> getTopicListing(boolean isInternal)
    throws InterruptedException, ExecutionException {
        ListTopicsOptions options = new ListTopicsOptions();
        options.listInternal(isInternal);
        return client.listTopics(options).listings().get();
    }

}
