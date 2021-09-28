package pt.app.sa.service.service

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import pt.app.sa.service.controller.dto.FilterData
import pt.app.sa.service.model.ClusterEntity
import pt.app.sa.service.repository.ClusterRepository
import pt.app.sa.service.repository.specification.ClusterSpecification
import pt.app.sa.service.schedule.data.ClusterData

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 11:12
 */
@Service
class ClusterService(
    val clusterRepository: ClusterRepository,
    val cacheManager: CacheManager
) {

    @Synchronized
    fun save(clusterData: ClusterData): ClusterEntity? {
        if (!exists(clusterData.name)) {
            val saved = clusterRepository.save(ClusterEntity(clusterData.name))
            cacheManager.getCache("clusterExistsByName")?.evict(clusterData.name)
            cacheManager.getCache("clusterFindByName")?.evict(clusterData.name)
            return saved
        }
        return null
    }

    @Cacheable(cacheNames = ["clusterExistsByName"], key = "#name")
    fun exists(name: String) = findByName(name) != null

    @Cacheable(cacheNames = ["clusterFindByName"], key = "#name")
    fun findByName(name: String): ClusterEntity? {
        return clusterRepository.findByName(name)
    }

    fun findAll(): List<ClusterEntity> {
        return clusterRepository.findAll()
    }

    fun findAll(filters: List<FilterData>, pageable: Pageable): Page<ClusterEntity> {
        return clusterRepository.findAll(ClusterSpecification.filter(filters), pageable)
    }
}