package collector

import kotlinx.serialization.Serializable

/**
 * @author Dganit David create on 10/02/2021
 */

@Serializable
data class TcpConversation(
  val port: Int,
  val packets: List<TcpNetworkPacket>,
)