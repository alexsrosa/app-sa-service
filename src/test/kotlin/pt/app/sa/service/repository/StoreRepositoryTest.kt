package pt.app.sa.service.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import pt.app.sa.service.model.ClusterEntity
import pt.app.sa.service.model.RegionEntity
import pt.app.sa.service.model.StoreEntity

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 16:19
 */
@DataJpaTest
class StoreRepositoryTest @Autowired constructor(
    val clusterRepository: ClusterRepository,
    val regionRepository: RegionRepository,
    val storeRepository: StoreRepository
) {

    @Test
    fun `When create new store then save on database`() {

        val cluster = ClusterEntity("testClusterName")
        val savedCluster = clusterRepository.save(cluster)
        val regionEntity = RegionEntity("regionName", "TP", savedCluster)
        val savedRegion = regionRepository.save(regionEntity)
        val storeEntity = StoreEntity("storeName", "theme", savedRegion)
        val savedStore = storeRepository.save(storeEntity)

        val found = savedStore.id?.let { storeRepository.findById(it) }
        assertThat(found?.get() ?: "").isEqualTo(savedStore)
    }

    @Test
    fun `When findById on store then return store`() {

        val cluster = ClusterEntity("testClusterName")
        val savedCluster = clusterRepository.save(cluster)
        val regionEntity = RegionEntity("regionName", "TP", savedCluster)
        val savedRegion = regionRepository.save(regionEntity)
        val storeEntity = StoreEntity("storeName", "theme", savedRegion)
        val savedStore = storeRepository.save(storeEntity)

        val found = savedStore.id?.let { storeRepository.findById(it) }
        val foundStore = found?.get() ?: storeEntity
        assertThat(storeEntity.name).isEqualTo(foundStore.name)
        assertThat(storeEntity.theme).isEqualTo(foundStore.theme)
        assertThat(storeEntity.region).isEqualTo(foundStore.region)
    }

    @Test
    fun `When findAll on stores then return all stores`() {

        val cluster = ClusterEntity("testClusterName")
        val savedCluster = clusterRepository.save(cluster)
        val regionEntity = RegionEntity("regionName", "TP", savedCluster)
        val savedRegion = regionRepository.save(regionEntity)

        var index = 0
        val total = 1000
        while (index < total) {
            val storeEntity = StoreEntity("storeName $index", "theme", savedRegion)
            storeRepository.save(storeEntity)
            index++
        }

        val findAll = storeRepository.findAll()

        assertThat(findAll.size).isEqualTo(total)
    }
}