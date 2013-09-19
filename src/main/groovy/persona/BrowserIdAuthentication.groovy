package persona

import org.springframework.security.core.Authentication

public interface BrowserIdAuthentication extends Authentication {

    /**
     *
     * @return Domain and optionally port for which the assertion is intended
     */
    String getAudience()
    /**
     *
     * @return Encoded JWT identity assertion.
     */
    String getAssertion()

    /**
     *
     * @return Verification response (if made), <code>null</code> otherwise
     */
    BrowserIdResponse getVerificationResponse()

}
