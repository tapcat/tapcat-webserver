package net.tapcat.config

import net.tapcat.security.ResponseCodeOnlyAuthHandler
import net.tapcat.security.ResponseCodeOnlyLogoutHandler
import net.tapcat.security.TopDomainAudienceResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AbstractAuthenticationProcessingFilter browserIdFilter

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterAfter(browserIdFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeUrls()
                .antMatchers('/monitoring/**', '/metrics/**').permitAll()
                .anyRequest().hasAuthority('USER')
                .and()
                .logout().logoutUrl('/logout').logoutSuccessHandler(new ResponseCodeOnlyLogoutHandler())
    }

    @Bean
    public AbstractAuthenticationProcessingFilter authFilter() {
        def filter = new BrowserIdProcessingFilter('/login')
        filter.authenticationManager = authenticationManagerBean()
        filter.authenticationFailureHandler = filter.authenticationSuccessHandler = new ResponseCodeOnlyAuthHandler()
        filter
    }

    @Bean
    public BrowserIdVerifier personaVerifier() {
        new BrowserIdVerifier(audienceResolver: new TopDomainAudienceResolver())
    }

    @Override
    protected void registerAuthentication(AuthenticationManagerBuilder auth)
    throws Exception {
        auth.authenticationProvider(new BrowserIdAuthenticationProvider())
    }

}
