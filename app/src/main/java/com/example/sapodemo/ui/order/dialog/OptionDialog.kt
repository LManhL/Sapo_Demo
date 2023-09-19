import android.app.AlertDialog
import android.content.Context
import com.example.sapodemo.R

class OptionDialog(private val context: Context) {

    interface YesNoListener {
        fun onYesClicked()
        fun onNoClicked()
    }

    fun showYesNoDialog(title: String, message: String, listener: YesNoListener) {
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.confirm)) { dialog, _ ->
            listener.onYesClicked()
            dialog.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel)) { dialog, _ ->
            listener.onNoClicked()
            dialog.dismiss()
        }
        alertDialog.show()
    }
}