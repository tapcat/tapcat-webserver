package net.tapcat.user
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping('/user')
class UserDetailsController {

    @RequestMapping(value = '', method = RequestMethod.GET)
    @Secured('ROLE_USER')
    def details(Authentication auth) {
        [name: auth.principal]
    }
}
