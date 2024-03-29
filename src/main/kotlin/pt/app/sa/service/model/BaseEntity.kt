package pt.app.sa.service.model

import java.time.LocalDateTime
import javax.persistence.*

/**
 * Base class for entities with commonly used attributes.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 17:28
 */
@MappedSuperclass
open class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var updatedAt: LocalDateTime = LocalDateTime.now()
    var createdAt: LocalDateTime = LocalDateTime.now()

    @PreUpdate
    fun save() {
        updatedAt = LocalDateTime.now()
    }
}