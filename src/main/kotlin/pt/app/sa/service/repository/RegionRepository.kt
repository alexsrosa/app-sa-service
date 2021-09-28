package pt.app.sa.service.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.PagingAndSortingRepository
import pt.app.sa.service.model.RegionEntity

/**
 * Class that contains database operations of the Regions.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 15:30
 */
interface RegionRepository :
    PagingAndSortingRepository<RegionEntity, Long>,
    JpaSpecificationExecutor<RegionEntity>,
    JpaRepository<RegionEntity, Long> {

    fun findByName(name: String): RegionEntity?
}