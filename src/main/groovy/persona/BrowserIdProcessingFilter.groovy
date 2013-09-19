package persona

import org.apache.commons.httpclient.HttpException
import org.apache.commons.httpclient.HttpHost
import org.apache.commons.httpclient.URIException
import org.json.JSONException
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.util.Assert

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class BrowserIdProcessingFilter  extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_ASSERTION_PARAMETER = 'assertion'

    private String verificationServiceUrl
    private String assertionParameterName = DEFAULT_ASSERTION_PARAMETER

    public BrowserIdProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl)
    }

    public String getAssertionParameterName() {
        return assertionParameterName
    }

    /**
     *
     * @param assertionParameterName
     */
    public void setAssertionParameterName(String assertionParameterName) {
        this.assertionParameterName = assertionParameterName
    }

    public String getVerificationServiceUrl() {
        return verificationServiceUrl
    }

    public void setVerificationServiceUrl(String verificationServiceUrl) {
        this.verificationServiceUrl = verificationServiceUrl
    }

    /**
     *
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String browserIdAssertion = request.getParameter(getAssertionParameterName())
        if(!browserIdAssertion) {
            throw new AuthenticationServiceException('Authentication request should contain assertion')
        }

        BrowserIdVerifier verifier = new BrowserIdVerifier(getVerificationServiceUrl())
        BrowserIdResponse response = null

        String audience  = request.getRequestURL().toString()
        try {
            URL url = new URL(audience)
            audience = url.getHost()
        } catch (MalformedURLException e) {
            throw new BrowserIdAuthenticationException('Malformed request URL', e)
        }

        try {
            response = verifier.verify(browserIdAssertion, audience)
            if(response.getStatus() == BrowserIdResponse.BrowserIdResponseStatus.OK){
                BrowserIdAuthenticationToken token = new BrowserIdAuthenticationToken(response, browserIdAssertion)
                //send to provider to get authorities
                return getAuthenticationManager().authenticate(token)
            }
            else {
                throw new BrowserIdAuthenticationException('BrowserID verification failed, reason: ' + response.getReason())
            }
        } catch (HttpException e) {
            throw new BrowserIdAuthenticationException('Error calling verify service [' + verifier.getVerifyUrl() + ']', e)
        } catch (IOException e) {
            throw new BrowserIdAuthenticationException('Error calling verify service [' + verifier.getVerifyUrl() + ']', e)
        } catch (JSONException e){
            throw new BrowserIdAuthenticationException('Could not parse response from verify service [' + verifier.getVerifyUrl() + ']', e)
        }
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet()
        Assert.hasLength(getAssertionParameterName(), 'assertionParameterName cannot be empty.')
        Assert.hasLength(getVerificationServiceUrl(), 'URL should not be empty')
        try{
            HttpHost host = new HttpHost(new org.apache.commons.httpclient.URI(getVerificationServiceUrl(), false))
            Assert.isTrue(host.getProtocol().isSecure(), 'verificationServiceUrl does not use a secure protocol')
        } catch (URIException e){
            throw new IllegalArgumentException('verificationServiceUrl is not a valid URI',e)
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if (authResult instanceof BrowserIdAuthenticationToken) {
            logger.debug(((BrowserIdAuthenticationToken) authResult))
        }
    }
}
