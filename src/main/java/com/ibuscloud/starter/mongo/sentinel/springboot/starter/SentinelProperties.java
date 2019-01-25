package com.ibuscloud.starter.mongo.sentinel.springboot.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author cmx
 * @date 2019/1/23
 */
@Data
@ConfigurationProperties("spring.mongodb.sentinel")
public class SentinelProperties {

    /**
     * Default port used when the configured port is {@code null}.
     */
    public static final int DEFAULT_PORT = 27017;

    public static final String DEFAULT_HOST = "localhost";

    /**
     * Mongo server host.
     */
    private String host;

    /**
     * Mongo server port.
     */
    private Integer port = null;

    /**
     * Database name.
     */
    private String database = "test";

    /**
     * Login user of the mongo server.
     */
    private String username;

    /**
     * Login password of the mongo server.
     */
    private char[] password;

    /**
     * replicaSetName
     */
    private String replicaSetName;

    /**
     * Mongo options with default value
     */
    private Integer minConnectionPerHost = 0;
    private Integer maxConnectionPerHost = 100;
    private Integer threadsAllowedToBlockForConnectionMultiplier = 5;
    private Integer serverSelectionTimeout = 30000;
    private Integer maxWaitTime = 120000;
    private Integer maxConnectionIdleTime = 0;
    private Integer maxConnectionLifeTime = 0;
    private Integer connectTimeout = 10000;
    private Integer socketTimeout = 0;
    private Boolean socketKeepAlive = false;
    private Boolean sslEnabled = false;
    private Boolean sslInvalidHostNameAllowed = false;
    private Boolean alwaysUseMBeans = false;
    private Integer heartbeatFrequency = 10000;
    private Integer minHeartbeatFrequency = 500;
    private Integer heartbeatConnectTimeout = 20000;
    private Integer heartbeatSocketTimeout = 20000;
    private Integer localThreshold = 15;

    public Integer getPort(){
        return this.port == null ? DEFAULT_PORT : this.port;
    }

    public String getHost(){
        return this.host == null ? DEFAULT_HOST : this.host;
    }

}
