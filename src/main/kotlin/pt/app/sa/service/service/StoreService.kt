package pt.app.sa.service.service

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import pt.app.sa.service.model.StoreEntity
import pt.app.sa.service.repository.StoreRepository
import pt.app.sa.service.schedule.data.StoreData

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 11:12
 */
@Service
class StoreService(
    val storeRepository: StoreRepository,
    val regionService: RegionService,
    val cacheManager: CacheManager
) {

    @Synchronized
    fun save(storeData: StoreData): StoreEntity? {
        val region = regionService.findByName(storeData.region) ?: return null
        val storeExists = findByName(storeData.name)

        if (storeExists == null) {
            val saved = storeRepository.save(StoreEntity(storeData.name, storeData.theme, region))
            cacheManager.getCache("storeFindByName")?.evict(saved.name)
            return saved
        } else {
            if (isChangedSomething(storeExists, storeData)) {
                return null
            }
            storeExists.theme = storeData.theme
            storeExists.region = region
            val saved = storeRepository.save(storeExists)
            cacheManager.getCache("storeFindByName")?.evict(saved.name)
            return saved
        }
    }

    @Cacheable(cacheNames = ["storeFindByName"], key = "#name")
    fun findByName(name: String): StoreEntity? {
        return storeRepository.findByName(name)
    }

    fun findAll(): List<StoreEntity> {
        return storeRepository.findAll()
    }

    private fun isChangedSomething(storeEntity: StoreEntity, storeData: StoreData): Boolean {
        return storeEntity.theme == storeData.theme && storeEntity.region.name == storeData.region
    }
}