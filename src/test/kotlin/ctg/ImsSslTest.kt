package ctg

import collector.ConnectionProperties
import ims.ImsCollector
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * @author Dganit David create on 12/04/2021
 */

class ImsSslTest {
    @Test
    fun `test buffers`() {
        val conversation = ImsCollector(
            ConnectionProperties(host = "192.86.32.238", port= 9999, ssl = false)
        ).collect()
        assertThat(conversation).isNotEmpty
    }

    @Test
    fun `test ssl buffers`() {
        val conversation = ImsCollector(
            ConnectionProperties(host = "192.86.32.238", port= 9998, ssl = true)
        ).collect(true)
        assertThat(conversation).isNotEmpty
    }
}