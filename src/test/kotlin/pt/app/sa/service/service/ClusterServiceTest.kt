package pt.app.sa.service.service

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.test.context.ActiveProfiles
import pt.app.sa.service.repository.ClusterRepository
import pt.app.sa.service.scheduler.data.ClusterData

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 27/09/2021 08:56
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClusterServiceTest @Autowired constructor(
    val clusterService: ClusterService,
    val clusterRepository: ClusterRepository,
    val cacheManager: CacheManager
) {

    @BeforeEach
    fun setUp() {
        clusterRepository.deleteAll()
        cacheManager.getCache("clusterExistsByName")?.clear()
        cacheManager.getCache("clusterFindByName")?.clear()
    }

    @AfterAll
    fun destroy() {
        clusterRepository.deleteAll()
    }

    @Test
    fun `When save a new clusters and try another the same name Then save only one`() {

        val clusterData = ClusterData("cluster1")
        val savedOne = clusterService.save(clusterData)
        assertNotNull(savedOne)

        val savedTwo = clusterService.save(clusterData)
        assertNull(savedTwo)
    }

    @Test
    fun `When verify if cluster exists Then return boolean`() {

        val existsHasFalse = clusterService.exists("cluster1")
        assertFalse(existsHasFalse)

        val clusterData = ClusterData("cluster1")
        val savedOne = clusterService.save(clusterData)
        assertNotNull(savedOne)

        val existsHasTrue = clusterService.exists("cluster1")
        assertTrue(existsHasTrue)
    }

    @Test
    fun `When findByName Then return cluster`() {

        val findByNameButNotExists = clusterService.findByName("cluster1")
        assertNull(findByNameButNotExists)

        val clusterData = ClusterData("cluster1")
        val savedOne = clusterService.save(clusterData)
        assertNotNull(savedOne)

        val findByNameButExists = clusterService.findByName("cluster1")
        assertNotNull(findByNameButExists)
    }

    @Test
    fun `When findByAll Then return all clusters`() {

        var i = 1
        val total = 100
        while (i <= total) {
            clusterService.save(ClusterData("cluster$i"))
            i++
        }

        i = 1
        while (i <= total) {
            clusterService.save(ClusterData("cluster$i"))
            i++
        }

        val findAll = clusterService.findAll()
        assertEquals(total, findAll.size)
    }
}