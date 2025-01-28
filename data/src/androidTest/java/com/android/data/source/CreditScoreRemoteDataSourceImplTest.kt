package com.android.data.source

import com.android.data.api.ApiCallHandler
import com.android.data.api.ApiResult
import com.android.data.api.AppApi
import com.android.data.api.ConnectivityChecker
import com.android.data.model.CoachingSummaryJson
import com.android.data.model.CreditReportInfoJson
import com.android.data.model.CreditScoreJson
import com.android.data.source.creditScore.CreditScoreRemoteDataSourceImpl
import com.android.di.buildRetrofitInstance
import com.clearscore.common_kotlin.TestCoroutineRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import java.io.EOFException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

class CreditScoreRemoteDataSourceImplTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val connectivityChecker: ConnectivityChecker = mockk()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var creditScoreRemoteDataSource: CreditScoreRemoteDataSourceImpl
    private lateinit var appApi: AppApi
    private lateinit var okHttpClient: OkHttpClient

    private val sut = ApiCallHandler(connectivityChecker = connectivityChecker)

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        okHttpClient = OkHttpClient.Builder().build()

        appApi = buildRetrofitInstance(
            client = okHttpClient,
            baseUrl = mockWebServer.url("/").toString()
        )

        creditScoreRemoteDataSource = CreditScoreRemoteDataSourceImpl(
            appApi = appApi,
            apiCallHandler = sut
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getCreditScore_SHOULD_return_Success_WHEN_API_call_is_successful() {
        runTest {
            every { connectivityChecker.checkConnectivity() } returns true
            val mockResponse = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(validJson)
            mockWebServer.enqueue(mockResponse)

            val result = creditScoreRemoteDataSource.getCreditScore()

            expectThat(result).isA<ApiResult.Success<CreditScoreJson>>().and {
                get { data }.isA<CreditScoreJson>()
                get { data }.isEqualTo(creditScoreJson)
            }
        }

    }

    @Test
    fun getCreditScore_SHOULD_return_ResponseError_WHEN_API_call_is_bad_request() {
        runTest {
            every { connectivityChecker.checkConnectivity() } returns true
            val mockResponse = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody("""{"error": "Bad Request"}""")
            mockWebServer.enqueue(mockResponse)

            val result = creditScoreRemoteDataSource.getCreditScore()

            expectThat(result).isA<ApiResult.ApiError.ResponseError>().and {
                get { cause }.isEqualTo(null)
            }
        }
    }

    @Test(expected = EOFException::class)
    fun getCreditScore_SHOULD_return_IOError_WHEN_API_call_is_bad_request() {
        runTest {
            every { connectivityChecker.checkConnectivity() } returns true
            val mockResponse = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody("")
            mockWebServer.enqueue(mockResponse)

            val result = creditScoreRemoteDataSource.getCreditScore()

            expectThat(result).isA<ApiResult.ApiError.IOError>()
        }
    }

    @Test(expected = SocketTimeoutException::class)
    fun getCreditScore_SHOULD_return_HttpError_WHEN_API_call_is_timed_out() {
        runTest {
            every { connectivityChecker.checkConnectivity() } returns true

            val result = creditScoreRemoteDataSource.getCreditScore()

            expectThat(result).isA<ApiResult.ApiError.HttpError>()
        }
    }

    @Test
    fun getCreditScore_SHOULD_return_ResponseError_WHEN_no_connection() {
        runTest {
            every { connectivityChecker.checkConnectivity() } returns false

            val result = creditScoreRemoteDataSource.getCreditScore()

            expectThat(result).isA<ApiResult.ApiError.ResponseError>()
        }
    }

    private val validJson = """
        {
          "accountIDVStatus": "PASS",
          "creditReportInfo": {
            "score": 514,
            "scoreBand": 4,
            "clientRef": "CS-SED-655426-708782",
            "status": "MATCH",
            "maxScoreValue": 700,
            "minScoreValue": 0,
            "monthsSinceLastDefaulted": -1,
            "hasEverDefaulted": false,
            "monthsSinceLastDelinquent": 1,
            "hasEverBeenDelinquent": true,
            "percentageCreditUsed": 44,
            "percentageCreditUsedDirectionFlag": 1,
            "changedScore": 0,
            "currentShortTermDebt": 13758,
            "currentShortTermNonPromotionalDebt": 13758,
            "currentShortTermCreditLimit": 30600,
            "currentShortTermCreditUtilisation": 44,
            "changeInShortTermDebt": 549,
            "currentLongTermDebt": 24682,
            "currentLongTermNonPromotionalDebt": 24682,
            "currentLongTermCreditLimit": null,
            "currentLongTermCreditUtilisation": null,
            "changeInLongTermDebt": -327,
            "numPositiveScoreFactors": 9,
            "numNegativeScoreFactors": 0,
            "equifaxScoreBand": 4,
            "equifaxScoreBandDescription": "Excellent",
            "daysUntilNextReport": 9
          },
          "dashboardStatus": "PASS",
          "personaType": "INEXPERIENCED",
          "coachingSummary": {
            "activeTodo": false,
            "activeChat": true,
            "numberOfTodoItems": 0,
            "numberOfCompletedTodoItems": 0,
            "selected": true
          },
          "augmentedCreditScore": null
        }
    """.trimIndent()

    private val creditScoreJson = CreditScoreJson(
        accountIDVStatus = "PASS",
        creditReportInfo = CreditReportInfoJson(
            score = 514,
            scoreBand = 4,
            clientRef = "CS-SED-655426-708782",
            status = "MATCH",
            maxScoreValue = 700,
            minScoreValue = 0,
            monthsSinceLastDefaulted = -1,
            hasEverDefaulted = false,
            monthsSinceLastDelinquent = 1,
            hasEverBeenDelinquent = true,
            percentageCreditUsed = 44,
            percentageCreditUsedDirectionFlag = 1,
            changedScore = 0,
            currentShortTermDebt = 13758,
            currentShortTermNonPromotionalDebt = 13758,
            currentShortTermCreditLimit = 30600,
            currentShortTermCreditUtilisation = 44,
            changeInShortTermDebt = 549,
            currentLongTermDebt = 24682,
            currentLongTermNonPromotionalDebt = 24682,
            currentLongTermCreditLimit = null,
            currentLongTermCreditUtilisation = null,
            changeInLongTermDebt = -327,
            numPositiveScoreFactors = 9,
            numNegativeScoreFactors = 0,
            equifaxScoreBand = 4,
            equifaxScoreBandDescription = "Excellent",
            daysUntilNextReport = 9
        ),
        dashboardStatus = "PASS",
        personaType = "INEXPERIENCED",
        coachingSummary = CoachingSummaryJson(
            activeTodo = false,
            activeChat = true,
            numberOfTodoItems = 0,
            numberOfCompletedTodoItems = 0,
            selected = true
        ),
        augmentedCreditScore = null
    )
}