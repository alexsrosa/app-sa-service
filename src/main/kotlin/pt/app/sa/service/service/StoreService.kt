package pt.app.sa.service.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pt.app.sa.service.controller.dto.FilterData
import pt.app.sa.service.exception.StoreNotFoundException
import pt.app.sa.service.model.StoreEntity
import pt.app.sa.service.repository.StoreRepository
import pt.app.sa.service.repository.specification.StoreSpecification
import pt.app.sa.service.scheduler.data.StoreData
import kotlin.streams.toList

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

    val logger: Logger = LoggerFactory.getLogger(StoreService::class.java)

    @Synchronized
    fun save(storeData: StoreData): StoreEntity? {
        val region = regionService.findByName(storeData.region) ?: return null
        val storeExists = findByName(storeData.name)

        if (storeExists == null) {
            val saved = storeRepository.save(StoreEntity(storeData.name, storeData.theme, region))
            cacheManager.getCache("storeFindByName")?.evict(saved.name)
            cacheManager.getCache("storeFindAll")?.clear()
            return saved
        } else {
            if (isChangedSomething(storeExists, storeData)) {
                return null
            }
            storeExists.theme = storeData.theme
            storeExists.region = region
            val saved = storeRepository.save(storeExists)
            cacheManager.getCache("storeFindByName")?.evict(saved.name)
            cacheManager.getCache("storeFindAll")?.clear()
            return saved
        }
    }

    @Transactional
    fun saveAll(list: List<StoreData>) {
        val allStoreDataAssociateBy = list.associateBy { it.name }
        val listToUpdate = storeRepository.findByNameIn(allStoreDataAssociateBy.keys)
        val listToUpdateAssociateBy = listToUpdate.associateBy { it.name }
        val listToInsert = allStoreDataAssociateBy.filter { !listToUpdateAssociateBy.containsKey(it.key) }.values
        val regionAssociateBy = regionService.findAll().associateBy { it.name }

        if (listToInsert.isNotEmpty()) {
            val rowWithValidRegion = listToInsert.filter { regionAssociateBy.containsKey(it.region) }
            storeRepository.saveAll(rowWithValidRegion.parallelStream().map {
                StoreEntity(it.name, it.theme, regionAssociateBy[it.region]!!)
            }.toList())
        }

        if (listToUpdate.isNotEmpty()) {
            listToUpdate.parallelStream().forEach {
                val storeData = allStoreDataAssociateBy[it.name]
                if (storeData != null && regionAssociateBy[storeData.region] != null) {
                    it.theme = storeData.theme
                    it.region = regionAssociateBy[storeData.region]!!
                    storeRepository.save(it)
                }
            }
        }
        cacheManager.getCache("storeFindByName")?.clear()
        cacheManager.getCache("storeFindAll")?.clear()
    }

    @Cacheable(cacheNames = ["storeFindByName"], key = "#name")
    fun findByName(name: String): StoreEntity? {
        return storeRepository.findByName(name)
    }

    @Cacheable(cacheNames = ["storeFindByNameAlias"], key = "#name")
    fun findByNameAlias(name: String): StoreEntity? {
        return storeRepository.findByNameAlias(name)
    }

    @Cacheable(cacheNames = ["storeFindAll"])
    fun findAll(): List<StoreEntity> {
        return storeRepository.findAll()
    }

    fun findAll(filters: List<FilterData>, pageable: Pageable): Page<StoreEntity> {
        return storeRepository.findAll(StoreSpecification.filter(filters), pageable)
    }

    fun updateNameAlias(storeName: String, nameAlias: String) {
        val existsStore = findByNameAlias(storeName) ?: throw StoreNotFoundException(storeName)

        existsStore.nameAlias = nameAlias
        storeRepository.save(existsStore)
        cacheManager.getCache("storeFindByNameAlias")?.evict(storeName)
    }

    private fun isChangedSomething(storeEntity: StoreEntity, storeData: StoreData): Boolean {
        return storeEntity.theme == storeData.theme && storeEntity.region.name == storeData.region
    }
}