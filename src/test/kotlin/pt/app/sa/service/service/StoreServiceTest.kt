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
import pt.app.sa.service.repository.StoreRepository
import pt.app.sa.service.scheduler.data.ClusterData
import pt.app.sa.service.scheduler.data.RegionData
import pt.app.sa.service.scheduler.data.StoreData

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 27/09/2021 11:00
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StoreServiceTest @Autowired constructor(
    val storeService: StoreService,
    val clusterService: ClusterService,
    val regionService: RegionService,
    val clusterRepository: ClusterRepository,
    val regionRepository: RegionRepository,
    val storeRepository: StoreRepository,
    val cacheManager: CacheManager
) {

    @BeforeAll
    fun setUp() {
        clusterService.save(ClusterData("cluster1"))
        regionService.save(RegionData("region1", "type", "cluster1"))
        regionService.save(RegionData("region2", "type", "cluster1"))
    }

    @BeforeEach
    fun setUpEach() {
        storeRepository.deleteAll()
        cacheManager.getCache("storeFindByName")?.clear()
    }

    @AfterAll
    fun destroy() {
        storeRepository.deleteAll()
        regionRepository.deleteAll()
        clusterRepository.deleteAll()
    }

    @Test
    fun `When save a new store and try another the same name Then save only one`() {

        val storeData = StoreData("store", "theme", "region1")
        val savedOne = storeService.save(storeData)
        assertNotNull(savedOne)

        val savedTwo = storeService.save(storeData)
        assertNull(savedTwo)
    }

    @Test
    fun `When save a new store and update before the same name Then save only one`() {

        val storeData = StoreData("store", "theme", "region1")
        storeService.save(storeData)
        storeData.theme = "themeChanged"
        storeData.region = "region2"

        val savedTwo = storeService.save(storeData)
        assertNotNull(savedTwo)
        assertEquals(storeData.theme, savedTwo?.theme)
        assertEquals(storeData.region, savedTwo?.region?.name)
    }

    @Test
    fun `When findByName Then return store`() {

        val findByNameButNotExists = storeService.findByName("store")
        assertNull(findByNameButNotExists)

        val storeData = StoreData("store", "theme", "region1")
        val saved = storeService.save(storeData)
        assertNotNull(saved)

        val findByNameButExists = storeService.findByName("store")
        assertNotNull(findByNameButExists)
    }

    @Test
    fun `When findByAll Then return all stores`() {

        var i = 1
        val total = 100
        while (i <= total) {
            storeService.save(StoreData("store$i", "theme", "region1"))
            i++
        }

        i = 1
        while (i <= total) {
            storeService.save(StoreData("store$i", "theme", "region1"))
            i++
        }

        val findAll = storeService.findAll()
        assertEquals(total, findAll.size)
    }

    @Test
    fun `When 3findByAll Then return all stores`(){
        var init = 18076
        val finish = 60000
        while(init >= finish){
            init++
            assertEquals(init, discoverPage(init))
        }
    }

    fun discoverPage(rangeToGus: Int): Int{
        val nowLastPage = 18076 + 1
        var execute = true
        var rangeSearch = 1000
        var pageSearch = nowLastPage + rangeSearch
        while(execute){
            if(pageSearch <= rangeToGus) {
                pageSearch += rangeSearch
            }else{
                pageSearch -= rangeSearch
                rangeSearch /= 2
            }

            if(rangeSearch == 1){
                execute = false
            }
        }

        return pageSearch
    }
}