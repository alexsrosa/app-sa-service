package pt.app.sa.service.scheduler.data

/**
 *
 * @author <a href="mailto:alexsros@gmail.com">Alex Rosa</a>
 * @since 26/09/2021 12:39
 */
class ProductData {
    var season: String = ""
    var model: String = ""
    var size: String = ""
    var sku: String = ""
    var ean: String = ""
    var description: String = ""

    constructor()

    constructor(season: String, model: String, size: String, sku: String, ean: String, description: String) {
        this.season = season
        this.model = model
        this.size = size
        this.sku = sku
        this.ean = ean
        this.description = description
    }
}