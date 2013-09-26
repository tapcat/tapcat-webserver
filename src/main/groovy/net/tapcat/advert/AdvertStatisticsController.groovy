package net.tapcat.advert

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping('/ad/stats')
class AdvertStatisticsController {

    @RequestMapping(value = '/', method = RequestMethod.GET)
    def getAll() {

    }

    @RequestMapping(value = '/{id}', method = RequestMethod.GET)
    def getById(@PathVariable Integer id) {

    }


}
