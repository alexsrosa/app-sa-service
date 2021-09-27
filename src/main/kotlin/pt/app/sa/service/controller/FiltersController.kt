package pt.app.sa.service.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.app.sa.service.usecase.FiltersUseCase

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 27/09/2021 23:57
 */
@RestController
@RequestMapping("/filters")
class FiltersController(
    val filtersUseCase: FiltersUseCase
) {

    @GetMapping
    fun getAllFilters() = filtersUseCase.getAllFilters()
}