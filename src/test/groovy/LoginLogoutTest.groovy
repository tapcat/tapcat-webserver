import groovy.mock.interceptor.MockFor
import net.tapcat.config.RestConfig
import net.tapcat.config.WebConfig
import net.tapcat.config.WebSecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.web.FilterChainProxy
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.client.RestOperations
import org.springframework.web.context.WebApplicationContext
import persona.BrowserIdProcessingFilter
import persona.BrowserIdAuthenticationResponse
import persona.BrowserIdVerifier
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebAppConfiguration
@ContextConfiguration(classes = [RestConfig, WebSecurityConfig, WebConfig])
class LoginLogoutTest  extends Specification {

    @Autowired
    private WebApplicationContext wac

    @Autowired
    private FilterChainProxy springSecurityFilterChain

    @Autowired
    BrowserIdProcessingFilter authenticationProcessingFilter

    private MockMvc mockMvc

    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(springSecurityFilterChain).build()
    }

    public void 'empty request to login entry point should be forbidden'() {
        expect:
        mockMvc.perform(get("/login"))
                .andExpect(status().isUnauthorized())
    }

    public void 'unauthorized request to logout entry point should be redirected'() {
        expect:
        mockMvc.perform(get("/logout"))
                .andExpect(status().isNoContent())
    }

    public void 'auth should be performed with persona verification request'() {
        given:
        def restOp = new MockFor(RestOperations)
        restOp.demand.postForObject(1) { url, data, mapTo -> assert url == 'persona-url';  new BrowserIdAuthenticationResponse(status: 'ok') }
        authenticationProcessingFilter.verifier = new BrowserIdVerifier('persona-url', restOp.proxyInstance())
        expect:
        mockMvc.perform(post("/login").param('assertion', '123').contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isNoContent())
    }

    public void 'auth should be performed when Persona answers with "OKAY"'() {
        given:
        def restOp = new MockFor(RestOperations)
        restOp.demand.postForObject(1) { url, data, mapTo -> assert url == 'persona-url';  new
                                            BrowserIdAuthenticationResponse(status: 'okay') }
        authenticationProcessingFilter.verifier = new BrowserIdVerifier('persona-url', restOp.proxyInstance())
        expect:
        mockMvc.perform(post("/login").param('assertion', '123').contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isNoContent())
    }

}
