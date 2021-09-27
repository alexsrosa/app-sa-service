package pt.app.sa.service.model

import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.ManyToOne
import javax.persistence.Table

/**
 * Entity that has store products information.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 12:33
 */
@Entity
@Table(
    name = "store_products",
    indexes = [Index(name = "index_store_products_unique", columnList = "product, season, store_id", unique = true)]
)
class StoreProductEntity(
    var product: String,
    var season: String,
    @ManyToOne var store: StoreEntity
) : BaseEntity() {
    override fun toString(): String = "$product, $season, $store"
}