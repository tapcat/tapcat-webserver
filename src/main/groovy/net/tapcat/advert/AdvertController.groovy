package net.tapcat.advert

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping('/ad')
class AdvertController {

    @RequestMapping(value = '/', method = RequestMethod.GET)
    def getAll() {

    }

    @RequestMapping(value = '/{id}', method = RequestMethod.GET)
    def getAdvert(@PathVariable Integer id) {

    }

    @RequestMapping(value = '/{id}', method = RequestMethod.PUT)
    def create() {

    }

    @RequestMapping(value = '/{id}', method = RequestMethod.DELETE)
    def remove() {

    }
}
