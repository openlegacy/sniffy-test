package http.cics

import collector.TcpCollector
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.runBlocking

/**
 * @author Dganit David create on 05/05/2021
 */

class HttpCollector(private val vertx: Vertx) : TcpCollector() {

    override fun doWork(): String {
        runBlocking {
            vertx.eventBus().request<String>(HttpVertical.address, "123").await()
        }
        return "localhost"
    }
}