package answer

import sun.misc.Unsafe

fun main() {
    replaceZeroWithOneInIntegerCache()

    val args = arrayOf(1, 0)
    println("Division by zero result is: ${args[0] / args[1]}")
}

private fun replaceZeroWithOneInIntegerCache() {
    val usf = Class.forName("sun.misc.Unsafe")
    val unsafeField = usf.getDeclaredField("theUnsafe")
    unsafeField.isAccessible = true
    val clazz = Class.forName("java.lang.Integer\$IntegerCache")
    val cacheField = clazz.getDeclaredField("cache")
    val unsafe = unsafeField[null] as Unsafe
    val cache = unsafe.getObject(unsafe.staticFieldBase(cacheField), unsafe.staticFieldOffset(cacheField)) as Array<Int>
    cache[128] = 1
}