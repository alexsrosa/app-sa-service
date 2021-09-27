package pt.app.sa.service.usecase

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import pt.app.sa.service.schedule.data.ClusterData
import pt.app.sa.service.service.ClusterService
import pt.app.sa.service.utils.LoadByRequestWithPageType
import pt.app.sa.service.utils.RestTemplateUtils

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 19:34
 */
@Component
class LoadClustersUseCase(
    val clusterService: ClusterService,
    val restTemplateUtils: RestTemplateUtils<List<ClusterData>>
) : LoadByRequestWithPageType<List<ClusterData>> {

    @Value("\${externalDataLoad.baseUri}")
    lateinit var baseUri: String

    @Value("\${externalDataLoad.endpoints.clusters}")
    lateinit var endpointCluster: String

    @Value("\${externalDataLoad.endpoints.clusters.errorsAccepted:10}")
    override var errorsAccepted: Int = 0

    override val logger: Logger = LoggerFactory.getLogger(LoadClustersUseCase::class.java)

    override fun request(page: Int): ResponseEntity<List<ClusterData>> {
        return restTemplateUtils.get(
            "$baseUri$endpointCluster?page=$page",
            object : ParameterizedTypeReference<List<ClusterData>>() {})
    }

    override fun save(list: List<ClusterData>) {
        list.forEach { cluster ->
            val saveCluster = clusterService.save(cluster)
            if (saveCluster != null) {
                logger.info("A new cluster entry has been created. $saveCluster")
            }
        }
    }

    override fun stopExecution(list: List<ClusterData>): Boolean {
        return list.isEmpty()
    }
}