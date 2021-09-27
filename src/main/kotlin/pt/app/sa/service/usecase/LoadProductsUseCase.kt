package pt.app.sa.service.usecase

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import pt.app.sa.service.schedule.data.ProductData
import pt.app.sa.service.service.ProductService
import pt.app.sa.service.utils.LoadByRequestReceiveFileType
import pt.app.sa.service.utils.RestTemplateUtils

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 20:11
 */
@Component
class LoadProductsUseCase(
    val productService: ProductService,
    val restTemplateUtils: RestTemplateUtils<String>
) : LoadByRequestReceiveFileType<String, ProductData> {

    override val logger: Logger = LoggerFactory.getLogger(LoadProductsUseCase::class.java)

    @Value("\${externalDataLoad.baseUri}")
    lateinit var baseUri: String

    @Value("\${externalDataLoad.endpoints.products}")
    lateinit var endpoint: String

    @Value("\${externalDataLoad.endpoints.products.errorsAccepted:10}")
    override var errorsAccepted: Int = 0

    override fun request(): ResponseEntity<String> {
        return restTemplateUtils.get("$baseUri$endpoint", object : ParameterizedTypeReference<String>() {})
    }

    override fun convert(file: String): MutableList<ProductData> {
        val listOfProducts = mutableListOf<ProductData>()

        val allRows = file.split("\n")
        val allRowsWithoutHeader = allRows.drop(1)
        val headers = allRows[0].split(",")

        for (line in allRowsWithoutHeader) {
            val productData = ProductData()
            line.split(",")
                .map { it.replace("\"", "") }
                .forEachIndexed { sequence, column ->
                    when (headers[sequence].replace("\"", "")) {
                        "season" -> productData.season = column
                        "model" -> productData.model = column
                        "size" -> productData.size = column
                        "sku" -> productData.sku = column
                        "ean" -> productData.ean = column
                        "description" -> productData.description = column
                    }
                }
            listOfProducts += productData
        }
        return listOfProducts
    }

    override fun saveAll(list: MutableList<ProductData>) {
        for (product in list) {
            val saved = productService.save(product)
            if (saved != null) {
                logger.info("The product $saved has been inserted/updated")
            }
        }
    }
}