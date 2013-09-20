package persona
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class BrowserIdAuthenticationToken extends AbstractAuthenticationToken implements BrowserIdAuthentication {

    private static final long serialVersionUID = 1L

    private BrowserIdResponse auth
    private String assertion
    private String audience


    public String getAudience() {
        return audience
    }

    public String getAssertion() {
        return assertion
    }

    @Override
    public Object getCredentials() {
        return getAssertion()
    }

    @Override
    public Object getPrincipal() {
        return auth?.getEmail()
    }

    public BrowserIdResponse getVerificationResponse(){
        return auth
    }

    /**
     * Create token with empty authorities.
     * @param response BrowserID verification response
     * @param assertion JWT encoded assertion as recieved from navigator.id.get
     */
    public BrowserIdAuthenticationToken(BrowserIdResponse response, String assertion){
        super([new SimpleGrantedAuthority('NONE')] as List)
        this.auth = response
        this.assertion = assertion
        setAuthenticated(false)
    }

    BrowserIdAuthenticationToken(Collection<? extends GrantedAuthority> authorities, BrowserIdResponse auth, String assertion) {
        super(authorities)
        this.auth = auth
        this.assertion = assertion
        setAuthenticated(auth != null && auth.getStatus() == BrowserIdResponse.BrowserIdResponseStatus.OK)
        setDetails(auth)
    }

    public String toString() {
        StringBuffer sb = new StringBuffer()
        sb.append(super.toString()).append(' ')
        sb.append('BrowserID response:')
        sb.append(auth == null ? '' : auth.toString())
        return sb.toString()
    }

}
