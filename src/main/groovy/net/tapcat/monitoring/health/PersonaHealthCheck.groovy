package net.tapcat.monitoring.health
import com.codahale.metrics.health.HealthCheck
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import persona.BrowserIdVerifier

@Component
class PersonaHealthCheck extends HealthCheck {

    @Autowired
    BrowserIdVerifier verifier

    @Override
    protected HealthCheck.Result check() throws Exception {
        def response = verifier.verify('test-assertion', 'tapcat.net')
        // no timeout exception is success
        HealthCheck.Result.healthy()
    }
}
