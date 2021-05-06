package collector

import kotlinx.serialization.Serializable

/**
 * @author Dganit David create on 10/02/2021
 */
@Serializable
data class TcpNetworkPacket(
  val data: String,
  val length: Int,
  val direction: String,
)