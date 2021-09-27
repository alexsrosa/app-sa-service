package pt.app.sa.service.usecase

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import pt.app.sa.service.schedule.data.RegionData
import pt.app.sa.service.service.RegionService
import pt.app.sa.service.utils.LoadByRequestWithPageType
import pt.app.sa.service.utils.RestTemplateUtils

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 19:34
 */
@Component
class LoadRegionsUseCase(
    val regionService: RegionService,
    val restTemplateUtils: RestTemplateUtils<List<RegionData>>
) : LoadByRequestWithPageType<List<RegionData>> {

    override val logger: Logger = LoggerFactory.getLogger(LoadRegionsUseCase::class.java)

    @Value("\${externalDataLoad.baseUri}")
    lateinit var baseUri: String

    @Value("\${externalDataLoad.endpoints.regions}")
    lateinit var endpoint: String

    @Value("\${externalDataLoad.endpoints.regions.errorsAccepted:10}")
    override var errorsAccepted: Int = 0

    override fun request(page: Int): ResponseEntity<List<RegionData>> {
        return restTemplateUtils.get(
            "$baseUri$endpoint?page=$page",
            object : ParameterizedTypeReference<List<RegionData>>() {})
    }

    override fun save(list: List<RegionData>) {
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