package com.linagora.android.linshare.view.myspace

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.linagora.android.linshare.domain.model.Credential
import com.linagora.android.linshare.domain.model.Token
import com.linagora.android.linshare.domain.model.document.Document
import com.linagora.android.linshare.domain.usecases.myspace.ContextMenuClick
import com.linagora.android.linshare.domain.usecases.myspace.DownloadClick
import com.linagora.android.linshare.domain.usecases.myspace.GetAllDocumentsInteractor
import com.linagora.android.linshare.domain.usecases.myspace.RemoveClick
import com.linagora.android.linshare.domain.usecases.myspace.SearchButtonClick
import com.linagora.android.linshare.domain.usecases.myspace.UploadButtonBottomBarClick
import com.linagora.android.linshare.domain.usecases.remove.RemoveDocumentInteractor
import com.linagora.android.linshare.operator.download.DownloadOperator
import com.linagora.android.linshare.util.CoroutinesDispatcherProvider
import com.linagora.android.linshare.view.LinShareApplication
import com.linagora.android.linshare.view.action.MySpaceItemActionImp
import com.linagora.android.linshare.view.base.ItemContextMenu
import com.linagora.android.linshare.view.base.LinShareViewModel
import com.linagora.android.linshare.view.base.ListItemBehavior
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import javax.inject.Inject

class MySpaceViewModel @Inject constructor(
    application: LinShareApplication,
    private val getAllDocumentsInteractor: GetAllDocumentsInteractor,
    private val dispatcherProvider: CoroutinesDispatcherProvider,
    private val downloadOperator: DownloadOperator,
    private val removeDocumentInteractor: RemoveDocumentInteractor
) : LinShareViewModel(application, dispatcherProvider),
    ListItemBehavior<Document>,
    ItemContextMenu<Document> {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MySpaceViewModel::class.java)

        val NO_DOWNLOADING_DOCUMENT = null
    }

    val mySpaceItemAction = MySpaceItemActionImp(this)

    private val downloadingDocument = MutableLiveData<Document>()

    fun getAllDocuments() {
        viewModelScope.launch(dispatcherProvider.io) {
            consumeStates(getAllDocumentsInteractor())
        }
    }

    fun onSwipeRefresh() {
        getAllDocuments()
    }

    override fun onContextMenuClick(document: Document) {
        LOGGER.info("onContextMenuClick() $document")
        dispatchState(Either.right(ContextMenuClick(document)))
    }

    override fun onDownloadClick(document: Document) {
        LOGGER.info("onDownloadClick() $document")
        setProcessingDocument(document)
        dispatchState(Either.right(DownloadClick(document)))
    }

    fun onUploadBottomBarClick() {
        LOGGER.info("onUploadBottomBarClick()")
        dispatchState(Either.right(UploadButtonBottomBarClick))
    }

    override fun onRemoveClick(document: Document) {
        dispatchState(Either.right(RemoveClick(document)))
    }

    fun onSearchButtonClick() {
        LOGGER.info("onSearchButtonClick()")
        dispatchState(Either.right(SearchButtonClick))
    }

    private fun setProcessingDocument(document: Document?) {
        viewModelScope.launch(dispatcherProvider.main) {
            downloadingDocument.value = document
        }
    }

    fun getDownloadingDocument(): Document? {
        return downloadingDocument.value
    }

    fun removeDocument(document: Document) {
        viewModelScope.launch(dispatcherProvider.io) {
            consumeStates(removeDocumentInteractor(document.documentId))
        }
    }

    fun downloadDocument(credential: Credential, token: Token, document: Document) {
        viewModelScope.launch(dispatcherProvider.io) {
            setProcessingDocument(NO_DOWNLOADING_DOCUMENT)
            downloadOperator.downloadDocument(credential, token, document)
        }
    }
}
