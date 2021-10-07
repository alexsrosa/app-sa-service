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
class FiltersByStoreThemeControllerTest @Autowired constructor(val mockMvc: MockMvc) {

    val mapper = jacksonObjectMapper()

    @Test
    fun `When get filters store name without filters Then return all stores`() {
        mockMvc.perform(
            post("/filters/STORE_THEME?page=0")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(10))
            .andExpect(jsonPath("$.[0]").value("A bootless store focused on a cougar"))
            .andExpect(jsonPath("$.[9]").value("What an an apish Store!"))

        mockMvc.perform(
            post("/filters/STORE_THEME?page=0")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("Season", listOf())))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(10))
            .andExpect(jsonPath("$.[0]").value("A bootless store focused on a cougar"))
            .andExpect(jsonPath("$.[9]").value("What an an apish Store!"))
    }

    @Test
    fun `When get filters store name by season Then return stores by season filtered`() {
        mockMvc.perform(
            post("/filters/STORE_THEME?page=0")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("Season", listOf("S18"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(4))
            .andExpect(jsonPath("$.[0]").value("A bootless store focused on a cougar"))
            .andExpect(jsonPath("$.[3]").value("What an a wigless Store!"))
    }

    @Test
    fun `When get filters store name by cluster Then return store name by cluster filtered`() {
        mockMvc.perform(
            post("/filters/STORE_THEME?page=0")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("cluster", listOf("USA"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(0))

        mockMvc.perform(
            post("/filters/STORE_THEME?page=0")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("cluster", listOf("Europe"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(4))
            .andExpect(jsonPath("$.[0]").value("A mangy store focused on a prose"))
            .andExpect(jsonPath("$.[3]").value("What an an apish Store!"))
    }

    @Test
    fun `When get filters season by region Then return season by cluster filtered`() {
        mockMvc.perform(
            post("/filters/STORE_THEME?page=0")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("region", listOf("North EU"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$.[0]").value("What an a shaky Store!"))
            .andExpect(jsonPath("$.[2]").value("What an an apish Store!"))

        mockMvc.perform(
            post("/filters/STORE_THEME?page=0")
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
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$.[0]").value("What an a shaky Store!"))
            .andExpect(jsonPath("$.[2]").value("What an an apish Store!"))
    }

    @Test
    fun `When get filters season by product Then return season by product filtered`() {
        mockMvc.perform(
            post("/filters/STORE_THEME?page=0")
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
            .andExpect(jsonPath("$.[0]").value("A bootless store focused on a cougar"))
    }

    @Test
    fun `When get filters store by product Then return season by store filtered`() {
        mockMvc.perform(
            post("/filters/STORE_THEME?page=0")
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
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$.[0]").value("What an a wigless Store!"))
    }
}