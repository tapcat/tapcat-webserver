package net.tapcat.security

import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
/**
 * CORS filter will help to work with Application from different domains.
 * For ex: locallhost can be used for dev purposes
 */
class CorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader('Access-Control-Allow-Origin', '//localhost')
        filterChain.doFilter(request, response)
    }
}
