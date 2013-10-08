package net.tapcat.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ResponseCodeOnlyAuthHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    int success = HttpServletResponse.SC_NO_CONTENT
    int failure = HttpServletResponse.SC_UNAUTHORIZED

    @Override
    void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(success)
    }

    @Override
    void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.sendError(failure, "Authentication Failed: " + exception.getMessage());
    }
}
