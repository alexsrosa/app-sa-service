package pt.app.sa.service.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.PagingAndSortingRepository
import pt.app.sa.service.model.ProductEntity

/**
 * Class that contains database operations of the Products.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 15:30
 */
interface ProductRepository :
    PagingAndSortingRepository<ProductEntity, Long>,
    JpaSpecificationExecutor<ProductEntity>,
    JpaRepository<ProductEntity, Long> {

    fun findByHashIdIn(ids: Set<Int>): Set<ProductEntity>
    fun findBySeasonAndSku(session: String, sku: String): ProductEntity?
}