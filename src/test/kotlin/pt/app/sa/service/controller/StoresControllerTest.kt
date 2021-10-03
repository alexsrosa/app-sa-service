package pt.app.sa.service.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import pt.app.sa.service.controller.dto.FilterData
import pt.app.sa.service.controller.dto.FiltersData
import pt.app.sa.service.controller.dto.InStoreDto
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
class StoresControllerTest @Autowired constructor(
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
        storeService.save(StoreData("store4", "theme4", "region3"))
        storeProductService.save(StoreProductData("product", "store3", "season"))
    }

    @AfterAll
    fun destroy() {
        storeProductRepository.deleteAll()
        storeRepository.deleteAll()
        regionRepository.deleteAll()
        clusterRepository.deleteAll()
    }

    @Test
    fun `When post store with filters for season Then return store filtered`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/stores")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("Season", listOf("season"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].name").value("store3"))
            .andExpect(jsonPath("\$.[0].theme").value("theme3"))
            .andExpect(jsonPath("\$.[0].region").value("region2"))
            .andExpect(jsonPath("$.length()").value(1))
    }

    @Test
    fun `When post store with filters for season and region Then return not found store`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/stores")
                .content(
                    mapper.writeValueAsString(
                        FiltersData(
                            listOf(
                                FilterData(
                                    "Season", listOf("season")
                                ),
                                FilterData(
                                    "Region", listOf("region3")
                                )
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
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `When post store with filters for season and region Then return store filtered`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/stores")
                .content(
                    mapper.writeValueAsString(
                        FiltersData(
                            listOf(
                                FilterData(
                                    "Season", listOf("season")
                                ),
                                FilterData(
                                    "Region", listOf("region2")
                                )
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
            .andExpect(jsonPath("\$.[0].name").value("store3"))
            .andExpect(jsonPath("\$.[0].theme").value("theme3"))
            .andExpect(jsonPath("\$.[0].region").value("region2"))

    }

    @Test
    fun `When post store with filters for season and region and page 1 Then return any store`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/stores?page=1")
                .content(
                    mapper.writeValueAsString(
                        FiltersData(
                            listOf(
                                FilterData(
                                    "Season", listOf("season")
                                ),
                                FilterData(
                                    "Region", listOf("region2")
                                )
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
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `When patch store to change and name not found Then return bad request`() {
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/stores/xxxx")
                .content(
                    mapper.writeValueAsString(
                        InStoreDto("yyyy")
                    )
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Store xxxx Not Found"))
    }

    @Test
    fun `When patch store to change and not pass new name Then return bad Request`() {
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/stores/xxxx")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").isNotEmpty)
    }

    @Test
    fun `When patch store to change and name found Then return 200 ok`() {

        val storeChangeName = "store4 - Changed"
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/stores/store4")
                .content(
                    mapper.writeValueAsString(
                        InStoreDto(storeChangeName)
                    )
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())

        assertEquals(storeChangeName, storeService.findByNameAlias(storeChangeName)?.nameAlias)
    }
}