package pt.app.sa.service.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import pt.app.sa.service.model.*

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 16:19
 */
@ActiveProfiles("test")
@DataJpaTest
class ProductsRepositoryTest @Autowired constructor(
    val productRepository: ProductRepository
) {

    @Test
    fun `When create new product then save on database`() {

        val product = ProductEntity("season", "model", "size", "sku", 1000L, "description")
        val savedProduct = productRepository.save(product)

        val found = savedProduct.id?.let { productRepository.findById(it) }
        assertThat(found?.get() ?: "").isEqualTo(savedProduct)
    }

    @Test
    fun `When findById on product then return product`() {

        val product = ProductEntity("season", "model", "size", "sku", 1000L, "description")
        val savedProduct = productRepository.save(product)

        val found = savedProduct.id?.let { productRepository.findById(it) }
        val foundProduct = found?.get() ?: product
        assertThat(product.season).isEqualTo(foundProduct.season)
        assertThat(product.model).isEqualTo(foundProduct.model)
        assertThat(product.size).isEqualTo(foundProduct.size)
        assertThat(product.sku).isEqualTo(foundProduct.sku)
        assertThat(product.ean).isEqualTo(foundProduct.ean)
        assertThat(product.description).isEqualTo(foundProduct.description)
    }

    @Test
    fun `When findAll on products then return all products`() {

        var index = 0
        val total = 10000
        while (index < total) {
            val product = ProductEntity(
                "season", "model$index", "size", "sku$index", 1000L, "description")
            productRepository.save(product)
            index++
        }

        val findAll = productRepository.findAll()

        assertThat(findAll.size).isEqualTo(total)
    }
}