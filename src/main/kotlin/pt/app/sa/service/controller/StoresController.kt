package pt.app.sa.service.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.app.sa.service.controller.dto.FiltersData
import pt.app.sa.service.usecase.StoresUseCase
import javax.websocket.server.PathParam

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
        @PathParam(value = "page") page: Int? = 0
    ): ResponseEntity<Any> {
        return ResponseEntity.ok(storesUseCase.findAllByFilter(filtersData, page))
    }
}