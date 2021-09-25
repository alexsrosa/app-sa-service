package pt.app.sa.service.model

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table

/**
 * Entity that has products information.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 15:29
 */
@Table(name = "products")
@Entity
class ProductEntity(
    var product: String,
    var season: String,
    @ManyToOne var store: StoreEntity
) : BaseEntity()