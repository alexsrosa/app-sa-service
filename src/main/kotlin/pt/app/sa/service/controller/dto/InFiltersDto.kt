package pt.app.sa.service.controller.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 28/09/2021 08:41
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class FiltersData(
    var filters: List<FilterData>
)

@JsonIgnoreProperties(ignoreUnknown = true)
class FilterData(
    var id: String,
    var values: List<String>
)