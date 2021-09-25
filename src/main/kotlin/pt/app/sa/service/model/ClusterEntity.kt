package pt.app.sa.service.model

import javax.persistence.Entity
import javax.persistence.Table

/**
 * Entity that has cluster information.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 15:29
 */
@Entity
@Table(name = "clusters")
class ClusterEntity(
    var name: String
) : BaseEntity()