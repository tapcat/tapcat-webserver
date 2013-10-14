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
        def audienceParts = audience.split('\\.')
        audienceParts.length >= 2 ? audienceParts[-2..-1].join('.') : audience
    }
}
