package com.example.trello.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.trello.R

object Utils {
    fun showErrorDialog(context: Context, message: String, title: String, onClick: () -> Unit) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.reusable_error_dialog)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.findViewById<TextView>(R.id.tvTitle)?.text = title
        dialog.findViewById<TextView>(R.id.tvMessage)?.text = message
        dialog.findViewById<Button>(R.id.buttonAccept)?.setOnClickListener {
            dialog.dismiss()
            onClick()
        }
        dialog.show()
    }
}