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

package com.linagora.android.linshare.inject

import com.linagora.android.linshare.inject.annotation.ActivityScoped
import com.linagora.android.linshare.view.MainActivity
import com.linagora.android.linshare.view.MainActivityModule
import com.linagora.android.linshare.view.accounts.AccountDetailModule
import com.linagora.android.linshare.view.authentication.login.LoginModule
import com.linagora.android.linshare.view.authentication.wizard.WizardModule
import com.linagora.android.linshare.view.main.MainFragmentModule
import com.linagora.android.linshare.view.myspace.MySpaceModule
import com.linagora.android.linshare.view.myspace.details.DocumentDetailsModule
import com.linagora.android.linshare.view.receivedshares.ReceivedSharesModule
import com.linagora.android.linshare.view.receivedshares.details.ReceivedSharesShareDetailsModule
import com.linagora.android.linshare.view.search.SearchModule
import com.linagora.android.linshare.view.share.ShareFragmentModule
import com.linagora.android.linshare.view.sharedspace.SharedSpacePresentationModule
import com.linagora.android.linshare.view.sharedspace.details.SharedSpaceAddMembersModule
import com.linagora.android.linshare.view.sharedspace.details.SharedSpaceDetailsModule
import com.linagora.android.linshare.view.sharedspacedestination.SharedSpaceDestinationModule
import com.linagora.android.linshare.view.sharedspacedestination.copy.myspace.CopyMySpaceDestinationModule
import com.linagora.android.linshare.view.sharedspacedestination.copy.receivedshare.CopyReceivedShareDestinationModule
import com.linagora.android.linshare.view.sharedspacedestination.copy.sharedspace.CopySharedSpaceDestinationModule
import com.linagora.android.linshare.view.sharedspacedocument.SharedSpaceDocumentPresentationModule
import com.linagora.android.linshare.view.sharedspacedocument.details.SharedSpaceDocumentDetailsModule
import com.linagora.android.linshare.view.sharedspacedocumentdestination.SharedSpaceDocumentDestinantionModule
import com.linagora.android.linshare.view.sharedspacedocumentdestination.copy.myspace.CopyMySpaceDestinationDocumentModule
import com.linagora.android.linshare.view.sharedspacedocumentdestination.copy.receivedshare.CopyReceivedShareDestinationDocumentModule
import com.linagora.android.linshare.view.sharedspacedocumentdestination.copy.sharedspace.CopySharedSpaceDestinationDocumentModule
import com.linagora.android.linshare.view.splash.SplashActivity
import com.linagora.android.linshare.view.splash.SplashActivityModule
import com.linagora.android.linshare.view.upload.UploadFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("UNUSED")
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SplashActivityModule::class])
    internal abstract fun splashActivity(): SplashActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [
        MainActivityModule::class,
        MainFragmentModule::class,
        WizardModule::class,
        LoginModule::class,
        AccountDetailModule::class,
        UploadFragmentModule::class,
        MySpaceModule::class,
        DocumentDetailsModule::class,
        SearchModule::class,
        ShareFragmentModule::class,
        ReceivedSharesModule::class,
        ReceivedSharesShareDetailsModule::class,
        SharedSpacePresentationModule::class,
        SharedSpaceDocumentPresentationModule::class,
        SharedSpaceDetailsModule::class,
        SharedSpaceDestinationModule::class,
        SharedSpaceDocumentDestinantionModule::class,
        SharedSpaceAddMembersModule::class,
        CopySharedSpaceDestinationModule::class,
        CopySharedSpaceDestinationDocumentModule::class,
        CopyMySpaceDestinationModule::class,
        CopyMySpaceDestinationDocumentModule::class,
        CopyReceivedShareDestinationModule::class,
        CopyReceivedShareDestinationDocumentModule::class,
        SharedSpaceDocumentDetailsModule::class
    ])
    internal abstract fun mainActivity(): MainActivity
}
