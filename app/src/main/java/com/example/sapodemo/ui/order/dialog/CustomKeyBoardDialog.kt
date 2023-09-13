package com.example.sapodemo.ui.order.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import com.example.sapodemo.R
import com.example.sapodemo.ui.order.custom.CustomKeyBoard

class CustomKeyBoardDialog(context: Context, private val initValue: String = "0") : Dialog(context) {
    private lateinit var editText: EditText
    private lateinit var clearTextButton: ImageView
    private lateinit var customKeyBoard: CustomKeyBoard
    var onClickCancel: (() -> Unit)? = null
    var onClickEnter: ((stringNumber: String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setCancelable(false)
        this.setCanceledOnTouchOutside(true)
        this.setContentView(R.layout.layout_custom_key_board_dialog)
        editText  = this.findViewById(R.id.edtCustomKeyboardDialog)
        customKeyBoard = this.findViewById(R.id.keyboardCustomerKeyboardDialog)
        clearTextButton = this.findViewById(R.id.ivCustomKeyboardDialogDeleteAll)


        val inputConnection = editText.onCreateInputConnection(EditorInfo())
        customKeyBoard.inputConnection = inputConnection

        customKeyBoard.initContent(initValue)
        customKeyBoard.onClickCancel = {
            onClickCancel?.invoke()
            this.dismiss()
        }
        customKeyBoard.onClickEnter = {
            onClickEnter?.invoke(editText.text.toString())
            this.dismiss()
        }

        clearTextButton.setOnClickListener {
            customKeyBoard.onClickDeleteAll()
        }
        editText.setRawInputType(InputType.TYPE_NULL)
        editText.setTextIsSelectable(true)
    }
}