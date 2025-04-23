package org.julianvelandia.raya

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import db.WalletDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = WalletDatabase.Schema,
            name = "Wallet.db"
        )
    }
}