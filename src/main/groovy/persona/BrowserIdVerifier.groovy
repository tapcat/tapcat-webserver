package persona
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations

@Component
class BrowserIdVerifier  {

    private String url

    @Autowired
    RestOperations restOperations

    AudienceResolver audienceResolver = new DomainAudienceResolver()

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
    public BrowserIdAuthenticationResponse verify(String assertion, String requestUrl) {
        //TODO: check certificate?
        try {
            String audience = audienceResolver.resolve(requestUrl)
            restOperations.postForObject(url, [assertion: assertion, audience: audience],
                    BrowserIdAuthenticationResponse)
        } catch (AudienceResolveException exception) {
            throw new BrowserIdAuthenticationException('Authentication failure', exception)
        }
    }

}
