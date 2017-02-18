# i18n helper

Provide Spring bean `com.biqasoft.microservice.i18n.MessageByLocaleService` which make translation from properties files with following rules:
 - If we have user context - use CurrentUser.getLanguage()
 - otherwise use HTTP header "Accept-Language" from http request
