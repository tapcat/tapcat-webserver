package persona

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations

@Component
class BrowserIdVerifier  {

    private static String DEFAULT_VERIFY_URL = 'https://browserid.org/verify'

    private String url = DEFAULT_VERIFY_URL

    @Autowired
    RestOperations restOperations

    BrowserIdVerifier(String url, RestOperations restOperations = null) {
        this.url = url
        this.restOperations = restOperations
    }

    /**
     * Verify if the given assertion is valid
     * @param assertion The assertion as returned
     * @param audience
     * @return auth response
     */
    public BrowserIdResponse verify(String assertion, String audience) {
        //TODO: check certificate?
        restOperations.postForObject(url, [assertion: assertion, audience: audience], BrowserIdResponse)
    }

}
