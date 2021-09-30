package pt.app.sa.service.service

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.test.context.ActiveProfiles
import pt.app.sa.service.repository.ClusterRepository
import pt.app.sa.service.repository.RegionRepository
import pt.app.sa.service.scheduler.data.ClusterData
import pt.app.sa.service.scheduler.data.RegionData

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 27/09/2021 11:00
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class RegionServiceTest @Autowired constructor(
    val regionService: RegionService,
    val clusterService: ClusterService,
    val clusterRepository: ClusterRepository,
    val regionRepository: RegionRepository,
    val cacheManager: CacheManager
) {

    @BeforeAll
    fun setUp() {
        clusterService.save(ClusterData("cluster1"))
        clusterService.save(ClusterData("cluster2"))
    }

    @BeforeEach
    fun setUpEach() {
        regionRepository.deleteAll()
        cacheManager.getCache("regionFindByName")?.clear()
    }

    @AfterAll
    fun destroy() {
        regionRepository.deleteAll()
        clusterRepository.deleteAll()
    }

    @Test
    fun `When save a new region and try another the same name Then save only one`() {

        val regionData = RegionData("region", "type", "cluster1")
        val savedOne = regionService.save(regionData)
        assertNotNull(savedOne)

        val savedTwo = regionService.save(regionData)
        assertNull(savedTwo)
    }

    @Test
    fun `When save a new region and update before the same name Then save only one`() {

        val regionData = RegionData("region", "type", "cluster1")
        regionService.save(regionData)
        regionData.type = "typeChanged"
        regionData.clusters = "cluster2"

        val savedTwo = regionService.save(regionData)
        assertNotNull(savedTwo)
        assertEquals(regionData.type, savedTwo?.type)
        assertEquals(regionData.clusters, savedTwo?.clusters?.name)
    }

    @Test
    fun `When findByName Then return region`() {

        val findByNameButNotExists = regionService.findByName("region")
        assertNull(findByNameButNotExists)

        val regionData = RegionData("region", "type", "cluster1")
        val saved = regionService.save(regionData)
        assertNotNull(saved)

        val findByNameButExists = regionService.findByName("region")
        assertNotNull(findByNameButExists)
    }

    @Test
    fun `When findByAll Then return all regions`() {

        var i = 1
        val total = 100
        while (i <= total) {
            regionService.save(RegionData("region$i", "type", "cluster1"))
            i++
        }

        i = 1
        while (i <= total) {
            regionService.save(RegionData("region$i", "type", "cluster1"))
            i++
        }

        val findAll = regionService.findAll()
        assertEquals(total, findAll.size)
    }
}