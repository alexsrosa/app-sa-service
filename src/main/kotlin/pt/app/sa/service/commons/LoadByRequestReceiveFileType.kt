package pt.app.sa.service.commons

import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalTime

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 23:02
 */
interface LoadByRequestReceiveFileType<T, D> {

    val errorsAccepted: Int
    val processName: String
    val logger: Logger

    fun request(): ResponseEntity<T>
    fun convert(file: T): MutableList<D>
    fun saveAll(list: MutableList<D>)

    fun load() {
        logger.info(":::: Started to load $processName ::::")
        val startedTime: LocalTime = LocalTime.now()
        var execute = true
        var errorsAcceptedCount = 1
        var hasError = false
        var messageErr = ""

        while (execute) {
            try {
                val response = request()
                val file: T? = response.body
                val convertedFile = file?.let { convert(it) }
                convertedFile?.let {
                    saveAll(it)
                    logger.info("${it.size} records were processed")
                }

                if (response.statusCode != HttpStatus.OK) {
                    hasError = true
                }
            } catch (ex: Exception) {
                messageErr = "Error requesting API: ${ex.message}"
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
                logger.info("Attempts [$errorsAcceptedCount] of [$errorsAccepted] in total. Detail error: $messageErr")
            }
        }

        logger.info("#### Finished in ${TimeUtils.getTotalTime(startedTime)} seconds ####")
    }
}