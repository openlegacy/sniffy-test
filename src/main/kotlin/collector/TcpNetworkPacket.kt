package collector

/**
 * @author Dganit David create on 10/02/2021
 */

data class TcpNetworkPacket(
  val data: String,
  val length: Int,
  val direction: String,
)