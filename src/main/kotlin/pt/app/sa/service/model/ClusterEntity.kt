package pt.app.sa.service.model

import javax.persistence.*

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clusters")
    val regions = mutableListOf<RegionEntity>()

    override fun toString(): String = name
}