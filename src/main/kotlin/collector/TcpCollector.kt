package collector

import io.sniffy.Sniffy
import io.sniffy.Spy
import io.sniffy.SpyConfiguration
import io.sniffy.Threads
import io.sniffy.configuration.SniffyConfiguration
import io.sniffy.socket.AddressMatchers
import io.sniffy.socket.NetworkPacket
import io.sniffy.socket.Protocol
import io.sniffy.socket.SocketMetaData
import utils.ByteUtils

/**
 * @author Dganit David create on 10/02/2021
 */

abstract class TcpCollector {

  init {
    SniffyConfiguration.INSTANCE.isMonitorSocket = true
    SniffyConfiguration.INSTANCE.isMonitorNio = true
  }

  abstract fun doWork(): String

  fun collect(): List<TcpConversation> {

    Sniffy.spy<Spy<*>>(SpyConfiguration.builder().captureNetworkTraffic(true).build()).use { spy: Spy<*> ->

      val host = doWork()

      val networkTraffic: Map<SocketMetaData, List<NetworkPacket>> =
        spy.getNetworkTraffic(
          Threads.ANY,  // capture traffic from all threads
          AddressMatchers.exactAddressMatcher(host) // capture traffic to any destinations
        )

      return networkTraffic
        .filter { (socketMetaData, _) -> socketMetaData.protocol == Protocol.TCP }
        .map { (socketMetaData, networkPackets) ->

          TcpConversation(
            port = socketMetaData.getAddress().port,
            packets = getNetworkPackets(networkPackets)
          )

        }
    }
  }

  private fun getNetworkPackets(networkPackets: List<NetworkPacket>) =
    networkPackets.map { networkPacket ->
      TcpNetworkPacket(
        data = ByteUtils.bytesToHex(networkPacket.bytes),
        length = networkPacket.bytes.size,
        direction = if (networkPacket.isSent) {
          "WRITE"
        } else {
          "READ"
        }
      )
    }
}