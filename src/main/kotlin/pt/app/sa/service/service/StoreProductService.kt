package pt.app.sa.service.service

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import pt.app.sa.service.exception.RecordAlreadyExistsException
import pt.app.sa.service.model.StoreEntity
import pt.app.sa.service.model.StoreProductEntity
import pt.app.sa.service.repository.StoreProductRepository
import pt.app.sa.service.schedule.data.StoreProductData
import java.util.*

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 11:12
 */
@Service
class StoreProductService(
    val storeProductRepository: StoreProductRepository,
    val storeService: StoreService,
    val cacheManager: CacheManager
) {

    @Synchronized
    fun save(storeProductData: StoreProductData): StoreProductEntity? {
        val store = storeService.findByName(storeProductData.store) ?: return null

        val firstOrNull = findByProductAndSeasonAndStore(storeProductData, store)
        if (firstOrNull != null) {
            throw RecordAlreadyExistsException()
        }

        val saved = storeProductRepository.save(
            StoreProductEntity(storeProductData.product, storeProductData.season, store)
        )

        cacheManager.getCache("storeProductFindByProductAndSeasonAndStore")
            ?.evict(Objects.hash(storeProductData, store))
        return saved
    }

    @Cacheable(
        cacheNames = ["storeProductFindByProductAndSeasonAndStore"],
        key = "T(java.util.Objects).hash(#storeProductData,#store)"
    )
    fun findByProductAndSeasonAndStore(
        storeProductData: StoreProductData,
        store: StoreEntity
    ): StoreProductEntity? {
        return storeProductRepository.findByProductAndSeasonAndStore(
            storeProductData.product,
            storeProductData.season,
            store
        )
    }

    fun findAll(): List<StoreProductEntity> {
        return storeProductRepository.findAll()
    }
}