package net.tapcat.monitoring.health

import com.codahale.metrics.health.HealthCheckRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletResponse

@RestController()
@RequestMapping('/monitoring/health')
class MetricsHealthCheckController {

    @Autowired
    HealthCheckRegistry registry

    @RequestMapping(value = '', method = RequestMethod.GET, produces = 'application/json')
    def status(HttpServletResponse resp) {
        def results = registry.runHealthChecks()
        def unhealthy = results.findAll {!it.value.healthy}
        if (results.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
        } else if (unhealthy) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        unhealthy.collect { [it.key, it.value.message] }
    }
}
