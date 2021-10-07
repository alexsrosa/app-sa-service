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
import pt.app.sa.service.model.ProductEntity
import pt.app.sa.service.repository.ProductRepository
import pt.app.sa.service.repository.specification.ProductSpecification
import pt.app.sa.service.scheduler.data.ProductData
import java.util.*
import kotlin.streams.toList

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 20:30
 */
@Service
class ProductService(
    val productRepository: ProductRepository,
    val cacheManager: CacheManager
) {
    val logger: Logger = LoggerFactory.getLogger(ProductService::class.java)

    fun save(product: ProductData): ProductEntity? {
        val productExists = findBySeasonAndSku(product.season, product.sku)
        if (productExists == null) {
            val saved = productRepository.save(
                ProductEntity(
                    product.season,
                    product.model,
                    product.size,
                    product.sku,
                    product.ean.toLong(),
                    product.description
                )
            )
            cacheManager.getCache("productFindBySeasonAndSku")?.evict(Objects.hash(saved.season, saved.sku))
            return saved
        } else {
            if (isChangedSomething(productExists, product)) {
                return null
            }
            productExists.model = product.model
            productExists.size = product.size
            productExists.ean = product.ean.toLong()
            productExists.description = product.description
            val saved = productRepository.save(productExists)
            cacheManager.getCache("productFindBySeasonAndSku")?.evict(Objects.hash(saved.season, saved.sku))
            return saved
        }
    }

    @Transactional
    fun saveAll(list: MutableList<ProductData>) {
        val allProductDataAssociateBy = list.associateBy { Objects.hash(it.season, it.sku) }
        val listToUpdate = productRepository.findByHashIdIn(allProductDataAssociateBy.keys)
        val listToUpdateAssociateBy = listToUpdate.associateBy { Objects.hash(it.season, it.sku) }
        val listToInsert = allProductDataAssociateBy.filter { !listToUpdateAssociateBy.containsKey(it.key) }.values

        if (listToInsert.isNotEmpty()) {
            productRepository.saveAll(listToInsert.parallelStream().map {
                ProductEntity(
                    it.season,
                    it.model,
                    it.size,
                    it.sku,
                    it.ean.toLong(),
                    it.description
                )
            }.toList())
        }

        if (listToUpdate.isNotEmpty()) {
            listToUpdate.parallelStream().forEach {
                val productData = allProductDataAssociateBy[Objects.hash(it.season, it.sku)]
                if (productData != null) {
                    it.model = productData.model
                    it.size = productData.size
                    it.ean = productData.ean.toLong()
                    it.description = productData.description
                    productRepository.save(it)
                }
            }
        }

        cacheManager.getCache("productFindBySeasonAndSku")?.clear()
    }

    @Cacheable(cacheNames = ["productFindBySeasonAndSku"], key = "T(java.util.Objects).hash(#season, #sku)")
    fun findBySeasonAndSku(season: String, sku: String): ProductEntity? {
        return productRepository.findBySeasonAndSku(season, sku)
    }

    fun findAll(filters: List<FilterData>, pageable: Pageable): Page<ProductEntity> {
        return productRepository.findAll(ProductSpecification.filter(filters), pageable)
    }

    private fun isChangedSomething(productEntity: ProductEntity, productData: ProductData): Boolean {
        return !(productEntity.model != productData.model
                || productEntity.size != productData.size
                || productEntity.sku != productData.sku
                || productEntity.ean != productData.ean.toLong()
                || productEntity.description != productData.description)
    }
}