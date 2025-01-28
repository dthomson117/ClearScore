package com.android.data.api

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class ConnectivityCheckerTest {
    private val network: Network = mockk()
    private val networkCapabilities: NetworkCapabilities = mockk()
    private val connectivityManager: ConnectivityManager = mockk(relaxed = true)

    private lateinit var sut: ConnectivityChecker

    @Before
    fun setUp() {
        sut = ConnectivityChecker(connectivityManager = connectivityManager)
    }

    @Test
    fun `checkConnectivity SHOULD return true WHEN wifi transport`() {
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns
                false
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) } returns
                false

        val result = sut.checkConnectivity()

        expectThat(result).isTrue()
    }

    @Test
    fun `checkConnectivity SHOULD return true WHEN cellular transport`() {
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns
                true
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) } returns
                false

        val result = sut.checkConnectivity()

        expectThat(result).isTrue()
    }

    @Test
    fun `checkConnectivity SHOULD return true WHEN ethernet transport`() {
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns
                false
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) } returns
                true

        val result = sut.checkConnectivity()

        expectThat(result).isTrue()
    }

    @Test
    fun `checkConnectivity SHOULD return false WHEN no active network`() {
        every { connectivityManager.activeNetwork } returns null

        val result = sut.checkConnectivity()

        expectThat(result).isFalse()
    }

    @Test
    fun `checkConnectivity SHOULD return false WHEN no capabilities`() {
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns null

        val result = sut.checkConnectivity()

        expectThat(result).isFalse()
    }

    @Test
    fun `checkConnectivity SHOULD return false WHEN no transport`() {
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasTransport(any()) } returns false

        val result = sut.checkConnectivity()

        expectThat(result).isFalse()
    }
}