package pt.app.sa.service.scheduler.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 12:05
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class StoreData(
    var name: String,
    var theme: String,
    var region: String
)