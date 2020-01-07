package com.linagora.android.linshare.inject.worker

import com.linagora.android.linshare.inject.annotation.WorkerKey
import com.linagora.android.linshare.view.upload.worker.UploadWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerBindingModule {

    @Binds
    @IntoMap
    @WorkerKey(UploadWorker::class)
    fun bindUploadWorker(factory: UploadWorker.Factory): ChildWorkerFactory
}