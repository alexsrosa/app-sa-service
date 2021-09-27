package pt.app.sa.service.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import pt.app.sa.service.model.ClusterEntity
import pt.app.sa.service.model.RegionEntity
import pt.app.sa.service.model.StoreEntity
import pt.app.sa.service.model.StoreProductEntity

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 16:19
 */
@ActiveProfiles("test")
@DataJpaTest
class StoreProductsRepositoryTest @Autowired constructor(
    val clusterRepository: ClusterRepository,
    val regionRepository: RegionRepository,
    val storeRepository: StoreRepository,
    val storeProductRepository: StoreProductRepository
) {

    @Test
    fun `When create new store product then save on database`() {

        val cluster = ClusterEntity("testClusterName")
        val savedCluster = clusterRepository.save(cluster)
        val regionEntity = RegionEntity("regionName", "TP", savedCluster)
        val savedRegion = regionRepository.save(regionEntity)
        val storeEntity = StoreEntity("storeName", "theme", savedRegion)
        val savedStore = storeRepository.save(storeEntity)
        val productEntity = StoreProductEntity("product", "sa", savedStore)
        val savedProduct = storeProductRepository.save(productEntity)

        val found = savedProduct.id?.let { storeProductRepository.findById(it) }
        assertThat(found?.get() ?: "").isEqualTo(savedProduct)
    }

    @Test
    fun `When findById on store product then return product`() {

        val cluster = ClusterEntity("testClusterName")
        val savedCluster = clusterRepository.save(cluster)
        val regionEntity = RegionEntity("regionName", "TP", savedCluster)
        val savedRegion = regionRepository.save(regionEntity)
        val storeEntity = StoreEntity("storeName", "theme", savedRegion)
        val savedStore = storeRepository.save(storeEntity)
        val productEntity = StoreProductEntity("product", "sa", savedStore)
        val savedProduct = storeProductRepository.save(productEntity)

        val found = savedProduct.id?.let { storeProductRepository.findById(it) }
        val foundProduct = found?.get() ?: productEntity
        assertThat(productEntity.product).isEqualTo(foundProduct.product)
        assertThat(productEntity.season).isEqualTo(foundProduct.season)
        assertThat(productEntity.store).isEqualTo(foundProduct.store)
    }

    @Test
    fun `When findAll on store products then return all products`() {

        val cluster = ClusterEntity("testClusterName")
        val savedCluster = clusterRepository.save(cluster)
        val regionEntity = RegionEntity("regionName", "TP", savedCluster)
        val savedRegion = regionRepository.save(regionEntity)
        val storeEntity = StoreEntity("storeName", "theme", savedRegion)
        val savedStore = storeRepository.save(storeEntity)

        var index = 0
        val total = 1000
        while (index < total) {
            val productEntity = StoreProductEntity("product $index", "sa", savedStore)
            storeProductRepository.save(productEntity)
            index++
        }

        val findAll = storeProductRepository.findAll()

        assertThat(findAll.size).isEqualTo(total)
    }
}