package pt.app.sa.service.repository.specification

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import pt.app.sa.service.controller.dto.FilterData
import pt.app.sa.service.controller.dto.FiltersEnum
import pt.app.sa.service.model.*
import pt.app.sa.service.usecase.LoadRegionsUseCase
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 28/09/2021 15:13
 */
object RegionSpecification {

    private val logger: Logger = LoggerFactory.getLogger(LoadRegionsUseCase::class.java)

    fun filter(filters: List<FilterData>): Specification<RegionEntity> {
        return Specification { root, query, cb ->
            val predicates = mutableListOf<Predicate>()

            filters.forEach lit@{

                if(it.values.isEmpty()) return@lit

                when (FiltersEnum.valueOfWithTry(it.id) ?: return@lit) {

                    FiltersEnum.SEASON -> {
                        predicates.add(
                            cb.`in`(
                                root.join(RegionEntity_.stores, JoinType.INNER)
                                    .join(StoreEntity_.storeProduct, JoinType.INNER)
                                    .get<Any>(StoreProductEntity_.SEASON)
                            )
                                .value(it.values)
                        )
                    }

                    FiltersEnum.CLUSTER -> {
                        predicates.add(
                            cb.`in`(
                                root.join<RegionEntity, ClusterEntity>(RegionEntity_.CLUSTERS)
                                    .get<Any>(ClusterEntity_.NAME)
                            )
                                .value(it.values)
                        )
                    }

                    FiltersEnum.REGION -> {
                        predicates.add(
                            cb.and(root.get(RegionEntity_.name).`in`(it.values))
                        )
                    }

                    FiltersEnum.REGION_TYPE -> {
                        predicates.add(
                            cb.and(root.get(RegionEntity_.type).`in`(it.values))
                        )
                    }

                    FiltersEnum.STORE_NAME -> {
                        predicates.add(
                            cb.`in`(
                                root.join<RegionEntity, StoreEntity>(RegionEntity_.STORES)
                                    .get<Any>(StoreEntity_.NAME_ALIAS)
                            )
                                .value(it.values)
                        )
                    }

                    FiltersEnum.STORE_THEME -> {
                        predicates.add(
                            cb.`in`(
                                root.join<RegionEntity, StoreEntity>(RegionEntity_.STORES)
                                    .get<Any>(StoreEntity_.THEME)
                            )
                                .value(it.values)
                        )
                    }
                    else -> logger.info("Filter for Region Not Found")
                }
            }

            query.distinct(true)
            cb.and(*predicates.toTypedArray())
        }
    }
}