package test

import collector.ConnectionProperties
import ims.ImsCollector
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlinx.serialization.json.Json
import javax.xml.catalog.CatalogFeatures.defaults

/**
 * @author Dganit David create on 12/04/2021
 */

class ImsSslTest {
    private val json = Json {
        defaults()
    }

    @Test
    fun `test buffers`() {
        val collector = ImsCollector(
            ConnectionProperties(host = "192.86.32.238", port= 9999, ssl = false)
        )
        val spy = collector.start()
        val conversation= collector.collect(false,spy)
        assertThat(conversation).isNotEmpty
    }

    @Test
    fun `test ssl buffers`() {
        val collector = ImsCollector(
            ConnectionProperties(host = "192.86.32.238", port= 9998, ssl = true)
        )
        val spy = collector.start()
        val conversation= collector.collect(true,spy)
        assertThat(conversation).isNotEmpty
    }
}