package pt.app.sa.service.service

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.test.context.ActiveProfiles
import pt.app.sa.service.repository.ProductRepository
import pt.app.sa.service.scheduler.data.ProductData

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 27/09/2021 08:56
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceTest @Autowired constructor(
    val productService: ProductService,
    val productRepository: ProductRepository,
    val cacheManager: CacheManager
) {

    @BeforeEach
    fun setUp() {
        productRepository.deleteAll()
        cacheManager.getCache("productFindBySeasonAndSku")?.clear()
    }

    @AfterAll
    fun destroy() {
        productRepository.deleteAll()
    }

    @Test
    fun `When save a new product and try another the same name Then save only one`() {
        val productData = ProductData("season1", "model", "size", "sku", "1000", "description")
        val savedOne = productService.save(productData)
        Assertions.assertNotNull(savedOne)

        val savedTwo = productService.save(productData)
        Assertions.assertNull(savedTwo)
    }

    @Test
    fun `When save a new product and update before the same name Then save only one`() {

        val productData = ProductData("season1", "model", "size", "sku", "1000", "description")
        productService.save(productData)
        productData.model = "model2"
        productData.size = "size2"
        productData.ean = "10003"
        productData.description = "description2"

        val savedTwo = productService.save(productData)
        Assertions.assertNotNull(savedTwo)
        Assertions.assertEquals(productData.model, savedTwo?.model)
        Assertions.assertEquals(productData.size, savedTwo?.size)
        Assertions.assertEquals(productData.ean.toLong(), savedTwo?.ean)
        Assertions.assertEquals(productData.description, savedTwo?.description)
    }

    @Test
    fun `When findBySku Then return product`() {

        val findByNameButNotExists = productService.findBySeasonAndSku("season1", "sku")
        Assertions.assertNull(findByNameButNotExists)

        val productData = ProductData("season1", "model", "size", "sku", "1000", "description")
        val saved = productService.save(productData)
        Assertions.assertNotNull(saved)

        val findByNameButExists = productService.findBySeasonAndSku("season1", "sku")
        Assertions.assertNotNull(findByNameButExists)
    }

    @Test
    fun `When findByAll Then return all products`() {

        var i = 1
        val total = 1000
        while (i <= total) {
            productService.save(ProductData("season1", "model", "size", "sku$i", "1000", "description"))
            i++
        }

        i = 1
        while (i <= total) {
            productService.save(ProductData("season1", "model", "size", "sku$i", "1000", "description"))
            i++
        }

        val findAll = productRepository.findAll()
        Assertions.assertEquals(total, findAll.size)
    }

    @Test
    fun `When save all new product and update if necessary Then save all`() {

        val mutableListOf = mutableListOf<ProductData>()

        var i = 1
        val total = 1000
        while (i <= total) {
            val productData = ProductData("season$i", "model", "size", "sku$i", "1000", "description")
            mutableListOf.add(productData)
            i++
        }
        productService.saveAll(mutableListOf)

        while (i <= total * 2) {
            val productData = ProductData("season$i", "model", "size", "sku$i", "1000", "description")
            mutableListOf.add(productData)
            i++
        }

        productService.saveAll(mutableListOf)

        val findAll = productRepository.findAll()
        Assertions.assertEquals(total * 2, findAll.size)
    }
}