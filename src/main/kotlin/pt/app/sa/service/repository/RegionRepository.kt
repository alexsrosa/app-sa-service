package pt.app.sa.service.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.app.sa.service.model.RegionEntity

/**
 * Class that contains database operations of the Regions.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 15:30
 */
interface RegionRepository : JpaRepository<RegionEntity, Long> {

}