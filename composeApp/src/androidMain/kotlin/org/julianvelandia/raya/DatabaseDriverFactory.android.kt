package org.julianvelandia.raya

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import db.WalletDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = WalletDatabase.Schema,
            context = context,
            name = "Wallet.db"
        )
    }
}