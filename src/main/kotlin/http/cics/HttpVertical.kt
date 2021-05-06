package http.cics

import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.kotlin.coroutines.CoroutineVerticle

/**
 * @author Dganit David create on 05/05/2021
 */

class HttpVertical : CoroutineVerticle() {

    companion object {
        const val address = "http-client-address"
    }

    override suspend fun start() {

        lateinit var webClient: WebClient
        vertx.eventBus().consumer<String>(address).apply {

            webClient = WebClient.create(
                vertx,
                WebClientOptions().apply {
                    isSsl = true
                    isVerifyHost = false
                    isTrustAll = true
                }
            )

            this.handler { message ->
                val httpRequest = webClient
                    .postAbs("https://127.0.0.1:10000/api")
                httpRequest.sendBuffer(Buffer.buffer(message.body())).onSuccess() {
                    message.reply(it.body())
                }.onFailure {
                    message.reply("failed")
                }
            }
        }
    }
}