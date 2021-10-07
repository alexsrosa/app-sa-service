package pt.app.sa.service.repository.specification

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import pt.app.sa.service.controller.dto.FilterData
import pt.app.sa.service.controller.dto.FiltersEnum
import pt.app.sa.service.model.*
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 28/09/2021 15:13
 */
object StoreSpecification {

    private val logger: Logger = LoggerFactory.getLogger(StoreSpecification::class.java)

    fun filter(filters: List<FilterData>): Specification<StoreEntity> {
        return Specification { root, query, cb ->
            val predicates = mutableListOf<Predicate>()

            filters.forEach lit@{

                if(it.values.isEmpty()) return@lit

                when (FiltersEnum.valueOfWithTry(it.id) ?: return@lit) {
                    FiltersEnum.SEASON -> {
                        predicates.add(
                            cb.`in`(
                                root.join<StoreEntity, StoreProductEntity>(StoreEntity_.STORE_PRODUCT)
                                    .get<Any>(StoreProductEntity_.SEASON)
                            )
                                .value(it.values)
                        )
                    }

                    FiltersEnum.CLUSTER -> {
                        predicates.add(
                            cb.`in`(
                                root.join(StoreEntity_.region, JoinType.INNER)
                                    .join(RegionEntity_.clusters, JoinType.INNER)
                                    .get<Any>(ClusterEntity_.NAME)
                            )
                                .value(it.values)
                        )
                    }

                    FiltersEnum.REGION -> {
                        predicates.add(
                            cb.`in`(
                                root.join<StoreEntity, RegionEntity>(StoreEntity_.REGION)
                                    .get<Any>(RegionEntity_.NAME)
                            )
                                .value(it.values)
                        )
                    }

                    FiltersEnum.REGION_TYPE -> {
                        predicates.add(
                            cb.`in`(
                                root.join<StoreEntity, RegionEntity>(StoreEntity_.REGION).get<Any>(RegionEntity_.TYPE)
                            )
                                .value(it.values)
                        )
                    }

                    FiltersEnum.PRODUCT_MODEL -> {
                        predicates.add(
                            cb.`in`(
                                root.join<StoreEntity, StoreProductEntity>(StoreEntity_.STORE_PRODUCT)
                                    .get<Any>(StoreProductEntity_.PRODUCT)
                            )
                                .value(it.values)
                        )
                    }

                    FiltersEnum.STORE_NAME -> {
                        predicates.add(
                            cb.and(root.get(StoreEntity_.nameAlias).`in`(it.values))
                        )
                    }

                    FiltersEnum.STORE_THEME -> {
                        predicates.add(
                            cb.and(root.get(StoreEntity_.theme).`in`(it.values))
                        )
                    }

                    else -> logger.info("Filter for Store Not Found")
                }
            }

            query.distinct(true)
            cb.and(*predicates.toTypedArray())
        }
    }
}