package mock

import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.json.JsonObject
import io.vertx.core.net.SelfSignedCertificate
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.await


class SslMockHttpServer(
  private val vertx: Vertx,
) {
  private lateinit var httpServer: HttpServer

  suspend fun start() {
    val mainRouter = Router.router(vertx)
    mainRouter
      .errorHandler(404) { routingContext: RoutingContext ->
        routingContext.response()
          .setChunked(true)
          .putHeader("Content-Type", "application/json")
          .setStatusCode(500)
          .end(JsonObject().put("error", "404 not found").toBuffer())
      }
      .errorHandler(500) { routingContext: RoutingContext ->
        routingContext.response()
          .setChunked(true)
          .putHeader("Content-Type", "application/json")
          .setStatusCode(500)
          .end(JsonObject().put("error", "500 internal server error").toBuffer())
      }
    mainRouter.route().handler(BodyHandler.create())
    mainRouter
      .post("/api")
      .handler { routingContext: RoutingContext ->
        routingContext.response()
          .setChunked(true)
          .putHeader("Content-Type", "application/octet-stream")
          .end(routingContext.body)
      }


    httpServer = vertx.createHttpServer(initHttpServerOptions())
      .requestHandler(mainRouter)
      .listen(10000).await()
  }

  private fun initHttpServerOptions(): HttpServerOptions {
    val httpServerOptions = HttpServerOptions()
    httpServerOptions.isSsl = true

      val certificate = SelfSignedCertificate.create()
      httpServerOptions.keyCertOptions = certificate.keyCertOptions()
    return httpServerOptions
  }

  suspend fun stopHttpServer() {
    httpServer.close().await()
  }
}
