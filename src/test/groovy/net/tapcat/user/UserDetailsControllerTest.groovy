package net.tapcat.user

import net.tapcat.config.RestConfig
import net.tapcat.config.WebConfig
import net.tapcat.config.WebSecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.web.FilterChainProxy
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebAppConfiguration
@ContextConfiguration(classes = [RestConfig, WebSecurityConfig, WebConfig])
class UserDetailsControllerTest {

    @Autowired
    private WebApplicationContext wac

    @Autowired
    private FilterChainProxy springSecurityFilterChain

    private MockMvc mockMvc

    @Autowired
    AuthenticationManager authenticationManager

    @Autowired
    AbstractAuthenticationProcessingFilter authenticationProcessingFilter

    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(springSecurityFilterChain).build()
        authenticationManager.authenticate()
    }

    public void 'should'() {
        expect:
        mockMvc.perform(get("/login"))
                .andExpect(status().isUnauthorized())
    }
}
