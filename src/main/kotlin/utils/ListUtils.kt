package utils

/**
 * The ListUtils for the AoC setup.
 *
 * @author mglembock
 */
object ListUtils {
    fun sumUpInt(list: List<Int>): Int {
        return list.stream().reduce { a: Int, b: Int -> Integer.sum(a, b) }.orElse(0)
    }

    fun sumUpLong(list: List<Long>): Long {
        return list.stream().reduce { a: Long, b: Long -> java.lang.Long.sum(a, b) }.orElse(0L)
    }

    fun multiplyUpInt(list: List<Int>): Int {
        var product = 1
        for (number in list) {
            product *= number
        }
        return product
    }

//    fun sumUpBd(list: List<BigDecimal?>?): BigDecimal {
//        return CollectionUtils.emptyIfNull(list).stream()
//            .filter { obj: Any? -> Objects.nonNull(obj) }
//            .reduce(BigDecimal.ZERO, BigDecimal::add)
//    }
}
