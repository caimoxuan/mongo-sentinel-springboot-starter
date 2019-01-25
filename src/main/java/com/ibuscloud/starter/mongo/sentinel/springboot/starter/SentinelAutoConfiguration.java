package com.ibuscloud.starter.mongo.sentinel.springboot.starter;

import com.mongodb.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author cmx
 * @date 2019/1/23
 */
@Slf4j
@Configuration
@ConditionalOnClass(MongoClient.class)
@EnableConfigurationProperties(SentinelProperties.class)
@ConditionalOnMissingBean(type = "org.springframework.data.mongodb.MongoDbFactory")
public class SentinelAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MongoDbFactory.class)
    public SimpleMongoDbFactory mongoDbFactory(MongoClient mongo, SentinelProperties sentinelProperties) throws Exception {
        String database = sentinelProperties.getDatabase();
        return new SimpleMongoDbFactory(mongo, database);
    }



    @Bean
    @ConditionalOnMissingBean
    public MongoClient mongo(SentinelProperties properties){

       log.info("start auto config mongo sentinel props: {} ", properties);
        if(properties == null){
            log.info("can not config mongo case properties is null");
            return new MongoClient(SentinelProperties.DEFAULT_HOST, SentinelProperties.DEFAULT_PORT);
        }

        String[] hosts = properties.getHost().split("[,| ]");
        List<ServerAddress> serverAddresses = new ArrayList<ServerAddress>();
        for(String h : hosts){
            serverAddresses.add(new ServerAddress(h, properties.getPort()));
        }

        MongoClientOptions.Builder mongoBuilder = MongoClientOptions.builder();
        mongoBuilder.minConnectionsPerHost(properties.getMinConnectionPerHost())
                .connectionsPerHost(properties.getMaxConnectionPerHost())
                .threadsAllowedToBlockForConnectionMultiplier(properties.getThreadsAllowedToBlockForConnectionMultiplier())
                .serverSelectionTimeout(properties.getServerSelectionTimeout())
                .maxWaitTime(properties.getMaxWaitTime())
                .maxConnectionIdleTime(properties.getMaxConnectionIdleTime())
                .maxConnectionLifeTime(properties.getMaxConnectionLifeTime())
                .connectTimeout(properties.getConnectTimeout())
                .socketTimeout(properties.getSocketTimeout())
                .socketKeepAlive(properties.getSocketKeepAlive())
                .sslEnabled(properties.getSslEnabled())
                .sslInvalidHostNameAllowed(properties.getSslInvalidHostNameAllowed())
                .alwaysUseMBeans(properties.getAlwaysUseMBeans())
                .heartbeatFrequency(properties.getHeartbeatFrequency())
                .minConnectionsPerHost(properties.getMinConnectionPerHost())
                .heartbeatConnectTimeout(properties.getHeartbeatConnectTimeout())
                .heartbeatSocketTimeout(properties.getSocketTimeout())
                .localThreshold(properties.getLocalThreshold());
        /**
         *  replicaSet exist
         */
        if(properties.getReplicaSetName() != null){
            mongoBuilder.readPreference(ReadPreference.primaryPreferred())
                    .requiredReplicaSetName(properties.getReplicaSetName());
        }

        MongoClientOptions ops = mongoBuilder
                .build();
        MongoClient mongoClient;

        if(StringUtils.isEmpty(properties.getUsername())){
            mongoClient = new MongoClient(serverAddresses, ops);
        }else {
            MongoCredential credential = MongoCredential.createCredential(properties.getUsername(), properties.getDatabase(), properties.getPassword());
            mongoClient = new MongoClient(serverAddresses, Collections.singletonList(credential), ops);
        }
        return mongoClient;
    }

}
