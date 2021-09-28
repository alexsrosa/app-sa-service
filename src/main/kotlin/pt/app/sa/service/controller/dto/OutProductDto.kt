package pt.app.sa.service.controller.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 18:06
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class OutProductDto(
    var season: String,
    var model: String,
    var size: String,
    var sku: String,
    var ean: Long,
    var description: String
)