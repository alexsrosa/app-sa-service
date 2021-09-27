package pt.app.sa.service.model

import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table

/**
 * Entity that has cluster information.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 15:29
 */
@Entity
@Table(
    name = "clusters",
    indexes = [Index(name = "index_clusters_name_unique", columnList = "name", unique = true)]
)
class ClusterEntity(
    var name: String
) : BaseEntity() {
    override fun toString(): String = name
}