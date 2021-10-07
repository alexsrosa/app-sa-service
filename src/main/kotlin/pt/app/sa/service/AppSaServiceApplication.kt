package pt.app.sa.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Main project class for application booting and some settings.
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 25/09/2021 16:39
 */
@SpringBootApplication
class AppSaServiceApplication

fun main(args: Array<String>) {
    runApplication<AppSaServiceApplication>(*args)
}
