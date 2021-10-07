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
class FiltersBySeasonControllerTest @Autowired constructor(val mockMvc: MockMvc) {

    val mapper = jacksonObjectMapper()

    @Test
    fun `When get filters season without filters Then return all seasons`() {
        mockMvc.perform(
            post("/filters/SEASON?page=0")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(6))
            .andExpect(jsonPath("$.[0]").value("S17"))
            .andExpect(jsonPath("$.[2]").value("S19"))
            .andExpect(jsonPath("$.[5]").value("S22"))

        mockMvc.perform(
            post("/filters/SEASON?page=0")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("Season", listOf())))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(6))
            .andExpect(jsonPath("$.[0]").value("S17"))
            .andExpect(jsonPath("$.[2]").value("S19"))
            .andExpect(jsonPath("$.[5]").value("S22"))
    }

    @Test
    fun `When get filters season by season Then return products by season filtered`() {
        mockMvc.perform(
            post("/filters/SEASON?page=0")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("Season", listOf("S18"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(1))
    }

    @Test
    fun `When get filters season by cluster Then return season by cluster filtered`() {
        mockMvc.perform(
            post("/filters/SEASON?page=0")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("cluster", listOf("USA"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(0))

        mockMvc.perform(
            post("/filters/SEASON?page=0")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("cluster", listOf("Europe"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(4))
            .andExpect(jsonPath("$.[0]").value("S17"))
            .andExpect(jsonPath("$.[3]").value("S22"))
    }

    @Test
    fun `When get filters season by region Then return season by cluster filtered`() {
        mockMvc.perform(
            post("/filters/SEASON?page=0")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("region", listOf("North EU"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(4))
            .andExpect(jsonPath("$.[0]").value("S17"))
            .andExpect(jsonPath("$.[3]").value("S22"))

        mockMvc.perform(
            post("/filters/SEASON?page=0")
                .content(
                    mapper.writeValueAsString(
                        FiltersData(
                            listOf(
                                FilterData("cluster", listOf("Europe")),
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
            .andExpect(jsonPath("$.length()").value(4))
            .andExpect(jsonPath("$.[0]").value("S17"))
            .andExpect(jsonPath("$.[3]").value("S22"))
    }

    @Test
    fun `When get filters season by product Then return season by product filtered`() {
        mockMvc.perform(
            post("/filters/SEASON?page=0")
                .content(
                    mapper.writeValueAsString(
                        FiltersData(
                            listOf(
                                FilterData(
                                    "product_model", listOf("PD_WATCHMAKER")
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
            .andExpect(jsonPath("$.[0]").value("S18"))
    }

    @Test
    fun `When get filters store by product Then return season by store filtered`() {
        mockMvc.perform(
            post("/filters/SEASON?page=0")
                .content(
                    mapper.writeValueAsString(
                        FiltersData(
                            listOf(
                                FilterData(
                                    "store_name", listOf("A faucial store")
                                ),
                                FilterData(
                                    "store_theme", listOf("What an a wigless Store!")
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
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$.[0]").value("S18"))
            .andExpect(jsonPath("$.[1]").value("S19"))
    }
}