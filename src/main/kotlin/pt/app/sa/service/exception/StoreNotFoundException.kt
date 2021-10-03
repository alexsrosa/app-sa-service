package pt.app.sa.service.exception

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 01/10/2021 18:03
 */
class StoreNotFoundException(name: String) : RuntimeException("Store $name Not Found")