/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.microservice.servicediscovery;

import com.netflix.config.ConfigurationManager;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

/**
 *
 * RibbonAutoConfiguration.java
 * https://github.com/spring-cloud/spring-cloud-netflix/blob/master/spring-cloud-netflix-core/src/main/java/org/springframework/cloud/netflix/ribbon/RibbonAutoConfiguration.java
 *
 */
@EnableDiscoveryClient
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ServiceDiscoveryConfiguration{

//    private String serverUrl;
//    private Long timeOut;
//    private int port;
//
//    @Autowired
//    public ServiceDiscoveryConfiguration(@Value("${spring.cloud.consul.port:8500}") Integer port,  @Value("${spring.cloud.consul.host}") String host,
//                                         @Value("${spring.cloud.consul.config.watch.delay:1000}") Long timeOut) {
//        this.serverUrl = host;
//        this.timeOut = timeOut;
//        this.port = port;
//    }
//
//    @Primary
//    @Bean
//    public ConsulClient consulClient(){
//        // fix issues in spring cloud (ConfigWatch#watchConfigKeyValues),
//        // when consul throw com.ecwid.consul.transport TransportException(java.net.SocketTimeoutException: Read timed out)
//        CloseableHttpClient httpClient = HttpClientBuilder.create().setConnectionTimeToLive(timeOut, TimeUnit.MILLISECONDS).build();
//
//        ConsulRawClient consulRawClient = new ConsulRawClient(serverUrl, port, httpClient);
//        ConsulClient client = new ConsulClient(consulRawClient);
//        return client;
//    }

    @PostConstruct
    public void init() {
        ConfigurationManager.getConfigInstance().setProperty("ribbon.ServerListRefreshInterval", 3000);
    }

}


