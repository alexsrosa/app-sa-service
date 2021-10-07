package pt.app.sa.service.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import pt.app.sa.service.controller.dto.FilterData
import pt.app.sa.service.controller.dto.FiltersData
import pt.app.sa.service.repository.ClusterRepository
import pt.app.sa.service.repository.RegionRepository
import pt.app.sa.service.repository.StoreProductRepository
import pt.app.sa.service.repository.StoreRepository
import pt.app.sa.service.scheduler.data.ClusterData
import pt.app.sa.service.scheduler.data.RegionData
import pt.app.sa.service.scheduler.data.StoreData
import pt.app.sa.service.scheduler.data.StoreProductData
import pt.app.sa.service.service.ClusterService
import pt.app.sa.service.service.RegionService
import pt.app.sa.service.service.StoreProductService
import pt.app.sa.service.service.StoreService

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 28/09/2021 00:17
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FiltersByRegionControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val regionService: RegionService,
    val clusterService: ClusterService,
    val storeService: StoreService,
    val storeProductService: StoreProductService,
    val clusterRepository: ClusterRepository,
    val regionRepository: RegionRepository,
    val storeRepository: StoreRepository,
    val storeProductRepository: StoreProductRepository
) {

    val mapper = jacksonObjectMapper()

    @BeforeAll
    fun setUp() {
        clusterService.save(ClusterData("cluster1"))
        clusterService.save(ClusterData("cluster2"))
        regionService.save(RegionData("region1", "type1", "cluster1"))
        regionService.save(RegionData("region2", "type2", "cluster1"))
        regionService.save(RegionData("region3", "type3", "cluster2"))
        storeService.save(StoreData("store1", "theme1", "region1"))
        storeService.save(StoreData("store2", "theme2", "region2"))
        storeService.save(StoreData("store3", "theme3", "region2"))
        storeProductService.save(StoreProductData("product1", "store1", "S20"))
        storeProductService.save(StoreProductData("product1", "store1", "S21"))
        storeProductService.save(StoreProductData("product2", "store1", "S21"))
        storeProductService.save(StoreProductData("product1", "store2", "S21"))
        storeProductService.save(StoreProductData("product2", "store2", "S21"))
    }

    @AfterAll
    fun destroy() {
        storeProductRepository.deleteAll()
        storeRepository.deleteAll()
        regionRepository.deleteAll()
        clusterRepository.deleteAll()
    }

    @Test
    fun `When get filters Region by cluster Then return Region filtered`() {
        mockMvc.perform(
            post("/filters/REGION")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("Cluster", listOf("cluster1"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$.[0]").value("region1"))
            .andExpect(jsonPath("$.[1]").value("region2"))
    }

    @Test
    fun `When get filters Region by cluster and Region Type Then return Region filtered`() {
        mockMvc.perform(
            post("/filters/REGION")
                .content(
                    mapper.writeValueAsString(
                        FiltersData(
                            listOf(
                                FilterData("Cluster", listOf("cluster2")),
                                FilterData("Region_Type", listOf("type3"))
                            )
                        )
                    )
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$.[0]").value("region3"))
    }

    @Test
    fun `When get filters Region by region name and store name Then return Region filtered`() {
        mockMvc.perform(
            post("/filters/REGION")
                .content(
                    mapper.writeValueAsString(
                        FiltersData(
                            listOf(
                                FilterData("REGION", listOf("region1", "region2")),
                                FilterData("STORE_NAME", listOf("store1"))
                            )
                        )
                    )
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$.[0]").value("region1"))
    }

    @Test
    fun `When get filters Region by region name and store theme Then return Region filtered`() {

        mockMvc.perform(
            post("/filters/REGION")
                .content(
                    mapper.writeValueAsString(
                        FiltersData(
                            listOf(
                                FilterData("REGION", listOf("region1", "region2")),
                                FilterData("STORE_THEME", listOf("theme1"))
                            )
                        )
                    )
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$.[0]").value("region1"))
    }

    @Test
    fun `When get filters Region by region name and Season Then return Region filtered`() {
        mockMvc.perform(
            post("/filters/REGION")
                .content(
                    mapper.writeValueAsString(
                        FiltersData(
                            listOf(
                                FilterData("REGION", listOf("region1", "region2")),
                                FilterData("SEASON", listOf("S20"))
                            )
                        )
                    )
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$.[0]").value("region1"))
    }
}