package net.tapcat.details
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
@RequestMapping('/user')
class UserDetailsController {

    @RequestMapping(value = '/', method = RequestMethod.GET)
    @Secured('ROLE_USER')
    def details(Authentication auth) {
        auth.principal
    }
}
