package pt.app.sa.service.utils

import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 23:02
 */
interface LoadByRequestReceiveFileType<T, D> {

    val errorsAccepted: Int
    val logger: Logger

    fun request(): ResponseEntity<T>
    fun convert(file: T): MutableList<D>
    fun saveAll(list: MutableList<D>)

    fun load() {
        var execute = true
        var errorsAcceptedCount = 0
        var hasError = false

        while (execute) {
            try {
                val response = request()
                val file: T? = response.body
                val convertedFile = file?.let { convert(it) }
                convertedFile?.let { saveAll(it) }

                if (response.statusCode != HttpStatus.OK) {
                    hasError = true
                }
            } catch (ex: Exception) {
                logger.error(ex.message)
                hasError = true
            }

            if (!hasError) {
                execute = false
            } else {
                if (errorsAcceptedCount >= errorsAccepted) {
                    execute = false
                }

                errorsAcceptedCount++
                hasError = false
            }
        }
    }
}