package persona
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.InitializingBean
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.SimpleGrantedAuthority

@Slf4j
class BrowserIdAuthenticationProvider implements InitializingBean, AuthenticationProvider {

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BrowserIdAuthenticationToken browserIdAuth = (BrowserIdAuthenticationToken) authentication
        if(!browserIdAuth.authenticated) {
            throw new BrowserIdAuthenticationException('User not verified: ' + browserIdAuth)
        }
        def grantedAuthorities = [new SimpleGrantedAuthority('USER')]
        new BrowserIdAuthentication(grantedAuthorities, browserIdAuth)
    }

    @Override
    public boolean supports(Class authentication) {
        return BrowserIdAuthenticationToken.class.isAssignableFrom(authentication)
    }

}
