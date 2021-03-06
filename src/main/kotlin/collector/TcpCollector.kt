package collector

import io.sniffy.*
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
        SniffyConfiguration.INSTANCE.socketCaptureEnabled = true
        SniffyConfiguration.INSTANCE.isDecryptTls = true
        Sniffy.initialize()
    }

    abstract fun doWork(): String

    fun start() = Sniffy.spy<Spy<*>>(SpyConfiguration.builder().captureNetworkTraffic(true).build())

    fun collect(ssl: Boolean = false, spy: Spy<*> ): List<TcpConversation> {

      //  val spy = Sniffy.spy<Spy<*>>(SpyConfiguration.builder().captureNetworkTraffic(true).build())

        val host = doWork()

        val networkTraffic: Map<SocketMetaData, List<NetworkPacket>> =
            if (ssl) {
                spy.getDecryptedNetworkTraffic(
                    Threads.ANY,  // capture traffic from all threads
                    AddressMatchers.exactAddressMatcher(host),
                    GroupingOptions(false, false, true) // capture traffic to any destinations
                )
            } else {
                spy.getNetworkTraffic(
                    Threads.ANY,  // capture traffic from all threads
                    AddressMatchers.exactAddressMatcher(host) // capture traffic to any destinations
                )
            }
        return networkTraffic
            .filter { (socketMetaData, _) -> socketMetaData.protocol == Protocol.TCP }
            .map { (socketMetaData, networkPackets) ->

                TcpConversation(
                    port = socketMetaData.getAddress().port,
                    packets = getNetworkPackets(networkPackets)
                )

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
