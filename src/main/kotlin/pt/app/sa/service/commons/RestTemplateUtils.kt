package pt.app.sa.service.commons

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.net.URI

/**
 * Utility class for using the rest template.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 17:49
 */
@Component
class RestTemplateUtils<D>(
    val restTemplate: RestTemplate
) {

    fun get(uri: String, respType: ParameterizedTypeReference<D>): ResponseEntity<D> {
        val request = RequestEntity<Any>(HttpMethod.GET, URI.create(uri))
        return restTemplate.exchange(request, respType)
    }
}