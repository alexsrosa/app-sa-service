package pt.app.sa.service.controller.dto

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 02/10/2021 11:14
 */
open class MessageExceptionApiDto(open val message: String)

data class MessageListExceptionApiDto(override val message: String, val details: List<String> = emptyList()) :
    MessageExceptionApiDto(message)