package pt.app.sa.service.usecase

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import pt.app.sa.service.exception.RecordAlreadyExistsException
import pt.app.sa.service.schedule.data.StoreProductData
import pt.app.sa.service.service.StoreProductService
import pt.app.sa.service.utils.LoadByRequestWithPageType
import pt.app.sa.service.utils.RestTemplateUtils

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 20:11
 */
@Component
class LoadStoreProductsUseCase(
    val storeProductService: StoreProductService,
    val restTemplateUtils: RestTemplateUtils<List<StoreProductData>>
) : LoadByRequestWithPageType<List<StoreProductData>> {

    override val logger: Logger = LoggerFactory.getLogger(LoadStoreProductsUseCase::class.java)

    @Value("\${externalDataLoad.baseUri}")
    lateinit var baseUri: String

    @Value("\${externalDataLoad.endpoints.storeProducts}")
    lateinit var endpoint: String

    @Value("\${externalDataLoad.endpoints.storeProducts.errorsAccepted:100}")
    override var errorsAccepted: Int = 0

    override fun request(page: Int): ResponseEntity<List<StoreProductData>> {
        return restTemplateUtils.get(
            "$baseUri$endpoint?page=$page",
            object : ParameterizedTypeReference<List<StoreProductData>>() {})
    }

    override fun save(list: List<StoreProductData>) {
        list.forEach { storeProduct ->
            try {
                val saveStoreProduct = storeProductService.save(storeProduct)
                if (saveStoreProduct != null) {
                    logger.info("A new store products entry has been created. $saveStoreProduct")
                }
            } catch (ex: RecordAlreadyExistsException) {
                logger.debug("A store products $storeProduct already exists")
            }

        }
    }

    override fun stopExecution(list: List<StoreProductData>): Boolean {
        return list.isEmpty()
    }
}