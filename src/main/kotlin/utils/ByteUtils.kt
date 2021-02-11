package utils

/**
 * @author Dganit David Jun 27, 2016
 */
object ByteUtils {
  private val hexArray = "0123456789ABCDEF".toCharArray()

  @JvmStatic
  fun hexStringToByteArray(hexString: String): ByteArray {
    val separator = ByteArray(hexString.length / 2)
    var i = 0
    while (i < hexString.length) {
      separator[i / 2] = (
        (Character.digit(hexString[i], 16) shl 4) +
          Character.digit(hexString[i + 1], 16)
        ).toByte()
      i += 2
    }
    return separator
  }

  @JvmStatic
  fun bytesToHex(bytes: ByteArray): String {
    val hexChars = CharArray(bytes.size * 2)
    for (j in bytes.indices) {
      val v: Int = bytes[j].toInt().and(0xFF)
      hexChars[j * 2] = hexArray[v ushr 4]
      hexChars[j * 2 + 1] = hexArray[v and 0x0F]
    }
    return String(hexChars)
  }
}
