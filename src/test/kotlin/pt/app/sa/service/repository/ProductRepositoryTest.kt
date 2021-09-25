package pt.app.sa.service.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import pt.app.sa.service.model.ClusterEntity
import pt.app.sa.service.model.ProductEntity
import pt.app.sa.service.model.RegionEntity
import pt.app.sa.service.model.StoreEntity

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 16:19
 */
@DataJpaTest
class ProductRepositoryTest @Autowired constructor(
    val clusterRepository: ClusterRepository,
    val regionRepository: RegionRepository,
    val storeRepository: StoreRepository,
    val productRepository: ProductRepository
) {

    @Test
    fun `When create new product then save on database`() {

        val cluster = ClusterEntity("testClusterName")
        val savedCluster = clusterRepository.save(cluster)
        val regionEntity = RegionEntity("regionName", "TP", savedCluster)
        val savedRegion = regionRepository.save(regionEntity)
        val storeEntity = StoreEntity("storeName", "theme", savedRegion)
        val savedStore = storeRepository.save(storeEntity)
        val productEntity = ProductEntity("product", "sa", savedStore)
        val savedProduct = productRepository.save(productEntity)

        val found = savedProduct.id?.let { productRepository.findById(it) }
        assertThat(found?.get() ?: "").isEqualTo(savedProduct)
    }

    @Test
    fun `When findById on product then return product`() {

        val cluster = ClusterEntity("testClusterName")
        val savedCluster = clusterRepository.save(cluster)
        val regionEntity = RegionEntity("regionName", "TP", savedCluster)
        val savedRegion = regionRepository.save(regionEntity)
        val storeEntity = StoreEntity("storeName", "theme", savedRegion)
        val savedStore = storeRepository.save(storeEntity)
        val productEntity = ProductEntity("product", "sa", savedStore)
        val savedProduct = productRepository.save(productEntity)

        val found = savedProduct.id?.let { productRepository.findById(it) }
        val foundProduct = found?.get() ?: productEntity
        assertThat(productEntity.product).isEqualTo(foundProduct.product)
        assertThat(productEntity.season).isEqualTo(foundProduct.season)
        assertThat(productEntity.store).isEqualTo(foundProduct.store)
    }

    @Test
    fun `When findAll on products then return all products`() {

        val cluster = ClusterEntity("testClusterName")
        val savedCluster = clusterRepository.save(cluster)
        val regionEntity = RegionEntity("regionName", "TP", savedCluster)
        val savedRegion = regionRepository.save(regionEntity)
        val storeEntity = StoreEntity("storeName", "theme", savedRegion)
        val savedStore = storeRepository.save(storeEntity)

        var index = 0
        val total = 1000
        while (index < total) {
            val productEntity = ProductEntity("product $index", "sa", savedStore)
            productRepository.save(productEntity)
            index++
        }

        val findAll = productRepository.findAll()

        assertThat(findAll.size).isEqualTo(total)
    }
}