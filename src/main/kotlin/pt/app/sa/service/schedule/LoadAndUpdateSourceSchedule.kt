package pt.app.sa.service.schedule

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import pt.app.sa.service.usecase.*
import java.time.LocalTime
import javax.annotation.PostConstruct

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
    val loadProductsUseCase: LoadProductsUseCase
) {

    private val logger = LoggerFactory.getLogger(LoadAndUpdateSourceSchedule::class.java)

    @PostConstruct
    fun onStartup() {
        loadAndUpdateSourceSchedule()
    }

    @Scheduled(cron = "\${schedule.loadAndUpdateSourceSchedule.cron}")
    fun loadAndUpdateSourceSchedule() {
        val startedTime: LocalTime = LocalTime.now()
        logger.info(">> Data load processing started")

        loadClustersUseCase.load()
        loadRegionsUseCase.load()
        loadStoresUseCase.load()
        loadStoreProductsUseCase.load()
        loadProductsUseCase.load()

        val totalTime = LocalTime.now().toSecondOfDay() - startedTime.toSecondOfDay()
        logger.info("Charge processing ended and took $totalTime seconds.")
    }
}
