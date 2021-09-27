package pt.app.sa.service.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 18:03
 */
@Configuration
class RestTemplateConfig {

    @Value("\${externalDataLoad.apiKey}")
    lateinit var apiKey: String

    @Bean
    fun restTemplate(): RestTemplate = RestTemplateBuilder()
        .setConnectTimeout(Duration.ofSeconds(10))
        .defaultHeader("API-Key", apiKey)
        .build()
}