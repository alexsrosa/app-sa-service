package pt.app.sa.service.service

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pt.app.sa.service.exception.RecordAlreadyExistsException
import pt.app.sa.service.model.StoreEntity
import pt.app.sa.service.model.StoreProductEntity
import pt.app.sa.service.repository.StoreProductRepository
import pt.app.sa.service.scheduler.data.StoreProductData
import java.util.*
import kotlin.streams.toList

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
            StoreProductEntity(
                storeProductData.product, storeProductData.season, store
            )
        )

        cacheManager.getCache("storeProductFindByProductAndSeasonAndStore")
            ?.evict(Objects.hash(storeProductData, store))
        return saved
    }

    @Transactional
    fun saveAll(list: List<StoreProductData>) {
        val allStoreProductDataAssociateBy = list.associateBy { Objects.hash(it.product, it.season, it.store) }
        val listToUpdate = storeProductRepository.findByHashIdIn(allStoreProductDataAssociateBy.keys)
        val listToUpdateAssociateBy = listToUpdate.associateBy { Objects.hash(it.product, it.season, it.store.name) }
        val listToInsert = allStoreProductDataAssociateBy.filter { !listToUpdateAssociateBy.containsKey(it.key) }.values

        if (listToInsert.isNotEmpty()) {
            val storesAssociateBy = storeService.findAll().associateBy { it.name }
            val rowWithValidStore = listToInsert.filter { storesAssociateBy.containsKey(it.store) }

            storeProductRepository.saveAll(rowWithValidStore.parallelStream().map {
                StoreProductEntity(
                    it.product,
                    it.season,
                    storesAssociateBy[it.store]!!
                )
            }.toList())

            cacheManager.getCache("storeProductFindByProductAndSeasonAndStore")?.clear()
        }
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