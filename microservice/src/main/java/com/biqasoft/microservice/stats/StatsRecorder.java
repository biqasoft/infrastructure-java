package com.biqasoft.microservice.stats;

import com.biqasoft.microservice.communicator.MicroserviceRequestMaker;
import com.biqasoft.microservice.communicator.http.MicroserviceRestTemplate;
import com.biqasoft.microservice.communicator.interfaceimpl.MicroserviceRequestInterceptor;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerContext;
import org.springframework.cloud.netflix.ribbon.RibbonStatsRecorder;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.Map;

/**
 * Print statistic about microservice request time
 *
 * for example output
 *
 * Microservice users info: DynamicServerListLoadBalancer:{NFLoadBalancer:name=users,current list of Servers=[192.168.1.98:11000, 192.168.1.98:11001],Load balancer stats=Zone stats: {unknown=[Zone:unknown;	Instance count:2;	Active connections count: 0;	Circuit breaker tripped count: 0;	Active connections per server: 0.0;]
 },Server stats: [[Server:192.168.1.98:11000;	Zone:UNKNOWN;	Total Requests:39;	Successive connection failure:0;	Total blackout seconds:0;	Last connection made:Mon Oct 03 19:27:52 MSK 2016;	First connection made: Mon Oct 03 19:03:36 MSK 2016;	Active Connections:0;	total failure count in last (1000) msecs:0;	average resp time:11.615384615384615;	90 percentile resp time:0.0;	95 percentile resp time:0.0;	min resp time:8.0;	max resp time:33.0;	stddev resp time:5.289638563656526]
 , [Server:192.168.1.98:11001;	Zone:UNKNOWN;	Total Requests:38;	Successive connection failure:0;	Total blackout seconds:0;	Last connection made:Mon Oct 03 19:27:52 MSK 2016;	First connection made: Mon Oct 03 19:03:36 MSK 2016;	Active Connections:0;	total failure count in last (1000) msecs:0;	average resp time:12.210526315789474;	90 percentile resp time:0.0;	95 percentile resp time:0.0;	min resp time:9.0;	max resp time:33.0;	stddev resp time:5.016592965963013]
 ]}ServerList:ConsulServerList{serviceId='users', tag=null}

 * Created by Nikita on 9/27/2016.
 */
@Service
public class StatsRecorder extends RibbonLoadBalancerClient {

    private final SpringClientFactory clientFactory;
    private static final Logger logger = LoggerFactory.getLogger(StatsRecorder.class);

    @Autowired
    public StatsRecorder(SpringClientFactory clientFactory) {
        super(clientFactory);
        this.clientFactory = clientFactory;
    }

    private boolean needCreateStats(MicroserviceRestTemplate microserviceRestTemplate){
        if (StringUtils.isEmpty(microserviceRestTemplate.getMicroserviceName())){
            return false;
        }
        return true;
    }

    @Autowired
    public void registerStatRecorderInterceptor(MicroserviceRequestMaker microserviceRequestMaker) {
        MicroserviceRequestInterceptor microserviceRequestInterceptor = new MicroserviceRequestInterceptor() {
            ThreadLocal<String> serviceId = new ThreadLocal<>();
            ThreadLocal<Server> server = new ThreadLocal<>();
            ThreadLocal<RibbonLoadBalancerContext> context = new ThreadLocal<>();
            ThreadLocal<RibbonStatsRecorder> statsRecorder = new ThreadLocal<>();

            @Override
            public void beforeCreateHttpEntity(MicroserviceRestTemplate restTemplate, Class returnType, Class[] returnGenericType, HttpHeaders httpHeaders) {
                // clear ThreadLocal before request
                serviceId.remove();
                server.remove();
                context.remove();
                statsRecorder.remove();
            }

            @Override
            public void beforeRequest(MicroserviceRestTemplate restTemplate, URI uri) {
                if (!needCreateStats(restTemplate)){
                    return;
                }

                try {
                    serviceId.set(restTemplate.getMicroserviceName());
                    server.set(new Server(uri.getHost(), uri.getPort()));

                    context.set(clientFactory.getLoadBalancerContext(serviceId.get()));
                    statsRecorder.set(new RibbonStatsRecorder(context.get(), server.get()));
                } catch (Exception e) {
                    logger.error("Error collect microservice metric before", e);
                }
            }

            @Override
            public void onException(MicroserviceRestTemplate restTemplate, URI uri, Exception e) {
                try {
                    statsRecorder.get().recordStats(e);
                } catch (Exception ex) {
                    logger.error("Error collect microservice metric after", e);
                }
            }

            @Override
            public void afterRequest(MicroserviceRestTemplate restTemplate, HttpEntity<Object> request, ResponseEntity<byte[]> responseEntity, Class returnType, Class[] returnGenericType) {
                if (!needCreateStats(restTemplate)){
                    return;
                }

                try {
                    statsRecorder.get().recordStats(responseEntity);
                } catch (Exception e) {
                    logger.error("Error collect microservice metric after", e);
                }
            }
        };
        microserviceRequestMaker.getMicroserviceRequestInterceptors().add(microserviceRequestInterceptor);
    }

    @SuppressWarnings("unchecked")
    @Scheduled(fixedDelay = 15000)
    public void printMicroservice() throws NoSuchFieldException, IllegalAccessException {

        Field contexts = NamedContextFactory.class.getDeclaredField("contexts");
        contexts.setAccessible(true);
        Map<String, AnnotationConfigApplicationContext> contextMap = (Map<String, AnnotationConfigApplicationContext>) contexts.get(clientFactory);

        for (String service : contextMap.keySet()) {
//            ILoadBalancer loadBalancer = clientFactory.getLoadBalancerContext(service).getLoadBalancer();
//            DynamicServerListLoadBalancer loadBalancer2 = (DynamicServerListLoadBalancer) loadBalancer;
//            Set<Map.Entry<Server, ServerStats>> entries = loadBalancer2.getLoadBalancerStats().getServerStats().entrySet();

            String s = clientFactory.getLoadBalancerContext(service).getLoadBalancer().toString();
            logger.info("Microservice {} info: {}", service, s);
//            for (Map.Entry<Server, ServerStats> entry : entries) {
//                if (entry.getValue().getTotalRequestsCount() == 0) {
//                    continue;
//                }
//            }
        }
    }

}
