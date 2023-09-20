package com.example.sapodemo.ui.order.custom

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.ExtractedTextRequest
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.LinearLayout
import com.example.sapodemo.R
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class CustomKeyBoard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    companion object {
        const val MAX_QUANTITY = 999999.999
    }

    private var mButton1: Button? = null
    private var mButton2: Button? = null
    private var mButton3: Button? = null
    private var mButton4: Button? = null
    private var mButton5: Button? = null
    private var mButton6: Button? = null
    private var mButton7: Button? = null
    private var mButton8: Button? = null
    private var mButton9: Button? = null
    private var mButton0: Button? = null
    private var mButtonDot: Button? = null
    private var mButtonCancel: Button? = null
    private var mButtonEnter: Button? = null
    private var mButtonDelete: Button? = null
    private var keyValues = SparseArray<String>()
    var inputConnection: InputConnection? = null
    var onClickEnter: (() -> Unit)? = null
    var onClickCancel: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_custom_key_board, this, true)
        mButton1 = findViewById<View>(R.id.btnCustomKeyBoardNumber1) as Button
        mButton2 = findViewById<View>(R.id.btnCustomKeyBoardNumber2) as Button
        mButton3 = findViewById<View>(R.id.btnCustomKeyBoardNumber3) as Button
        mButton4 = findViewById<View>(R.id.btnCustomKeyBoardNumber4) as Button
        mButton5 = findViewById<View>(R.id.btnCustomKeyBoardNumber5) as Button
        mButton6 = findViewById<View>(R.id.btnCustomKeyBoardNumber6) as Button
        mButton7 = findViewById<View>(R.id.btnCustomKeyBoardNumber7) as Button
        mButton8 = findViewById<View>(R.id.btnCustomKeyBoardNumber8) as Button
        mButton9 = findViewById<View>(R.id.btnCustomKeyBoardNumber9) as Button
        mButton0 = findViewById<View>(R.id.btnCustomKeyBoardNumber0) as Button
        mButtonDot = findViewById<View>(R.id.btnCustomKeyBoardDot) as Button
        mButtonCancel = findViewById<View>(R.id.btnCustomKeyBoardCancel) as Button
        mButtonEnter = findViewById<View>(R.id.btnCustomKeyBoardConfirm) as Button
        mButtonDelete = findViewById<View>(R.id.btnCustomKeyBoardDelete) as Button

        mButton1!!.setOnClickListener(this)
        mButton2!!.setOnClickListener(this)
        mButton3!!.setOnClickListener(this)
        mButton4!!.setOnClickListener(this)
        mButton5!!.setOnClickListener(this)
        mButton6!!.setOnClickListener(this)
        mButton7!!.setOnClickListener(this)
        mButton8!!.setOnClickListener(this)
        mButton9!!.setOnClickListener(this)
        mButton0!!.setOnClickListener(this)
        mButtonDot!!.setOnClickListener(this)
        mButtonDelete!!.setOnClickListener(this)

        mButtonCancel!!.setOnClickListener {
            onClickCancel?.invoke()
        }
        mButtonEnter!!.setOnClickListener {
            onClickEnter?.invoke()
        }

        keyValues.put(R.id.btnCustomKeyBoardNumber1, "1")
        keyValues.put(R.id.btnCustomKeyBoardNumber2, "2")
        keyValues.put(R.id.btnCustomKeyBoardNumber3, "3")
        keyValues.put(R.id.btnCustomKeyBoardNumber4, "4")
        keyValues.put(R.id.btnCustomKeyBoardNumber5, "5")
        keyValues.put(R.id.btnCustomKeyBoardNumber6, "6")
        keyValues.put(R.id.btnCustomKeyBoardNumber7, "7")
        keyValues.put(R.id.btnCustomKeyBoardNumber8, "8")
        keyValues.put(R.id.btnCustomKeyBoardNumber9, "9")
        keyValues.put(R.id.btnCustomKeyBoardNumber0, "0")
        keyValues.put(R.id.btnCustomKeyBoardDot, ".")
    }

    override fun onClick(v: View) {
        if (inputConnection == null) return
        when (v.id) {
            R.id.btnCustomKeyBoardDelete -> {
                handleClickDelete()
            }
            R.id.btnCustomKeyBoardDot -> {
                handleClickDot(v)
            }
            else -> {
                handleClickDigit(v)
            }
        }
        formatAndSetValue()
    }

    fun initContent(initValue: String) {
        inputConnection?.commitText(initValue, 1)
    }

    fun onClickDeleteAll() {
        val currentText = inputConnection!!.getExtractedText(ExtractedTextRequest(), 0).text.toString()
        inputConnection!!.deleteSurroundingText(currentText.length, currentText.length)
        inputConnection!!.commitText("0", 1)
    }

    private fun handleClickDelete() {
        inputConnection!!.deleteSurroundingText(1, 0)
    }

    private fun handleClickDot(v: View) {
        val value = keyValues[v.id]
        val currentText =
            inputConnection!!.getExtractedText(ExtractedTextRequest(), 0).text.toString()
        if (!currentText.contains('.')) inputConnection!!.commitText(value, 1)
    }

    private fun handleClickDigit(v: View) {
        val value = keyValues[v.id]
        val prevText = inputConnection!!.getExtractedText(ExtractedTextRequest(), 0).text.toString()

        if (prevText.startsWith("0") && !prevText.contains('.')) {
            inputConnection!!.deleteSurroundingText(1, 0)
        }

        inputConnection!!.commitText(value, 1)
    }

    private fun formatAndSetValue() {
        val currentText = inputConnection!!.getExtractedText(ExtractedTextRequest(), 0).text.toString()

        if (currentText.isEmpty()) {
            inputConnection!!.commitText("0", 1)
            return
        }

        var currentNumber = currentText.filter { it.isDigit() || it == '.' }.toDoubleOrNull()
        if (currentNumber == null) {
            // Do nothing
        } else {
            if (currentNumber >= MAX_QUANTITY) {
                currentNumber = MAX_QUANTITY
            }

            inputConnection!!.deleteSurroundingText(currentText.length, currentText.length)

            if (!currentText.contains('.')) {
                inputConnection!!.commitText(formatNumberFloor(currentNumber), 1)
            } else {
                val currentNumberToString: String = formatNumberFloor(currentNumber)
                val parts = currentNumberToString.split(".")

                val integerPart = parts[0]
                var decimalPart = currentText.split('.')[1]
                decimalPart = if (decimalPart.length > 3) decimalPart.subSequence(0, 3).toString() else decimalPart

                val res = "$integerPart.$decimalPart"
                inputConnection!!.commitText(res, 1)
            }
        }
    }

    private fun formatNumberFloor(number: Double): String {
        val decimalFormat = DecimalFormat("#,##0.###", DecimalFormatSymbols(Locale.US))
        decimalFormat.roundingMode = RoundingMode.FLOOR
        return decimalFormat.format(number)
    }
}