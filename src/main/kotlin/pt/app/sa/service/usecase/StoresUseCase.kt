package pt.app.sa.service.usecase

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import pt.app.sa.service.controller.dto.FiltersData
import pt.app.sa.service.controller.dto.InStoreDto
import pt.app.sa.service.controller.dto.OutStoreDto
import pt.app.sa.service.service.StoreService

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 27/09/2021 23:59
 */
@Component
class StoresUseCase(
    val storeService: StoreService
) {

    fun findAllByFilter(filtersData: FiltersData?, numPage: Int?, orderBy: String = "nameAlias"): List<OutStoreDto> {
        val filters: FiltersData = filtersData ?: FiltersData(emptyList())
        val page = numPage ?: 0

        val pageable = PageRequest.of(page, 100, Sort.Direction.ASC, orderBy)
        return storeService.findAll(filters.filters, pageable).content.map {
            OutStoreDto(it.nameAlias, it.theme, it.region.name)
        }
    }

    fun update(storeName: String, inStoreDto: InStoreDto) {
        storeService.updateNameAlias(storeName, inStoreDto.name)
    }
}