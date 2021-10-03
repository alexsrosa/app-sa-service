package pt.app.sa.service.controller.dto

import javax.validation.constraints.NotEmpty

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 01/10/2021 17:19
 */
data class InStoreDto(

    @field:NotEmpty(message = "The name attribute must be informed and cannot be empty.")
    var name: String
)