package com.nt.red_distribute_api.service;

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
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.AlterUserScramCredentialsResult;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.CreateAclsResult;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.DeleteAclsResult;
import org.apache.kafka.clients.admin.DescribeAclsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.ScramCredentialInfo;
import org.apache.kafka.clients.admin.ScramMechanism;
import org.apache.kafka.clients.admin.UserScramCredentialDeletion;
import org.apache.kafka.clients.admin.UserScramCredentialUpsertion;
import org.apache.kafka.common.KafkaFuture;
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
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.dto.req.kafka.TopicReq;

@Service
public class KafkaClientService {
    private AdminClient client = null;

    public KafkaClientService() {
        // Ideally, you would import these settings from a properties file or the like
        Properties props = new Properties();
        // props.setProperty("ssl.endpoint.identification.algorithm", "https");
        props.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "34.142.215.79:9092,34.142.251.254:9092");
        props.setProperty("security.protocol", "SASL_PLAINTEXT");
        props.setProperty("sasl.mechanism", "SCRAM-SHA-256");
        props.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"admin\" password=\"admin-secret\";");
        client = AdminClient.create(props);
    }
    public void teardown() {
        client.close();
    }

    public void topicListing() throws InterruptedException, ExecutionException {
        ListTopicsResult ltr = client.listTopics();
        KafkaFuture<Set<String>> names = ltr.names();
        System.out.println(names.get());
    }

    public Object ListUserAcls(String username) throws InterruptedException, ExecutionException {
        List<Object> aclHashMaps = new ArrayList<>();
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
                    HashMap<String,Object> aclHashMap = new HashMap<String,Object>();
                    aclHashMap.put("name", aclBinding.pattern().name());
                    aclHashMap.put("resource_type", aclBinding.pattern().resourceType());
                    aclHashMap.put("pattern_type", aclBinding.pattern().patternType());
                    aclHashMap.put("principal", aclBinding.entry().principal());
                    aclHashMap.put("host", aclBinding.entry().host());
                    aclHashMap.put("operation", aclBinding.entry().operation());
                    aclHashMap.put("permission_type", aclBinding.entry().permissionType());
                    aclHashMaps.add(aclHashMap);
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

    public ArrayList<Object> createUserAndAcls(String user, String password, String[] topics, String consumerGroup){
        ArrayList<Object> userAndAcls = new ArrayList<Object>();
        // 1. Create a new user
        userAndAcls.add(createUser(user, password));

        // 2. Create a ACLs 
        userAndAcls.add(createAcls(user, topics, consumerGroup));
        return userAndAcls;
    }

    public Object createUser(String user, String password){
        ScramMechanism userScramMechanism = ScramMechanism.SCRAM_SHA_256;
        AlterUserScramCredentialsResult results = client.alterUserScramCredentials(Arrays.asList(
            new UserScramCredentialUpsertion(user, new ScramCredentialInfo(userScramMechanism, 8192), password)
        ));
        
        while(!results.all().isDone()){}
        return results.values();
    }

    public Object createAcls(String user, String[] topics, String consumerGroup){
        try {

            ArrayList<AclBinding> arrayUserAcls = new ArrayList<AclBinding>();

            for (String topic : topics){
                System.out.println(topic);
                AclBinding userTopicAcl = new AclBinding(
                    new ResourcePattern(ResourceType.TOPIC, topic, PatternType.LITERAL),
                    new AccessControlEntry("User:"+user, "*", AclOperation.ALL, AclPermissionType.ALLOW)
                );

                AclBinding userGroupAcl = new AclBinding(
                    new ResourcePattern(ResourceType.GROUP, consumerGroup, PatternType.LITERAL),
                    new AccessControlEntry("User:"+user, "*", AclOperation.READ, AclPermissionType.ALLOW)
                );
                arrayUserAcls.add(userTopicAcl);
                arrayUserAcls.add(userGroupAcl);
            }
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

    public void deleteUserAndAcls(String user, String[] topics){
        // 1. Delete User
        this.deleteUser(user);

        // 2. Delete User ACLs
        this.deleteAcls(user, topics);
    }

    public void deleteUser(String user){
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

        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        
    }

    public void deleteAcls(String user, String[] topics){
        
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
            for (String topic : topics){
                AclBindingFilter userAcl = new AclBindingFilter(
                    new ResourcePatternFilter(ResourceType.TOPIC, topic, PatternType.ANY),
                    new AccessControlEntryFilter("User:"+user, "*", AclOperation.ALL, AclPermissionType.ANY)
                );  
                arrayUserAcls.add(userAcl);
            }

        
            DeleteAclsResult results = client.deleteAcls(arrayUserAcls);
            results.all().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void createTopic(TopicReq topic) {
        try {
            KafkaFuture<Void> future = client.createTopics(
                Collections.singleton(
                    new NewTopic(
                        topic.getTopicName(), 
                        topic.getPartitions(), 
                        topic.getReplicationFactor()
                    )
                ),
                new CreateTopicsOptions().timeoutMs(10000)).all();
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void updateTopic(TopicReq topic) throws InterruptedException, ExecutionException {
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

    }

    public void deleteTopic(String topicName) {
        KafkaFuture<Void> future = client.deleteTopics(Collections.singleton(topicName)).all();
        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
