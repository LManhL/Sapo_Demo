package com.example.sapodemo.model.formatter

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.floor

object FormatNumberUtil {
    fun formatNumberCeil(number: Double): String {
        val decimalFormat = DecimalFormat("#,##0.###", DecimalFormatSymbols(Locale.US))
        decimalFormat.roundingMode = RoundingMode.CEILING
        return decimalFormat.format(number)
    }
    fun formatNumberFloor(number: Double): String {
        val decimalFormat = DecimalFormat("#,##0.###", DecimalFormatSymbols(Locale.US))
        decimalFormat.roundingMode = RoundingMode.FLOOR
        return decimalFormat.format(number)
    }
}