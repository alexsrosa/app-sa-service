package pt.app.sa.service.service

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.test.context.ActiveProfiles
import pt.app.sa.service.exception.RecordAlreadyExistsException
import pt.app.sa.service.repository.ClusterRepository
import pt.app.sa.service.repository.RegionRepository
import pt.app.sa.service.repository.StoreProductRepository
import pt.app.sa.service.repository.StoreRepository
import pt.app.sa.service.scheduler.data.*

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 27/09/2021 11:00
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StoreProductServiceTest @Autowired constructor(
    val storeService: StoreService,
    val clusterService: ClusterService,
    val regionService: RegionService,
    val storeProductService: StoreProductService,
    val clusterRepository: ClusterRepository,
    val regionRepository: RegionRepository,
    val storeRepository: StoreRepository,
    val storeProductRepository: StoreProductRepository,
    val cacheManager: CacheManager
) {

    @BeforeAll
    fun setUp() {
        clusterService.save(ClusterData("cluster1"))
        regionService.save(RegionData("region1", "type", "cluster1"))
        regionService.save(RegionData("region2", "type", "cluster1"))
        storeService.save(StoreData("store1", "theme", "region1"))
        storeService.save(StoreData("store2", "theme", "region2"))
    }

    @BeforeEach
    fun setUpEach() {
        storeProductRepository.deleteAll()
        cacheManager.getCache("storeProductFindByProductAndSeasonAndStore")?.clear()
    }

    @AfterAll
    fun destroy() {
        storeProductRepository.deleteAll()
        storeRepository.deleteAll()
        regionRepository.deleteAll()
        clusterRepository.deleteAll()
    }

    @Test
    fun `When save a new store and try another the same name Then save only one`() {

        val storeProductData = StoreProductData("product", "store1", "season")
        val savedOne = storeProductService.save(storeProductData)
        assertNotNull(savedOne)

        assertThrows(
            RecordAlreadyExistsException::class.java
        ) { storeProductService.save(storeProductData) }
    }

    @Test
    fun `When findByName Then return store`() {
        val storeProductData = StoreProductData("product", "store1", "season")
        val store1 = storeService.findByName("store1")
        val findByNameButNotExists =
            store1?.let { storeProductService.findByProductAndSeasonAndStore(storeProductData, it) }
        assertNull(findByNameButNotExists)

        val savedOne = storeProductService.save(storeProductData)
        assertNotNull(savedOne)

        val findByNameButExists =
            store1?.let { storeProductService.findByProductAndSeasonAndStore(storeProductData, it) }
        assertNotNull(findByNameButExists)
    }

    @Test
    fun `When save all new store product and update if necessary Then save all`() {

        val mutableListOf = mutableListOf<StoreProductData>()

        var i = 1
        val total = 1000
        while (i <= total) {
            val storeProductData = StoreProductData("product$i", "store1", "SA")
            mutableListOf.add(storeProductData)
            i++
        }
        storeProductService.saveAll(mutableListOf)

        while (i <= total * 2) {
            val storeProductData = StoreProductData("product$i", "store1", "SA")
            mutableListOf.add(storeProductData)
            i++
        }

        storeProductService.saveAll(mutableListOf)

        val findAll = storeProductService.findAll()
        assertEquals(total * 2, findAll.size)
    }
}