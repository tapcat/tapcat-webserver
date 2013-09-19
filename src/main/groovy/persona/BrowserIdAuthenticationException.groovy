package persona

import org.springframework.security.core.AuthenticationException

class BrowserIdAuthenticationException extends AuthenticationException {

    BrowserIdAuthenticationException(String msg) {
        super(msg)
    }

    BrowserIdAuthenticationException(String msg, Throwable t){
        super(msg, t)
    }

    private static final long serialVersionUID = 1L

}
