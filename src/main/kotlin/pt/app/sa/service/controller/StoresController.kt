package pt.app.sa.service.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.app.sa.service.controller.dto.FiltersData
import pt.app.sa.service.controller.dto.InStoreDto
import pt.app.sa.service.usecase.StoresUseCase

import javax.validation.Valid

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 29/09/2021 19:29
 */
@RestController
@RequestMapping("/stores")
class StoresController(
    val storesUseCase: StoresUseCase
) {

    @PostMapping
    fun findAllByFilter(
        @RequestBody(required = false) filtersData: FiltersData?,
        @RequestParam(value = "page") page: Int? = 0
    ): ResponseEntity<Any> {
        return ResponseEntity.ok(storesUseCase.findAllByFilter(filtersData, page))
    }

    @PatchMapping("/{storeName}")
    fun updateStore(
        @RequestBody @Valid inStoreDto: InStoreDto,
        @PathVariable storeName: String
    ): ResponseEntity<Any> {
        storesUseCase.update(storeName, inStoreDto)
        return ResponseEntity.ok().build()
    }
}