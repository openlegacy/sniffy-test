package test

import http.cics.HttpCollector
import http.cics.HttpVertical
import io.vertx.core.Vertx
import kotlinx.coroutines.runBlocking
import mock.SslMockHttpServer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

/**
 * @author Dganit David create on 05/05/2021
 */
class HttpTest {

    companion object {

        private val vertx = Vertx.vertx()
        private val mock = SslMockHttpServer(vertx)

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            vertx.deployVerticle(HttpVertical())
            runBlocking {
                mock.start()
            }
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            runBlocking {
                mock.stopHttpServer()
            }
        }
    }

    @Test
    fun `test buffers`() {
        val collector = HttpCollector(vertx)
        val spy = collector.start()
        val conversation = collector.collect(true, spy)
        Assertions.assertThat(conversation).isNotEmpty
    }
}