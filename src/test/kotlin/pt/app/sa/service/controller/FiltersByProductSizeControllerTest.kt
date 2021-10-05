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
class FiltersByProductSizeControllerTest @Autowired constructor(val mockMvc: MockMvc) {

    val mapper = jacksonObjectMapper()

    @Test
    fun `When get filters product size without filters Then return all products models`() {
        mockMvc.perform(
            post("/filters/PRODUCT_SIZE?page=0")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(4))
            .andExpect(jsonPath("$.[0]").value("L"))
            .andExpect(jsonPath("$.[3]").value("XXS"))

        mockMvc.perform(
            post("/filters/PRODUCT_SIZE?page=0")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("Season", listOf())))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(4))
            .andExpect(jsonPath("$.[0]").value("L"))
            .andExpect(jsonPath("$.[3]").value("XXS"))
    }

    @Test
    fun `When get filters product size by season Then return products by season filtered`() {
        mockMvc.perform(
            post("/filters/PRODUCT_SIZE?page=0")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("Season", listOf("S19"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$.[0]").value("XS"))
    }

    @Test
    fun `When get filters product model by model Then return season by model filtered`() {
        mockMvc.perform(
            post("/filters/PRODUCT_SIZE?page=0")
                .content(
                    mapper.writeValueAsString(
                        FiltersData(
                            listOf(
                                FilterData(
                                    "PRODUCT_MODEL",
                                    listOf("PD_BERRY", "PD_WATCHMAKER")
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
            .andExpect(jsonPath("$.[0]").value("XS"))
            .andExpect(jsonPath("$.[1]").value("XXS"))
    }

    @Test
    fun `When get filters product by size Then return product by size filtered`() {
        mockMvc.perform(
            post("/filters/PRODUCT_SIZE?page=0")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("product_size", listOf("XXS"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$.[0]").value("XXS"))
    }

    @Test
    fun `When get filters product by sku Then return product by sku filtered`() {
        mockMvc.perform(
            post("/filters/PRODUCT_SIZE?page=0")
                .content(mapper.writeValueAsString(FiltersData(listOf(FilterData("sku", listOf("000001"))))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$.[0]").value("XS"))
            .andExpect(jsonPath("$.[1]").value("XXS"))
    }
}