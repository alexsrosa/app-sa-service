package pt.app.sa.service.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.app.sa.service.controller.dto.FiltersData
import pt.app.sa.service.controller.dto.FiltersEnum
import pt.app.sa.service.usecase.FiltersUseCase
import javax.websocket.server.PathParam

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
    fun getAllFilters() = FiltersEnum.values()

    @PostMapping("/{filterId}")
    fun filterSources(
        @PathVariable filterId: String,
        @RequestBody(required = false) filtersData: FiltersData?,
        @PathParam(value = "page") page: Int? = 0
    ): ResponseEntity<Any> {
        return ResponseEntity.ok(filtersUseCase.filterSources(filterId, filtersData, page))
    }
}