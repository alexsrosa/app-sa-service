package pt.app.sa.service.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.PagingAndSortingRepository
import pt.app.sa.service.model.StoreEntity
import pt.app.sa.service.model.StoreProductEntity

/**
 * Class that contains database operations of the Store Products.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 12:40
 */
interface StoreProductRepository :
    PagingAndSortingRepository<StoreProductEntity, Long>,
    JpaSpecificationExecutor<StoreProductEntity>,
    JpaRepository<StoreProductEntity, Long> {

    fun findByProductAndSeasonAndStore(product: String, season: String, store: StoreEntity): StoreProductEntity?
    fun findByHashIdIn(ids: Set<Int>): Set<StoreProductEntity>
}