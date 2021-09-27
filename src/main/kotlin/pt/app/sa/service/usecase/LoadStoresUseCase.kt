package pt.app.sa.service.usecase

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import pt.app.sa.service.schedule.data.StoreData
import pt.app.sa.service.service.StoreService
import pt.app.sa.service.utils.LoadByRequestWithPageType
import pt.app.sa.service.utils.RestTemplateUtils

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 20:11
 */
@Component
class LoadStoresUseCase(
    val storeService: StoreService,
    val restTemplateUtils: RestTemplateUtils<List<StoreData>>
) : LoadByRequestWithPageType<List<StoreData>> {

    override val logger: Logger = LoggerFactory.getLogger(LoadStoresUseCase::class.java)

    @Value("\${externalDataLoad.baseUri}")
    lateinit var baseUri: String

    @Value("\${externalDataLoad.endpoints.stores}")
    lateinit var endpoint: String

    @Value("\${externalDataLoad.endpoints.stores.errorsAccepted:20}")
    override var errorsAccepted: Int = 0

    override fun request(page: Int): ResponseEntity<List<StoreData>> {
        return restTemplateUtils.get(
            "$baseUri$endpoint?page=$page",
            object : ParameterizedTypeReference<List<StoreData>>() {})
    }

    override fun save(list: List<StoreData>) {
        list.forEach { region ->
            val saveStore = storeService.save(region)
            if (saveStore != null) {
                logger.info("A new store entry has been created. $saveStore")
            }
        }
    }

    override fun stopExecution(list: List<StoreData>): Boolean {
        return list.isEmpty()
    }
}