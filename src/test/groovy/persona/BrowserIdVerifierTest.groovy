package persona

import org.springframework.web.client.RestOperations
import spock.lang.Specification

class BrowserIdVerifierTest extends Specification {

    def 'should extract audience from request URL'() {
        given:
        RestOperations restApiMock = Mock()
        BrowserIdVerifier verifier = new BrowserIdVerifier(restOperations: restApiMock)

        when:
        verifier.verify('assertion', requestUrl)

        then:
        interaction {
            1 * restApiMock.postForObject(_, { it.audience == audience}, _)
        }

        where:
            requestUrl              |   audience
            'tapcat.net'            |   'tapcat.net'
            'http://tapcat.net'     |   'tapcat.net'
            'http://tapcat.net:443' |   'tapcat.net:443'
            'ya.ru?none'            |   'ya.ru'

    }
}
