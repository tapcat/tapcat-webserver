package persona
import groovy.util.logging.Slf4j
import org.json.JSONException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.util.Assert

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
class BrowserIdProcessingFilter  extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_ASSERTION_PARAMETER = 'assertion'

    private String assertionParameterName = DEFAULT_ASSERTION_PARAMETER

    private BrowserIdVerifier verifier

    public BrowserIdProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl)
    }

    public String getAssertionParameterName() {
        return assertionParameterName
    }

    /**
     *
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String browserIdAssertion = request.getParameter(getAssertionParameterName())
        if(!browserIdAssertion) {
            throw new BrowserIdAuthenticationException('Authentication request should contain assertion')
        }
        String audience  = request.getRequestURL().toString()

        try {
            URL url = new URL(audience)
            audience = url.getHost()
            tryAuthenticate(browserIdAssertion, audience)
        } catch (IOException | JSONException e) {
            throw new BrowserIdAuthenticationException("Error calling verify service with audience ${audience}", e)
        }
    }

    private Authentication tryAuthenticate(String browserIdAssertion, String audience) {
        BrowserIdResponse response = verifier.verify(browserIdAssertion, audience)
        if(response.getStatus() != BrowserIdResponse.BrowserIdResponseStatus.OK){
            throw new BrowserIdAuthenticationException('BrowserID verification failed, reason: ' + response.getReason())
        }
        BrowserIdAuthenticationToken token = new BrowserIdAuthenticationToken(response, browserIdAssertion)
        getAuthenticationManager().authenticate(token)
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet()
        Assert.notNull(verifier, 'Verifier should be set for this service')
        Assert.hasLength(getAssertionParameterName(), 'assertionParameterName cannot be empty.')
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if (authResult instanceof BrowserIdAuthenticationToken && log.isDebugEnabled()) {
            log.debug(authResult.principal.toString())
        }
    }

    @Autowired
    void setVerifier(BrowserIdVerifier verifier) {
        this.verifier = verifier
    }

    /**
     *
     * @param assertionParameterName
     */
    public void setAssertionParameterName(String assertionParameterName) {
        this.assertionParameterName = assertionParameterName
    }

}
