package persona

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class BrowserIdAuthentication implements Authentication {

    private static final long serialVersionUID = 1L

    private BrowserIdAuthenticationResponse auth

    private long expires

    Collection<? extends GrantedAuthority> authorities

    boolean authenticated = true

    public BrowserIdAuthentication(Collection<? extends GrantedAuthority> authorities,
                                   BrowserIdAuthenticationToken authToken) {
        this.auth = (BrowserIdAuthenticationResponse) authToken.details
        this.authorities = authorities
    }


    public int getExpriationTime() { auth.expires }

    @Override
    public Object getPrincipal() { auth.email }

    @Override
    public Object getCredentials() { auth.audience }

    @Override
    Object getDetails() { auth }

    @Override
    boolean isAuthenticated() { authenticated }

    @Override
    void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authenticated = isAuthenticated
    }

    @Override
    String getName() { auth.email }

    public String toString() {
        "Expiration: ${auth.expires}"
    }
}
