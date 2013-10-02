package net.tapcat.monitoring.health

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping('/monitoring')
class SimpleController {

    @RequestMapping('/simple')
    def ok() {
        'ok'
    }
}
