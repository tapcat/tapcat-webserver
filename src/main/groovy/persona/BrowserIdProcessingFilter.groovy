package persona
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
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

    private String assertionParameterName = 'assertion'

    @Autowired
    BrowserIdVerifier verifier

    public BrowserIdProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl)
    }

    /**
     *
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String browserIdAssertion = request.getParameter(assertionParameterName)

        if(!browserIdAssertion || !HttpMethod.POST.name().equals(request.getMethod())) {
            throw new BrowserIdAuthenticationException('Authentication request should contain assertion')
        }
        def requestUrl = request.getRequestURL().toString() // proxy pass will rewrite
        // request url.
        // can restore it from Host header
        authenticate(browserIdAssertion, requestUrl)
    }

    private Authentication authenticate(String browserIdAssertion, String requestUrl) {
        BrowserIdAuthenticationResponse response = verifier.verify(browserIdAssertion, requestUrl)
        if(!response.getStatus().startsWith('ok')) {
            throw new BrowserIdAuthenticationException('BrowserID verification failed, reason: ' + response)
        }
        BrowserIdAuthenticationToken token = new BrowserIdAuthenticationToken(response)
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

    /**
     *
     * @param assertionParameterName
     */
    public void setAssertionParameterName(String assertionParameterName) {
        this.assertionParameterName = assertionParameterName
    }

    public String getAssertionParameterName() {
        return assertionParameterName
    }

}
