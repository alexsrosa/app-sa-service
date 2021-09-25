package pt.app.sa.service.model

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table

/**
 * Entity that has region information.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 15:29
 */
@Table(name = "regions")
@Entity
class RegionEntity(
    var name: String,
    var type: String,
    @ManyToOne var clusters: ClusterEntity
) : BaseEntity()