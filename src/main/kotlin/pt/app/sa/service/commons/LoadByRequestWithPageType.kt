package pt.app.sa.service.commons

import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalTime

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 19:37
 */
interface LoadByRequestWithPageType<T> {

    val errorsAccepted: Int
    val processName: String
    val logger: Logger
    val totalBatch: Int

    fun request(page: Int): ResponseEntity<List<T>>
    fun saveAll(list: List<T>)
    fun stopExecution(list: List<T>): Boolean

    fun load() {
        logger.info(":::: Started to load $processName ::::")

        val startedTime: LocalTime = LocalTime.now()
        var execute = true
        var page = 0
        var errorsAcceptedCount = 0
        var hasError = false
        var listBatch = mutableListOf<T>()
        var messageErr = ""

        while (execute) {
            try {
                val response = request(page)
                val list: List<T>? = response.body
                list?.let { listBatch.addAll(it.toList()) }

                if (listBatch.size >= totalBatch) {
                    saveAll(listBatch)
                    logger.info("${listBatch.size} records were processed")
                    listBatch = mutableListOf()
                }

                if (list?.let { stopExecution(it) } == true) {
                    execute = false
                } else if (response.statusCode != HttpStatus.OK) {
                    hasError = true
                }
            } catch (ex: Exception) {
                messageErr = "Error requesting API: ${ex.message}"
                hasError = true
            }

            if (hasError) {
                if (errorsAcceptedCount >= errorsAccepted) {
                    execute = false
                }

                logger.info("Attempts [$errorsAcceptedCount] of [$errorsAccepted] in total. Detail error: $messageErr")
                errorsAcceptedCount++
                hasError = false
            } else {
                page++
            }
        }

        if (listBatch.size > 0) {
            saveAll(listBatch)
            logger.info("${listBatch.size} records were processed")
        }

        logger.info("#### Finished in ${TimeUtils.getTotalTime(startedTime)} seconds ####")
    }
}