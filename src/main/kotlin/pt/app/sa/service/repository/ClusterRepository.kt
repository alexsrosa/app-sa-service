package pt.app.sa.service.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.app.sa.service.model.ClusterEntity

/**
 * Class that contains database operations of the clusters.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 15:29
 */
interface ClusterRepository : JpaRepository<ClusterEntity, Long> {

}