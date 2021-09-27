package pt.app.sa.service.model

import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table

/**
 * Entity that has products information.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 15:29
 */
@Entity
@Table(
    name = "products",
    indexes = [Index(name = "index_products_season_sku_unique", columnList = "season, sku", unique = true)]
)
class ProductEntity(
    var season: String,
    var model: String,
    var size: String,
    var sku: String,
    var ean: Long,
    var description: String
) : BaseEntity() {
    override fun toString(): String = "$season, $model, $size, $sku, $ean, $description"
}