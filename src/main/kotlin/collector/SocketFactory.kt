package collector

import java.net.Socket
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * @author Dganit David create on 08/04/2021
 */

class SocketFactory(private val connectionProperties: ConnectionProperties) {

  private val sslSocketFactory: SSLSocketFactory? = if (connectionProperties.ssl) {
    val trustManager = arrayOf(TrustAllTrustManager)

    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, trustManager, null)
    sslContext.socketFactory
  } else {
    null
  }

  fun create(): Socket {
    return sslSocketFactory?.let {
      sslSocketFactory.createSocket(connectionProperties.host, connectionProperties.port)
    } ?: Socket(connectionProperties.host, connectionProperties.port)
  }

}

object TrustAllTrustManager : X509TrustManager {
  override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
  }

  override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
  }

  override fun getAcceptedIssuers(): Array<X509Certificate>? {
    return null
  }
}
