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

package com.linagora.android.linshare.view.sharedspace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import arrow.core.Either
import com.linagora.android.linshare.databinding.DialogRenameSharedSpaceBinding
import com.linagora.android.linshare.domain.model.sharedspace.SharedSpaceNodeNested
import com.linagora.android.linshare.domain.model.workgroup.NewNameRequest
import com.linagora.android.linshare.domain.usecases.utils.Failure
import com.linagora.android.linshare.domain.usecases.utils.Success
import com.linagora.android.linshare.view.dialog.DaggerBottomSheetDialogFragment
import com.linagora.android.linshare.view.dialog.NoOpCallback
import com.linagora.android.linshare.view.dialog.OnNegativeCallback
import com.linagora.android.linshare.view.dialog.OnNewNameRequestChange
import com.linagora.android.linshare.view.dialog.OnRenameSharedSpace

class RenameSharedSpaceDialog(
    private val currentSharedSpaceNodeNested: SharedSpaceNodeNested,
    private val listSharedSpaceNodeNested: LiveData<List<SharedSpaceNodeNested>>,
    private val onNegativeCallback: OnNegativeCallback = NoOpCallback,
    private val onRenameSharedSpace: OnRenameSharedSpace,
    private val onNewNameRequestChange: OnNewNameRequestChange,
    private val viewState: LiveData<Either<Failure, Success>>
) : DaggerBottomSheetDialogFragment() {

    companion object {
        const val TAG = "rename_shared_space_node"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogRenameSharedSpaceBinding.inflate(inflater, container, false)
            .apply { lifecycleOwner = viewLifecycleOwner }
        initView(binding)
        return binding.root
    }

    private fun initView(binding: DialogRenameSharedSpaceBinding) {
        binding.apply {
            currentNode = currentSharedSpaceNodeNested
            listWorkGroupNode = listSharedSpaceNodeNested
            state = viewState

            newName.doAfterTextChanged { name ->
                name?.toString()
                    ?.takeIf { it.isNotBlank() }
                    ?.let { onNewNameRequestChange(NewNameRequest(it)) } }

            cancelButton.setOnClickListener {
                onNegativeCallback(it)
                dismiss() }

            renameButton.setOnClickListener {
                onRenameSharedSpace(currentSharedSpaceNodeNested, NewNameRequest(newName.text.toString()))
                dismiss() }

            executePendingBindings()
        }
    }
}
