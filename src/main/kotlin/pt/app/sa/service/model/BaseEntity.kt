package pt.app.sa.service.model

import java.time.LocalDateTime
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

/**
 * Base class for entities with commonly used attributes.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 17:28
 */
@MappedSuperclass
open class BaseEntity() {
    @Id @GeneratedValue
    var id: Long? = null
    var updatedAt: LocalDateTime = LocalDateTime.now()
    var cratedAt: LocalDateTime = LocalDateTime.now()
}