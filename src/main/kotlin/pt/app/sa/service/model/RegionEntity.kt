package pt.app.sa.service.model

import javax.persistence.*

/**
 * Entity that has region information.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 15:29
 */
@Entity
@Table(
    name = "regions",
    indexes = [Index(name = "index_regions_name_unique", columnList = "name", unique = true)]
)
class RegionEntity(
    @Column(name = "name", unique = true)
    var name: String,
    var type: String,
    @ManyToOne var clusters: ClusterEntity
) : BaseEntity() {
    override fun toString(): String = "$name, $type, $clusters"
}