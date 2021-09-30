package pt.app.sa.service.model

import javax.persistence.*

/**
 * Entity that has store information.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 15:29
 */
@Entity
@Table(
    name = "stores",
    indexes = [Index(name = "index_store_name_unique", columnList = "name", unique = true)]
)
class StoreEntity(
    var name: String,
    var theme: String,
    @ManyToOne var region: RegionEntity,

    ) : BaseEntity() {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "store")
    val storeProduct = mutableListOf<StoreProductEntity>()

    override fun toString(): String = "$name, $theme, $region"
}