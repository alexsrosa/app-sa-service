package pt.app.sa.service.usecase

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import pt.app.sa.service.controller.dto.*
import pt.app.sa.service.controller.dto.FiltersEnum.*
import pt.app.sa.service.service.ClusterService
import pt.app.sa.service.service.ProductService
import pt.app.sa.service.service.RegionService
import pt.app.sa.service.service.StoreService

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 27/09/2021 23:59
 */
@Component
class FiltersUseCase(
    val clusterService: ClusterService,
    val productService: ProductService,
    val regionService: RegionService,
    val storeService: StoreService
) {

    private val logger: Logger = LoggerFactory.getLogger(FiltersUseCase::class.java)

    fun filterSources(filterId: String, filtersData: FiltersData?, numPage: Int?): Any {

        val filters: FiltersData = filtersData ?: FiltersData(emptyList())
        val page = numPage ?: 0

        when (valueOf(filterId.uppercase())) {
            SEASON, PRODUCT_MODEL, PRODUCT_SIZE, SKU -> return filterByProduct(filters, page)
            CLUSTER -> return filterByCluster(filters, page)
            REGION, REGION_TYPE -> return filterByRegion(filters, page)
            STORE_NAME, STORE_THEME -> return filterByStore(filters, page)
            else -> logger.info("Filter Not Found")
        }
        return Any()
    }

    private fun filterByProduct(filtersData: FiltersData, page: Int): List<OutProductDto> {
        val pageable = PageRequest.of(page, 100, Sort.Direction.ASC, "ean")
        return productService.findAll(filtersData.filters, pageable).content.map {
            OutProductDto(it.season, it.model, it.size, it.sku, it.ean, it.description)
        }
    }

    private fun filterByCluster(filtersData: FiltersData, page: Int): List<OutClusterDto> {
        val pageable = PageRequest.of(page, 100, Sort.Direction.ASC, "name")
        return clusterService.findAll(filtersData.filters, pageable).content.map { OutClusterDto(it.name) }
    }

    private fun filterByRegion(filtersData: FiltersData, page: Int): List<OutRegionDto> {
        val pageable = PageRequest.of(page, 100, Sort.Direction.ASC, "name")
        return regionService.findAll(filtersData.filters, pageable).content.map {
            OutRegionDto(it.name, it.type, it.clusters.name)
        }
    }

    private fun filterByStore(filtersData: FiltersData, page: Int): List<OutStoreDto> {
        val pageable = PageRequest.of(page, 100, Sort.Direction.ASC, "name")
        return storeService.findAll(filtersData.filters, pageable).content.map {
            OutStoreDto(it.name, it.theme, it.region.name)
        }
    }

}