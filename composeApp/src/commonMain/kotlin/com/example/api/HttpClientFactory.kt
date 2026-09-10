package com.example.api

import io.ktor.client.HttpClient

/** Ktor client backed by OkHttp on Android and Darwin on iOS. */
expect fun createHttpClient(): HttpClient
