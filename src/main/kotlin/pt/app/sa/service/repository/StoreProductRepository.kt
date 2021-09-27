package pt.app.sa.service.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.app.sa.service.model.StoreEntity
import pt.app.sa.service.model.StoreProductEntity

/**
 * Class that contains database operations of the Store Products.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 12:40
 */
interface StoreProductRepository : JpaRepository<StoreProductEntity, Long> {

    fun findByProductAndSeasonAndStore(product: String, season: String, store: StoreEntity): StoreProductEntity?
}