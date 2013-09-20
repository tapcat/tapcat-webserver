package persona
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.InitializingBean
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
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

        if(response.getStatus() == 'OK' ){
            String identity = response.getEmail()
            GrantedAuthority[] grantedAuthorities = ['USER']
            if(grantedAuthorities.length == 0) {
                throw new BrowserIdAuthenticationException('No authorities granted to ' + identity)
            }

            if(log.isDebugEnabled()) {
                log.debug('Upgraded token with authorities')
            }
            new BrowserIdAuthenticationToken(grantedAuthorities as
                List, response, browserIdAuth.getAssertion())
        }

        else {
            throw new BrowserIdAuthenticationException('User not verified: ' + response)
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
