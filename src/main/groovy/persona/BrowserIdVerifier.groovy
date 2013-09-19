package persona
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpConnectionManager
import org.apache.commons.httpclient.HttpException
import org.apache.commons.httpclient.methods.PostMethod
import org.json.JSONException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class BrowserIdVerifier  {

    private static String DEFAULT_VERIFY_URL = 'https://browserid.org/verify'

    private String url = DEFAULT_VERIFY_URL

    @Autowired
    private HttpConnectionManager connectionManager

    BrowserIdVerifier(String url, HttpConnectionManager connectionManager = null) {
        this.url = url
        this.connectionManager = connectionManager
    }

    /**
     * Verify if the given assertion is valid
     * @param assertion The assertion as returned
     * @param audience
     * @return
     * @throws HttpException if an HTTP protocol exception occurs or the service returns a code not in the 200 range.
     * @throws IOException if a transport error occurs.
     * @throws JSONException if the result cannot be parsed as JSON markup
     */
    public BrowserIdResponse verify(String assertion, String audience) throws HttpException, IOException, JSONException {
        HttpClient client = new HttpClient(connectionManager)

        //TODO: check certificate?

        PostMethod post = new PostMethod(url)

        post.addParameter('assertion', assertion)
        post.addParameter('audience', audience)

        try{
            int statusCode = client.executeMethod(post)
            if(isHttpResponseOK(statusCode)){
                return new BrowserIdResponse(post.getResponseBodyAsString())
            }
            else throw new HttpException('http request error to host: ' + url)
        }
        finally {
            post.releaseConnection()
        }

    }

    private static boolean isHttpResponseOK(int statusCode){
        return statusCode >= 200 && statusCode < 300
    }


}
