package com.osfans.trime.ime.core

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.osfans.trime.R
import com.osfans.trime.databinding.MainInputLayoutBinding
import com.osfans.trime.databinding.SymbolInputLayoutBinding
import com.osfans.trime.util.styledFloat
import splitties.views.dsl.constraintlayout.bottomOfParent
import splitties.views.dsl.constraintlayout.centerHorizontally
import splitties.views.dsl.constraintlayout.constraintLayout
import splitties.views.dsl.constraintlayout.lParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.withTheme
import splitties.views.dsl.core.wrapContent

/**
 * Successor of the old InputRoot
 */
@SuppressLint("ViewConstructor")
class InputView(
    val service: Trime,
) : ConstraintLayout(service) {
    private val themedContext = context.withTheme(android.R.style.Theme_DeviceDefault_Settings)

    val oldMainInputView = MainInputLayoutBinding.inflate(LayoutInflater.from(context))
    val oldSymbolInputView = SymbolInputLayoutBinding.inflate(LayoutInflater.from(context))

    val keyboardView: View

    init {
        keyboardView =
            constraintLayout {
                isMotionEventSplittingEnabled = true
                add(
                    oldMainInputView.root,
                    lParams(matchParent, wrapContent) {
                        centerHorizontally()
                        bottomOfParent()
                    },
                )
                add(
                    oldSymbolInputView.root,
                    lParams(matchParent, wrapContent) {
                        centerHorizontally()
                        bottomOfParent()
                    },
                )
            }

        add(
            keyboardView,
            lParams(matchParent, wrapContent) {
                centerHorizontally()
                bottomOfParent()
            },
        )
    }

    private var showingDialog: Dialog? = null

    fun showDialog(dialog: Dialog) {
        showingDialog?.dismiss()
        val windowToken = windowToken
        check(windowToken != null) { "InputView Token is null." }
        val window = dialog.window!!
        window.attributes.apply {
            token = windowToken
            type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
        }
        window.addFlags(
            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM or
                WindowManager.LayoutParams.FLAG_DIM_BEHIND,
        )
        window.setDimAmount(themedContext.styledFloat(android.R.attr.backgroundDimAmount))
        showingDialog =
            dialog.apply {
                setOnDismissListener { this@InputView.showingDialog = null }
                show()
            }
    }

    fun finishInput() {
        showingDialog?.dismiss()
    }
}