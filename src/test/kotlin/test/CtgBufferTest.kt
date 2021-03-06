package test

import collector.TcpNetworkPacket
import com.ibm.ctg.monitoring.RequestExitMonitor
import io.sniffy.util.ReflectionUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * @author Dganit David create on 10/02/2021
 */

class CtgBufferTest {

    @Test
    fun `test buffers`() {

        val expected = listOf(
            TcpNetworkPacket(
                direction = "WRITE",
                data = "4761746500902000000000050000000000000000000000044241534500000000160002656E0002494C0000000000000000000000000000",
                length = 55
            ),
            TcpNetworkPacket(
                direction = "READ",
                data = "47617465009020000000000500000000000000000000000442415345000000003C0002656E00025553000000264F533A207A2F4F5320417263683A2073333930782056657273696F6E3A2030322E30342E303000000000000000000000",
                length = 93
            ),
            TcpNetworkPacket(
                direction = "WRITE",
                data = "47617465009020000000000100000000000000000000000345434900000001140000000C000000000000000000000000000000000649504353534C000000000000000000000000000000000000000046494E494E5132000000000000000000000000BE0000F4F1F260F8F3F2F5F44040404040404040404040404040404040404040404040404040404040404040400000000040404040404040404040404040404040404040404040404040404040404040400000000040404040404040404040404040404040404040404040404040404040404040400000000040404040404040404040404040404040404040404040404040404040404040400000000040404040404040404040404040404040404040404040404040404040404040400000000000000000FFFFFFFF00000001FFFFFFFF00",
                length = 308
            ),
            TcpNetworkPacket(
                direction = "READ",
                data = "47617465009020000000000300000000000000000000000345434900000001190000000C00000000000000000000000000000000000000BE00F4F1F260F8F3F2F5F440F4F5F8F0F1F2F3F4F1F2F3F4F1F2F3F4C7D6D3C4404040404040404040404040138806F7F4F5F8F0F0F0F2F3F7F7F8F2F6F4F5F2D7D3C1E3C9D5E4D4404040404040404027100237F4F5F8F0F8F8F7F3F8F6F2F5F5F2F6F5C2E4E2C9D5C5E2E260C74040404040401B581309F4F5F8F0F1F0F8F3F7F2F5F3F3F4F2F4C2C1E2C9C3404040404040404040404003E80000F4F5F8F0F7F7F3F6F8F5F9F8F6F2F4F4C6E360D4C5D4C2C5D94040404040404007D00258000100084354474150504C4400085553415344563032000443534D490000000E3137362E3233312E36372E3134340000F122D936C51931FDE9000004495076340000",
                length = 313
            ),
            TcpNetworkPacket(
                direction = "WRITE",
                data = "47617465009020000000000AFFFFFFFF0000000000000004424153450000000000",
                length = 33
            ), TcpNetworkPacket(
                direction = "READ",
                data = "476174650090200000000004FFFFFFFF0000F00400000004424153450000000000",
                length = 33
            )
        )

        for (i in 1..10) {

            println("Iteration #$i")

            // RequestExitMonitor.nextCtgCorrelator is a static counter which is included in payloads;
            // We need to reset it in order to get sustainable and reproducible payloads
            ReflectionUtil.setField(RequestExitMonitor::class.java, null, "nextCtgCorrelator", 0)

            val collector = CtgCollector()
            val spy = collector.start()
            val conversation = CtgCollector().collect(false, spy)
            assertThat(conversation.size).isEqualTo(1)
            assertThat(conversation[0].port).isEqualTo(13200)
            val packets = conversation[0].packets
            assertThat(packets.size).isEqualTo(6)
            packets.forEachIndexed { index, tcpNetworkPacket ->
                println("checking buffer #${index}")
                if (index != 3) {
                    assertThat(tcpNetworkPacket.data).isEqualTo(expected[index].data)
                } else {
                    // Second response contains timestamp from server so we cannot match it with hardcoded value
                    // Also length check isn't good enough since response contains IP address of client encoded as String
                    // So it's length may vary from machine to machine as well

                    // skip length comparison
                    assertThat(tcpNetworkPacket.data.substring(0, 60)).isEqualTo(expected[index].data.substring(0, 60))
                    // skip end comparison since it contains timestamp and depends on client IP address
                    assertThat(tcpNetworkPacket.data.substring(68)).startsWith(expected[index].data.substring(68, 261))
                    // ctg data section in our case is always the same
                    assertThat(tcpNetworkPacket.data.substring(114, 494)).startsWith(
                        expected[index].data.substring(
                            114,
                            494
                        )
                    )
                }
            }
        }
    }
}