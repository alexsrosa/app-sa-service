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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import pt.app.sa.service.controller.dto.FilterData
import pt.app.sa.service.controller.dto.FiltersData
import pt.app.sa.service.repository.ClusterRepository
import pt.app.sa.service.repository.RegionRepository
import pt.app.sa.service.repository.StoreRepository
import pt.app.sa.service.schedule.data.ClusterData
import pt.app.sa.service.schedule.data.RegionData
import pt.app.sa.service.schedule.data.StoreData
import pt.app.sa.service.service.ClusterService
import pt.app.sa.service.service.RegionService
import pt.app.sa.service.service.StoreService

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 28/09/2021 00:17
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FiltersControllerTest(
    @Autowired
    val mockMvc: MockMvc,
    @Autowired
    val regionService: RegionService,
    @Autowired
    val clusterService: ClusterService,
    @Autowired
    val storeService: StoreService,
    @Autowired
    val clusterRepository: ClusterRepository,
    @Autowired
    val regionRepository: RegionRepository,
    @Autowired
    val storeRepository: StoreRepository,
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
    }

    @AfterAll
    fun destroy() {
        storeRepository.deleteAll()
        regionRepository.deleteAll()
        clusterRepository.deleteAll()
    }

    @Test
    fun `When get filters Then return all available filters`() {
        mockMvc.perform(
            get("/filters")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun `When get filters cluster Then return cluster filtered`() {
        mockMvc.perform(
            post("/filters/CLUSTER")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("Cluster", listOf("cluster1"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].name").value("cluster1"))
    }

    @Test
    fun `When get filters Region Then return Region filtered`() {
        mockMvc.perform(
            post("/filters/REGION")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("Cluster", listOf("cluster1"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].name").value("region1"))
            .andExpect(jsonPath("\$.[0].type").value("type1"))
            .andExpect(jsonPath("\$.[1].name").value("region2"))
            .andExpect(jsonPath("\$.[1].type").value("type2"))

        mockMvc.perform(
            post("/filters/REGION")
                .content(mapper.writeValueAsString(
                    FiltersData(listOf(FilterData("Cluster", listOf("cluster2"))
                    , FilterData("Region_Type", listOf("type3"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].name").value("region3"))
            .andExpect(jsonPath("\$.[0].type").value("type3"))
    }

    @Test
    fun `When get filters stores Then return stores filtered`() {
        mockMvc.perform(
            post("/filters/STORE_NAME")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("REGION", listOf("region1"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].name").value("store1"))
            .andExpect(jsonPath("\$.[0].theme").value("theme1"))
            .andExpect(jsonPath("\$.[0].region").value("region1"))

        mockMvc.perform(
            post("/filters/STORE_NAME")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("STORE_NAME", listOf("store1"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].name").value("store1"))
            .andExpect(jsonPath("\$.[0].theme").value("theme1"))
            .andExpect(jsonPath("\$.[0].region").value("region1"))

    }
}