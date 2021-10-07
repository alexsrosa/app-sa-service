package pt.app.sa.service.model

import java.util.*
import javax.persistence.*

/**
 * Entity that has store products information.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 12:33
 */
@Entity
@Table(
    name = "store_products",
    indexes = [
        Index(name = "index_store_products_unique", columnList = "product, season, store_id", unique = true),
        Index(name = "index_store_products_hash_id", columnList = "hash_id")
    ]
)
class StoreProductEntity(
    var product: String,
    var season: String,
    @ManyToOne var store: StoreEntity
) : BaseEntity() {
    @Column(name = "hash_id")
    var hashId: Int = Objects.hash(product, season, store.name)

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "model")
    val products = mutableListOf<ProductEntity>()

    override fun toString(): String = "$product, $season, $store"
}