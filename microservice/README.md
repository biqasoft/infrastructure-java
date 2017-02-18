# Microservice common...

## Main features
 - MongoDB connection
 - Helpers to service discovery and make requests to microservice with HA
 - Spring Cloud - Service Discovery
 - Runtime auto implement microservice communication interface. See example `com.biqasoft.gateway.export.MicroserviceExcelRepository`

## Important

Every microservice that depends on this module should have following files in project:
(com.biqasoft.microservice.configs.profiles)

 - `resources/application-development.properties`
 - `resources/application-production.properties`

## Spring Cloud - Consul

 - http://cloud.spring.io/spring-cloud-consul/spring-cloud-consul.html
 - http://consul.server:8500/ui/#/dc1/kv/config/
 - KV: config/application,development
