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
object ClusterSpecification {

    private val logger: Logger = LoggerFactory.getLogger(ClusterSpecification::class.java)

    fun filter(filters: List<FilterData>): Specification<ClusterEntity> {
        return Specification { root, query, cb ->
            val predicates = mutableListOf<Predicate>()

            filters.forEach lit@{

                if (it.values.isEmpty()) return@lit

                when (FiltersEnum.valueOfWithTry(it.id) ?: return@lit) {
                    FiltersEnum.CLUSTER -> {
                        predicates.add(
                            cb.and(root.get(ClusterEntity_.name).`in`(it.values))
                        )
                    }

                    FiltersEnum.SEASON -> {
                        predicates.add(
                            cb.`in`(
                                root.join(ClusterEntity_.regions, JoinType.INNER)
                                    .join(RegionEntity_.stores, JoinType.INNER)
                                    .join(StoreEntity_.storeProduct, JoinType.INNER)
                                    .get<Any>(StoreProductEntity_.SEASON)
                            )
                                .value(it.values)
                        )
                    }

                    FiltersEnum.REGION -> {
                        predicates.add(
                            cb.`in`(
                                root.join(ClusterEntity_.regions, JoinType.INNER)
                                    .get<Any>(RegionEntity_.NAME)
                            )
                                .value(it.values)
                        )
                    }

                    FiltersEnum.REGION_TYPE -> {
                        predicates.add(
                            cb.`in`(
                                root.join(ClusterEntity_.regions, JoinType.INNER)
                                    .get<Any>(RegionEntity_.TYPE)
                            )
                                .value(it.values)
                        )
                    }

                    FiltersEnum.STORE_NAME -> {
                        predicates.add(
                            cb.`in`(
                                root.join(ClusterEntity_.regions, JoinType.INNER)
                                    .join(RegionEntity_.stores, JoinType.INNER)
                                    .get<Any>(StoreEntity_.NAME)
                            )
                                .value(it.values)
                        )
                    }

                    FiltersEnum.STORE_THEME -> {
                        predicates.add(
                            cb.`in`(
                                root.join(ClusterEntity_.regions, JoinType.INNER)
                                    .join(RegionEntity_.stores, JoinType.INNER)
                                    .get<Any>(StoreEntity_.THEME)
                            )
                                .value(it.values)
                        )
                    }

                    else -> logger.info("Filter for Cluster Not Found")
                }
            }

            query.distinct(true)
            cb.and(*predicates.toTypedArray())
        }
    }
}