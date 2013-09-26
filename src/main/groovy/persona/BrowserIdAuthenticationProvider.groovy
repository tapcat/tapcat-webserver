package persona
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.InitializingBean
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService

@Slf4j
class BrowserIdAuthenticationProvider implements InitializingBean, AuthenticationProvider {

    private String verificationServiceUrl = 'https://browserid.org/verify'

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BrowserIdAuthentication browserIdAuth = (BrowserIdAuthentication) authentication

        BrowserIdResponse response = browserIdAuth.getVerificationResponse()

        if(!response.getStatus().equalsIgnoreCase('OK') ) {
            throw new BrowserIdAuthenticationException('User not verified: ' + response)
        } else {
            String identity = response.getEmail()
            def grantedAuthorities = [new SimpleGrantedAuthority('USER')]

            if(log.isDebugEnabled()) {
                log.debug('Upgraded token with authorities')
            }
            new BrowserIdAuthenticationToken(grantedAuthorities, response, browserIdAuth.getAssertion())
        }
    }

    @Override
    public boolean supports(Class authentication) {
        return BrowserIdAuthentication.class.isAssignableFrom(authentication)
    }

    public String getVerificationServiceUrl() {
        return verificationServiceUrl
    }

    public void setVerificationServiceUrl(String verificationServiceUrl) {
        this.verificationServiceUrl = verificationServiceUrl
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.authoritiesService = userDetailsService
    }
}
