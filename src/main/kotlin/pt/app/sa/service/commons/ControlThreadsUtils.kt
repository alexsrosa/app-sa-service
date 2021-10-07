package pt.app.sa.service.commons

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 03/10/2021 22:47
 */
class ControlThreadsUtils {

    companion object {
        fun returnWhenCloseAllTreadsOnList(listNameThreads: MutableList<Thread>) {
            var execute = true
            val threadsEnd = mutableListOf<Thread>()
            while (execute) {
                for (thread in listNameThreads) {
                    if (!thread.isAlive && !threadsEnd.contains(thread)) {
                        threadsEnd.add(thread)
                    }
                }

                if (threadsEnd.size == listNameThreads.size) {
                    execute = false
                }
            }
        }
    }
}