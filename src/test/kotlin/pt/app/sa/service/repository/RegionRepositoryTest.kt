package pt.app.sa.service.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import pt.app.sa.service.model.ClusterEntity
import pt.app.sa.service.model.RegionEntity

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 15:45
 */
@ActiveProfiles("test")
@DataJpaTest
class RegionRepositoryTest @Autowired constructor(
    val clusterRepository: ClusterRepository,
    val regionRepository: RegionRepository
) {

    @Test
    fun `When create new cluster then save on database`() {

        val cluster = ClusterEntity("testClusterName")
        val savedCluster = clusterRepository.save(cluster)
        val regionEntity = RegionEntity("regionName", "TP", savedCluster)
        val savedRegion = regionRepository.save(regionEntity)

        val found = savedRegion.id?.let { regionRepository.findById(it) }
        assertThat(found?.get() ?: "").isEqualTo(savedRegion)
    }

    @Test
    fun `When findById on region then return region`() {

        val cluster = ClusterEntity("testClusterName")
        val savedCluster = clusterRepository.save(cluster)
        val regionEntity = RegionEntity("regionName", "TP", savedCluster)
        val savedRegion = regionRepository.save(regionEntity)

        val found = savedRegion.id?.let { regionRepository.findById(it) }
        val foundRegion = found?.get() ?: regionEntity
        assertThat(savedRegion.name).isEqualTo(foundRegion.name)
        assertThat(savedRegion.type).isEqualTo(foundRegion.type)
        assertThat(savedRegion.clusters).isEqualTo(foundRegion.clusters)
    }

    @Test
    fun `When findAll on cluster then return all regions`() {

        val cluster = ClusterEntity("testClusterName")
        val savedCluster = clusterRepository.save(cluster)

        var index = 0
        val total = 1000
        while (index < total) {
            val regionEntity = RegionEntity("regionName $index", "TP", savedCluster)
            regionRepository.save(regionEntity)
            index++
        }

        val findAll = regionRepository.findAll()

        assertThat(findAll.size).isEqualTo(total)
    }
}