package pt.app.sa.service.usecase

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pt.app.sa.service.repository.ProductRepository
import java.io.File
import java.util.*

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 27/09/2021 08:56
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoadProductsUseCaseTest @Autowired constructor(
    val loadProductsUseCase: LoadProductsUseCase,
    val productRepository: ProductRepository
) {

    @BeforeEach
    fun setUp() {
        productRepository.deleteAll()
    }

    @AfterAll
    fun destroy() {
        productRepository.deleteAll()
    }

    @Test
    fun `When call convert file string to list Then return list`() {
        val toString = File(Objects.requireNonNull(javaClass.classLoader.getResource("csv/product-data.csv")).file)
            .inputStream().readBytes().toString(Charsets.UTF_8)

        val convertList = loadProductsUseCase.convert(toString)
        assertTrue(convertList.isNotEmpty())
        assertEquals(1435, convertList.size)
    }

    @Test
    fun `When call convert and saveAll rows to list Then save all`() {
        val toString = File(Objects.requireNonNull(javaClass.classLoader.getResource("csv/product-data.csv")).file)
            .inputStream().readBytes().toString(Charsets.UTF_8)

        val convertList = loadProductsUseCase.convert(toString)
        loadProductsUseCase.saveAll(convertList)
        val findAll = productRepository.findAll()
        assertEquals(1435, findAll.size)
    }
}