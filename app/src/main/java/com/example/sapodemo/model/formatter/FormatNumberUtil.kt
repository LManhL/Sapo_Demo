package com.example.sapodemo.model.formatter

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object FormatNumberUtil {
    fun formatNumber(number: Double): String {
        val decimalFormat = DecimalFormat("#,##0.###", DecimalFormatSymbols(Locale.US))
        return decimalFormat.format(number)
    }
}