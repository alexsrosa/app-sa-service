package pt.app.sa.service.usecase

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import pt.app.sa.service.scheduler.data.StoreData
import pt.app.sa.service.service.StoreService
import pt.app.sa.service.commons.LoadByRequestWithPageType
import pt.app.sa.service.commons.RestTemplateUtils

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 20:11
 */
@Component
class LoadStoresUseCase(
    val storeService: StoreService,
    val restTemplateUtils: RestTemplateUtils<List<StoreData>>
) : LoadByRequestWithPageType<StoreData> {

    override val logger: Logger = LoggerFactory.getLogger(LoadStoresUseCase::class.java)

    @Value("\${externalDataLoad.baseUri}")
    lateinit var baseUri: String

    @Value("\${externalDataLoad.endpoints.stores}")
    lateinit var endpoint: String

    @Value("\${externalDataLoad.endpoints.stores.errorsAccepted:100}")
    override var errorsAccepted: Int = 0

    override var totalBatch: Int = 100
    override var processName: String = "Store"

    override fun request(page: Int): ResponseEntity<List<StoreData>> {
        return restTemplateUtils.get(
            "$baseUri$endpoint?page=$page",
            object : ParameterizedTypeReference<List<StoreData>>() {})
    }

    override fun saveAll(list: List<StoreData>) {
        storeService.saveAll(list)
    }

    override fun stopExecution(list: List<StoreData>): Boolean {
        return list.isEmpty()
    }
}