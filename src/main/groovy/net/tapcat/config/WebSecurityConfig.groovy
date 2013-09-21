package net.tapcat.config
import org.apache.commons.httpclient.HttpConnectionManager
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import persona.BrowserIdAuthenticationProvider
import persona.BrowserIdProcessingFilter
import persona.BrowserIdVerifier

@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(authFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeUrls()
                .antMatchers('/assets/**').permitAll()
                .anyRequest().hasAuthority('USER')
                .and()
                .logout().logoutUrl('/logout')
    }

    @Bean
    public def authFilter() {
        def filter = new BrowserIdProcessingFilter('/login')
        filter.verifier = personaVerifier()
        def authManager = new ProviderManager([new BrowserIdAuthenticationProvider()])
        filter.setAuthenticationManager(authManager)
        filter
    }

    @Bean
    public BrowserIdVerifier personaVerifier() {
        new BrowserIdVerifier('https://browserid.org/verify', httpFactory())
    }

    @Bean
    public HttpConnectionManager httpFactory() {
        new MultiThreadedHttpConnectionManager()
    }

}
