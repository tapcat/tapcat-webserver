package persona
import org.springframework.security.authentication.AbstractAuthenticationToken

class BrowserIdAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 1L

    private BrowserIdAuthenticationResponse auth

    @Override
    public Object getPrincipal() { auth.email }

    @Override
    public Object getCredentials() { auth.audience }

    public BrowserIdAuthenticationToken(BrowserIdAuthenticationResponse auth) {
        super(null) // none authorities can be set up in token
        this.auth = auth
        setAuthenticated(true)
        setDetails(auth)
    }

    public String toString() {
        "Expiration: ${auth.expires}"
    }

}
