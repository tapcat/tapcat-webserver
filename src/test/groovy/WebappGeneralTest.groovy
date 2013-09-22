import net.tapcat.config.JsonConfig
import net.tapcat.config.WebConfig
import net.tapcat.config.WebSecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.web.FilterChainProxy
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebAppConfiguration
@ContextConfiguration(classes = [JsonConfig, WebSecurityConfig, WebConfig])
class WebappGeneralTest extends Specification {

    @Autowired
    private WebApplicationContext wac

    @Autowired
    private FilterChainProxy springSecurityFilterChain

    private MockMvc mockMvc

    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(springSecurityFilterChain).build()
    }

    public void 'to root should be forbidden'() {
        expect:
        mockMvc.perform(get("/"))
                .andExpect(status().isUnauthorized())
    }

    public void 'unauthorized request to user controller should be unauthorised'() {
        expect:
        mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized())
    }

}
