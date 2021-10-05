package pt.app.sa.service.usecase

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import pt.app.sa.service.controller.dto.FiltersData
import pt.app.sa.service.controller.dto.FiltersEnum.*
import pt.app.sa.service.model.ProductEntity
import pt.app.sa.service.model.RegionEntity
import pt.app.sa.service.service.ClusterService
import pt.app.sa.service.service.ProductService
import pt.app.sa.service.service.RegionService
import pt.app.sa.service.service.StoreProductService

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 27/09/2021 23:59
 */
@Component
class FiltersUseCase(
    val clusterService: ClusterService,
    val storeProductService: StoreProductService,
    val productService: ProductService,
    val regionService: RegionService,
    val storesUseCase: StoresUseCase
) {

    private val logger: Logger = LoggerFactory.getLogger(FiltersUseCase::class.java)

    fun filterSources(filterId: String, filtersData: FiltersData?, numPage: Int?): Any {

        val filters: FiltersData = filtersData ?: FiltersData(emptyList())
        val page = numPage ?: 0

        return when (valueOf(filterId.uppercase())) {
            SEASON -> filterBySeason(filters, page)
            CLUSTER -> filterByCluster(filters, page)
            REGION -> filterByRegion(filters, page).content.map { it.name }.toSet()
            REGION_TYPE -> filterByRegion(filters, page).content.map { it.type }.toSet()
            PRODUCT_MODEL -> filterByProduct(filters, page).content.map { it.model }.toSet()
            PRODUCT_SIZE -> filterByProduct(filters, page, "size").content.map { it.size }.toSet()
            SKU -> filterByProduct(filters, page, "sku").content.map { it.sku }.toSet()
            STORE_NAME -> storesUseCase.findAllByFilter(filters, page).map { it.name }.toSet()
            STORE_THEME -> storesUseCase.findAllByFilter(filters, page, "theme").map { it.theme }.toSet()
        }
    }

    private fun filterBySeason(filtersData: FiltersData, page: Int): Set<String> {
        val pageable = PageRequest.of(page, 100, Sort.Direction.ASC, "season")
        return storeProductService.findAll(filtersData.filters, pageable).content.map { it.season }.toSet()
    }

    private fun filterByCluster(filtersData: FiltersData, page: Int): Set<String> {
        val pageable = PageRequest.of(page, 100, Sort.Direction.ASC, "name")
        return clusterService.findAll(filtersData.filters, pageable).content.map { it.name }.toSet()
    }

    private fun filterByRegion(filtersData: FiltersData, page: Int): Page<RegionEntity> {
        val pageable = PageRequest.of(page, 100, Sort.Direction.ASC, "name")
        return regionService.findAll(filtersData.filters, pageable)
    }

    private fun filterByProduct(filtersData: FiltersData, page: Int, orderBy: String = "model"): Page<ProductEntity> {
        val pageable = PageRequest.of(page, 100, Sort.Direction.ASC, orderBy)
        return productService.findAll(filtersData.filters, pageable)
    }

}