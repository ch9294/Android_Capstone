package com.example.dialogfragment


import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TestDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity).apply {
            setTitle("타이틀입니다")
            setMessage("본문입니다")
            setPositiveButton("positive", DialogListener())
            setNegativeButton("negative", DialogListener())
            setNeutralButton("netural", DialogListener())
        }
        return builder.create()
    }

    inner class DialogListener : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    Toast.makeText(activity as MainActivity,"POSITIVE",Toast.LENGTH_SHORT)
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    Toast.makeText(activity as MainActivity,"NEGATIVE",Toast.LENGTH_SHORT)

                }
                DialogInterface.BUTTON_NEUTRAL -> {
                    Toast.makeText(activity as MainActivity,"NETURAL",Toast.LENGTH_SHORT)

                }
            }
        }
    }
}
