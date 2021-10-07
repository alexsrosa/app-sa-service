package pt.app.sa.service.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import pt.app.sa.service.controller.dto.FilterData
import pt.app.sa.service.controller.dto.FiltersData

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 28/09/2021 00:17
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql("/sql/import_test_data.sql")
class FiltersByClusterControllerTest @Autowired constructor(val mockMvc: MockMvc) {

    val mapper = jacksonObjectMapper()

    @Test
    fun `When get filters season without filters Then return all seasons`() {
        mockMvc.perform(
            post("/filters/CLUSTER?page=0")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$.[0]").value("Asia"))
            .andExpect(jsonPath("$.[1]").value("Europe"))
            .andExpect(jsonPath("$.[2]").value("USA"))

        mockMvc.perform(
            post("/filters/CLUSTER?page=0")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("Cluster", listOf())))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$.[0]").value("Asia"))
            .andExpect(jsonPath("$.[1]").value("Europe"))
            .andExpect(jsonPath("$.[2]").value("USA"))
    }

    @Test
    fun `When get filters cluster Then return cluster filtered`() {
        mockMvc.perform(
            post("/filters/CLUSTER")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("Cluster", listOf("Europe"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0]").value("Europe"))

        mockMvc.perform(
            post("/filters/CLUSTER?page=1")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("Cluster", listOf("Europe2"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `When get filters cluster by region Then return cluster by region filtered`() {
        mockMvc.perform(
            post("/filters/CLUSTER?page=0")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("region", listOf("North EU"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$.[0]").value("Europe"))

        mockMvc.perform(
            post("/filters/CLUSTER?page=0")
                .content(
                    mapper.writeValueAsString(
                        FiltersData(
                            listOf(
                                FilterData("region", listOf("North EU")),
                                FilterData("region_type", listOf("XY"))
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
            .andExpect(jsonPath("$.[0]").value("Europe"))
    }

    @Test
    fun `When get filters cluster by store Then return cluster by store filtered`() {
        mockMvc.perform(
            post("/filters/CLUSTER?page=0")
                .content(
                    mapper.writeValueAsString(
                        FiltersData(
                            listOf(
                                FilterData(
                                    "store_name", listOf("A quiet store")
                                ),
                                FilterData(
                                    "store_theme", listOf("A conjoined store focused on an accordion")
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
            .andExpect(jsonPath("$.[0]").value("Asia"))
    }
}