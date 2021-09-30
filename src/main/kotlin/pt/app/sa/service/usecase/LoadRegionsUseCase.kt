package pt.app.sa.service.usecase

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import pt.app.sa.service.scheduler.data.RegionData
import pt.app.sa.service.service.RegionService
import pt.app.sa.service.commons.LoadByRequestWithPageType
import pt.app.sa.service.commons.RestTemplateUtils

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 19:34
 */
@Component
class LoadRegionsUseCase(
    val regionService: RegionService,
    val restTemplateUtils: RestTemplateUtils<List<RegionData>>
) : LoadByRequestWithPageType<RegionData> {

    override val logger: Logger = LoggerFactory.getLogger(LoadRegionsUseCase::class.java)

    @Value("\${externalDataLoad.baseUri}")
    lateinit var baseUri: String

    @Value("\${externalDataLoad.endpoints.regions}")
    lateinit var endpoint: String

    @Value("\${externalDataLoad.endpoints.regions.errorsAccepted:100}")
    override var errorsAccepted: Int = 0

    override var totalBatch: Int = 100
    override var processName: String = "Region"

    override fun request(page: Int): ResponseEntity<List<RegionData>> {
        return restTemplateUtils.get(
            "$baseUri$endpoint?page=$page",
            object : ParameterizedTypeReference<List<RegionData>>() {})
    }

    override fun saveAll(list: List<RegionData>) {
        list.forEach { region ->
            val saveRegion = regionService.save(region)
            if (saveRegion != null) {
                logger.info("The region $saveRegion has been inserted/updated")
            }
        }
    }

    override fun stopExecution(list: List<RegionData>): Boolean {
        return list.isEmpty()
    }
}