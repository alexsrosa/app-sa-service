package pt.app.sa.service.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import pt.app.sa.service.model.ClusterEntity

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 15:45
 */
@DataJpaTest
class ClusterRepositoryTest @Autowired constructor(
    val clusterRepository: ClusterRepository
) {

    @Test
    fun `When create new cluster then save on database`() {

        val cluster = ClusterEntity("testClusterName")
        val saved = clusterRepository.save(cluster)

        val found = saved.id?.let { clusterRepository.findById(it) }

        assertThat(found?.get() ?: "").isEqualTo(saved)
    }

    @Test
    fun `When findById on cluster then return cluster`() {

        val cluster = ClusterEntity("testClusterName")
        val saved = clusterRepository.save(cluster)

        val found = saved.id?.let { clusterRepository.findById(it) }
        val foundCluster = found?.get() ?: cluster

        assertThat(foundCluster.name).isEqualTo(saved.name)
    }

    @Test
    fun `When findAll on cluster then return all cluster`() {

        var index = 0
        val total = 1000
        while (index < total) {
            val cluster = ClusterEntity("testClusterName $index")
            clusterRepository.save(cluster)
            index++
        }

        val findAll = clusterRepository.findAll()

        assertThat(findAll.size).isEqualTo(total)
    }
}