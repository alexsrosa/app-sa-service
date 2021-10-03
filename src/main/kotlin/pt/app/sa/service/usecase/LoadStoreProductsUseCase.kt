package pt.app.sa.service.usecase

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pt.app.sa.service.scheduler.data.StoreProductData
import pt.app.sa.service.service.StoreProductService
import pt.app.sa.service.commons.LoadByRequestWithPageMultiThreadType
import pt.app.sa.service.commons.RestTemplateUtils

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 20:11
 */
@Component
class LoadStoreProductsUseCase(
    val storeProductService: StoreProductService,
    val restTemplateUtils: RestTemplateUtils<List<StoreProductData>>
) : LoadByRequestWithPageMultiThreadType<StoreProductData> {

    override val logger: Logger = LoggerFactory.getLogger(LoadStoreProductsUseCase::class.java)
    override var totalBatch: Int = 3000
    override var processName: String = "StoreProduct"

    @Value("\${externalDataLoad.baseUri}")
    lateinit var baseUri: String

    @Value("\${externalDataLoad.endpoints.storeProducts.lastPage}")
    var lastPage: Int = 0

    @Value("\${externalDataLoad.endpoints.storeProducts.path}")
    lateinit var endpoint: String

    @Value("\${externalDataLoad.endpoints.storeProducts.errorsAccepted:1500}")
    override var errorsAccepted: Int = 0

    @Value("\${externalDataLoad.endpoints.storeProducts.totalThreads:20}")
    override var totalThreads: Int = 0

    override fun request(page: Int): ResponseEntity<List<StoreProductData>> {
        return restTemplateUtils.get(
            "$baseUri$endpoint?page=$page",
            object : ParameterizedTypeReference<List<StoreProductData>>() {})
    }

    @Transactional
    override fun saveAll(list: List<StoreProductData>) {
        storeProductService.saveAll(list)
    }

    override fun stopExecution(list: List<StoreProductData>): Boolean {
        return list.isEmpty()
    }

    override fun discoverLastPage(): Int {
        if ((request(lastPage + 1).body?.size ?: 0) == 0) return lastPage

        val nowLastPage = lastPage + 1
        var execute = true
        var rangeSearch = 1000
        var pageSearch = nowLastPage + rangeSearch
        val maxRetry = 100
        while (execute) {

            var executeRequest = true
            var retryCount = 0
            while (executeRequest) {
                try {
                    if ((request(pageSearch).body?.size ?: 0) > 0) pageSearch += rangeSearch
                    else {
                        pageSearch -= rangeSearch
                        rangeSearch /= 2
                    }

                    if (rangeSearch == 1) execute = false

                    executeRequest = false
                } catch (ex: Exception) {
                    retryCount++
                }

                if (retryCount > maxRetry) {
                    executeRequest = false
                    execute = false
                }
            }
        }

        return pageSearch
    }
}