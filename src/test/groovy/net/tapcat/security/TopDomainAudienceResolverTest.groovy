package net.tapcat.security

import spock.lang.Specification

class TopDomainAudienceResolverTest extends Specification {

    def 'should extract top level domain'() {
        given:
        def resolver = new TopDomainAudienceResolver()

        expect:
        assert resolver.resolve(url) == result

        where:
        url             | result
        'tapcat.net'    | 'tapcat.net'
        'tapcat.net:8080'|'tapcat.net:8080'
        'http://ya.ru'  | 'ya.ru'
        'cat.tapcat.net'| 'tapcat.net'
        'dev.tapcat.net:9000' | 'tapcat.net:9000'
        'cat.ya.ru:90'  | 'ya.ru:90'
    }

}
