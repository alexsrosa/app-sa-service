package pt.app.sa.service.schedule.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 12:39
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class StoreProductData(
    var product: String,
    var store: String,
    var season: String
)