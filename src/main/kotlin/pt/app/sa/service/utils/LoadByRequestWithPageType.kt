package pt.app.sa.service.utils

import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 19:37
 */
interface LoadByRequestWithPageType<T> {

    val errorsAccepted: Int
    val logger: Logger

    fun request(page: Int): ResponseEntity<T>
    fun save(list: T)
    fun stopExecution(list: T): Boolean

    fun load() {
        var execute = true
        var page = 0
        var errorsAcceptedCount = 0
        var hasError = false

        while (execute) {
            try {
                val response = request(page)
                val list: T? = response.body
                list?.let { save(it) }

                if (list?.let { stopExecution(it) } == true) {
                    execute = false;
                } else if (response.statusCode != HttpStatus.OK) {
                    hasError = true
                }
            } catch (ex: Exception) {
                logger.error(ex.message)
                hasError = true
            }

            if (hasError) {
                if (errorsAcceptedCount >= errorsAccepted) {
                    execute = false
                }

                errorsAcceptedCount++
                hasError = false
            } else {
                page++
            }
        }
    }
}