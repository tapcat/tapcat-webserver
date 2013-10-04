package persona
import org.json.JSONException
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
    public BrowserIdAuthenticationResponse verify(String assertion, String requestUrl) {
        //TODO: check certificate?
        restOperations.postForObject(url, [assertion: assertion, audience: resolveAudience(requestUrl)],
                BrowserIdAuthenticationResponse)
    }

    private static String resolveAudience(String requestUrl) {
        try {
            URL url = new URL(requestUrl)
            "${url.getHost()}:${url.getPort()}"
        } catch (IOException | JSONException e) {
            throw new BrowserIdAuthenticationException("Error calling verify service for URL: ${requestUrl} ", e)
        }
    }

}
