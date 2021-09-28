package pt.app.sa.service.repository.specification

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import pt.app.sa.service.controller.dto.FilterData
import pt.app.sa.service.controller.dto.FiltersEnum
import pt.app.sa.service.model.ClusterEntity
import pt.app.sa.service.model.ClusterEntity_
import pt.app.sa.service.model.RegionEntity
import pt.app.sa.service.model.RegionEntity_
import pt.app.sa.service.usecase.LoadRegionsUseCase
import javax.persistence.criteria.Predicate

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 28/09/2021 15:13
 */
object RegionSpecification {

    private val logger: Logger = LoggerFactory.getLogger(LoadRegionsUseCase::class.java)

    fun filter(filters: List<FilterData>): Specification<RegionEntity> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            filters.forEach lit@{
                when (FiltersEnum.valueOfWithTry(it.id) ?: return@lit) {
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
                    else -> logger.info("Filter for Region Not Found")
                }
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}