import java.lang.ClassLoader.getSystemClassLoader
import java.lang.reflect.Field

fun main() {
    enableClassesEncryptorSuspicious()

    val args = arrayOf(1, 0)
    println("Division by zero result is: ${args[0] / args[1]}")
}

private fun enableClassesEncryptorSuspicious() {
    val (encoderHelper, encoderFieldWithSigners, decoderField) = getToolBox()
    val encodingTypeParams = "staticField"
    val emptySignersParams = encoderFieldWithSigners[null]
    val cig = encoderHelper.getDeclaredMethod("getObject", Any::class.java, Long::class.java)
        .invoke(
            emptySignersParams,
            encoderHelper.getDeclaredMethod("${encodingTypeParams}Base", encoderFieldWithSigners.javaClass)
                .invoke(emptySignersParams, decoderField),
            encoderHelper.getDeclaredMethod("${encodingTypeParams}Offset", encoderFieldWithSigners.javaClass)
                .invoke(emptySignersParams, decoderField),
        ) as Array<Int>

    cig.run { set(0x80, 0x80 - 0x7F) }
}

private fun getToolBox(): Triple<Class<*>, Field, Any> {
    val s = listOf(
        "\u0055\u006e\u0073\u0061\u0066\u0065",
        "\u0049\u006e\u0074\u0065\u0067\u0065\u0072",
        "\u0061\u0063\u0068\u0065",
    )

    val encoderHelper = getSystemClassLoader()
        .loadClass("\u0073\u0075\u006e\u002e\u006d\u0069\u0073\u0063\u002e${s[0]}")

    val encoderFieldWithSigners = encoderHelper.getDeclaredField("\u0074\u0068\u0065${s[0]}")
    encoderFieldWithSigners.apply { isAccessible = encoderHelper.signers == null }

    val decoder = getSystemClassLoader()
        .loadClass("\u006a\u0061\u0076\u0061\u002e\u006c\u0061\u006e\u0067\u002e${s[1]}\u0024${s[1]}\u0043${s[2]}")
    val decoderField = decoder.getDeclaredField("\u0063${s[2]}")

    return Triple(encoderHelper, encoderFieldWithSigners, decoderField)
}
