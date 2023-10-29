package com.rickyrobot.triformula

import android.text.InputFilter
import android.text.Spanned

class DecimalDigitsInputFilter(private val maxDigitsBeforeDecimal: Int, private val maxDigitsAfterDecimal: Int) : InputFilter {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val builder = StringBuilder(dest)
        builder.insert(dstart, source.toString())
        val updatedText = builder.toString()

        val indexOfDecimal = updatedText.indexOf('.')
        if (indexOfDecimal != -1 && dstart <= indexOfDecimal) {
            if (dend - indexOfDecimal - 1 >= maxDigitsAfterDecimal) {
                return ""
            }
        }

        val textParts = updatedText.split(".")
        if (textParts.size > 1 && textParts[1].length > maxDigitsAfterDecimal) {
            return ""
        }

        if (textParts[0].length > maxDigitsBeforeDecimal) {
            return ""
        }

        return null
    }
}