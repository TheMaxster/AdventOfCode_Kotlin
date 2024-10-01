package year2023

import java.math.BigDecimal

object BigDecimalExtensions {
    fun List<BigDecimal>.sum(): BigDecimal {
        return this.fold(BigDecimal.ZERO) { acc, bigDecimal -> acc + bigDecimal }
    }
}
