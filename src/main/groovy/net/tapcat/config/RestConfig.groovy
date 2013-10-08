package net.tapcat.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class RestConfig {

    @Autowired
    HttpMessageConverter httpMessageConverter

    @Bean
    ObjectMapper objectMapper() {
        def mapper = new ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    @Bean
    HttpMessageConverter httpMessageConverter(ObjectMapper mapper) {
        def converter = new MappingJackson2HttpMessageConverter()
        converter.setObjectMapper(mapper)
        converter
    }

    @Bean
    public RestTemplate httpFactory() {
        def connectionFactory = new SimpleClientHttpRequestFactory(readTimeout: 1000, connectTimeout: 1000)
        def restTemplate = new RestTemplate(connectionFactory)
        restTemplate.messageConverters = [httpMessageConverter] as List
        restTemplate
    }
}
