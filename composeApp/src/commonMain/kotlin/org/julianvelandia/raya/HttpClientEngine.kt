package org.julianvelandia.raya

import io.ktor.client.engine.HttpClientEngineFactory

expect fun getKtorEngine(): HttpClientEngineFactory<*>