package net.tapcat.security

import persona.AudienceResolveException
import persona.AudienceResolver
import persona.DomainAudienceResolver

/**
 * Resolve top domain as Audience
 */
class TopDomainAudienceResolver implements AudienceResolver {

    private AudienceResolver internalResolver = new DomainAudienceResolver()

    @Override
    String resolve(String url) throws AudienceResolveException {
        def audience = internalResolver.resolve(url)
        def indexOfLastDot = audience.lastIndexOf('.')
        indexOfLastDot > 0 ? audience.substring(audience.lastIndexOf('.')) : audience
    }
}
