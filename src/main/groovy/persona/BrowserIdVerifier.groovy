package persona

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations

@Component
class BrowserIdVerifier  {

    private String url

    @Autowired
    RestOperations restOperations

    BrowserIdVerifier(String url = 'https://verifier.login.persona.org/verify',
                      RestOperations restOperations = null) {
        this.url = url
        this.restOperations = restOperations
    }

    /**
     * Verify if the given assertion is valid
     * @param assertion The assertion as returned
     * @param audience
     * @return auth response
     */
    public BrowserIdAuthenticationResponse verify(String assertion, String audience) {
        //TODO: check certificate?
        restOperations.postForObject(url, [assertion: assertion, audience: audience], BrowserIdAuthenticationResponse)
    }

}
