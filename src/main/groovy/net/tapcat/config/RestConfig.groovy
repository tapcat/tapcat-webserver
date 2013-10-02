package net.tapcat.config
import org.codehaus.jackson.map.DeserializationConfig
import org.codehaus.jackson.map.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class RestConfig {

    @Autowired
    HttpMessageConverter httpMessageConverter

    @Bean
    ObjectMapper objectMapper() {
        def mapper = new ObjectMapper()
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    @Bean
    HttpMessageConverter httpMessageConverter(ObjectMapper mapper) {
        def converter = new MappingJacksonHttpMessageConverter()
        converter.setObjectMapper(mapper)
        converter
    }

    @Bean
    public RestTemplate httpFactory() {
        def connectionFactory = new SimpleClientHttpRequestFactory()
        connectionFactory.setReadTimeout(500)
        connectionFactory.setConnectTimeout(500)
        def restTemplate = new RestTemplate(connectionFactory)
        restTemplate.messageConverters = [httpMessageConverter] as List
        restTemplate
    }
}
