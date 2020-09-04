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

package com.linagora.android.linshare.domain.usecases.sharedspace

import arrow.core.Either
import com.linagora.android.linshare.domain.model.order.OrderListConfigurationType
import com.linagora.android.linshare.domain.model.sharedspace.SharedSpaceId
import com.linagora.android.linshare.domain.model.sharedspace.WorkGroupDocument
import com.linagora.android.linshare.domain.model.sharedspace.WorkGroupFolder
import com.linagora.android.linshare.domain.model.sharedspace.WorkGroupNode
import com.linagora.android.linshare.domain.model.sharedspace.WorkGroupNodeId
import com.linagora.android.linshare.domain.repository.sharedspacesdocument.SharedSpacesDocumentRepository
import com.linagora.android.linshare.domain.usecases.utils.Failure
import com.linagora.android.linshare.domain.usecases.utils.State
import com.linagora.android.linshare.domain.usecases.utils.Success
import com.linagora.android.linshare.domain.utils.emitState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSharedSpaceChildDocumentsOrderedInteractor @Inject constructor(
    private val sharedSpacesDocumentRepository: SharedSpacesDocumentRepository
) {
    operator fun invoke(
        sharedSpaceId: SharedSpaceId,
        parentNodeId: WorkGroupNodeId?,
        orderListConfigurationType: OrderListConfigurationType
    ): Flow<State<Either<Failure, Success>>> {
        return flow<State<Either<Failure, Success>>> {
            emitState { Either.right(Success.Loading) }

            val state = Either.catch {
                sharedSpacesDocumentRepository.getAllChildNodes(sharedSpaceId, parentNodeId)
            }
                .bimap(
                    { SharedSpaceDocumentFailure(it) },
                    { sortSharedSpaceDocumentListByOrderListType(it, orderListConfigurationType) })

            emitState { state }
        }
    }

    private fun sortSharedSpaceDocumentListByOrderListType(
        sharedSpaceDocuments: List<WorkGroupNode>,
        orderListConfigurationType: OrderListConfigurationType
    ): Success {
        if (sharedSpaceDocuments.isEmpty()) {
            return SharedSpaceDocumentEmpty
        }
        return when (orderListConfigurationType) {
            OrderListConfigurationType.AscendingModificationDate -> {
                SharedSpaceDocumentViewState(sharedSpaceDocuments.sortedBy { it.modificationDate })
            }
            OrderListConfigurationType.DescendingModificationDate -> {
                SharedSpaceDocumentViewState(sharedSpaceDocuments.sortedByDescending { it.modificationDate })
            }
            OrderListConfigurationType.AscendingCreationDate -> {
                SharedSpaceDocumentViewState(sharedSpaceDocuments.sortedBy { it.creationDate })
            }
            OrderListConfigurationType.DescendingCreationDate -> {
                SharedSpaceDocumentViewState(sharedSpaceDocuments.sortedByDescending { it.creationDate })
            }
            OrderListConfigurationType.AscendingName -> {
                SharedSpaceDocumentViewState(sharedSpaceDocuments.sortedBy { it.name })
            }
            OrderListConfigurationType.DescendingName -> {
                SharedSpaceDocumentViewState(sharedSpaceDocuments.sortedByDescending { it.name })
            }
            OrderListConfigurationType.AscendingFileSize, OrderListConfigurationType.DescendingFileSize -> {
                sortSharedSpaceDocumentListByFileSize(sharedSpaceDocuments, orderListConfigurationType)
            }
        }
    }

    private fun sortSharedSpaceDocumentListByFileSize(
        sharedSpaceDocuments: List<WorkGroupNode>,
        orderListConfigurationType: OrderListConfigurationType
    ): Success {
        val workGroupDocumentList = sharedSpaceDocuments.filterIsInstance<WorkGroupDocument>()
        return orderListConfigurationType.takeIf { orderListConfigurationType == OrderListConfigurationType.AscendingFileSize }
            ?.let {
                SharedSpaceDocumentViewState(
                    workGroupDocumentList.sortedBy { it.size }
                        .plus(sharedSpaceDocuments.filterIsInstance<WorkGroupFolder>())

                )
            }
            ?: SharedSpaceDocumentViewState(
                workGroupDocumentList.sortedByDescending { it.size }
                    .let { documentList ->
                        sharedSpaceDocuments.filterIsInstance<WorkGroupFolder>()
                            .plus(documentList)
                    }
            )
    }
}
