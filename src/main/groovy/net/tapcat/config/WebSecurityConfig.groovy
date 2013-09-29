package net.tapcat.config
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.client.RestTemplate
import persona.BrowserIdAuthenticationProvider
import persona.BrowserIdProcessingFilter
import persona.BrowserIdVerifier

@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AbstractAuthenticationProcessingFilter browserIdFilter

    @Autowired
    HttpMessageConverter httpMessageConverter

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterAfter(browserIdFilter, UsernamePasswordAuthenticationFilter.class)
                .anonymous().disable()
                .authorizeUrls()
                .anyRequest().hasAuthority('USER')
                .and()
                .logout().logoutUrl('/logout').logoutSuccessUrl('/')
    }

    @Bean
    public AbstractAuthenticationProcessingFilter authFilter() {
        def filter = new BrowserIdProcessingFilter('/login')
        filter.setAuthenticationManager(authenticationManagerBean())
        filter
    }

    @Bean
    public BrowserIdVerifier personaVerifier() {
        new BrowserIdVerifier('https://browserid.org/verify', httpFactory())
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

    @Override
    protected void registerAuthentication(AuthenticationManagerBuilder auth)
    throws Exception {
        auth.authenticationProvider(new BrowserIdAuthenticationProvider())
    }

}
