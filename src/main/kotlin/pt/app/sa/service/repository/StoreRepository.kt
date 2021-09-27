package pt.app.sa.service.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.app.sa.service.model.StoreEntity

/**
 * Class that contains database operations of the Stores.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 15:31
 */
interface StoreRepository : JpaRepository<StoreEntity, Long> {

    fun findByName(name: String): StoreEntity?
}