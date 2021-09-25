package pt.app.sa.service.model

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table

/**
 * Entity that has store information.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 15:29
 */
@Table(name = "stores")
@Entity
class StoreEntity(
    var name: String,
    var theme: String,
    @ManyToOne var region: RegionEntity
) : BaseEntity()