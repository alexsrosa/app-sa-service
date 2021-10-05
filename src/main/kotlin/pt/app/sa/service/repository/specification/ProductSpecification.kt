package pt.app.sa.service.repository.specification

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import pt.app.sa.service.controller.dto.FilterData
import pt.app.sa.service.controller.dto.FiltersEnum
import pt.app.sa.service.model.ProductEntity
import pt.app.sa.service.model.ProductEntity_
import javax.persistence.criteria.Predicate

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 28/09/2021 15:13
 */
object ProductSpecification {

    private val logger: Logger = LoggerFactory.getLogger(ProductSpecification::class.java)

    fun filter(filters: List<FilterData>): Specification<ProductEntity> {
        return Specification { root, query, cb ->
            val predicates = mutableListOf<Predicate>()

            filters.forEach lit@{

                if (it.values.isEmpty()) return@lit

                when (FiltersEnum.valueOfWithTry(it.id) ?: return@lit) {

                    FiltersEnum.SEASON -> {
                        predicates.add(
                            cb.and(root.get(ProductEntity_.season).`in`(it.values))
                        )
                    }

                    FiltersEnum.PRODUCT_MODEL -> {
                        predicates.add(
                            cb.and(root.get(ProductEntity_.model).`in`(it.values))
                        )
                    }

                    FiltersEnum.PRODUCT_SIZE -> {
                        predicates.add(
                            cb.and(root.get(ProductEntity_.size).`in`(it.values))
                        )
                    }

                    FiltersEnum.SKU -> {
                        predicates.add(
                            cb.or(cb.like(root.get(ProductEntity_.sku), "%${it.values[0]}%"))
                        )
                    }

                    else -> logger.info("Filter for Product Not Found")
                }
            }

            query.distinct(true)
            cb.and(*predicates.toTypedArray())
        }
    }
}