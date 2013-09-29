package net.tapcat.config

import org.codehaus.jackson.map.DeserializationConfig
import org.codehaus.jackson.map.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter

@Configuration
class JsonConfig {

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
}
