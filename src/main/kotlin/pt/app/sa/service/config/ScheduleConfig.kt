package pt.app.sa.service.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 27/09/2021 08:42
 */
@Profile("!test")
@Configuration
@EnableScheduling
class ScheduleConfig