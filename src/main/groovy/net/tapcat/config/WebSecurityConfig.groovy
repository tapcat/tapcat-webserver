package net.tapcat.config

import net.tapcat.security.Http401UnauthorizedEntryPoint
import org.apache.commons.httpclient.HttpConnectionManager
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import persona.BrowserIdAuthenticationProvider
import persona.BrowserIdProcessingFilter
import persona.BrowserIdVerifier

@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AbstractAuthenticationProcessingFilter browserIdFilter

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterAfter(browserIdFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(new Http401UnauthorizedEntryPoint())
                .and()
                .anonymous().disable()
                .authorizeUrls()
                .anyRequest().hasAuthority('USER')
                .and()
                .logout().logoutUrl('/logout')
    }

    @Bean
    public AbstractAuthenticationProcessingFilter authFilter() {
        def filter = new BrowserIdProcessingFilter('/login')
        filter.verifier = personaVerifier()
        filter.setAuthenticationManager(authenticationManagerBean())
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

    @Override
    protected void registerAuthentication(AuthenticationManagerBuilder auth)
    throws Exception {
        auth.authenticationProvider(new BrowserIdAuthenticationProvider())
    }

}
