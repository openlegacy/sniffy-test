package collector

/**
 * @author Dganit David create on 12/04/2021
 */

data class ConnectionProperties(
    val host: String,
    val port: Int,
    val ssl:Boolean,
)