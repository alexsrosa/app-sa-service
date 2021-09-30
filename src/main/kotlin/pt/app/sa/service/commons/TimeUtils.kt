package pt.app.sa.service.commons

import java.time.LocalTime

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 30/09/2021 08:49
 */
class TimeUtils {
    companion object {
        fun getTotalTime(startedTime: LocalTime): Int {
            val time = LocalTime.now().toSecondOfDay() - startedTime.toSecondOfDay()
            if (time < 0) {
                return 0
            }
            return time
        }
    }
}