package ctg

import collector.TcpCollector
import com.ibm.ctg.client.ECIRequest
import com.ibm.ctg.client.JavaGateway
import utils.ByteUtils
import java.io.IOException
import java.util.*

/**
 * @author Dganit David create on 10/02/2021
 */

class CtgCollector() : TcpCollector() {
  override fun doWork(): String {

    Locale.setDefault(Locale("en", "IL"))

    val host = "192.86.32.238"
    val data =
      "F4F1F260F8F3F2F5F440404040404040404040404040404040404040404040404040404040404040404000000000404040404040404040404040404040404040404040404040404040404040404000000000404040404040404040404040404040404040404040404040404040404040404000000000404040404040404040404040404040404040404040404040404040404040404000000000404040404040404040404040404040404040404040404040404040404040404000000000"

    val connection: JavaGateway = JavaGateway().apply {
      if (!isOpen) {
        try {
          url = host
          port = 13200
          open()
        } catch (e: IOException) {
        }
      }
    }
    val request = ECIRequest()
    request.Server = "IPCSSL"
    request.eciTimeout
    request.Call_Type = 12
    request.Program = "FININQ2"
    request.Commarea = ByteUtils.hexStringToByteArray(data)
    request.Commarea_Length = 190
    request.Extend_Mode = 0

    val rc = connection.flow(request)
    if (rc == 0) {
      println("Run OK")
    } else {
      println("we have an error")
    }
    connection.close()
    return host
  }
}