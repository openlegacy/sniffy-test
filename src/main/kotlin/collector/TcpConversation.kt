package collector

/**
 * @author Dganit David create on 10/02/2021
 */

data class TcpConversation(
  val port: Int,
  val packets: List<TcpNetworkPacket>,
)