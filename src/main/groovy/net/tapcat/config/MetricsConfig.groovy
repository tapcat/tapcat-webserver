package net.tapcat.config

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.SharedMetricRegistries
import com.codahale.metrics.Slf4jReporter
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration

import java.util.concurrent.TimeUnit

@Configuration
@EnableMetrics
class MetricsConfig extends MetricsConfigurerAdapter {

    @Override
    public MetricRegistry getMetricRegistry() {
        return SharedMetricRegistries.getOrCreate("springMetrics");
    }

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        final Slf4jReporter reporter = Slf4jReporter.forRegistry(metricRegistry)
                .outputTo(LoggerFactory.getLogger("net.tapcat.metrics"))
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(1, TimeUnit.MINUTES);
    }
}
