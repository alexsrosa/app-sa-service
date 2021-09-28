package pt.app.sa.service.repository.specification

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import pt.app.sa.service.controller.dto.FilterData
import pt.app.sa.service.controller.dto.FiltersEnum
import pt.app.sa.service.model.ClusterEntity
import pt.app.sa.service.model.ClusterEntity_
import javax.persistence.criteria.Predicate

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 28/09/2021 15:13
 */
object ClusterSpecification {

    private val logger: Logger = LoggerFactory.getLogger(ClusterSpecification::class.java)

    fun filter(filters: List<FilterData>): Specification<ClusterEntity> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            filters.forEach lit@{
                when (FiltersEnum.valueOfWithTry(it.id) ?: return@lit) {
                    FiltersEnum.CLUSTER -> {
                        predicates.add(
                            cb.and(root.get(ClusterEntity_.name).`in`(it.values))
                        )
                    }
                    else -> logger.info("Filter for Cluster Not Found")
                }
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}