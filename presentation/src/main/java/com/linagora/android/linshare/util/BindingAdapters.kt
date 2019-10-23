package com.linagora.android.linshare.util

import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.linagora.android.linshare.R
import com.linagora.android.linshare.view.authentication.login.ErrorType
import com.linagora.android.linshare.view.authentication.login.LoginFormState

@BindingAdapter("guide")
fun bindLoginGuide(textView: TextView, loginFormState: LoginFormState) {
    try {
        require(!loginFormState.isLoading)
        loginFormState.errorMessage
            ?.let {
                textView.apply {
                    setText(it)
                    setTextColor(resources.getColor(R.color.error_border_color))
                }
            }
            ?: textView.apply {
                setText(R.string.please_enter_credential)
                setTextColor(resources.getColor(R.color.text_with_logo_color))
            }
    } catch (exp: Exception) {
        exp.printStackTrace()
    }
}

@BindingAdapter("inputError")
fun bindingInputError(editText: EditText, loginFormState: LoginFormState) {
    try {
        require(!loginFormState.isLoading)
        val background = when (loginFormState.errorType) {
            ErrorType.WRONG_CREDENTIAL -> {
                editText.id.takeIf { it != R.id.edtLoginUrl }
                    ?.let { R.drawable.round_error_layout }
                    ?: R.drawable.round_layout
            }
            ErrorType.WRONG_URL -> {
                editText.id.takeIf { it == R.id.edtLoginUrl }
                    ?.let { R.drawable.round_error_layout }
                    ?: R.drawable.round_layout
            }
            ErrorType.UNKNOWN_ERROR -> R.drawable.round_error_layout
            else -> R.drawable.round_layout
        }

        editText.setBackgroundResource(background)
    } catch (exp: Exception) {
        exp.printStackTrace()
    }
}