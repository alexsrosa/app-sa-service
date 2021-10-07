package pt.app.sa.service.scheduler

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import pt.app.sa.service.commons.ControlThreadsUtils
import pt.app.sa.service.commons.TimeUtils
import pt.app.sa.service.usecase.*
import java.time.LocalTime
import javax.annotation.PostConstruct
import javax.annotation.Priority
import kotlin.concurrent.thread

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 17:49
 */
@Component
class LoadAndUpdateSourceSchedule(
    val restTemplate: RestTemplate,
    val loadClustersUseCase: LoadClustersUseCase,
    val loadRegionsUseCase: LoadRegionsUseCase,
    val loadStoresUseCase: LoadStoresUseCase,
    val loadStoreProductsUseCase: LoadStoreProductsUseCase,
    val loadProductsUseCase: LoadProductsUseCase,
    @Value("\${scheduler.activeOnStart}") private val activeOnStart: Boolean
) {

    private val logger = LoggerFactory.getLogger(LoadAndUpdateSourceSchedule::class.java)

    @PostConstruct
    fun onStartup() {
        if (activeOnStart){
            thread(name = "loadAndUpdateSource") {
                loadAndUpdateSourceSchedule()
            }
        }
    }

    @Scheduled(cron = "\${scheduler.loadAndUpdateSourceSchedule.cron}")
    fun loadAndUpdateSourceSchedule() {
        val startedTime: LocalTime = LocalTime.now()
        logger.info(">> Data load processing started")

        loadClustersUseCase.load()
        loadRegionsUseCase.load()
        loadStoresUseCase.load()

        val threadProducts = loadProductsUseCase.load()
        val threadStoreProducts = loadStoreProductsUseCase.load()
        threadStoreProducts.add(threadProducts)

        ControlThreadsUtils.returnWhenCloseAllTreadsOnList(threadStoreProducts)
        logger.info("Charge processing ended and took ${TimeUtils.getTotalTime(startedTime)} seconds.")
    }
}
