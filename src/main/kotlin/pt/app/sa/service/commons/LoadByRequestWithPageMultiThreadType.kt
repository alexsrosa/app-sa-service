package pt.app.sa.service.commons

import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalTime
import kotlin.concurrent.thread

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 19:37
 */
interface LoadByRequestWithPageMultiThreadType<T> {

    val errorsAccepted: Int
    val processName: String
    val logger: Logger
    val totalBatch: Int
    val totalThreads: Int

    fun request(page: Int): ResponseEntity<List<T>>
    fun saveAll(list: List<T>)
    fun stopExecution(list: List<T>): Boolean
    fun discoverLastPage(): Int

    fun load() {
        val discoverLastPage = discoverLastPage()
        val pageForThread = discoverLastPage / totalThreads

        var execute = true
        var nextPage = 0
        var nextPageForThread = pageForThread
        while (execute) {
            val nextPageThead = nextPage
            val nextPageForThreadThread = nextPageForThread
            thread(start = true, name = "${nextPageThead}-to-${nextPageForThreadThread}") {
                loadRun(nextPageThead, nextPageForThreadThread)
            }
            nextPage = nextPageForThread + 1
            nextPageForThread += pageForThread

            if (nextPage > discoverLastPage) {
                execute = false
            }
        }
    }

    fun loadRun(pageInit: Int, pageFinish: Int?) {
        val startedTime: LocalTime = LocalTime.now()
        logger.info(":::: Started to load $processName :::: Pages start on $pageInit to $pageFinish")

        var execute = true
        var page = pageInit
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

                errorsAcceptedCount++
                hasError = false
                logger.warn("Attempts [$errorsAcceptedCount] of [$errorsAccepted] in total. Detail error: $messageErr")
            } else {
                if (pageFinish != null && page == pageFinish) execute = false
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