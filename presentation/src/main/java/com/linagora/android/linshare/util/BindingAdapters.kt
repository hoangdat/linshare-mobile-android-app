/*
 * LinShare is an open source filesharing software, part of the LinPKI software
 * suite, developed by Linagora.
 *
 * Copyright (C) 2020 LINAGORA
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version,
 * provided you comply with the Additional Terms applicable for LinShare software by
 * Linagora pursuant to Section 7 of the GNU Affero General Public License,
 * subsections (b), (c), and (e), pursuant to which you must notably (i) retain the
 * display in the interface of the “LinShare™” trademark/logo, the "Libre & Free" mention,
 * the words “You are using the Free and Open Source version of LinShare™, powered by
 * Linagora © 2009–2020. Contribute to Linshare R&D by subscribing to an Enterprise
 * offer!”. You must also retain the latter notice in all asynchronous messages such as
 * e-mails sent with the Program, (ii) retain all hypertext links between LinShare and
 * http://www.linshare.org, between linagora.com and Linagora, and (iii) refrain from
 * infringing Linagora intellectual property rights over its trademarks and commercial
 * brands. Other Additional Terms apply, see
 * <http://www.linshare.org/licenses/LinShare-License_AfferoGPL-v3.pdf>
 * for more details.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for
 * more details.
 * You should have received a copy of the GNU Affero General Public License and its
 * applicable Additional Terms for LinShare along with this program. If not, see
 * <http://www.gnu.org/licenses/> for the GNU Affero General Public License version
 *  3 and <http://www.linshare.org/licenses/LinShare-License_AfferoGPL-v3.pdf> for
 *  the Additional Terms applicable to LinShare software.
 */

package com.linagora.android.linshare.util

import android.text.format.Formatter
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import arrow.core.Either
import arrow.core.orNull
import com.auth0.android.jwt.JWT
import com.linagora.android.linshare.R
import com.linagora.android.linshare.domain.usecases.account.AccountDetailsViewState
import com.linagora.android.linshare.domain.usecases.quota.ExceedMaxFileSize
import com.linagora.android.linshare.domain.usecases.quota.ExtractInfoFailed
import com.linagora.android.linshare.domain.usecases.quota.PreUploadExecuting
import com.linagora.android.linshare.domain.usecases.quota.QuotaAccountNoMoreSpaceAvailable
import com.linagora.android.linshare.domain.usecases.sharedspace.SharedSpaceDocumentEmpty
import com.linagora.android.linshare.domain.usecases.utils.Failure
import com.linagora.android.linshare.domain.usecases.utils.Success
import com.linagora.android.linshare.glide.GlideApp
import com.linagora.android.linshare.model.parcelable.UploadDestinationInfo
import com.linagora.android.linshare.model.upload.UploadDocumentRequest
import com.linagora.android.linshare.util.FileSize.SizeFormat.SHORT
import com.linagora.android.linshare.util.TimeUtils.LinShareTimeFormat.LastLoginFormat
import com.linagora.android.linshare.view.Navigation
import com.linagora.android.linshare.view.authentication.login.ErrorType
import com.linagora.android.linshare.view.authentication.login.LoginFormState
import org.slf4j.LoggerFactory
import timber.log.Timber

private val LOGGER = LoggerFactory.getLogger(BindingAdapter::class.java)

@BindingAdapter("guide")
fun bindLoginGuide(textView: TextView, loginFormState: LoginFormState) {
    try {
        require(!loginFormState.isLoading)
        loginFormState.errorMessage
            ?.let {
                textView.apply {
                    setText(it)
                    setTextColor(ContextCompat.getColor(textView.context, R.color.error_border_color))
                }
            }
            ?: textView.apply {
                setText(R.string.please_enter_credential)
                setTextColor(ContextCompat.getColor(textView.context, R.color.text_with_logo_color))
            }
    } catch (exp: Exception) {
        Timber.w("bindLoginGuide() ignore this exception: ${exp.message}")
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
            ErrorType.WRONG_EMAIL -> {
                editText.id.takeIf { it == R.id.edtLoginUsername }
                    ?.let { R.drawable.round_error_layout }
                    ?: R.drawable.round_layout
            }
            ErrorType.UNKNOWN_ERROR -> R.drawable.round_error_layout
            else -> R.drawable.round_layout
        }

        editText.setBackgroundResource(background)
    } catch (exp: Exception) {
        Timber.w("bindInputError() ignore this exception: ${exp.message}")
    }
}

@BindingAdapter("android:text")
fun bindingDomainName(textView: TextView, accountDetailsViewState: AccountDetailsViewState) {
    textView.text = accountDetailsViewState.credential
        ?.serverUrl
        ?.authority
}

@BindingAdapter("lastName")
fun bindingLastName(textView: TextView, accountDetailsViewState: AccountDetailsViewState) {
    textView.text = accountDetailsViewState.user
        ?.lastName
}

@BindingAdapter("firstName")
fun bindingFirstName(textView: TextView, accountDetailsViewState: AccountDetailsViewState) {
    textView.text = accountDetailsViewState.user
        ?.firstName
}

@BindingAdapter("subject")
fun bindingSubjectFromDecodedToken(textView: TextView, accountDetailsViewState: AccountDetailsViewState) {
    textView.text = runCatching { JWT(accountDetailsViewState.token!!.token).subject }.getOrNull()
}

@BindingAdapter("lastLogin")
fun bindingLastLogin(textView: TextView, accountDetailsViewState: AccountDetailsViewState) {
    textView.text = runCatching {
        TimeUtils(textView.context)
            .convertToLocalTime(accountDetailsViewState.lastLogin!!.date, LastLoginFormat) }
        .getOrNull()
}

@BindingAdapter("availableSpace")
fun bindingAvailabeSpace(textView: TextView, accountDetailsViewState: AccountDetailsViewState) {
    textView.text = runCatching {
        val accountQuota = accountDetailsViewState.quota!!
        val quotaSize = FileSize(accountQuota.quota.size)
            .format(SHORT)
        val availableSize = FileSize(accountQuota.quota - accountQuota.usedSpace)
            .format(SHORT)
        String.format(textView.context.getString(R.string.available_space), availableSize, quotaSize)
    }.getOrNull()
}

@BindingAdapter("uploadSize")
fun bindingFileSize(textView: TextView, document: UploadDocumentRequest?) {
    textView.text = runCatching {
        Formatter.formatFileSize(textView.context, document!!.uploadFileSize)
    }.getOrNull()
}

@BindingAdapter("uploadInfo", "uploadErrorStateInfo")
fun bindingUploadInfo(textView: TextView, document: UploadDocumentRequest?, uploadErrorState: Either<Failure, Success>) {
    textView.text = uploadErrorState.map { success ->
        when (success) {
            PreUploadExecuting -> textView.context.resources.getString(R.string.executing)
            else -> document?.uploadFileName
        }
    }.orNull()
}

@BindingAdapter("documentIcon", "uploadErrorStateIcon")
fun bindingUploadIcon(imageView: AppCompatImageView, document: UploadDocumentRequest?, uploadErrorState: Either<Failure, Success>) {
    GlideApp.with(imageView.context)
        .load(document?.uploadUri)
        .placeholder(
            document?.uploadMediaType?.getDrawableIcon()
                ?: uploadErrorState.fold(
                    ifLeft = { R.drawable.ic_warning },
                    ifRight = { android.R.drawable.screen_background_light_transparent })
        )
        .into(imageView)
}

@BindingAdapter("uploadErrorStateProgress")
fun bindingUploadProgressIcon(imageView: AppCompatImageView, uploadErrorState: Either<Failure, Success>) {
    uploadErrorState.fold(
        ifLeft = { imageView.stopAnimationDrawable() },
        ifRight = { success ->
            success.takeIf { success is PreUploadExecuting }
                ?.let { imageView.startAnimationDrawable() }
                ?: imageView.stopAnimationDrawable() }
    )
}

@BindingAdapter("uploadErrorMessage")
fun bindingUploadError(textView: TextView, uploadErrorState: Either<Failure, Success>) {
    LOGGER.info("uploadErrorMessage() $uploadErrorState")
    textView.visibility = View.GONE
    uploadErrorState.mapLeft(::getUploadErrorMessageId)
        .mapLeft {
            textView.setText(it)
            textView.visibility = View.VISIBLE
        }
}

private fun getUploadErrorMessageId(failure: Failure): Int {
    return when (failure) {
        QuotaAccountNoMoreSpaceAvailable -> R.string.no_more_space_avalable
        ExceedMaxFileSize -> R.string.exceed_max_file_size
        ExtractInfoFailed -> R.string.extrac_info_failed
        else -> R.string.unable_to_prepare_file_for_upload
    }
}

@BindingAdapter("uploadState")
fun bindingUploadButton(button: Button, uploadState: Either<Failure, Success>) {
    uploadState.fold(
        ifLeft = { disableButtonUpload(button) },
        ifRight = { success -> bindingUploadButtonWhenSuccess(success, button) }
    )
}

@BindingAdapter("shareReceiversCount", "uploadType", requireAll = true)
fun bindingUploadButtonText(button: Button, shareReceiversCount: Int, uploadType: Navigation.UploadType) {
    when (uploadType) {
        Navigation.UploadType.INSIDE_APP_TO_WORKGROUP, Navigation.UploadType.OUTSIDE_APP_TO_WORKGROUP -> button.setText(R.string.upload)
        else -> shareReceiversCount.takeIf { it > 0 }
            ?.run { button.setText(R.string.upload_and_share) }
            ?: button.setText(R.string.upload_to_my_space)
    }
}

private fun bindingUploadButtonWhenSuccess(success: Success, button: Button) {
    when (success) {
        PreUploadExecuting -> disableButtonPreUploadExecuting(button)
        else -> enableButtonUpload(button)
    }
}

private fun disableButtonPreUploadExecuting(button: Button) {
    button.isEnabled = false
    button.setTextColor(ContextCompat.getColor(button.context, R.color.disable_state_color))
    button.setBackgroundResource(R.drawable.round_with_border_loading_button_layout)
}

private fun enableButtonUpload(button: Button) {
    button.isEnabled = true
    button.setTextColor(ContextCompat.getColor(button.context, R.color.white))
    button.setBackgroundResource(R.drawable.round_button_primary_solid)
}

private fun disableButtonUpload(button: Button) {
    button.isEnabled = false
    button.setTextColor(ContextCompat.getColor(button.context, R.color.white))
    button.setBackgroundResource(R.drawable.round_with_border_disable_button_layout)
}

@BindingAdapter("visibleEmptyMessage")
fun bindingEmptyMessage(textView: TextView, state: Either<Failure, Success>?) {
    val visible = state?.fold(
        ifLeft = { true },
        ifRight = { success -> success is SharedSpaceDocumentEmpty })
    textView.isVisible = visible ?: false
}

@BindingAdapter("visibilityPickDestinationContainer")
fun bindingVisibilityPickDestinationContainer(constraintLayout: ConstraintLayout, uploadType: Navigation.UploadType) {
    when (uploadType) {
        Navigation.UploadType.OUTSIDE_APP, Navigation.UploadType.OUTSIDE_APP_TO_WORKGROUP -> constraintLayout.visibility = View.VISIBLE
        else -> constraintLayout.visibility = View.GONE
    }
}

@BindingAdapter("uploadDestination")
fun bindingUploadDestination(appCompatTextView: AppCompatTextView, uploadDestinationInfo: UploadDestinationInfo?) {
    appCompatTextView.text = uploadDestinationInfo
        ?.parentDestinationInfo
        ?.parentNodeName
        ?: appCompatTextView.context.getString(R.string.my_space)
}
