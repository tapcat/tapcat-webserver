package persona

import org.json.JSONException

/**
 * If you see it, than I've forgotten javadoc
 *
 * @author Denis Golovachev
 * @author $Author$ (current maintainer)
 * @since 1.0
 */
class DomainAudienceResolver implements AudienceResolver {

    @Override
    String resolve(String requestUrl) throws AudienceResolveException {
        try {
            if(!requestUrl.startsWith("http") && !requestUrl.startsWith("https")){
                requestUrl = "http://" + requestUrl;
            }
            URI url = new URI(requestUrl)
            def host =  url.host.startsWith("www.") ? url.host.substring(4) : url.host

            if (url.port > 0 && url.port != 80) {
                "${host}:${url.port}"
            } else {
                host
            }
        } catch (IOException | JSONException e) {
            throw new AudienceResolveException("Error resolve audience for token: ${requestUrl} ", e)
        }
    }
}
