package net.tapcat.security

import org.springframework.security.web.util.matcher.RequestMatcher

import javax.servlet.http.HttpServletRequest

class NotRequestMatcher implements RequestMatcher {

    @Delegate RequestMatcher inernal

    NotRequestMatcher(RequestMatcher inernal) {
        this.inernal = inernal
    }

    @Override
    boolean matches(HttpServletRequest request) {
        return !inernal.matches(request)
    }
}
