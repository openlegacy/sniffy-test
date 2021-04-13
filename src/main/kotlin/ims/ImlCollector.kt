package ims

import collector.ConnectionProperties
import collector.SocketFactory
import collector.TcpCollector
import utils.ByteUtils

/**
 * @author Dganit David create on 12/04/2021
 */

class ImsCollector(private val connectionProperties: ConnectionProperties) : TcpCollector() {

    private val socketFactory = SocketFactory(connectionProperties)

    override fun doWork(): String {
        val data =
            "00000093005000005CE2C1D4D7D3F15C00000000426700004040404040404040002000404040404040404040C9E5D7F140404040C1D9C9C8E3C5D940D6D3E2F0F0F0F140C7D9D6E4D7F14040D6D3E2F0F0F0F140003B0000C9E5E3C3C24040404040C4C9E2D7D3C1E840D3C9E5D5C9404040404040404040404040404040404040404040404040404040404040404000040000"
        val buffer = ByteUtils.hexStringToByteArray(data)
        val socket = socketFactory.create()
        socket.keepAlive = true
        val os = socket.getOutputStream()
        os.write(buffer)
        os.flush()
        val inStream = socket.getInputStream()
        val size = socket.receiveBufferSize
        val recived = ByteArray(size)
        inStream.read(recived, 0, size)

        return connectionProperties.host
    }
}