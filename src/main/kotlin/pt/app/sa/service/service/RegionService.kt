package pt.app.sa.service.service

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import pt.app.sa.service.model.RegionEntity
import pt.app.sa.service.repository.RegionRepository
import pt.app.sa.service.schedule.data.RegionData

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 11:12
 */
@Service
class RegionService(
    val regionRepository: RegionRepository,
    val clusterService: ClusterService,
    val cacheManager: CacheManager
) {

    @Synchronized
    fun save(regionData: RegionData): RegionEntity? {
        val cluster = clusterService.findByName(regionData.clusters) ?: return null
        val regionExists = findByName(regionData.name)

        if (regionExists == null) {
            val saved = regionRepository.save(RegionEntity(regionData.name, regionData.type, cluster))
            cacheManager.getCache("regionFindByName")?.evict(saved.name)
            return saved
        } else {
            if (isChangedSomething(regionExists, regionData)) {
                return null
            }
            regionExists.clusters = cluster
            regionExists.type = regionData.type
            val saved = regionRepository.save(regionExists)
            cacheManager.getCache("regionFindByName")?.evict(saved.name)
            return saved
        }
    }

    @Cacheable(cacheNames = ["regionFindByName"], key = "#name")
    fun findByName(name: String): RegionEntity? {
        return regionRepository.findByName(name)
    }

    fun findAll(): List<RegionEntity> {
        return regionRepository.findAll()
    }

    private fun isChangedSomething(regionEntity: RegionEntity, regionData: RegionData): Boolean {
        return regionEntity.type == regionData.type && regionEntity.clusters.name == regionData.clusters
    }
}