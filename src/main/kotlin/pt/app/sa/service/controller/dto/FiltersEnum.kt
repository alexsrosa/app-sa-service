package pt.app.sa.service.controller.dto

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 28/09/2021 08:41
 */
enum class FiltersEnum {

    SEASON,
    CLUSTER,
    REGION,
    REGION_TYPE,
    PRODUCT_MODEL,
    PRODUCT_SIZE,
    SKU,
    STORE_NAME,
    STORE_THEME;

    companion object {
        fun valueOfWithTry(value: String): FiltersEnum? {
            return try {
                valueOf(value.uppercase())
            } catch (ex: Exception) {
                null
            }
        }
    }
}