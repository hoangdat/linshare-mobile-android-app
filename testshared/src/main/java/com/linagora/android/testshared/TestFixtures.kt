package com.linagora.android.testshared

import android.net.Uri
import arrow.core.Either
import com.linagora.android.linshare.data.database.downloading.DownloadingTaskEntity
import com.linagora.android.linshare.domain.model.AccountQuota
import com.linagora.android.linshare.domain.model.Credential
import com.linagora.android.linshare.domain.model.LastLogin
import com.linagora.android.linshare.domain.model.Password
import com.linagora.android.linshare.domain.model.Token
import com.linagora.android.linshare.domain.model.User
import com.linagora.android.linshare.domain.model.Username
import com.linagora.android.linshare.domain.model.document.Document
import com.linagora.android.linshare.domain.model.document.DocumentRequest
import com.linagora.android.linshare.domain.model.download.EnqueuedDownloadId
import com.linagora.android.linshare.domain.model.quota.QuotaSize
import com.linagora.android.linshare.domain.usecases.account.AccountDetailsViewState
import com.linagora.android.linshare.domain.usecases.auth.AuthenticationException
import com.linagora.android.linshare.domain.usecases.auth.AuthenticationFailure
import com.linagora.android.linshare.domain.usecases.auth.AuthenticationViewState
import com.linagora.android.linshare.domain.usecases.auth.BadCredentials
import com.linagora.android.linshare.domain.usecases.auth.ConnectError
import com.linagora.android.linshare.domain.usecases.auth.EmptyToken
import com.linagora.android.linshare.domain.usecases.auth.ServerNotFound
import com.linagora.android.linshare.domain.usecases.auth.SuccessRemoveAccount
import com.linagora.android.linshare.domain.usecases.auth.UnknownError
import com.linagora.android.linshare.domain.usecases.myspace.MySpaceViewState
import com.linagora.android.linshare.domain.usecases.quota.ExceedMaxFileSize
import com.linagora.android.linshare.domain.usecases.quota.QuotaAccountNoMoreSpaceAvailable
import com.linagora.android.linshare.domain.usecases.quota.ValidAccountQuota
import com.linagora.android.linshare.domain.usecases.upload.UploadSuccessViewState
import com.linagora.android.linshare.domain.usecases.utils.Failure.Error
import com.linagora.android.linshare.domain.usecases.utils.Success.Idle
import com.linagora.android.linshare.domain.usecases.utils.Success.Loading
import com.linagora.android.testshared.TestFixtures.Accounts.LAST_LOGIN
import com.linagora.android.testshared.TestFixtures.Accounts.LINSHARE_USER
import com.linagora.android.testshared.TestFixtures.Accounts.QUOTA
import com.linagora.android.testshared.TestFixtures.Credentials.LINSHARE_CREDENTIAL
import com.linagora.android.testshared.TestFixtures.Documents.DOCUMENT
import com.linagora.android.testshared.TestFixtures.Documents.DOCUMENT_2
import com.linagora.android.testshared.TestFixtures.Tokens.TOKEN
import okhttp3.MediaType
import java.net.URL
import java.util.Date
import java.util.UUID

object TestFixtures {

    object Tokens {
        val TOKEN_UUID_VALUE = "3c105392-66f8-4947-a604-aa03a14784da"

        val TOKEN_UUID_VALUE_2 = "3c105392-66f8-4947-a604-aa03a14784da"

        val TOKEN_UUID: UUID = UUID.fromString(TOKEN_UUID_VALUE)

        val TOKEN_UUID_2: UUID = UUID.fromString(TOKEN_UUID_VALUE_2)

        const val TOKEN_VALUE = "eyJhbGciOiJSUzUxMiJ9.eyJzdWIiOiJ1c2VyMUBsaW5zaGFyZS5vcmciLCJkb21haW" +
                "4iOiI0YTBjMmNmNC01ZjA5LTRmYjAtOWNhZS0yYzdlYTdiNjRmYWEiLCJpc3MiOiJMaW5TaGFyZSIsImV4cCI6" +
                "MTU3MDY4OTk1OSwiaWF0IjoxNTcwNjg5NjU5fQ.YQgIhRShjcq1_l3MLeQX7rTqPZGi67hqi3SF_WK2yuuVbIe" +
                "wD4r4X6A-HU0myeHh4YHhfeQ_1TsOBAP88Q86QzD0fYqL5Oe3MY5Ula7j29KR7R2_WFooHAq4y9UTOId8GY-ue" +
                "GB3m9DYZt-1-ViC05rGR92wBF86FGpfuunDboh-SApghG-S7LFJ-_J99leXMblvRkIPblxI8z-nbjy6ANHlN5_" +
                "PPOSudS3eOwKuDDzv0uiyztKuSakWkVz0IaByxlKAR_0-KEnGHf4tUcazy7v3NjxpliKrNyPurrTWMdvUlIA4Z" +
                "K_64WjHoDad3ho7lZsPDSq44UrTaUUYncVHvQ"
        const val TOKEN_VALUE_2 = "ZHBoYW1ob2GzX0BsaW5hZ29yYS5jb206aThqckJ3KTgzNk4="

        val TOKEN = Token(TOKEN_UUID, TOKEN_VALUE)
        val TOKEN_2 = Token(TOKEN_UUID_2, TOKEN_VALUE_2)
    }

    object Credentials {

        const val NAME = "alica@domain.com"
        const val NAME2 = "bob@domain.com"
        const val SERVER_NAME = "http://domain.com"
        const val LINSHARE_NAME = "user1@linagora.com"
        const val LINSHARE_URL = "http://linshare.org"

        val USER_NAME = Username(NAME)
        val USER_NAME2 = Username(NAME2)
        val LINSHARE_USER1 = Username(LINSHARE_NAME)
        val LINSHARE_BASE_URL = URL(LINSHARE_URL)
        val LINSHARE_CREDENTIAL = Credential(LINSHARE_BASE_URL, LINSHARE_USER1)
        val SERVER_URL = URL(SERVER_NAME)
        val CREDENTIAL = Credential(SERVER_URL, USER_NAME)
        val CREDENTIAL2 = Credential(SERVER_URL, USER_NAME2)
    }

    object Authentications {

        const val PASSWORD_VALUE = "qwertyui"
        const val PASSWORD_VALUE_2 = "asdasdasd"
        const val LINSHARE_PASSWORD = "password1"

        val LINSHARE_PASSWORD1 = Password(LINSHARE_PASSWORD)
        val PASSWORD = Password(PASSWORD_VALUE)
        val PASSWORD_2 = Password(PASSWORD_VALUE_2)
    }

    object State {

        val INIT_STATE = Either.Right(Idle)

        val LOADING_STATE = Either.Right(Loading)

        val AUTHENTICATE_SUCCESS_STATE = Either.Right(
            AuthenticationViewState(LINSHARE_CREDENTIAL, TOKEN))

        val WRONG_CREDENTIAL_STATE = Either.Left(
            AuthenticationFailure(
                BadCredentials(
                    AuthenticationException.WRONG_CREDENTIAL
                )
            )
        )

        val WRONG_PASSWORD_STATE = Either.Left(
            AuthenticationFailure(
                BadCredentials(
                    AuthenticationException.WRONG_PASSWORD
                )
            )
        )

        val CONNECT_ERROR_STATE = Either.Left(
            AuthenticationFailure(ConnectError)
        )

        val UNKNOW_ERROR_STATE = Either.Left(
            AuthenticationFailure(UnknownError)
        )

        val SERVER_NOT_FOUND_STATE = Either.Left(
            AuthenticationFailure(ServerNotFound)
        )

        val EMPTY_TOKEN_STATE = Either.Left(
            AuthenticationFailure(EmptyToken)
        )

        val ACCOUNT_DETAILS_WITH_CREDENTIAL = AccountDetailsViewState(
            credential = LINSHARE_CREDENTIAL,
            token = TOKEN
        )

        val ACCOUNT_DETAILS_SUCCESS_STATE = Either.Right(
            AccountDetailsViewState(
                credential = LINSHARE_CREDENTIAL,
                token = TOKEN,
                lastLogin = LAST_LOGIN,
                user = LINSHARE_USER,
                quota = QUOTA
            )
        )

        val ERROR_STATE = Either.Left(Error)

        val SUCCESS_REMOVE_ACCOUNT_STATE = Either.right(SuccessRemoveAccount)

        val VALID_QUOTA_ACCOUNT_STATE = Either.right(ValidAccountQuota)

        val EXCEED_MAX_FILE_SIZE = Either.left(ExceedMaxFileSize)

        val QUOTA_ACCOUNT_NO_MORE_AVAILABLE_SPACE = Either.left(QuotaAccountNoMoreSpaceAvailable)

        val UPLOAD_SUCCESS_VIEW_STATE = Either.right(UploadSuccessViewState(DOCUMENT))
    }

    object Accounts {

        private const val MILLISECONDS_LAST_LOGIN = 1572939335187

        val LAST_LOGIN_DATE = Date(MILLISECONDS_LAST_LOGIN)

        val LAST_LOGIN = LastLogin(LAST_LOGIN_DATE)

        const val QUOTA_UUID_VALUE = "77d10c28-583c-45a8-b747-d8a028f980bb"

        private val QUOTA_UUID = UUID.fromString(QUOTA_UUID_VALUE)

        val LINSHARE_USER = User(
            UUID.fromString("b7240862-d03c-4b30-a46b-ffa1eb65301c"),
            "John",
            "Doe",
            "user1@linshare.org",
            Date(1570681477515),
            Date(1570681477515),
            QUOTA_UUID,
            "INTERNAL",
            "SIMPLE",
            true
        )

        val QUOTA = AccountQuota(
            QuotaSize(6000000),
            QuotaSize(5000005),
            QuotaSize(6000000),
            false
        )

        val LOW_QUOTA = AccountQuota(
            QuotaSize(6000),
            QuotaSize(5000),
            QuotaSize(6000),
            false
        )
    }

    object DocumentRequests {

        val DOCUMENT_REQUEST = DocumentRequest(
            uri = Uri.parse("content://0@media/external/file/276"),
            fileName = "document.txt",
            fileSize = 25,
            mediaType = MediaType.get("text/plain")
        )

        val DOCUMENT_REQUEST_BIG_SIZE = DocumentRequest(
            uri = Uri.parse("content://0@media/external/file/276"),
            fileName = "document.txt",
            fileSize = 1000000,
            mediaType = MediaType.get("text/plain")
        )
    }

    object Documents {

        val DOCUMENT = Document(
            uuid = UUID.fromString("21a2901-b120-4111-9b0d-cbd9d493d7f9"),
            description = "",
            name = "document.txt",
            creationDate = Date(1574837876965),
            modificationDate = Date(1574837876965),
            expirationDate = Date(1582786676962),
            ciphered = false,
            type = MediaType.get("text/plain"),
            size = 25,
            metaData = "",
            sha256sum = "00d0235f6bfd0134d418f0d662c60ada1c45e087bd485bc8b77a24e7a8508b55",
            hasThumbnail = true,
            shared = 0
        )

        val DOCUMENT_2 = Document(
            uuid = UUID.fromString("21a2901-b120-4111-9b0d-cbd8d493d7f9"),
            description = "",
            name = "documents.txt",
            creationDate = Date(1574837876965),
            modificationDate = Date(1574837876965),
            expirationDate = Date(1582786676962),
            ciphered = false,
            type = MediaType.get("text/plain"),
            size = 25,
            metaData = "",
            sha256sum = "00d0235f6bfd0134d418f0d662c60ada1c45e087bd485bc8b77a24e7a8508b55",
            hasThumbnail = true,
            shared = 0
        )
    }

    object MySpaces {

        private val MY_SPACE_VIEW_STATE = MySpaceViewState(listOf(DOCUMENT, DOCUMENT_2))

        val ALL_DOCUMENTS_STATE = Either.Right(MY_SPACE_VIEW_STATE)
    }

    object DownloadingTasks {

        private val ENQUEUED_DOWNLOAD_ID = EnqueuedDownloadId(2911)
        private val ENQUEUED_DOWNLOAD_ID_2 = EnqueuedDownloadId(1006)

        val DOWNLOADING_TASK_ENTITY = DownloadingTaskEntity(
            ENQUEUED_DOWNLOAD_ID,
            UUID.fromString("21a2901-b120-4111-9b0d-cbd7d493d7f8"),
            "documents.txt",
            25,
            MediaType.get("text/plain")
        )

        val CONFLICT_DOWNLOADING_TASK_ENTITY = DownloadingTaskEntity(
            ENQUEUED_DOWNLOAD_ID,
            UUID.fromString("21a2901-b120-4111-9b0d-cbd7d493d7f8"),
            "documents.txt",
            3000,
            MediaType.get("text/plain")
        )

        val DOWNLOADING_TASK_ENTITY_2 = DownloadingTaskEntity(
            ENQUEUED_DOWNLOAD_ID_2,
            UUID.fromString("21a2901-b120-4111-9b0d-cbd7d493d7f8"),
            "documents.txt",
            25,
            MediaType.get("text/plain")
        )
    }
}
