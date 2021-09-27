package pt.app.sa.service.usecase

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 27/09/2021 23:59
 */
@Component
class FiltersUseCase {

    @Value("\${rest.filters}")
    lateinit var filters: List<String>

    fun getAllFilters() = filters
}