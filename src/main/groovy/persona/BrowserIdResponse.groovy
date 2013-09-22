package persona

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString(includes = ['email'])
class BrowserIdResponse {

    String status
    String email
    String audience
    long expires
    String issuer
    String reason

}
