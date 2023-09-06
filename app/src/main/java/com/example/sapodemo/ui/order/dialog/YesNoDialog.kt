import android.app.AlertDialog
import android.content.Context

class YesNoDialog(private val context: Context) {

    interface YesNoListener {
        fun onYesClicked()
        fun onNoClicked()
    }

    fun showYesNoDialog(title: String, message: String, listener: YesNoListener) {
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Đồng ý") { dialog, _ ->
            listener.onYesClicked()
            dialog.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Thoát") { dialog, _ ->
            listener.onNoClicked()
            dialog.dismiss()
        }
        alertDialog.show()
    }
}