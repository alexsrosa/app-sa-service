package pt.app.sa.service.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import pt.app.sa.service.controller.dto.MessageExceptionApiDto
import pt.app.sa.service.controller.dto.MessageListExceptionApiDto

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 02/10/2021 09:49
 */
@ControllerAdvice
class ExceptionApiHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [RuntimeException::class, StoreNotFoundException::class])
    fun handleBadRequest(exception: RuntimeException): ResponseEntity<MessageExceptionApiDto> {
        return ResponseEntity.badRequest().body(MessageExceptionApiDto(exception.message!!))
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val details = mutableListOf<String>()
        for (error in ex.bindingResult.fieldErrors.sortedBy { it.field }) {
            details.add(error.field + ": " + (if (error.isBindingFailure) "Invalid type" else error.defaultMessage))
        }

        for (error in ex.bindingResult.globalErrors) {
            details.add(error.objectName + ": " + error.defaultMessage)
        }

        return ResponseEntity.badRequest().body(MessageListExceptionApiDto("Invalid parameters", details))
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return ResponseEntity.badRequest().body(MessageExceptionApiDto(ex.message!!))
    }
}
